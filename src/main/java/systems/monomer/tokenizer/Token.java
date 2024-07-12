package systems.monomer.tokenizer;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import systems.monomer.errorhandling.ErrorBlock;
import systems.monomer.errorhandling.Index;
import systems.monomer.syntaxtree.*;
import systems.monomer.syntaxtree.literals.*;
import systems.monomer.syntaxtree.operators.*;

import java.util.*;
import java.util.stream.Collectors;

import static systems.monomer.syntaxtree.literals.TupleNode.isTuple;

//sub_ are helpers for partial_

@Getter
public class Token extends ErrorBlock {
    public static enum Usage {
        OPERATOR, STRING_BUILDER, STRING, CHARACTER, INTEGER, FLOAT, GROUP, IDENTIFIER,
        CHARACTER_FROM_INT
    }

    private String value;
    private List<Token> children = null;
    @Getter
    private final Usage usage;

    public Token(Usage usage, String value) {
        this.usage = usage;
        this.value = value;
    }
    public Token(Usage usage) {
        this.usage = usage;
    }

    private static Node subGroupSuffixToNode(Node cur, Token suffixGroup, ListIterator<Token> iter) {
        Node opChildrenNode = suffixGroup.toNode();

        Node opNode = switch (suffixGroup.value) {
            case "()" -> Node.init.callNode().with(cur).with(opChildrenNode);
            case "[]" -> Node.init.indexNode().with(cur).with(opChildrenNode.get(0));
            case "{}" -> {
                Token peekToken = iter.next();
                if(peekToken.usage == Usage.GROUP && "()".equals(peekToken.value)) {
                    yield Node.init.callNode()
                            .with(cur)
                            .with(peekToken.toNode())
                            .with(opChildrenNode);
                }
                else {
                    iter.previous();
                    yield Node.init.fieldNode().with(cur).with(opChildrenNode);
                }
            }
            default -> throw suffixGroup.syntaxError("Expected (), {}, or []");
        };
        return opNode.with(cur.getStart(), suffixGroup.getStop(), cur.getSource());
    }

    private Node groupToNode(String paren) {
        return switch (paren) {
            case "()", "block" -> Node.init.tupleNode();
            case "[]" -> Node.init.listNode();
            case "{}" -> Node.init.structureNode();
            case "[)" -> Node.init.rangeNode(true, false);
            case "(]" -> Node.init.rangeNode(false, true);
            default -> throw syntaxError("Invalid group type " + paren);
        };
    }

    /**
     * Gets the next Node when the first part of the node is already known
     * @param cur an expression
     * @param iter
     * @return the new expression
     */
    private static Node partialToNode(Node cur, ListIterator<Token> iter) {    //TODO move this under partialOperator or remember to call after every operation
        if (!iter.hasNext()) return cur;

        Token nextOp = iter.next();
        return switch (nextOp.usage) {
            case IDENTIFIER -> partialToNode(
                    Node.init.fieldNode()
                            .with(cur).with(nextOp.toNode())
                            .with(cur.getStart(), nextOp.getStop(), cur.getSource()),
                    iter);
            case GROUP -> partialToNode(subGroupSuffixToNode(cur, nextOp, iter), iter);
            case OPERATOR -> {
                iter.previous();
                yield cur;
            }
            default -> throw nextOp.syntaxError("Expected operator, group, or identifier");
        };
    }


    private Node subControlPartToNode(Token control, boolean endAtColon, ListIterator<Token> iter) {
        if (!iter.hasNext()) throw control.syntaxError("Unexpected control at end of file");

        String stopAt = endAtColon ? ":" : null;
        Token token = iter.next();
        Node part;

        if (endAtColon && token.usage == Usage.OPERATOR && ":".equals(token.value)) {
            part = Node.init.boolNode(true);
        }
        else {
            part = partialOperatorToNode(control, token, iter, stopAt);
            if(iter.hasNext()) iter.next();   //skip colon or semicolon
        }
        if(isTuple(part) && part.size() == 1) part = part.get(0);

        return part;
    }

    private Node partialControlToNode(Token control, ListIterator<Token> iter) {
        if (!iter.hasNext()) throw control.syntaxError("Unexpected control at end of file");

        Node condition = subControlPartToNode(control, true, iter);
        Node body = subControlPartToNode(control, false, iter);
        Node controlNode = control.toNode();

        return controlNode.with(condition).with(body);
    }

    private Node partialOperatorToNode(@Nullable Token prevOp, @Nullable Node cur, @Nullable Token nullableOp, ListIterator<Token> iter) {
        return partialOperatorToNode(prevOp, cur, nullableOp, iter, null);
    }

    /**
     * partialOperatorToNode but handles prefix operators and tokens as cur (read from iter)
     * @param prevOp the previous operation Token (null if none)
     * @param token the current Token (null if none)
     */
    private Node partialOperatorToNode(@Nullable Token prevOp, @Nullable Token token, ListIterator<Token> iter, @Nullable String stopAt) {
        if(token == null) {
            if(!iter.hasNext()) throw syntaxError("Expected token after this");
            token = iter.next();
        }
        if(token.usage == Usage.OPERATOR) {
            if(Operator.isPrefix(token.value)) {
                Node cur = partialOperatorToNode(prevOp, null, token, iter, stopAt);
                return partialToNode(cur, iter);
            }
            else throw token.syntaxError("Expected operator to be prefix");
        }
        else {
            Node cur = partialToNode(token.toNode(), iter);
            return partialOperatorToNode(prevOp, cur, null, iter, stopAt);
        }
    }

    /**
     * Gets the next Node when parts of the operation are already known
     * @param prevOp the previous operation Token (null if none)
     * @param cur the current Node, the first value of the operation (null if none)
     * @param nullableOp the current operation Token (if null, the function will get the next Token)
     * @param iter the iterator of the Token stream
     * @param stopAt the value of the operation to stop at; does not eat (null unless this is being used for reading an end delimiter)
     * @return
     */
    private Node partialOperatorToNode(@Nullable Token prevOp, @Nullable Node cur, @Nullable Token nullableOp, ListIterator<Token> iter, @Nullable String stopAt) {
        Token op = nullableOp;
        if (op == null) {
            if (iter.hasNext()) {
                op = iter.next();
                if (op.usage != Usage.OPERATOR) throw op.syntaxError("Expected operator");
            } else if (cur != null) return cur;
            else throw syntaxError("Expected operator after this");
        }

        //check if previous operation is before this op
        boolean isAfter = cur != null && prevOp != null && prevOp.rightPrec() >= op.leftPrec();
        boolean hardStop = op.value.equals(stopAt);
        if (isAfter || hardStop) {
            iter.previous();
            return cur;
        }

        if (!iter.hasNext()) {
            //check suffix
            if (Operator.isSuffix(op.value)) {
                if(cur != null)
                    return op.toNode().with(cur);
                else if(Operator.isPrefix(op.value))
                    return op.toNode();
            } else throw op.syntaxError("Expected value after operator");
        }

        //check if condition operation
        if(Operator.isPrimaryControl(op.value)) {
            assert cur == null;
            cur = Node.init.controlGroupNode().with(partialControlToNode(op, iter));

            //TODO ugly
            if(!iter.hasNext()) return cur;
            Token peekToken;
            while(iter.hasNext()) {
                peekToken = iter.next();
                if(Operator.isSecondaryControl(peekToken.value))
                    cur.add(partialControlToNode(peekToken, iter));
                else {
                    iter.previous();
                    break;
                }
            }

            Index start = cur.get(0).getStart();
            Index stop = cur.get(cur.size()-1).getStop();
            cur.setContext(start, stop, op.getSource());

            if(iter.hasNext())
                return partialOperatorToNode(prevOp, cur, new Token(Usage.OPERATOR, ";"), iter);
            else
                return cur;
        }

        Token token = iter.next();
        Node opNode = op.toNode(), tokenNode = token.toNode();
        Node next = null;

        //check prefixes/suffixes
        if (token.usage == Usage.OPERATOR) {
            if (Operator.isPrefix(token.value))
                next = partialOperatorToNode(op, null, token, iter, stopAt);
            else if (Operator.isSuffix(op.value)) {
                next = opNode.with(cur);
            } else throw token.syntaxError("Expected value or prefix");
        } else
            next = tokenNode;
        assert next != null;
        next = partialToNode(next, iter);
        next = partialOperatorToNode(op, next, null, iter, stopAt);


        //check for chaining
        if (cur != null) {
            if (Operator.isChained(cur.getName(), opNode.getName()))
                opNode = cur;
            else if (cur.getUsage() == Node.Usage.CONTROL_GROUP && opNode.getUsage() == Node.Usage.LABEL){
                cur.add(opNode);
            }
            else
                opNode.add(cur);
        }

        //check if there is no next operation
        if (!iter.hasNext()) {
            return opNode.with(next);
        }

        //do next operation
        Token nextOp = iter.next();
        if (op.rightPrec() >= nextOp.leftPrec()) {  //do current operation first
            opNode.add(next);
            if (iter.hasNext())
                return partialOperatorToNode(prevOp, opNode, nextOp, iter, stopAt);
            else
                return opNode;
        } else {    //do second operation first
            next = partialOperatorToNode(op, next, nextOp, iter, stopAt);
            opNode.add(next);
            if (iter.hasNext())
                return partialOperatorToNode(op, opNode, iter.next(), iter, stopAt);
            else
                return opNode;
        }
    }

    public Node toNode() {
        return (switch (usage) {
            case STRING_BUILDER -> {
                if (children == null) yield  StringNode.EMPTY;
                if (children.size() == 1 && children.get(0).usage == Usage.STRING) yield children.get(0).toNode();
                yield Node.init.stringBuilderNode(children.stream().map(Token::toNode).collect(Collectors.toList()));
            }
            case STRING -> Node.init.stringNode(value);
            case CHARACTER -> Node.init.charNode(value.charAt(0));
            case CHARACTER_FROM_INT -> Node.init.charNode((char) Integer.parseInt(value));
            case INTEGER -> Node.init.intNode(Integer.valueOf(value));
            case FLOAT -> Node.init.floatNode(Double.valueOf(value));
            case IDENTIFIER -> Node.init.variableNode(value);
            case OPERATOR -> Operator.getOperator(value);
            case GROUP -> {
                Node node = groupToNode(value);
                if(children == null) yield node;

                ListIterator<Token> iter = children.listIterator();
                Token token = iter.next();

                Node cur = (token.usage == Usage.OPERATOR && Operator.isPrefix(token.value)) ?
                        partialOperatorToNode(null,null, token, iter) : partialToNode(token.toNode(), iter);

                while (iter.hasNext()) {
                    token = iter.next();
                    if (token.usage != Usage.OPERATOR) throw token.syntaxError("Expected operator");   //TODO **
                    cur = partialOperatorToNode(null, cur, token, iter);
                }

                if(OperatorNode.isOperator(cur, "...")) { //also make sure it's range, not spread
                    node = switch (value) {
                        case "()" -> Node.init.rangeNode(false, false);
                        case "[)" -> Node.init.rangeNode(true, false);
                        case "(]" -> Node.init.rangeNode(false, true);
                        case "[]" -> Node.init.rangeNode(true, true);
                        default -> throw syntaxError("Invalid range braces " + value);
                    };
                    yield node.with(cur.getChildren()); //.with(cur.getContext());
                }
//                node.setContext(cur.getContext());

                if("()".equals(value)) node = cur;
//                else if(cur.isControl()) node = new AssertTypeNode(node, cur);
                else if(isTuple(cur)) node.addAll(cur.getChildren());
                else node.add(cur);

                yield node;
            }
        }).with(getContext());
    }

    public void add(Token child) {
        if(children == null) children = new ArrayList<>();
        children.add(child);
    }
    public void addAll(Token token) {
        if(children == null) children = new ArrayList<>(token.children);
        else children.addAll(token.children);
    }

    public Token getFirst() {
        return children.get(0);
    }
    public Token getLast() {
        return children.get(children.size() - 1);
    }

    public int leftPrec() {
        return Operator.precedence(value).getFirst();
    }
    public int rightPrec() {
        return Operator.precedence(value).getSecond();
    }

    public Token with(String value) {
        this.value = value;
        return this;
    }
    public Token with(Index start, Index stop, Source source) {
        setContext(start, stop, source);
        return this;
    }

    public List<Token> markupBlock() {
        if(getStop().getPosition() <= getStart().getPosition()) return List.of();
        if(children == null) return List.of(this);

        List<Token> tokens = new ArrayList<>();
        Index start1 = getStart(), stop1 = getFirst().getStart();
        int posStart1 = start1.getPosition(), posStop1 = stop1.getPosition();
        Index start2 = getLast().getStop(), stop2 = getStop();
        int posStart2 = start2.getPosition(), posStop2 = stop2.getPosition();

        if(posStop1 > posStart1) {
            Token firstToken = new Token(usage, value).with(start1, stop1, getSource());
            tokens.add(firstToken);
        }
        for(Token child : children) {
            tokens.addAll(child.markupBlock());
        }
        if(posStop2 > posStart2) {
            Token secondToken = new Token(usage, value).with(start2, stop2, getSource());
            tokens.add(secondToken);
        }
        return tokens;
    }

    public String toString() {
        String part1 = usage + " " + value;
        if(children == null) return part1;
        else if(children.size() == 1) return part1 + "[" + children.get(0) + "]";
        else return part1 + "[\n\t" + children.stream().map(Objects::toString)
                        .collect(Collectors.joining(",\n\t")) + "\n\t]\n";
    }
}
