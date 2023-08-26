package systems.monomer;

import org.junit.Test;
import systems.monomer.tokenizer.Source;
import systems.monomer.tokenizer.SourceFile;
import systems.monomer.tokenizer.SourceString;
import systems.monomer.tokenizer.Token;

import static org.junit.Assert.*;

public class SourceTest {
    @Test
    public void testStringSourceGetLine() {
        SourceString source = new SourceString("y = x + 500;");
        Source.Line line = source.getLine();
        assertEquals("test short string source get line", line.toString(), "(0)y = x + 500;\n");
    }

    @Test
    public void testShortStringSourceParse() {
        SourceString source = new SourceString("y = x + 500;");
        Token token = source.parse();
        assertEquals("test parse short string source", "GROUP block[\n\tIDENTIFIER y,\n\tOPERATOR =,\n\tIDENTIFIER x,\n\tOPERATOR +,\n\tINTEGER 500,\n\tOPERATOR ;\n\t]\n",
                token.toString());
    }

    @Test
    public void testShortStringSourceParse2() {
        SourceString source = new SourceString("\"Hello World!\"");
        Token token = source.parse();
        assertEquals("test parse short string source 2", "GROUP block[STRING_BUILDER null[STRING Hello World!]]",
                token.toString());
    }

    @Test
    public void testLongStringSourceParse() {
        SourceString source = new SourceString(
                "y = x + 500\n" +
                "io write(y)"
        );
        Token token = source.parse();
        assertEquals("test parse long string source", "GROUP block[\n\t" +
                "IDENTIFIER y,\n\tOPERATOR =,\n\tIDENTIFIER x,\n\tOPERATOR +,\n\tINTEGER 500,\n\tOPERATOR ;,\n\t" +
                "IDENTIFIER io,\n\tIDENTIFIER write,\n\tGROUP ()[IDENTIFIER y]\n\t" +
                "]\n",
                token.toString());
    }

    @Test
    public void testErrorFileSource() {
        String path = "samples/ErrorFile.m";

        Source source = new SourceFile(path);

        try {
            System.out.println(source.parse());
        } catch (RuntimeException e) {
            assertEquals("Test parse in error file source", "ERROR Missing end delimiter in " + path + ":\n" +
                            "1| if (nextStarting > starting {\n" +
                            " |    ^^^^^^^^^^^^^^^^^^^^^^^^^^^\n" +
                            "2| \ttokens.add(tokenize(source));\n" +
                            " | ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n" +
                            "3| } else if (nextStarting < starting) {\n" +
                            " | ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n" +
                            "4| \treturn tokens;\n" +
                            " | ^^^^^^^^^^^^^^^^\n" +
                            "5| } else {\n" +
                            " | ^^^^^^^^^\n" +
                            "6| \tline.skipSpaces();\n" +
                            " | ^^^^^^^^^^^^^^^^^^^^\n" +
                            "7| }\n" +
                            " | ^^\n",
                    e.getMessage());
        }
    }

    @Test
    public void testSmallFileSource() {
        String path = "samples/control-small.m";

        Source source = new SourceFile(path);

        Token token = source.parse();
        assertEquals("GROUP block[\n" +
                        "\tOPERATOR if,\n" +
                        "\tIDENTIFIER nextStarting,\n" +
                        "\tOPERATOR >,\n" +
                        "\tIDENTIFIER starting,\n" +
                        "\tOPERATOR :,\n" +
                        "\tGROUP block[\n" +
                        "\tIDENTIFIER tokens,\n" +
                        "\tIDENTIFIER add,\n" +
                        "\tGROUP ()[\n" +
                        "\tIDENTIFIER tokenize,\n" +
                        "\tGROUP ()[IDENTIFIER source]\n" +
                        "\t]\n" +
                        "\n" +
                        "\t]\n" +
                        ",\n" +
                        "\tOPERATOR ;,\n" +
                        "\tOPERATOR else,\n" +
                        "\tIDENTIFIER nextStarting,\n" +
                        "\tOPERATOR <,\n" +
                        "\tIDENTIFIER starting,\n" +
                        "\tOPERATOR :,\n" +
                        "\tGROUP block[\n" +
                        "\tIDENTIFIER return,\n" +
                        "\tIDENTIFIER tokens\n" +
                        "\t]\n" +
                        ",\n" +
                        "\tOPERATOR ;,\n" +
                        "\tIDENTIFIER else,\n" +
                        "\tOPERATOR :,\n" +
                        "\tGROUP block[\n" +
                        "\tIDENTIFIER line,\n" +
                        "\tIDENTIFIER skipSpaces,\n" +
                        "\tGROUP ()\n" +
                        "\t]\n" +
                        ",\n" +
                        "\tOPERATOR ;\n" +
                        "\t]\n",
                token.toString());
    }

    @Test
    public void testShortStringSourceParse3() {
        SourceString source = new SourceString("asterisk");
        Token token = source.parse();
        assertEquals("test parse short string source 3", "GROUP block[IDENTIFIER asterisk]",
                token.toString());
    }
}
