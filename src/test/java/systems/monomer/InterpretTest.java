package systems.monomer;

import org.junit.Test;
import systems.monomer.execution.commandline.Interpret;
import systems.monomer.execution.Constants;
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

/**
 * some tests fail because Constants is using static variables
 * TODO fix this
 * for now just rerun failed tests one at a time
 */
public class InterpretTest {
    @Test
    public void testInterpretBasicAdd() {
        Source source = new SourceString("1+1");
        Token token = source.parse();
        Node node = token.toNode();
        assertEquals("interpret 1+1", "(2)", node.interpretValue().asValue().valueString());
    }
    @Test
    public void testInterpretDebugPrint() {
        Source source = new SourceString("@\"hello world\"");
        Token token = source.parse();
        Node node = token.toNode();
        assertEquals("interpret hello world", "(hello world)", node.interpretValue().asValue().valueString());
    }
    @Test
    public void testInterpretParenthesis() {
        Source source = new SourceString("@(1+1)");
        Token token = source.parse();
        Node node = token.toNode();
        assertEquals("interpret @(1+1)", "(2)", node.interpretValue().asValue().valueString());
    }
    @Test
    public void testInterpretAssignment() {
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
    public void testInterpretChainPrefixes() {
        Source source = new SourceString("@!@?@[1,2,3]");
        Token token = source.parse();
        Node node = token.toNode();
        assertEquals("interpret some operators and list literal", "(false)", node.interpretValue().asValue().valueString());
    }
    @Test
    public void testInterpretIfStatement() {
        Source source = new SourceString("if 1 == 1: @1");
        Token token = source.parse();
        Node node = token.toNode();
        System.out.println(node);
        assertEquals("interpret some operators and list literal", "(1)", node.interpretValue().asValue().valueString());
    }
    @Test
    public void testInterpretMultiwordVariable() {
        Source source = new SourceString("a number = 1\n" +
                "@(a number)");
        Token token = source.parse();
        Node node = token.toNode();
        ModuleNode global = new ModuleNode(source.getTitle());
        global.add(node);
        global.matchVariables();
        global.matchTypes();
        global.initVariables();

        System.out.println(node);
        assertEquals("interpret multiword variables", "(1,1)", node.interpretValue().asValue().valueString());
    }

    @Test
    public void interpretTestObjectFields() {
        Source source = new SourceString("a x = 1\n" +
                "a y = 2\n" +
                "a z = 3\n" +
                "@a");
        Token token = source.parse();
        Node node = token.toNode();
        ModuleNode global = new ModuleNode(source.getTitle());
        global.add(node);
        global.matchVariables();
        global.matchTypes();
        global.initVariables();

        System.out.println(node);
        assertEquals("print multiword variable", "(1,2,3,{x=1,y=2,z=3})", node.interpretValue().asValue().valueString());
    }

    @Test
    public void interpretTestTypelessFunction() {
        Source source = new SourceString("f(x) = @x\n" +
                "f(1991)");
        Token token = source.parse();
        Node node = token.toNode();
        ModuleNode global = new ModuleNode(source.getTitle());
        global.add(node);
        global.matchVariables();
        global.matchTypes();
        global.initVariables();

//        System.out.println(node);
        assertEquals("print multiword variable", "((),1991)", node.interpretValue().asValue().valueString());
    }

    @Test
    public void interpretTestTypelessFunctionLarge() {
        Source source = new SourceString("f(x) = \n" +
                "\t@x\n" +
                "\tif ?x: f(x-1)\n" +
                "\t@x\n" +
                "f(3)");
        Token token = source.parse();
        Node node = token.toNode();
        ModuleNode global = new ModuleNode(source.getTitle());
        global.add(node);
        global.matchVariables();
        global.matchTypes();
        global.initVariables();

        System.out.println(node);
        InterpretValue value = node.interpretValue().asValue();
        assertEquals("recursion", "((),(3,(2,(1,(0,(),0),1),2),3))", value.valueString());
    }

    private String wrapTest(String code) {
        return wrapTest(code, true);
    }

    private String wrapTest(String code, boolean defaults) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Source source = new SourceString(code);
        if(code.indexOf('@') >= 0) Constants.setOut(out);

        Interpret.interpret(source, defaults, Constants.getListener(), out);
        return out.toString().strip();
    }

    @Test
    public void interpretTestBuiltinValues() {
        assertEquals("printing constant true", "true", wrapTest("@true"));
    }

    @Test
    public void interpretTestBuiltinIO() {
        assertEquals("simple printing constant 10", "10",
                wrapTest("io write(10); io write(\"\n\")"));
    }
    @Test
    public void interpretTestTypelessFunctionField() {
        assertEquals("function printing variable", "1991",
                wrapTest("a function(x) = @x\n" +
                        "a function(1991)"));
    }

    @Test
    public void interpretTestReturnValue() {
        assertEquals("function returning operation", "2.561",
                wrapTest("a(x) = x*/1.5\n" +
                        "@a(4.1)", false).substring(0, 5));
    }

    @Test
    public void interpretTestTypedVariable() {
        assertEquals("matching function with variable with type", "123",
                wrapTest("x = int: 123\n" +
                        "io write(x)\n" +
                        "io write(\"\\n\")"));
    }

    @Test
    public void interpretTestTypedFunction() {
//        Source source = new SourceString("f(int:a,int:b) = a+b\n" +
//                "io write(f(1,2))");
//        Interpret.interpret(source);
        assertEquals("function with multiple typed arguments", "3",
                wrapTest("f(int:a,int:b) = a+b\n" +
                        "io write(f(1,2))"));
    }

    @Test
    public void interpretTestForLoop() {
        String output = wrapTest("for i in [1,2,3]:\n" +
                "    if i == 3:\n" +
                "        break\n" +
                "    @(i)\n");
        assertEquals("for loop", "1\n2", output);
    }

    @Test
    public void interpretTestReadingFile() {
        URL f = Editor.class.getResource("/generic sample.txt");
        String fpath = f.getPath().replaceAll("%20", " ");

        assertEquals("read file", "Goals",
                wrapTest("f = io: uri: \""+ fpath + "\"\n" +
                "@(string: f read())"));
    }

    @Test
    public void interpretTestOverloadedFunction() {
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
    public void interpretTestObjectField() {
//        Source source = new SourceString("a={x=1;y=3};@a x");
//        Interpret.interpret(source);
        assertEquals("field access", "1",
                wrapTest("a={x=1;y=3};@a x"));
    }

    @Test
    public void interpretTestNamedArg() {
//        Source source = new SourceString("f{c=1}() = @c\n" +
//                "f{c=2}()");
//        Interpret.interpret(source);
        assertEquals("named args", "2",
                wrapTest("f{c=1}() = @c\n" +
                        "f{c=2}()"));
    }

    @Test
    public void interpretTestDefaultArg() {
        assertEquals("default named args", "1",
                wrapTest("f{c=1}() = @c\n" +
                        "f()"));
    }

    @Test
    public void interpretTestStringInterpolation() {
        assertEquals("string interpolation", "hi1",
                wrapTest("@\"hi\\(1)\""));
    }

    @Test
    public void interpretTestConditions() {
        assertEquals("long condition chain", "even\ndivisible by 6\ndivisible by 2 or 3",
                wrapTest("n = 102 \n" +
                        "if n % 2 == 0: io write(\"even\\n\") \n" +
                        "else n % 3 == 0: io write(\"divisible by 3 but not 2\\n\") \n" +
                        "all: io write(\"divisible by 6\\n\") \n" +
                        "any: io write(\"divisible by 2 or 3\\n\") \n" +
                        "else: io write(\"not divisible by 2 or 3\")"));
    }

    @Test
    public void interpretTestList() {
        assertEquals("lists", "1",
                wrapTest("a = [1,2,3]\n" +
                        "@a[0]"));
    }
    @Test
    public void interpretTestListComprehension() {
        assertEquals("spread in lists", "1",
                wrapTest("a = [repeat 3: 1]\n" +
                        "@a[1]"));
    }

    @Test
    public void interpretTestTypedObjectNarrowing() {
        assertEquals("casting objects", "{a=1}",
                wrapTest("x = {a=int}:{a=1;b=2}\n" +
                        "@x"));
    }

    @Test
    public void interpretTestTypelessObjectNarrowing() {
        assertEquals("casting objects without type", "{a=2}",
                wrapTest("b = {a}:{a=2}\n" +
                        "@b"));
    }
}
