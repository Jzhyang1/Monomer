import java.util.ArrayList;

public class Tokenizer {

    public static Token tokenize(Source source) {
        SourceLine line = source.getLine();
        int starting = line.startingSpaces();
        LineIndex tokenStart = line.getIndex();
        line.skipSpaces();

        while (!source.eof()) {
            if (line.peek() == '\n') {
                return tokenize(source);
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
}
