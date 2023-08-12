package systems.monomer.tokenizer;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import systems.monomer.errorhandling.ErrorBlock;
import systems.monomer.errorhandling.Index;
import systems.monomer.syntaxtree.*;
import systems.monomer.syntaxtree.controls.*;
import systems.monomer.syntaxtree.literals.*;
import systems.monomer.syntaxtree.operators.*;

import java.util.*;
import java.util.stream.Collectors;

public class Token extends ErrorBlock {
    public static enum Usage {
        OPERATOR, STRING_BUILDER, STRING, CHARACTER, INTEGER, FLOAT, GROUP, IDENTIFIER,
        CHARACTER_FROM_INT
    }

    @Getter
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

    private Node partialToNode(Node cur, ListIterator<Token> iter) {
        if (!iter.hasNext()) return cur;

        Token nextOp = iter.next();
        switch (nextOp.usage) {
            case IDENTIFIER -> {    //TODO move this under partialOperator or remember to call after every operation
                Node fieldNode = new FieldNode().with(cur).with(nextOp.toNode());
                return partialToNode(fieldNode, iter);
            }
            case GROUP -> { //TODO move this as well
                Node callNode = new CallNode().with(cur).with(nextOp.toNode());
                return partialToNode(callNode, iter);
            }
            case OPERATOR -> {
                iter.previous();
                return cur;
            }
            default -> {
                nextOp.throwError("Expected operator, group, or identifier");
                return null;
            }
        }
    }

    private Node partialControlToNode(Token control, ListIterator<Token> iter) {
        if (!iter.hasNext()) control.throwError("Unexpected control at end of file");

        Node controlNode = control.toNode();
        Token token = iter.next();

        Node cur = (token.usage == Usage.OPERATOR && OperatorNode.symbolPrefixes().contains(token.value)) ?
             partialOperatorToNode(control, null, token, iter) : partialToNode(token.toNode(), iter);
        token = iter.next();

        cur = partialOperatorToNode(control, cur, token, iter);
        return controlNode.with(cur);
    }

    private Node partialOperatorToNode(@Nullable Token prevOp, @Nullable Node cur, @Nullable Token nullableOp, ListIterator<Token> iter) {
        Token op = nullableOp;
        if (op == null) {
            if (iter.hasNext()) {
                op = iter.next();
                if (op.usage != Usage.OPERATOR) op.throwError("Expected operator");
            } else if (cur != null) return cur;
            else throwError("Expected operator somewhere in here");

            assert op != null;
        }

        //check if previous operation is before this op
        if (cur != null && prevOp != null && prevOp.rightPrec() >= op.leftPrec()) {
            iter.previous();
            return cur;
        }

        //check suffix
        if (!iter.hasNext()) {
            if (OperatorNode.symbolSuffixes().contains(op.value)) {
                return op.toNode().with(cur);
            } else op.throwError("Expected value after operator");
        }

        Token token = iter.next();

        Node opNode = op.toNode(), tokenNode = token.toNode();
        Node next = null;
        //check prefixes/suffixes
        if (token.usage == Usage.OPERATOR) {
            if (OperatorNode.symbolPrefixes().contains(token.value))
                next = partialOperatorToNode(op, null, token, iter);
            else if (OperatorNode.symbolControls().contains(token.value))
                next = partialControlToNode(token, iter);
            else if (OperatorNode.symbolSuffixes().contains(op.value)) {
                next = opNode.with(cur);
            } else token.throwError("Expected value or prefix");
        } else
            next = token.toNode();
        next = partialToNode(next, iter);
        next = partialOperatorToNode(op, next, null, iter);


        //check for chaining
        if (cur != null) {
            if ((cur.getUsage() == Node.Usage.OPERATOR || cur.getUsage() == Node.Usage.LABEL)   //TODO this sucks
                    &&
                    OperatorNode.isChained(cur.getName(), opNode.getName()))    //TODO doesn't work for ";" because tuple doesn't store the original operator
                opNode = cur;
            else
                opNode.add(cur);
        }

        if (!iter.hasNext()) {
            return opNode.with(next);
        }

        Token nextOp = iter.next();

        if (op.rightPrec() >= nextOp.leftPrec()) {  //do current operation first
//            iter.previous();
            opNode.add(next);
            if (iter.hasNext())
                return partialOperatorToNode(prevOp, opNode, nextOp, iter);
            else
                return opNode;
        } else {    //do second operation first
            next = partialOperatorToNode(op, next, nextOp, iter);
//            if (OperatorNode.isChained(opNode.getName(), next.getName())) { //chaining
//                opNode.addName(next.getName());
//                opNode.addAll(next.getChildren());
//            } else {
            opNode.add(next);
//            }
            if (iter.hasNext())
                return partialOperatorToNode(op, opNode, iter.next(), iter);
            else
                return opNode;
        }
    }

    public Node toNode() {
        switch (usage) {
            case STRING_BUILDER -> {
                if(children == null) return new StringBuilderNode(List.of(StringNode.EMPTY));
                return new StringBuilderNode(children.stream().map(Token::toNode).collect(Collectors.toList()));
            }
            case STRING -> {
                return new StringNode(value);
            }
            case CHARACTER -> {
                return new CharNode(value.charAt(0));
            }
            case CHARACTER_FROM_INT -> {
                return new CharNode((char) Integer.parseInt(value));
            }
            case INTEGER -> {
                return new IntNode(Integer.valueOf(value));
            }
            case FLOAT -> {
                return new FloatNode(Double.valueOf(value));
            }
            case IDENTIFIER -> {
                return new VariableNode(value);
            }
            case OPERATOR -> {
                return switch (value) {
                    case "=" -> new AssignNode();
//                    case ":", "to" -> new ConvertNode(value);
//                    case ";", "," -> new TupleNode(value);
                    case "as" -> new CastNode();
                    case "if" -> new IfNode();
                    case "repeat" -> new RepeatNode();
                    case "while" -> new WhileNode();
                    case "for" -> new ForNode();
                    case "else" -> new ElseNode();
                    case "any" -> new AnyNode();
                    case "all" -> new AllNode();
                    default ->
                            OperatorNode.getOperator(value);  //TODO make sure the GenericOperatorNode gets the function for interpret, compile, etc
                };
            }
            case GROUP -> { //TODO account for all the different types of groups
                Node node = switch (value) {
                    case "()" -> new TupleNode();
                    case "[]" -> new ListNode();
                    case "{}" -> new StructureNode();
                    case "block" -> new ModuleNode("--block--"); //TODO make this better
                    default -> null;
                };
                if(children == null) return node;

                ListIterator<Token> iter = children.listIterator();
                Token token = iter.next();

                Node cur = (token.usage == Usage.OPERATOR && OperatorNode.symbolPrefixes().contains(token.value)) ?
                        partialOperatorToNode(null,null, token, iter) : partialToNode(token.toNode(), iter);

                while (iter.hasNext()) {
                    token = iter.next();
                    if (token.usage != Usage.OPERATOR) token.throwError("Expected operator");
                    cur = partialOperatorToNode(null, cur, token, iter);
                }

                if(value.equals("()"))  return cur;    //TODO this is ugly
                node.addAll(cur.getChildren());
                return node;
            }
            default -> {
                throw new Error("reached unreachable statement");
            }
        }
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
        return OperatorNode.precedence(value).getFirst();
    }
    public int rightPrec() {
        return OperatorNode.precedence(value).getSecond();
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
        return usage + " " + value + (children == null ? "" : children.size() == 1 ? children :
                "[\n\t" + children.stream().map(Objects::toString)
                        .collect(Collectors.joining(",\n\t")) + "\n\t]\n");
    }
}
