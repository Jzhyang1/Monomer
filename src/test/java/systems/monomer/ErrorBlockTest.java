package systems.monomer;

import org.junit.*;
import static org.junit.Assert.*;

import systems.monomer.errorhandling.Context;
import systems.monomer.errorhandling.ErrorBlock;
import systems.monomer.tokenizer.Source;
import systems.monomer.tokenizer.SourceFile;
import systems.monomer.tokenizer.SourceString;

public class ErrorBlockTest {
    @Test
    public void testShortStringSource() {
        SourceString source = new SourceString("y = x + 500x;");
        Context context = new Context(0, 8, 0, 0, 11, 11, source);

        ErrorBlock errorBlock = new ErrorBlock();
        errorBlock.setContext(context);

        try {
            errorBlock.throwError("Bad Number Format");
        } catch (RuntimeException e) {
            assertEquals("Error-printing for short string input",
                    "ERROR Bad Number Format in String source:\n" +
                            " |\n"+
                            "1| y = x + 500x;\n" +
                            " |         ^^^\n",
                    e.getMessage());
        }
    }

    @Test
    public void testLongStringSource() {
        SourceString source = new SourceString(
                "if (nextStarting > starting {\n" +
                "\ttokens.add(tokenize(source));\n" +
                "} else if (nextStarting < starting) {\n" +
                "\treturn tokens;\n" +
                "} else {\n" +
                "\tline.skipSpaces();\n" +
                "}"
        );
        Context context = new Context(0, 3, 6, 0, source);

        ErrorBlock errorBlock = new ErrorBlock();
        errorBlock.setContext(context);

        try {
            errorBlock.throwError("Syntax");
        } catch (RuntimeException e) {
            assertEquals("ERROR Syntax in String source:\n"  +
                            " |\n"+
                            "1| if (nextStarting > starting {\n" +
                            " |    ^^^^^^^^^^^^^^^^^^^^^^^^^^\n" +
                            "2| \ttokens.add(tokenize(source));\n" +
                            " | ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n" +
                            "3| } else if (nextStarting < starting) {\n" +
                            " | ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n" +
                            "4| \treturn tokens;\n" +
                            " | ^^^^^^^^^^^^^^^\n" +
                            "5| } else {\n" +
                            " | ^^^^^^^^\n" +
                            "6| \tline.skipSpaces();\n" +
                            " | ^^^^^^^^^^^^^^^^^^^\n" +
                            "7| }\n" +
                            " | \n",
                    e.getMessage());
        }
    }

    @Test
    public void testFileSource() {
        String path = "samples/ErrorFile.m";

        Source source = new SourceFile(path);
        Context context = new Context(0, 3, 6, 0, source);

        ErrorBlock errorBlock = new ErrorBlock();
        errorBlock.setContext(context);

        try {
            errorBlock.throwError("Syntax");
        } catch (RuntimeException e) {
            assertEquals("ERROR Syntax in " + path + ":\n"+
                            " |\n" +
                            "1| if (nextStarting > starting {\n" +
                            " |    ^^^^^^^^^^^^^^^^^^^^^^^^^^\n" +
                            "2| \ttokens.add(tokenize(source));\n" +
                            " | ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n" +
                            "3| } else if (nextStarting < starting) {\n" +
                            " | ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n" +
                            "4| \treturn tokens;\n" +
                            " | ^^^^^^^^^^^^^^^\n" +
                            "5| } else {\n" +
                            " | ^^^^^^^^\n" +
                            "6| \tline.skipSpaces();\n" +
                            " | ^^^^^^^^^^^^^^^^^^^\n" +
                            "7| }\n" +
                            " | \n",
                    e.getMessage());
        }
    }
}
