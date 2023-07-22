package systems.merl.monomer.tokenizer;

import systems.merl.monomer.errorHandling.ErrorBlock;
import systems.merl.monomer.syntaxTree.Node;
import systems.merl.monomer.syntaxTree.OperatorNode;

import java.util.*;

public abstract class Source {
    public static class Token extends ErrorBlock {
        public static enum Usage {
            OPERATOR, STRING_BUILDER, STRING, CHARACTER, INTEGER, FLOAT, GROUP, IDENTIFIER
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
        public int skipSpaces(int num) {
            throw new Error("TODO unimplemented");
        }

        public String matchNext(Collection<String> next) {
            throw new Error("TODO unimplemented");
        }
        public boolean matchNext(String next) {
            throw new Error("TODO unimplemented");
        }

        public boolean eol() {
            return line.length() == x;
        }

        public boolean allSpaces() {
            return line.isBlank();
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
    private class SourceLineReader {
        public Line line;
        public int starting;
        private Token tokens; //wrapper token
        public Index tokenStart;  //beginning of current token

        public SourceLineReader() {
            line = getNonemptyLine();
            starting = line.skipSpaces();
            tokens = new Token(Token.Usage.GROUP);
            tokenStart = line.getIndex();
        }

        public void addSeparator() {
            tokens.add(new Token(Token.Usage.OPERATOR, ";"));
        }

        public void addToken(Token token) {
            tokens.add(token);
        }

        public Token getTokens() {
            return tokens;
        }

        public void addOperator(String operator) {
            addToken(new Token(Token.Usage.OPERATOR, operator));
        }

//        public void simpleNextLine() {
//            saveIfToken();
//            line = getLine();
//        }
    }

    private static final int DEFAULT_BUFFER_COUNT = 100;
    private int y = 0;
    protected Deque<Line> buffer;


    public Token parse(){
        SourceLineReader reader = new SourceLineReader();

        while (!eof()) {
            char peek = reader.line.peek();
            switch (peek) {
                case '\\' -> {
                    //comments
                    if (reader.line.matchNext("\\\\")) {
                        //comment to end-of-line
                        //TODO
                    } else if (reader.line.matchNext("\\\n")) {
                        //escape newline
                        reader.line = getNonemptyLine();
                        reader.line.skipSpaces();
                    } else {
                        //comment next symbol
                        parseNext(reader);
                    }
                }
                case '\n' -> {
                    //new line
                    Line nextLine = getNonemptyLine();
                    int nextStarting = nextLine.startingSpaces();

                    if (nextStarting > reader.starting) {
                        //child group
                        ungetLine(nextLine);
                        reader.addToken(parse());
                    } else if (nextStarting < reader.starting) {
                        return reader.getTokens();
                    } else {
                        reader.addSeparator();
                        reader.line = nextLine;
                    }
                }
                default -> {
                    //space, identifier, operator, or number
                    if (Character.isSpaceChar(peek)) {
                        reader.line.skipSpaces();
                    } else {
                        reader.addToken(parseNext(reader));
                    }
                }
            }
        }

        return reader.getTokens();
    }

    /**
     * assumes that the first quote is not handled,
     * handles all escape sequences,
     * and removes the appropriate number of starting spaces at the beginning of each line
     * @return the string literal
     */
    public Token parseStringLiteral(SourceLineReader reader) {
        char delim = reader.line.get();
        int startingSpaces = reader.starting;

        Token ret = new Token(Token.Usage.STRING_BUILDER);
        StringBuilder strbuild = new StringBuilder();

        if(reader.line.peek() == '\n') {
            reader.line = getLine();
            startingSpaces = reader.line.skipSpaces();
        }

        while(reader.line.peek() != delim) {
            switch (reader.line.peek()) {
                case '\\' -> {
                    reader.line.get();  //removes the escape char
                    char nextChar = reader.line.get();
                    switch (nextChar) {
                        case '\\', '"', '\'' -> strbuild.append(nextChar);
                        case 'n' -> strbuild.append('\n');
                        case 't' -> strbuild.append('\t');
                        case '\n' -> {
                            reader.line = getLine();
                            reader.line.skipSpaces();
                        }
                        case 'a' -> {
                            //TODO ASCII
                        }
                        case 'u' -> {
                            //TODO UNICODE
                        }
                        case 'c' -> {
                            //TODO interpolate char
                        }
                        default -> {
                            //interpolation via \(VALUE)
                            if(!strbuild.isEmpty()) ret.add(new Token(Token.Usage.STRING, strbuild.toString()));
                            Token interpolate = parseNext(reader);

                            if(interpolate.usage != Token.Usage.GROUP) {
                                interpolate.throwError("unexpected escape sequence. " +
                                        "If the sequence is intended to be interpolated, please wrap it using \\(...) or " +
                                        "if the sequence is a unicode character, please use the unicode sequence \\u____");
                            }
                        }
                    }
                }
                case '\n' -> {
                    strbuild.append(reader.line.get());
                    reader.line = getLine();
                    Index start = reader.line.getIndex();

                    if(!reader.line.allSpaces() && reader.line.skipSpaces(startingSpaces) != startingSpaces) {
                        throwParseError(start, reader.line.getIndex(), Token.Usage.STRING, strbuild.toString(), "Insufficient spacing for line of string");
                    }
                }
                default -> strbuild.append(reader.line.get());
            }
        }

        if(!strbuild.isEmpty()) ret.add(new Token(Token.Usage.STRING, strbuild.toString()));
        return ret;
    }
    public Token parseNumberLiteral(SourceLineReader reader) {
        Index start = reader.line.getIndex();

        StringBuilder strbuild = new StringBuilder();
        boolean hasE = false, hasDot = false;
        boolean isE = false, isDot = false;

        while (Character.isDigit(reader.line.peek()) ||
                (isE = (reader.line.peek() == 'e' || reader.line.peek() == 'E')) ||
                (isDot = (reader.line.peek() == '.'))) {
            if (isE && hasE) {
                throwParseError(start, reader.line.getIndex(), Token.Usage.FLOAT, strbuild.toString(), "Duplicate occurrence of 'E' in float literal");
            }
            if (isDot && hasDot) {
                throwParseError(start, reader.line.getIndex(), Token.Usage.FLOAT, strbuild.toString(), "Duplicate occurrence of '.' in float literal");
            }
            if (hasE && isDot) {
                throwParseError(start, reader.line.getIndex(), Token.Usage.FLOAT, strbuild.toString(), "'E' before '.' in float literal");
            }

            hasE = isE;
            hasDot = isDot;
            strbuild.append(reader.line.get());
        }

        return new Token(hasE || hasDot ? Token.Usage.FLOAT : Token.Usage.INTEGER, strbuild.toString());
    }
    public Token parseIdentifier(SourceLineReader reader) {
        StringBuilder strbuild = new StringBuilder();
        while (isIdentifierChar(reader.line.peek())) {
            strbuild.append(reader.line.get());
        }
        return new Token(Token.Usage.IDENTIFIER, strbuild.toString());
    }

    /**
     * returns either the next string literal, int literal, identifier, group, or operator
     * @return the next token from the above list
     */
    public Token parseNext(SourceLineReader reader) {
        Index start = reader.line.getIndex();
        char peek = reader.line.peek();

        String operator = reader.line.matchNext(OperatorNode.symbolOperators());
        if(operator != null) {
            Token operatorToken = new Token(Token.Usage.OPERATOR, operator);
            operatorToken.setContext(start, reader.line.getIndex(), Source.this);
            return operatorToken;
        }

        String groupDelim = reader.line.matchNext(OperatorNode.symbolStartDelimiters());
        if (groupDelim != null) {
            //TODO
            // also, don't forget to handle context
        } else if (Character.isDigit(peek)) {
            return parseNumberLiteral(reader);
        } else if (peek == '"' || peek == '\'') {
            return parseStringLiteral(reader);
        } else {
            return parseIdentifier(reader);
        }
        throw new InternalError("Reached unreachable block");
    }

    private void throwParseError(Index start, Index stop, Token.Usage usage, String value, String reason) {
        Token errorBlock = new Token(usage, value);
        errorBlock.setContext(start, stop, Source.this);
        errorBlock.throwError(reason);
    }


    public boolean isIdentifierChar(char c) {
        return ('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z') ||  //common english letters
                ('0' <= c && c <= '9') ||   //numbers
                c == '_' ||                 //underscore
                ('\u0391' <= c && c <= '\u03ff') ||  //greek characters
                ('\u0400' <= c && c <= '\u04ff');  //cryllic characters
    }


    /**
     * gets the next line from the file, skipping any empty lines
     * @return the next non-empty line
     */
    public Line getNonemptyLine() {
        Line ret = getLine();
        if(ret.allSpaces()) return getNonemptyLine();
        else return ret;
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

    /**
     * returns all lines of the files from the startLine and ending at endLine
     * both are inclusive
     */
    public abstract List<String> getCodeBlock(int startLine, int endLine);

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