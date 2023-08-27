package systems.monomer.commandline;

import systems.monomer.interpreter.*;
import systems.monomer.syntaxtree.ModuleNode;
import systems.monomer.syntaxtree.Node;
import systems.monomer.tokenizer.Source;
import systems.monomer.tokenizer.SourceFile;
import systems.monomer.tokenizer.SourceString;
import systems.monomer.tokenizer.Token;
import systems.monomer.variables.VariableKey;

import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class Interpret {
    public static void interpret(Source source) {
        Token body = source.parse();
//        System.out.println(body);
        Node node = body.toNode();
//        System.out.println(node);
        Node global = new ModuleNode(source.getTitle());
        //global constants here
        global.putVariable("true", new VariableKey(){{setValue(new InterpretBool(true));}});
        global.putVariable("false", new VariableKey(){{setValue(new InterpretBool(false));}});
        global.putVariable("bool", new VariableKey(){{setValue(new InterpretBool(false));}});
        global.putVariable("int", new VariableKey(){{setValue(new InterpretNumber<>(0));}});
        global.putVariable("float", new VariableKey(){{setValue(new InterpretNumber<>(0.0));}});
        global.putVariable("char", new VariableKey(){{setValue(new InterpretChar('\0'));}});
        global.putVariable("string", new VariableKey(){{setValue(new InterpretString(""));}});
        global.putVariable("io", new VariableKey(){{setValue(new InterpretFile(new InputStreamReader(System.in), new OutputStreamWriter(System.out)));}});

        global.add(node);

        global.matchVariables();
        //TODO
//        global.matchTypes();
//        global.matchOverloads();
        global.interpretValue();
    }
    public static void interpret(File sourceFile) {
        Source source = new SourceFile(sourceFile);
        interpret(source);
    }
    public static void interpret(String code) {
        Source source = new SourceString(code);
        interpret(source);
    }
}
