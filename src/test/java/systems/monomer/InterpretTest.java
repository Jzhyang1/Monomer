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
        System.out.println(token);
        Node node = token.toNode();
        System.out.println(node);
        assertEquals("interpret 1+1", "2", node.interpretValue().valueString());
    }
}
