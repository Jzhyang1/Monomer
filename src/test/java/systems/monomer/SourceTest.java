package systems.monomer;

import org.junit.Test;
import systems.monomer.tokenizer.Source;
import systems.monomer.tokenizer.SourceString;
import systems.monomer.tokenizer.Token;

import static org.junit.Assert.*;

public class SourceTest {
    @Test
    public void testStringSourceGetLine() {
        SourceString source = new SourceString("y = x + 500;");
        Source.Line line = source.getLine();
        System.out.println(line);
        assertEquals("test short string source get line", line.toString(), "(0)y = x + 500;\n");
    }

    @Test
    public void testStringSourceParse() {
        SourceString source = new SourceString("y = x + 500;");
        Token token = source.parse();
        assertEquals("test short string source", token.toString(),
                "GROUP null[IDENTIFIER y, OPERATOR =, IDENTIFIER x, OPERATOR +, INTEGER 500, OPERATOR ;]");
    }
}
