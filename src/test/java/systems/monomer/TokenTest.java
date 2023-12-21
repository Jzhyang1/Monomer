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
                "            break\n" +
                "    if good:\n" +
                "        @i"
        );


        Token token = source.parse();
        System.out.println(token);
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
        Source source = new SourceString("-(1,2,3)");
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
}
