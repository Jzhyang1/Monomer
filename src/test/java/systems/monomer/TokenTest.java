package systems.monomer;

import org.junit.Test;
import systems.monomer.syntaxTree.Node;
import systems.monomer.tokenizer.Source;
import systems.monomer.tokenizer.SourceFile;
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
        String path = "samples/OperatorFile.m";

        Source source = new SourceFile(path);

        Token token = source.parse();
        Node node = token.toNode();
        assertEquals("identifier token toNode", "OPERATOR ; ;[\n" +
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
                "]", node.toString());
    }
}
