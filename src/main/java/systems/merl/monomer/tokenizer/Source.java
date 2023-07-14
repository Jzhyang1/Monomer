package systems.merl.monomer.tokenizer;

import systems.merl.monomer.errorHandling.ErrorBlock;
import systems.merl.monomer.errorHandling.error.SourceIndex;
import systems.merl.monomer.syntaxTree.Node;

import java.util.*;

public abstract class Source {
    public static class Token extends ErrorBlock {
        public static enum Usage {
            OPERATOR, STRING_BUILDER, STRING, CHARACTER, INTEGER, FLOAT, GROUP
        }

        private String value;
        private final List<Token> children = new ArrayList<>();
        private final Usage usage;

        public Token(Usage usage, String value) {
            this.usage = usage;
            this.value = value;
        }
        public Token(Usage usage) {
            this.usage = usage;
        }

        public Node toNode() {
            throw new Error("TODO unimplemented");
        }

        public void add(Token child) {
            children.add(child);
        }
        public Token with(String value) {
            this.value = value;
            return Token.this;
        }
    }
    public static class Line {
        public static final Map<Character, Integer> SPACE_CHARS = new HashMap<>(){{
            put(' ', 1);
            put('\t', 4);
        }};

        private String line;
        private int x=0, y;

        public Line(String line, int y) {
            this.line = line + "\n";
            this.y = y;
        }

        public Index getIndex(){
            return new Index(x, y);
        }

        public char peek() {
            return line.charAt(x);
        }
        public char get() {
            return line.charAt(x++);
        }
        public void unget() {
            --x;
        }

        public int startingSpaces() {
            int startingSpaces=0;
            for(int i = 0; i < line.length() && SPACE_CHARS.containsKey(line.charAt(i)); ++i) {
                startingSpaces += SPACE_CHARS.get(line.charAt(i));
            }
            return startingSpaces;
        }
        public int skipSpaces() {
            throw new Error("TODO unimplemented");
        }

        public String matchNext(List<String> next) {
            throw new Error("TODO unimplemented");
        }
        public boolean matchNext(String next) {
            throw new Error("TODO unimplemented");
        }
    }
    public static class Index {
        private int x, y;

        public Index(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    public static class Context {
        private Index start, stop;
        private Source source;

        public Context(Index start, Index stop, Source source) {
            this.start = start;
            this.stop = stop;
            this.source = source;
        }

        public Context(int startRow, int startCol, int stopRow, int stopCol, Source source) {
            this.start = new Index(startRow, startCol);
            this.stop = new Index(stopRow, stopCol);
            this.source = source;
        }

        public Source getSource(){
            return source;
        }
    }

    private static final int DEFAULT_BUFFER_COUNT = 100;
    private int y = 0;
    protected Deque<Line> buffer;


    public Token parse(){
        /* TODO */

        Line line = getLine();
        int starting = line.startingSpaces();
        Index tokenStart = line.getIndex();
        line.skipSpaces();

        while (!eof()) {
            if (line.peek() == '\n') {
                return parse();
            } else {
                StringBuilder strbuild = new StringBuilder();
                Token tokens = new Token(strbuild.toString(), tokenStart, line.getIndex(), source, Token.Usage.WORD);

                for (int i = 0; i < starting; i++) {
                    char c = line.get();

                    if (c == '\n') {
                        tokens.add(new Token(";", tokenStart, line.getIndex(), source, Token.Usage.WORD));
                        line = source.getLine();
                        int nextStarting = line.startingSpaces();
                        if (nextStarting > starting) {
                            tokens.add(tokenize(source));
                        } else if (nextStarting < starting) {
                            return tokens;
                        } else {
                            line.skipSpaces();
                        }
                        continue;
                    } else if (c == ' ') {
                        tokens.add(
                                new Token(strbuild.toString(), tokenStart, line.getIndex(), source, Token.Usage.WORD));
                        strbuild.setLength(0);
                        tokenStart = line.getIndex();
                    } else if (c == '\\') {
                        if (line.peek() == '\n') {
                            line = source.getLine();
                            line.skipSpaces();
                        } else if (line.peek() == '\\') {
                            tokens.add(new Token(";", tokenStart, line.getIndex(), source, Token.Usage.WORD));
                            line = source.getLine();
                            int nextStarting = line.startingSpaces();
                            if (nextStarting > starting) {
                                tokens.add(tokenize(source));
                            } else if (nextStarting < starting) {
                                return tokens;
                            } else {
                                line.skipSpaces();
                            }
                            continue;
                        } else if (line.peek() == '(') {
                            // readmultilinecomment(source)
                        }
                    } else {
                        if(line.matchNext(OperatorNode.symbolOperators()) != null) {
                            //push line.get()
                            OperatorNode.symbolOperators().push
                        }
                    }
                }
            }
        }
    }
    public Token parseStringLiteral(){
        throw new Error("TODO unimplemented");
    }
    public Token parseNumberLiteral(){
        throw new Error("TODO unimplemented");
    }
    public Token parseIdentifier(){
        throw new Error("TODO unimplemented");
    }


    public Line getLine() {
        if (buffer.isEmpty()) {
            bufferLines(DEFAULT_BUFFER_COUNT);
        }
        ++y;
        return buffer.remove();
    }
    public void ungetLine(Line sourceLine) {
        buffer.push(sourceLine);
        --y;
    }

    public boolean eof() {
        return buffer.isEmpty();
    }

    public int getY() {
        return y;
    }
    public int getRow() {
        return y+1;
    }

    public abstract String getTitle();
    protected abstract void bufferLines(int num);
}