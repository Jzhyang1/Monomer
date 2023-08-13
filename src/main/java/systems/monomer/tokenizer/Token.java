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

import static systems.monomer.syntaxtree.Node.Usage.LITERAL;

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

    public Token(Usage usage, String value) {
        this.usage = usage;
        this.value = value;
    }

    public Token(Usage usage) {
        this.usage = usage;
    }

    private Node partialControlToNode(Token control, ListIterator<Token> iter) {
        if (!iter.hasNext()) control.throwError("Unexpected control at end of file");

        Node controlNode = control.toNode();
        Token token = iter.next();

        Node condition;
        if (token.usage == Usage.OPERATOR && OperatorNode.symbolPrefixes().contains(token.value))
             condition = partialOperatorToNode(control, null, token, iter, ":");
        else {
            condition = partialToNode(token.toNode(), iter);
            if(iter.hasNext()) {
                token = iter.next();
                condition = partialOperatorToNode(control, condition, token, iter, ":");
            }
        }
        iter.next();    //skip colon
        token = iter.next();

        Node body;
        if(token.usage == Usage.GROUP && token.value.equals("block"))
            body = partialToNode(token.toNode(), iter);
        else {
            if (token.usage == Usage.OPERATOR && OperatorNode.symbolPrefixes().contains(token.value))
                body = partialOperatorToNode(control, null, token, iter);
            else {
                body = partialToNode(token.toNode(), iter);
                token = iter.next();
                body = partialOperatorToNode(control, body, token, iter);
            }
        }

        return controlNode.with(condition == null ? new BoolNode() : condition).with(body); //TODO set BoolNode to true
    }

    private Node partialOperatorToNode(@Nullable Token prevOp, @Nullable Node cur, @Nullable Token nullableOp, ListIterator<Token> iter) {
        return partialOperatorToNode(prevOp, cur, nullableOp, iter, null);
    }
    private Node partialOperatorToNode(@Nullable Token prevOp, @Nullable Node cur, @Nullable Token nullableOp, ListIterator<Token> iter, @Nullable String stopAt) {
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
        boolean isAfter = prevOp != null && prevOp.rightPrec() >= op.leftPrec();
        boolean hardStop = op.value.equals(stopAt);
        if (isAfter || hardStop) {
            iter.previous();
            return cur;
        }

        if (!iter.hasNext()) {
            //check suffix
            if (OperatorNode.symbolSuffixes().contains(op.value)) {
                return op.toNode().with(cur);
            } else op.throwError("Expected value after operator");
        }

        //check if condition operation
        if(OperatorNode.symbolControls().contains(op.value)) {
            //TODO ugly
            cur = OperatorNode.symbolPrimaryControls().contains(op.value)?
                    new ControlGroupNode().with(partialControlToNode(op, iter)) :
                    cur.with(partialControlToNode(op, iter));
            cur = partialToNode(cur, iter);
            if(iter.hasNext()) return partialOperatorToNode(prevOp, cur, iter.next(), iter, stopAt);
            else return cur;
        }

        Token token = iter.next();
        Node opNode = op.toNode(), tokenNode = token.toNode();
        Node next = null;

        //check prefixes/suffixes
        if (token.usage == Usage.OPERATOR) {
//            if (OperatorNode.symbolControls().contains(op.value)) {
//                next = partialControlToNode(op, iter);
//            } else
            if (OperatorNode.symbolPrefixes().contains(token.value))
                next = partialOperatorToNode(op, null, token, iter, stopAt);
            else if (OperatorNode.symbolSuffixes().contains(op.value)) {
                next = opNode.with(cur);
            } else token.throwError("Expected value or prefix");
        } else
            next = tokenNode;
        assert next != null;
        next = partialToNode(next, iter);
        next = partialOperatorToNode(op, next, null, iter, stopAt);


        //check for chaining
        if (cur != null) {
            if (OperatorNode.isChained(cur.getName(), opNode.getName()))
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
                    case "as" -> new CastNode();
                    default ->
                            OperatorNode.getOperator(value);  //TODO make sure the GenericOperatorNode gets the function for interpret, compile, etc
                };
            }
            case GROUP -> { //TODO account for all the different types of groups
                Node node = switch (value) {
                    case "()", "block" -> new TupleNode("block");
                    case "[]" -> new ListNode();
                    case "{}" -> new StructureNode();
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

                //TODO this is ugly
                if(cur instanceof TupleNode) node.addAll(cur.getChildren());
                else if(value.equals("()"))  return cur;
                else node.add(cur);
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
