package systems.monomer;

import org.junit.Test;
import systems.monomer.commandline.Interpret;
import systems.monomer.ide.Editor;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.syntaxtree.ModuleNode;
import systems.monomer.syntaxtree.Node;
import systems.monomer.tokenizer.Source;
import systems.monomer.tokenizer.SourceFile;
import systems.monomer.tokenizer.SourceString;
import systems.monomer.tokenizer.Token;

import java.io.ByteArrayOutputStream;
import java.net.URL;

import static org.junit.Assert.*;

/**
 * some tests fail because Constants is using static variables
 * TODO fix this
 * for now just rerun failed tests one at a time
 */
public class InterpretTest {
    @Test
    public void testInterpret1() {
        Source source = new SourceString("1+1");
        Token token = source.parse();
        Node node = token.toNode();
        assertEquals("interpret 1+1", "(2)", node.interpretValue().asValue().valueString());
    }
    @Test
    public void testInterpret2() {
        Source source = new SourceString("@\"hello world\"");
        Token token = source.parse();
        Node node = token.toNode();
        assertEquals("interpret hello world", "(hello world)", node.interpretValue().asValue().valueString());
    }
    @Test
    public void testInterpret3() {
        Source source = new SourceString("@(1+1)");
        Token token = source.parse();
        Node node = token.toNode();
        assertEquals("interpret @(1+1)", "(2)", node.interpretValue().asValue().valueString());
    }
    @Test
    public void testInterpret4() {
        Source source = new SourceString("@(a=1);@a");
        Token token = source.parse();
        Node node = new ModuleNode("module").with(token.toNode());
        node.matchVariables();
        node.matchTypes();
        assertEquals("interpret assign", "((1,1))", node.interpretValue().asValue().valueString());
    }
    @Test
    public void testInterpretFile() {
        Source source = new SourceFile("samples/operator-sample.m");
        Token token = source.parse();
        Node node = new ModuleNode("module").with(token.toNode());
        node.matchVariables();
        node.matchTypes();
        assertEquals("interpret assign", "((0,1,0,0,1,0))", node.interpretValue().asValue().valueString());
    }
    @Test
    public void testInterpret5() {
        Source source = new SourceString("@!@?@[1,2,3]");
        Token token = source.parse();
        Node node = token.toNode();
        assertEquals("interpret some operators and list literal", "(false)", node.interpretValue().asValue().valueString());
    }
    @Test
    public void testInterpret6() {
        Source source = new SourceString("if 1 == 1: @1");
        Token token = source.parse();
        Node node = token.toNode();
        System.out.println(node);
        assertEquals("interpret some operators and list literal", "(1)", node.interpretValue().asValue().valueString());
    }
    @Test
    public void testInterpret7() {
        Source source = new SourceString("a number = 1\n" +
                "@(a number)");
        Token token = source.parse();
        Node node = token.toNode();
        Node global = new ModuleNode(source.getTitle());
        global.add(node);
        global.matchVariables();
        global.matchTypes();

        System.out.println(node);
        assertEquals("interpret multiword variables", "(1,1)", node.interpretValue().asValue().valueString());
    }

    @Test
    public void interpretTest8() {
        Source source = new SourceString("a x = 1\n" +
                "a y = 2\n" +
                "a z = 3\n" +
                "@a");
        Token token = source.parse();
        Node node = token.toNode();
        Node global = new ModuleNode(source.getTitle());
        global.add(node);
        global.matchVariables();
        global.matchTypes();

        System.out.println(node);
        assertEquals("print multiword variable", "(1,2,3,{x=1,y=2,z=3})", node.interpretValue().asValue().valueString());
    }

    @Test
    public void interpretTest9() {
        Source source = new SourceString("f(x) = @x\n" +
                "f(1991)");
        Token token = source.parse();
        Node node = token.toNode();
        Node global = new ModuleNode(source.getTitle());
        global.add(node);
        global.matchVariables();
        global.matchTypes();
//        global.matchOverloads();

//        System.out.println(node);
        assertEquals("print multiword variable", "((),1991)", node.interpretValue().asValue().valueString());
    }

    @Test
    public void interpretTest10() {
        Source source = new SourceString("f(x) = \n" +
                "\t@x\n" +
                "\tif ?x: f(x-1)\n" +
                "\t@x\n" +
                "f(3)");
        Token token = source.parse();
        Node node = token.toNode();
        Node global = new ModuleNode(source.getTitle());
        global.add(node);
        global.matchVariables();
        global.matchTypes();
//        global.matchOverloads();

        System.out.println(node);
        InterpretValue value = node.interpretValue().asValue();
        assertEquals("recursion", "((),(3,(2,(1,(0,(),0),1),2),3))", value.valueString());
    }

    private String wrapTest(String code) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Constants.setOut(out);
        Source source = new SourceString(code);
        Interpret.interpret(source);
        return out.toString().strip();
    }

    @Test
    public void interpretTest10_1() {
        assertEquals("debug printing constant true", "true", wrapTest("@true"));
    }

    @Test
    public void interpretTest11() {
        assertEquals("simple printing constant 10", "10",
                wrapTest("io write(10); io write(\"\n\")"));
    }
    @Test
    public void interpretTest11_1() {
        assertEquals("function printing variable", "1991",
                wrapTest("a function(x) = @x\n" +
                        "a function(1991)"));
    }

    @Test
    public void interpretTest12() {
        assertEquals("function returning operation", "2.561",
                wrapTest("a(x) = x*/1.5\n" +
                        "@a(4.1)").substring(0, 5));
    }

    @Test
    public void interpretTest13() {
        assertEquals("matching function with variable with type", "123",
                wrapTest("x = int: 123\n" +
                        "io write(x)\n" +
                        "io write(\"\\n\")"));
    }

    @Test
    public void interpretTest14() {
//        Source source = new SourceString("f(int:a,int:b) = a+b\n" +
//                "io write(f(1,2))");
//        Interpret.interpret(source);
        assertEquals("function with multiple typed arguments", "3",
                wrapTest("f(int:a,int:b) = a+b\n" +
                        "io write(f(1,2))"));
    }

    @Test
    public void interpretTest15() {
        String output = wrapTest("for i in [1,2,3]:\n" +
                "    if i == 3:\n" +
                "        break\n" +
                "    @(i)\n");
        assertEquals("for loop", "1\n2", output);
    }

    @Test
    public void interpretTest16() {
        URL f = Editor.class.getResource("/generic sample.txt");
        String fpath = f.getPath().replaceAll("%20", " ");

        assertEquals("read file", "Goals",
                wrapTest("f = io: uri: \""+ fpath + "\"\n" +
                "@(string: f read())"));
    }

    @Test
    public void interpretTest17() {
//        Source source = new SourceString("f(int:x) = 123\n" +
//                "f(string:x) = \"hi\"\n" +
//                "io write(f(1))\n" +
//                "io write(\"\\n\")");
//        Interpret.interpret(source);
        assertEquals("overloaded function", "123",
                wrapTest("f(int:x) = 123\n" +
                        "f(string:x) = \"hi\"\n" +
                        "io write(f(1))\n" +
                        "io write(\"\\n\")"));
    }

    @Test
    public void interpretTest18() {
//        Source source = new SourceString("a={x=1;y=3};@a x");
//        Interpret.interpret(source);
        assertEquals("field access", "1",
                wrapTest("a={x=1;y=3};@a x"));
    }

    @Test
    public void interpretTest19() {
//        Source source = new SourceString("f{c=1}() = @c\n" +
//                "f{c=2}()");
//        Interpret.interpret(source);
        assertEquals("named args", "2",
                wrapTest("f{c=1}() = @c\n" +
                        "f{c=2}()"));
    }

    @Test
    public void interpretTest20() {
        assertEquals("default named args", "1",
                wrapTest("f{c=1}() = @c\n" +
                        "f()"));
    }

    @Test
    public void interpretTest21() {
        assertEquals("string interpolation", "hi1",
                wrapTest("@\"hi\\(1)\""));
    }

    @Test
    public void interpretTest22() {
        assertEquals("long condition chain", "even\ndivisible by 3\ndivisible by 6\ndivisible by 2 or 3\n",
                wrapTest("n = 102 \n" +
                        "if n % 2 == 0: io write(\"even\\n\") \n" +
                        "else n % 3 == 0: io write(\"divisible by 3\\n\") \n" +
                        "all: io write(\"divisible by 6\\n\") \n" +
                        "any: io write(\"divisible by 2 or 3\\n\") \n" +
                        "else: io write(\"not divisible by 2 or 3\")"));
    }
}
