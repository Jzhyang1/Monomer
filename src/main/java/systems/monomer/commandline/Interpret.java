package systems.monomer.commandline;

import systems.monomer.interpreter.InterpretBool;
import systems.monomer.interpreter.InterpretCharValue;
import systems.monomer.interpreter.InterpretNumberValue;
import systems.monomer.interpreter.InterpretStringValue;
import systems.monomer.syntaxtree.ModuleNode;
import systems.monomer.syntaxtree.Node;
import systems.monomer.tokenizer.Source;
import systems.monomer.tokenizer.SourceFile;
import systems.monomer.tokenizer.SourceString;
import systems.monomer.tokenizer.Token;
import systems.monomer.variables.VariableKey;

import java.io.File;


public class Interpret {
    public static void interpret(Source source) {
        Token body = source.parse();
        Node node = body.toNode();
        Node global = new ModuleNode(source.getTitle());
        //global constants here
        global.putVariable("true", new VariableKey(){{setValue(new InterpretBool(true));}});
        global.putVariable("false", new VariableKey(){{setValue(new InterpretBool(false));}});
        global.putVariable("bool", new VariableKey(){{setValue(new InterpretBool(false));}});
        global.putVariable("int", new VariableKey(){{setValue(new InterpretNumberValue<>(0));}});
        global.putVariable("float", new VariableKey(){{setValue(new InterpretNumberValue<>(0.0));}});
        global.putVariable("char", new VariableKey(){{setValue(new InterpretCharValue('\0'));}});
        global.putVariable("string", new VariableKey(){{setValue(new InterpretStringValue(""));}});

        global.add(node);

        global.matchVariables();
        global.matchTypes();
        global.matchOverloads();
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
