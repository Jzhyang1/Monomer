package systems.monomer.tokenizer;

import systems.monomer.Constants;
import systems.monomer.errorhandling.Index;
import systems.monomer.syntaxtree.OperatorNode;

import java.util.*;

public abstract class Source {
    public static class Line {
        public static final Map<Character, Integer> SPACE_CHARS = new HashMap<>() {{
            put(' ', 1);
            put('\t', Constants.TAB_SIZE);
        }};

        private String line;
        private int position;
        private int x = 0, y;

        public Line(String line, int y, int position) {
            this.line = line + "\n";
            this.y = y;
            this.position = position;
        }

        public Index getIndex() {
            return new Index(x, y, position);
        }

        public char peek() {
            return line.charAt(x);
        }

        public char get() {
            ++position;
            return line.charAt(x++);
        }

        public String get(int num) {
            x += num;
            position += num;
            return line.substring(x - num, x);
        }

        public void unget() {
            --position;
            --x;
        }

        /**
         * counts the number of starting spaces
         *
         * @return the number of starting spaces
         */
        public int startingSpaces() {
            int startingSpaces = 0;
            for (int i = 0; i < line.length() && SPACE_CHARS.containsKey(line.charAt(i)); ++i) {
                startingSpaces += SPACE_CHARS.get(line.charAt(i));
            }
            return startingSpaces;
        }

        /**
         * @return the number of skipped spaces
         */
        public int skipSpaces() {
            int spaces = 0;
            while (SPACE_CHARS.containsKey(peek())) {
                spaces += SPACE_CHARS.get(get());
            }
            return spaces;
        }

        /**
         * @param num the maximum number of spaces to skip
         * @return the number of spaces skipped
         */
        public int skipSpaces(int num) {
            int spaces = 0;
            while (SPACE_CHARS.containsKey(peek())) {
                int add = SPACE_CHARS.get(peek());
                if (spaces + add > num) break;

                spaces += add;
                get();
            }
            return spaces;
        }

        private boolean isNext(String next) {
            for (int i = 0; i < next.length(); ++i) {
                if (line.charAt(x + i) != next.charAt(i)) return false;
            }
            return true;
        }

        public String matchNext(Collection<String> next) {
            String bestOption = null;
            for (String option : next) {
                if (isNext(option)) {
                    if (bestOption == null || bestOption.length() < option.length())
                        bestOption = option;
                }
            }

            if (bestOption != null) {
                x += bestOption.length();
                position += bestOption.length();
            }
            return bestOption;
        }

        public boolean matchNext(String next) {
            if (isNext(next)) {
                x += next.length();
                position += next.length();
                return true;
            }
            return false;
        }

        public boolean eol() {
            return line.length() == x;
        }

        /**
         * sets the pointer to the newline
         */
        public void toEnd() {
            position += line.length() - 1 - x;
            x = line.length() - 1;
        }

        public boolean allSpaces() {
            return line.isBlank();
        }

        public String toString() {
            return "(%d)%s".formatted(y, line);
        }
    }

    private static final int DEFAULT_BUFFER_COUNT = 100;
    private int y = 0;
    protected Deque<Line> buffer = new LinkedList<>();
    private Line line = null;


    public Token parseBlock() {
        Token ret = new Token(Token.Usage.GROUP, "block");
        Index start = line.getIndex();
        int startingSpaces = line.startingSpaces();

        while (!(eof() && line.eol())) {
            line.skipSpaces();
            char peek = line.peek();
            if (peek == '\\') {
                //comments
                line.get();
                switch (line.peek()) {
                    case '\\' -> {
                        //comment to end-of-line
                        line.toEnd();
                    }
                    case '\n' -> {
                        //escape newline
                        Token nextLine = parse();
                        ret.addAll(nextLine);
                    }
                    default -> {
                        //comment next symbol
                        parseNext();
                    }
                }
            } else if (peek == '\n') {
                if (eof()) break;
                line.get();

                //new line
                do {
                    nextLine();
                } while (!eof() && line.allSpaces());
                int nextStarting = line.startingSpaces();

                if (nextStarting > startingSpaces) {
                    //child group
                    ret.add(parseBlock());
                } else if (nextStarting < startingSpaces) {
                    break;
                } else {
                    Token lastToken = ret.getLast();
                    if (!(lastToken.getUsage() == Token.Usage.OPERATOR &&
                            OperatorNode.isBreaking(lastToken.getValue()))) {
                        ret.add(new Token(Token.Usage.OPERATOR, ";").with(line.getIndex(), line.getIndex(), this));
                    }
                }
            } else if (OperatorNode.signEndDelimiters().contains(peek)) {
                break;
            } else {
                //identifier, operator, or number
                ret.add(parseNext());
            }
        }

        ret.setContext(start, line.getIndex(), this);
        return ret;
    }

    public Token parse() {
        nextLine();
        return parseBlock();
    }

    /**
     * assumes that the first quote is not handled,
     * handles all escape sequences,
     * and removes the appropriate number of starting spaces at the beginning of each line
     *
     * @return the string literal
     */
    public Token parseStringLiteral() {
        Index lineStartingIndex = line.getIndex();

        char delim = line.get();
        int initialStartingSpaces = line.startingSpaces();
        int startingSpaces = initialStartingSpaces + Constants.TAB_SIZE;

        Token ret = new Token(Token.Usage.STRING_BUILDER);
        StringBuilder strbuild = new StringBuilder();
        Index currentStartingIndex = line.getIndex();

        boolean multilineString = line.peek() == '\n';
        if (multilineString) {
            nextLine();
            line.skipSpaces(startingSpaces);
        }

        while (line.peek() != delim) {
            switch (line.peek()) {
                case '\\' -> {
                    final String INTERPOLATION_ERROR = "unexpected escape sequence.\n" +
                            "If the sequence is intended to be interpolated, please wrap it using \\c(...) for chars or \\(...) \n" +
                            "If the sequence is a Unicode/ASCII character, please use the Unicode/ASCII sequence \\u____ or \\a__";

                    line.get();  //removes the escape char
                    char nextChar = line.get();
                    switch (nextChar) {
                        case '\\', '"', '\'' -> strbuild.append(nextChar);
                        case 'n' -> strbuild.append('\n');
                        case 't' -> strbuild.append('\t');
                        case '\n' -> {
                            line = getLine();
                            line.skipSpaces();
                        }
                        case 'a' -> {
                            Index start = line.getIndex();
                            String num = line.get(2);
                            Index stop = line.getIndex();

                            try {
                                char c = (char) Integer.parseInt(num, 16);
                                strbuild.append(c);
                            } catch (NumberFormatException e) {
                                throwParseError(start, stop, Token.Usage.STRING, num, "Invalid ASCII character format");
                            }
                        }
                        case 'u' -> {
                            Index start = line.getIndex();
                            String num = line.get(4);
                            Index stop = line.getIndex();

                            try {
                                char c = (char) Integer.parseInt(num, 16);
                                strbuild.append(c);
                            } catch (NumberFormatException e) {
                                throwParseError(start, stop, Token.Usage.STRING, num, "Invalid UNICODE character format");
                            }
                        }
                        case 'c' -> {
                            //interpolation via \c(CHARVALUE)
                            if (!strbuild.isEmpty()) ret.add(new Token(Token.Usage.STRING, strbuild.toString()));
                            Token interpolate = parseNext();

                            if (interpolate.getUsage() != Token.Usage.GROUP) {
                                throwParseError(interpolate, INTERPOLATION_ERROR);
                            }

                            Token charToken = new Token(Token.Usage.CHARACTER_FROM_INT);
                            charToken.add(interpolate);
                            ret.add(charToken);
                        }
                        default -> {
                            //interpolation via \(VALUE)
                            line.unget();
                            if (!strbuild.isEmpty()) ret.add(new Token(Token.Usage.STRING, strbuild.toString()));
                            Token interpolate = parseNext();

                            if (interpolate.getUsage() != Token.Usage.GROUP) {
                                throwParseError(interpolate, INTERPOLATION_ERROR);
                            }
                            ret.add(interpolate);
                        }
                    }
                }
                case '\n' -> {
                    if (!multilineString)
                        throwParseError(lineStartingIndex, line.getIndex(), Token.Usage.STRING_BUILDER, "string", "Attempting to spread single line string across multiple lines. \nIf multiple lines are needed, type a newline immediately after the initial quotation mark");

                    strbuild.append(line.get());
                    line = getLine();
                    Index start = line.getIndex();
                    int spaces = line.skipSpaces(startingSpaces);

                    boolean isEmpty = line.allSpaces(), failEmptyCheck = !isEmpty;
                    boolean isTabbed = spaces == startingSpaces, failTabCheck = !isTabbed;
                    boolean isEnd = line.peek() == delim, failEndCheck = !(multilineString && isEnd && spaces != initialStartingSpaces);

                    if (failEmptyCheck && failTabCheck && failEndCheck) {
                        Index stop = line.getIndex();
                        if (isEnd)
                            throwParseError(start, stop, Token.Usage.STRING, strbuild.toString(), "End delimiter should have the same amount of tabbing as the line of the start delimiter");
                        else
                            throwParseError(start, stop, Token.Usage.STRING, strbuild.toString(), "Insufficient spacing for line of string");
                    }
                }
                default -> strbuild.append(line.get());
            }
        }

        line.get();
        if (!strbuild.isEmpty())
            ret.add(new Token(Token.Usage.STRING, strbuild.toString()).with(currentStartingIndex, line.getIndex(), this));
        return ret.with(lineStartingIndex, line.getIndex(), this);
    }

    public Token parseNumberLiteral() {
        Index start = line.getIndex();

        StringBuilder strbuild = new StringBuilder();
        boolean hasE = false, hasDot = false;
        boolean isE = false, isDot = false;

        while (Character.isDigit(line.peek()) ||
                (isE = (line.peek() == 'e' || line.peek() == 'E')) ||
                (isDot = (line.peek() == '.'))) {
            if (isE && hasE) {
                throwParseError(start, line.getIndex(), Token.Usage.FLOAT, strbuild.toString(), "Duplicate occurrence of 'E' in float literal");
            }
            if (isDot && hasDot) {
                throwParseError(start, line.getIndex(), Token.Usage.FLOAT, strbuild.toString(), "Duplicate occurrence of '.' in float literal");
            }
            if (hasE && isDot) {
                throwParseError(start, line.getIndex(), Token.Usage.FLOAT, strbuild.toString(), "'E' before '.' in float literal");
            }

            hasE = isE;
            hasDot = isDot;
            strbuild.append(line.get());

            if (isE && line.peek() == '-') strbuild.append(line.get());
        }

        return new Token(hasE || hasDot ? Token.Usage.FLOAT : Token.Usage.INTEGER, strbuild.toString())
                .with(start, line.getIndex(), Source.this);
    }

    public Token parseIdentifier() {
        Index start = line.getIndex();
        StringBuilder strbuild = new StringBuilder();
        while (isIdentifierChar(line.peek())) {
            strbuild.append(line.get());
        }
        return new Token(Token.Usage.IDENTIFIER, strbuild.toString())
                .with(start, line.getIndex(), Source.this);
    }

    /**
     * returns either the next string literal, int literal, identifier, group, or operator
     *
     * @return the next token from the above list
     */
    public Token parseNext() {
        Index start = line.getIndex();
        char peek = line.peek();

        String operator = line.matchNext(OperatorNode.symbolOperators());
        if (operator != null) {
            return new Token(Token.Usage.OPERATOR, operator)
                    .with(start, line.getIndex(), Source.this);
        }

        if (OperatorNode.signStartDelimiters().contains(peek)) {
            line.get(); //clear the delimiter
            Token ret = parseBlock().with(start, line.getIndex(), Source.this);
            char endDelim = line.get();
            if (!OperatorNode.signEndDelimiters().contains(endDelim))
                throwParseError(ret, "Missing end delimiter");
            return ret.with("" + peek + endDelim);
        } else if (Character.isDigit(peek)) {
            return parseNumberLiteral();
        } else if (peek == '"' || peek == '\'') {
            return parseStringLiteral();
        } else if (isIdentifierChar(peek)) {
            return parseIdentifier();
        } else {
            throwParseError(start, line.getIndex(), Token.Usage.GROUP, "token", "Invalid character while parsing");
            return null;
        }
    }

    private void throwParseError(Index start, Index stop, Token.Usage usage, String value, String reason) {
        new Token(usage, value)
                .with(start, stop, Source.this)
                .throwError(reason);
    }

    private void throwParseError(Token token, String reason) {
        token.throwError(reason);
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
     *
     * @return the next non-empty line
     */
    public Line getNonemptyLine() {
        Line ret = getLine();
        if (ret.allSpaces()) return getNonemptyLine();
        else return ret;
    }

    public void nextNonemptyLine() {
        line = getNonemptyLine();
    }

    public Line getLine() {
        if (eof()) {
            Index endpoint = new Index(0, y, getPosition());
            throwParseError(endpoint, endpoint, Token.Usage.GROUP, "",
                    "Unexpected EOF while parsing.\n" +
                            "This may be because of an unclosed parenthesis");
        }
        Line ret = buffer.remove();
        ++y;

        if (buffer.isEmpty()) {
            bufferLines(DEFAULT_BUFFER_COUNT);
        }
        return ret;
    }

    public void nextLine() {
        line = getLine();
    }

    public void ungetLine(Line sourceLine) {
        buffer.push(sourceLine);
        --y;
    }

    public void ungetLine() {
        ungetLine(line);
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
        return y + 1;
    }

    protected abstract int getPosition();

    public abstract String getTitle();

    protected void init() {
        bufferLines(DEFAULT_BUFFER_COUNT);
    }

    protected abstract void bufferLines(int num);
}
