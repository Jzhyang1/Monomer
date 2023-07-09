public class Token {
    private String name;
    private Token[] children;
    private LineContext context;
    private Usage usage;

    public static enum Usage {
        OPERATOR, INT, FLOAT, CHAR, STRINGBUILD, STRING, GROUP, WORD
    }

    public Token(String name, Token[] children, LineContext context, Usage usage) {
        this.name = name;
        this.children = children;
        this.context = context;
        this.usage = usage;
    }

    public Token(String name, LineIndex lineIndex, LineIndex lineIndex2, Source source, Usage usage) {
        this.name = name;
        this.context = new LineContext(lineIndex, lineIndex2, source);
        this.usage = usage;     
    }

    public Token with(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Node makeNode() {
        return null;
    }

    public void add(Token token) {
        if (children == null) {
            children = new Token[1];
            children[0] = token;
        } else {
            Token[] newChildren = new Token[children.length + 1];
            for (int i = 0; i < children.length; i++) {
                newChildren[i] = children[i];
            }
            newChildren[children.length] = token;
            children = newChildren;
        }
    }
}
