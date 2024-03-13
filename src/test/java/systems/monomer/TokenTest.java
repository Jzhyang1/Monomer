package systems.monomer;

import org.junit.Test;
import systems.monomer.syntaxtree.Node;
import systems.monomer.tokenizer.Source;
import systems.monomer.tokenizer.SourceFile;
import systems.monomer.tokenizer.SourceString;
import systems.monomer.tokenizer.Token;

import static org.junit.Assert.*;

public class TokenTest {
    @Test
    public void testToNode1() {
        Token token = new Token(Token.Usage.IDENTIFIER, "x");
        Node node = token.toNode();
        assertEquals("identifier token toNode", node.toString(), "IDENTIFIER x");
    }

    @Test
    public void testToNode2() {
        String path = "samples/operator-sample.m";

        Source source = new SourceFile(path);

        Token token = source.parse();
        Node node = token.toNode();
        System.out.println(node);
        assertEquals("operator file token toNode", "LITERAL block[\n" +
                "    OPERATOR =[\n" +
                "        IDENTIFIER x\n" +
                "        LITERAL 0\n" +
                "    ]\n" +
                "    OPERATOR =[\n" +
                "        IDENTIFIER y\n" +
                "        OPERATOR +[\n" +
                "            LITERAL 1\n" +
                "            IDENTIFIER x\n" +
                "        ]\n" +
                "    ]\n" +
                "    OPERATOR =[\n" +
                "        IDENTIFIER z\n" +
                "        OPERATOR *[\n" +
                "            IDENTIFIER y\n" +
                "            IDENTIFIER x\n" +
                "        ]\n" +
                "    ]\n" +
                "    OPERATOR @[\n" +
                "        IDENTIFIER x\n" +
                "    ]\n" +
                "    OPERATOR @[\n" +
                "        IDENTIFIER y\n" +
                "    ]\n" +
                "    OPERATOR @[\n" +
                "        IDENTIFIER z\n" +
                "    ]\n" +
                "]", node.toString());
    }

    @Test
    public void testToNode3() {
        String path = "samples/control-small.m";

        Source source = new SourceFile(path);

        Token token = source.parse();
        Node node = token.toNode();
        assertEquals("condition file token toNode", "LITERAL block[\n" +
                "    CONTROL_GROUP control[\n" +
                "        LABEL if[\n" +
                "            OPERATOR >[\n" +
                "                IDENTIFIER nextStarting\n" +
                "                IDENTIFIER starting\n" +
                "            ]\n" +
                "            OPERATOR call[\n" +
                "                OPERATOR field[\n" +
                "                    IDENTIFIER tokens\n" +
                "                    IDENTIFIER add\n" +
                "                ]\n" +
                "                OPERATOR call[\n" +
                "                    IDENTIFIER tokenize\n" +
                "                    IDENTIFIER source\n" +
                "                ]\n" +
                "            ]\n" +
                "        ]\n" +
                "        LABEL else[\n" +
                "            OPERATOR <[\n" +
                "                IDENTIFIER nextStarting\n" +
                "                IDENTIFIER starting\n" +
                "            ]\n" +
                "            OPERATOR return[\n" +
                "                IDENTIFIER tokens\n" +
                "            ]\n" +
                "        ]\n" +
                "        LABEL else[\n" +
                "            LITERAL bool\n" +
                "            OPERATOR call[\n" +
                "                OPERATOR field[\n" +
                "                    IDENTIFIER line\n" +
                "                    IDENTIFIER skipSpaces\n" +
                "                ]\n" +
                "                LITERAL block\n" +
                "            ]\n" +
                "        ]\n" +
                "    ]\n" +
                "]", node.toString());
    }

    @Test
    public void testToNode4() {
        Source source = new SourceString(""+
                "for i in 1 ... 100:\n" +
                "    good = true\n" +
                "    for j in 2 ... i */ 2:\n" +
                "        if !?(i%j):\n" +
                "            good = false\n" +
                "    if good:\n" +
                "        @i"
        );


        Token token = source.parse();
        Node node = token.toNode();
        assertEquals("LITERAL block[\n" +
                "    CONTROL_GROUP control[\n" +
                "        LABEL for[\n" +
                "            OPERATOR in[\n" +
                "                IDENTIFIER i\n" +
                "                OPERATOR ...[\n" +
                "                    LITERAL 1\n" +
                "                    LITERAL 100\n" +
                "                ]\n" +
                "            ]\n" +
                "            LITERAL block[\n" +
                "                OPERATOR =[\n" +
                "                    IDENTIFIER good\n" +
                "                    IDENTIFIER true\n" +
                "                ]\n" +
                "                CONTROL_GROUP control[\n" +
                "                    LABEL for[\n" +
                "                        OPERATOR in[\n" +
                "                            IDENTIFIER j\n" +
                "                            OPERATOR ...[\n" +
                "                                LITERAL 2\n" +
                "                                OPERATOR */[\n" +
                "                                    IDENTIFIER i\n" +
                "                                    LITERAL 2\n" +
                "                                ]\n" +
                "                            ]\n" +
                "                        ]\n" +
                "                        CONTROL_GROUP control[\n" +
                "                            LABEL if[\n" +
                "                                OPERATOR ![\n" +
                "                                    OPERATOR ?[\n" +
                "                                        OPERATOR %[\n" +
                "                                            IDENTIFIER i\n" +
                "                                            IDENTIFIER j\n" +
                "                                        ]\n" +
                "                                    ]\n" +
                "                                ]\n" +
                "                                OPERATOR =[\n" +
                "                                    IDENTIFIER good\n" +
                "                                    IDENTIFIER false\n" +
                "                                ]\n" +
                "                            ]\n" +
                "                        ]\n" +
                "                    ]\n" +
                "                ]\n" +
                "                CONTROL_GROUP control[\n" +
                "                    LABEL if[\n" +
                "                        IDENTIFIER good\n" +
                "                        OPERATOR @[\n" +
                "                            IDENTIFIER i\n" +
                "                        ]\n" +
                "                    ]\n" +
                "                ]\n" +
                "            ]\n" +
                "        ]\n" +
                "    ]\n" +
                "]", node.toString());
    }

    @Test
    public void testToNode5() {
        Source source = new SourceString("\"hello\"");
        Token token = source.parse();
        Node node = token.toNode();
        assertEquals("string token toNode", "LITERAL block[\n" +
                "    LITERAL stringbuilder[\n" +
                "        LITERAL string\n" +
                "    ]\n" +
                "]", node.toString());
        assertEquals("string token toNode value", "\"hello\"", node.getChildren().get(0).getChildren().get(0).toString());
    }

    @Test
    public void testToNode6() {
        Source source = new SourceString("(1,2,3)");
        Token token = source.parse();
        Node node = token.toNode();
        assertEquals("tuple token toNode", "LITERAL block[\n" +
                "    LITERAL tuple[\n" +
                "        LITERAL 1\n" +
                "        LITERAL 2\n" +
                "        LITERAL 3\n" +
                "    ]\n" +
                "]", node.toString());
    }

    @Test
    public void testToNode7() {
        Source source = new SourceString("" +
                "for i in 1 ... 100:\n" +
                "    good = true\n" +
                "    for j in 2 ... i */ 2:\n" +
                "        if !?(i%j):\n" +
                "            good = false\n" +
                "            break\n" +
                "    if good:\n" +
                "        @i"
        );
        assertEquals("Tokenize for loop", "GROUP block[\n" +
                "\tOPERATOR for,\n" +
                "\tIDENTIFIER i,\n" +
                "\tOPERATOR in,\n" +
                "\tINTEGER 1,\n" +
                "\tOPERATOR ...,\n" +
                "\tINTEGER 100,\n" +
                "\tOPERATOR :,\n" +
                "\tGROUP block[\n" +
                "\tIDENTIFIER good,\n" +
                "\tOPERATOR =,\n" +
                "\tIDENTIFIER true,\n" +
                "\tOPERATOR ;,\n" +
                "\tOPERATOR for,\n" +
                "\tIDENTIFIER j,\n" +
                "\tOPERATOR in,\n" +
                "\tINTEGER 2,\n" +
                "\tOPERATOR ...,\n" +
                "\tIDENTIFIER i,\n" +
                "\tOPERATOR */,\n" +
                "\tINTEGER 2,\n" +
                "\tOPERATOR :,\n" +
                "\tGROUP block[\n" +
                "\tOPERATOR if,\n" +
                "\tOPERATOR !,\n" +
                "\tOPERATOR ?,\n" +
                "\tGROUP ()[\n" +
                "\tIDENTIFIER i,\n" +
                "\tOPERATOR %,\n" +
                "\tIDENTIFIER j\n" +
                "\t]\n" +
                ",\n" +
                "\tOPERATOR :,\n" +
                "\tGROUP block[\n" +
                "\tIDENTIFIER good,\n" +
                "\tOPERATOR =,\n" +
                "\tIDENTIFIER false,\n" +
                "\tOPERATOR ;,\n" +
                "\tOPERATOR break\n" +
                "\t]\n" +
                ",\n" +
                "\tOPERATOR ;\n" +
                "\t]\n" +
                ",\n" +
                "\tOPERATOR ;,\n" +
                "\tOPERATOR if,\n" +
                "\tIDENTIFIER good,\n" +
                "\tOPERATOR :,\n" +
                "\tGROUP block[\n" +
                "\tOPERATOR @,\n" +
                "\tIDENTIFIER i\n" +
                "\t]\n" +
                ",\n" +
                "\tOPERATOR ;\n" +
                "\t]\n" +
                ",\n" +
                "\tOPERATOR ;\n" +
                "\t]\n", source.parse().toString());
    }

    @Test
    public void testToNode8() {
        Source source = new SourceString("" +
                "if a:\n" +
                "    @a\n" +
                "else b:\n" +
                "    @b\n" +
                "else:\n" +
                "    @c\n"
        );
        assertEquals("Tokenize if else", "GROUP block[\n" +
                "\tOPERATOR if,\n" +
                "\tIDENTIFIER a,\n" +
                "\tOPERATOR :,\n" +
                "\tGROUP block[\n" +
                "\tOPERATOR @,\n" +
                "\tIDENTIFIER a\n" +
                "\t]\n" +
                ",\n" +
                "\tOPERATOR ;,\n" +
                "\tOPERATOR else,\n" +
                "\tIDENTIFIER b,\n" +
                "\tOPERATOR :,\n" +
                "\tGROUP block[\n" +
                "\tOPERATOR @,\n" +
                "\tIDENTIFIER b\n" +
                "\t]\n" +
                ",\n" +
                "\tOPERATOR ;,\n" +
                "\tOPERATOR else,\n" +
                "\tOPERATOR :,\n" +
                "\tGROUP block[\n" +
                "\tOPERATOR @,\n" +
                "\tIDENTIFIER c\n" +
                "\t]\n" +
                ",\n" +
                "\tOPERATOR ;\n" +
                "\t]\n", source.parse().toString());
    }

    @Test
    public void testToNode9() {
        Source source = new SourceString("n = 102 \n" +
                "if n % 2 == 0: io write(\"even\\n\") \n" +
                "else n % 3 == 0: io write(\"divisible by 3\\n\") \n" +
                "all: io write(\"divisible by 6\\n\") \n" +
                "any: io write(\"divisible by 2 or 3\\n\") \n" +
                "else: io write(\"not divisible by 2 or 3\")"
        );
        Token token = source.parse();
        assertEquals("Tokenize if-else-all-any", "GROUP block[\n" +
                "\tIDENTIFIER n,\n" +
                "\tOPERATOR =,\n" +
                "\tINTEGER 102,\n" +
                "\tOPERATOR ;,\n" +
                "\tOPERATOR if,\n" +
                "\tIDENTIFIER n,\n" +
                "\tOPERATOR %,\n" +
                "\tINTEGER 2,\n" +
                "\tOPERATOR ==,\n" +
                "\tINTEGER 0,\n" +
                "\tOPERATOR :,\n" +
                "\tIDENTIFIER io,\n" +
                "\tIDENTIFIER write,\n" +
                "\tGROUP ()[STRING_BUILDER null[STRING even\n" +
                "]],\n" +
                "\tOPERATOR ;,\n" +
                "\tOPERATOR else,\n" +
                "\tIDENTIFIER n,\n" +
                "\tOPERATOR %,\n" +
                "\tINTEGER 3,\n" +
                "\tOPERATOR ==,\n" +
                "\tINTEGER 0,\n" +
                "\tOPERATOR :,\n" +
                "\tIDENTIFIER io,\n" +
                "\tIDENTIFIER write,\n" +
                "\tGROUP ()[STRING_BUILDER null[STRING divisible by 3\n" +
                "]],\n" +
                "\tOPERATOR ;,\n" +
                "\tOPERATOR all,\n" +
                "\tOPERATOR :,\n" +
                "\tIDENTIFIER io,\n" +
                "\tIDENTIFIER write,\n" +
                "\tGROUP ()[STRING_BUILDER null[STRING divisible by 6\n" +
                "]],\n" +
                "\tOPERATOR ;,\n" +
                "\tOPERATOR any,\n" +
                "\tOPERATOR :,\n" +
                "\tIDENTIFIER io,\n" +
                "\tIDENTIFIER write,\n" +
                "\tGROUP ()[STRING_BUILDER null[STRING divisible by 2 or 3\n" +
                "]],\n" +
                "\tOPERATOR ;,\n" +
                "\tOPERATOR else,\n" +
                "\tOPERATOR :,\n" +
                "\tIDENTIFIER io,\n" +
                "\tIDENTIFIER write,\n" +
                "\tGROUP ()[STRING_BUILDER null[STRING not divisible by 2 or 3]]\n" +
                "\t]\n", token.toString());

        Node node = token.toNode();
        assertEquals("Node if-else-all-any", "LITERAL block[\n" +
                "    OPERATOR =[\n" +
                "        IDENTIFIER n\n" +
                "        LITERAL 102\n" +
                "    ]\n" +
                "    CONTROL_GROUP control[\n" +
                "        LABEL if[\n" +
                "            OPERATOR ==[\n" +
                "                OPERATOR %[\n" +
                "                    IDENTIFIER n\n" +
                "                    LITERAL 2\n" +
                "                ]\n" +
                "                LITERAL 0\n" +
                "            ]\n" +
                "            OPERATOR call[\n" +
                "                OPERATOR field[\n" +
                "                    IDENTIFIER io\n" +
                "                    IDENTIFIER write\n" +
                "                ]\n" +
                "                LITERAL string \"even\n" +
                "\"\n" +
                "            ]\n" +
                "        ]\n" +
                "        LABEL else[\n" +
                "            OPERATOR ==[\n" +
                "                OPERATOR %[\n" +
                "                    IDENTIFIER n\n" +
                "                    LITERAL 3\n" +
                "                ]\n" +
                "                LITERAL 0\n" +
                "            ]\n" +
                "            OPERATOR call[\n" +
                "                OPERATOR field[\n" +
                "                    IDENTIFIER io\n" +
                "                    IDENTIFIER write\n" +
                "                ]\n" +
                "                LITERAL string \"divisible by 3\n" +
                "\"\n" +
                "            ]\n" +
                "        ]\n" +
                "        LABEL all[\n" +
                "            LITERAL bool\n" +
                "            OPERATOR call[\n" +
                "                OPERATOR field[\n" +
                "                    IDENTIFIER io\n" +
                "                    IDENTIFIER write\n" +
                "                ]\n" +
                "                LITERAL string \"divisible by 6\n" +
                "\"\n" +
                "            ]\n" +
                "        ]\n" +
                "        LABEL any[\n" +
                "            LITERAL bool\n" +
                "            OPERATOR call[\n" +
                "                OPERATOR field[\n" +
                "                    IDENTIFIER io\n" +
                "                    IDENTIFIER write\n" +
                "                ]\n" +
                "                LITERAL string \"divisible by 2 or 3\n" +
                "\"\n" +
                "            ]\n" +
                "        ]\n" +
                "        LABEL else[\n" +
                "            LITERAL bool\n" +
                "            OPERATOR call[\n" +
                "                OPERATOR field[\n" +
                "                    IDENTIFIER io\n" +
                "                    IDENTIFIER write\n" +
                "                ]\n" +
                "                LITERAL string \"not divisible by 2 or 3\"\n" +
                "            ]\n" +
                "        ]\n" +
                "    ]\n" +
                "]", node.toString());
    }
}
