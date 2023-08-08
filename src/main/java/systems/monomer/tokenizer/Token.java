package systems.monomer.tokenizer;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import systems.monomer.errorHandling.ErrorBlock;
import systems.monomer.errorHandling.Index;
import systems.monomer.syntaxTree.*;

import java.util.*;
import java.util.stream.Collectors;

public class Token extends ErrorBlock {
    public static enum Usage {
        OPERATOR, STRING_BUILDER, STRING, CHARACTER, INTEGER, FLOAT, GROUP, IDENTIFIER,
        CHARACTER_FROM_INT
    }

    @Getter
    private String value;
    private final List<Token> children = new ArrayList<>();
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
        if(!iter.hasNext()) return cur;

        Token nextOp = iter.next();
        switch (nextOp.usage) {
            case IDENTIFIER -> {
                FieldOperatorNode fieldNode = new FieldOperatorNode();
                fieldNode.add(cur);
                fieldNode.add(nextOp.toNode());
                return partialToNode(fieldNode, iter);
            }
            case GROUP -> {
                Node callNode = new CallOperatorNode();
                callNode.add(cur);
                callNode.add(nextOp.toNode());
                return callNode;
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
    private Node partialOperatorToNode(@Nullable Node cur, Token op, ListIterator<Token> iter) {
        Token token = iter.next();
        Node next = null;
        if(token.usage == Usage.OPERATOR) {
            if(!OperatorNode.symbolPrefixes().contains(token.value))
                token.throwError("Expected value or prefix");
            next = partialOperatorToNode(null, token, iter);
        }
        else
            next = token.toNode();
        next = partialToNode(next, iter);

        Token nextOp = iter.next();
        Node opNode = op.toNode();

        if(cur != null) {
            if(cur.getUsage() == Node.Usage.OPERATOR && OperatorNode.isChained(cur.getName(), opNode.getName())) opNode = cur;
            else opNode.add(cur);
        }
        if(op.rightPrec() >= nextOp.leftPrec()) {
            opNode.add(next);
            return partialOperatorToNode(opNode, nextOp, iter);
        }
        else {
            next = partialOperatorToNode(next, nextOp, iter);
            opNode.add(next);
            if(iter.hasNext())
                return partialOperatorToNode(opNode, iter.next(), iter);
            else
                return opNode;
        }
    }

    public Node toNode() {
        switch (usage) {
            case STRING_BUILDER -> {
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
                    case "=" -> new AssignOperatorNode();
                    case ":", "to" -> new ConvertOperatorNode();
                    case "as" -> new CastOperatorNode();
                    case "if", "repeat", "while" ->
                            new ControlGroupNode() {{ //TODO how will the GROUP branch handle this?
                                add(null);  //TODO add the if/repeat/while node
                            }};
                    case "else", "any", "all" -> null;    //TODO
                    case "for" -> null;    //TODO
                    default -> new GenericOperatorNode(value);  //TODO make sure the GenericOperatorNode gets the function for interpret, compile, etc
                };
            }
            case GROUP -> {
                ListIterator<Token> iter = children.listIterator();
                Token token = iter.next();

                Node cur = (token.usage == Usage.OPERATOR && OperatorNode.symbolPrefixes().contains(token.value)) ?
                        partialOperatorToNode(null, token, iter) : token.toNode();

                while(iter.hasNext()) {
                    token = iter.next();
                    if(token.usage != Usage.OPERATOR) token.throwError("Expected operator");
                    cur = partialOperatorToNode(cur, token, iter);
                }

                return cur;
            }
            default -> {
                throw new Error("reached unreachable statement");
            }
        }
    }

    public void add(Token child) {
        children.add(child);
    }

    public void addAll(Token token) {
        children.addAll(token.children);
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

    public void addSeparator() {
        add(new Token(Usage.OPERATOR, ";"));
    }

    public String toString() {
        return usage + " " + value + (children.isEmpty() ? "" : children.size() == 1 ? children :
                "[\n\t" + children.stream().map(Objects::toString)
                        .collect(Collectors.joining(",\n\t")) + "\n\t]\n");
    }
}
