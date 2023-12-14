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
                "\tOPERATOR =[\n" +
                "\t\tIDENTIFIER x\n" +
                "\t\tLITERAL 0\n" +
                "\t]\n" +
                "\tOPERATOR =[\n" +
                "\t\tIDENTIFIER y\n" +
                "\t\tOPERATOR +[\n" +
                "\t\t\tLITERAL 1\n" +
                "\t\t\tIDENTIFIER x\n" +
                "\t\t]\n" +
                "\t]\n" +
                "\tOPERATOR =[\n" +
                "\t\tIDENTIFIER z\n" +
                "\t\tOPERATOR *[\n" +
                "\t\t\tIDENTIFIER y\n" +
                "\t\t\tIDENTIFIER x\n" +
                "\t\t]\n" +
                "\t]\n" +
                "\tOPERATOR @[\n" +
                "\t\tIDENTIFIER x\n" +
                "\t]\n" +
                "\tOPERATOR @[\n" +
                "\t\tIDENTIFIER y\n" +
                "\t]\n" +
                "\tOPERATOR @[\n" +
                "\t\tIDENTIFIER z\n" +
                "\t]\n" +
                "]", node.toString());
    }

    @Test
    public void testToNode3() {
        String path = "samples/control-small.m";

        Source source = new SourceFile(path);

        Token token = source.parse();
        Node node = token.toNode();
        assertEquals("condition file token toNode", "LITERAL block[\n" +
                "\tCONTROL_GROUP control[\n" +
                "\t\tLABEL if[\n" +
                "\t\t\tOPERATOR >[\n" +
                "\t\t\t\tIDENTIFIER nextStarting\n" +
                "\t\t\t\tIDENTIFIER starting\n" +
                "\t\t\t]\n" +
                "\t\t\tLITERAL block[\n" +
                "\t\t\t\tOPERATOR call[\n" +
                "\t\t\t\t\tOPERATOR field[\n" +
                "\t\t\t\t\t\tIDENTIFIER tokens\n" +
                "\t\t\t\t\t\tIDENTIFIER add\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t\tOPERATOR call[\n" +
                "\t\t\t\t\t\tIDENTIFIER tokenize\n" +
                "\t\t\t\t\t\tIDENTIFIER source\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t]\n" +
                "\t\t\t]\n" +
                "\t\t]\n" +
                "\t\tLABEL else[\n" +
                "\t\t\tOPERATOR <[\n" +
                "\t\t\t\tIDENTIFIER nextStarting\n" +
                "\t\t\t\tIDENTIFIER starting\n" +
                "\t\t\t]\n" +
                "\t\t\tLITERAL block[\n" +
                "\t\t\t\tOPERATOR field[\n" +
                "\t\t\t\t\tIDENTIFIER return\n" +
                "\t\t\t\t\tIDENTIFIER tokens\n" +
                "\t\t\t\t]\n" +
                "\t\t\t]\n" +
                "\t\t]\n" +
                "\t\tLABEL else[\n" +
                "\t\t\tLITERAL bool\n" +
                "\t\t\tLITERAL block[\n" +
                "\t\t\t\tOPERATOR call[\n" +
                "\t\t\t\t\tOPERATOR field[\n" +
                "\t\t\t\t\t\tIDENTIFIER line\n" +
                "\t\t\t\t\t\tIDENTIFIER skipSpaces\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t\tLITERAL block\n" +
                "\t\t\t\t]\n" +
                "\t\t\t]\n" +
                "\t\t]\n" +
                "\t]\n" +
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
                "            break\n" +
                "    if good:\n" +
                "        @i"
        );


        Token token = source.parse();
        Node node = token.toNode();
        assertEquals("nested condition tokens toNode", "LITERAL block[\n" +
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
                "                        LITERAL block[\n" +
                "                            CONTROL_GROUP control[\n" +
                "                                LABEL if[\n" +
                "                                    OPERATOR ![\n" +
                "                                        OPERATOR ?[\n" +
                "                                            OPERATOR %[\n" +
                "                                                IDENTIFIER i\n" +
                "                                                IDENTIFIER j\n" +
                "                                            ]\n" +
                "                                        ]\n" +
                "                                    ]\n" +
                "                                    LITERAL block[\n" +
                "                                        OPERATOR =[\n" +
                "                                            IDENTIFIER good\n" +
                "                                            IDENTIFIER false\n" +
                "                                        ]\n" +
                "                                        OPERATOR break\n" +
                "                                    ]\n" +
                "                                ]\n" +
                "                            ]\n" +
                "                        ]\n" +
                "                    ]\n" +
                "                ]\n" +
                "            ]\n" +
                "            CONTROL_GROUP control[\n" +
                "                LABEL if[\n" +
                "                    IDENTIFIER good\n" +
                "                    OPERATOR @[\n" +
                "                        IDENTIFIER i\n" +
                "                    ]\n" +
                "                ]\n" +
                "            ]\n" +
                "        ]\n" +
                "    ]\n" +
                "]", node.toString());
    }
}
