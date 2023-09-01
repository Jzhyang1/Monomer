package systems.monomer.commandline;

import systems.monomer.interpreter.*;
import systems.monomer.syntaxtree.ModuleNode;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.literals.TupleNode;
import systems.monomer.tokenizer.Source;
import systems.monomer.tokenizer.SourceFile;
import systems.monomer.tokenizer.SourceString;
import systems.monomer.tokenizer.Token;
import systems.monomer.types.CharType;
import systems.monomer.types.NumberType;
import systems.monomer.types.StringType;
import systems.monomer.variables.VariableKey;

import java.io.*;
import java.util.List;


public class Interpret {
    public static void interpret(Source source) {
        Token body = source.parse();
//        System.out.println(body);
        Node node = body.toNode();
//        System.out.println(node);
        Node global = new ModuleNode(source.getTitle());
        //global constants here
        global.putVariable("true", new VariableKey(){{
            setValue(new InterpretBool(true));
            setType(InterpretBool.BOOL);
        }});
        global.putVariable("false", new VariableKey(){{
            setValue(new InterpretBool(false));
            setType(InterpretBool.BOOL);
        }});
        global.putVariable("bool", new VariableKey(){{
            setValue(new InterpretBool(false));
            setType(InterpretBool.BOOL);
        }});
        global.putVariable("int", new VariableKey(){{
            setValue(new InterpretNumber<>(0));
            setType(InterpretNumber.INTEGER);
        }});
        global.putVariable("float", new VariableKey(){{
            setValue(new InterpretNumber<>(0.0));
            setType(InterpretNumber.FLOAT);
        }});
        global.putVariable("char", new VariableKey(){{
            setValue(new InterpretChar('\0'));
            setType(CharType.CHAR);
        }});
        global.putVariable("string", new VariableKey(){{
            setValue(new InterpretString(""));
            setType(StringType.STRING);
        }});
        global.putVariable("io", new InterpretFile(new InputStreamReader(System.in), new OutputStreamWriter(System.out)));

        global.add(node);

        global.matchVariables();
        //TODO
        global.matchTypes();
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
