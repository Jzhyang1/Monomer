package systems.monomer;

import org.junit.Test;
import systems.monomer.syntaxtree.Node;
import systems.monomer.tokenizer.Source;
import systems.monomer.tokenizer.SourceFile;
import systems.monomer.tokenizer.SourceString;
import systems.monomer.tokenizer.Token;

import static org.junit.Assert.*;

public class InterpretTest {
    @Test
    public void testInterpret1() {
        Source source = new SourceString("1+1");
        Token token = source.parse();
        Node node = token.toNode();
        assertEquals("interpret 1+1", "(2)", node.interpretValue().valueString());
    }
    @Test
    public void testInterpret2() {
        Source source = new SourceString("@\"hello world\"");
        Token token = source.parse();
        Node node = token.toNode();
        assertEquals("interpret hello world", "(hello world)", node.interpretValue().valueString());
    }
    @Test
    public void testInterpret3() {
        Source source = new SourceString("@(1+1)");
        Token token = source.parse();
        Node node = token.toNode();
        assertEquals("interpret @(1+1)", "(2)", node.interpretValue().valueString());
    }
    @Test
    public void testInterpret4() {
        Source source = new SourceString("@(a=1);@a");
        Token token = source.parse();
        Node node = token.toNode();
        node.matchVariables();
        node.matchTypes();
        assertEquals("interpret assign", "(1,1)", node.interpretValue().valueString());
    }
    @Test
    public void testInterpretFile() {
        Source source = new SourceFile("samples/operator-sample.m");
        Token token = source.parse();
        Node node = token.toNode();
        node.matchVariables();
        node.matchTypes();
        assertEquals("interpret assign", "(0,1,0,0,1,0)", node.interpretValue().valueString());
    }
    @Test
    public void testInterpret5() {
        Source source = new SourceString("@!@?@[1,2,3]");
        Token token = source.parse();
        Node node = token.toNode();
        assertEquals("interpret some operators and list literal", "(false)", node.interpretValue().valueString());
    }
    @Test
    public void testInterpret6() {
        Source source = new SourceString("if 1 == 1: @1");
        Token token = source.parse();
        Node node = token.toNode();
        System.out.println(node);
        assertEquals("interpret some operators and list literal", "(1)", node.interpretValue().valueString());
    }
}
