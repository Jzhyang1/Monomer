package systems.monomer.commandline;

import systems.monomer.Constants;
import systems.monomer.commandline.EnvironmentDefaults.*;
import systems.monomer.interpreter.InterpretModuleNode;
import systems.monomer.syntaxtree.Configuration;
import systems.monomer.syntaxtree.Node;
import systems.monomer.tokenizer.Source;
import systems.monomer.tokenizer.SourceFile;
import systems.monomer.tokenizer.SourceString;
import systems.monomer.tokenizer.Token;

import java.io.*;


public class Interpret {
    public static void interpret(Source source, boolean defaults, InputStream input, OutputStream output) {
        Configuration.setAction(Configuration.INTERPRET);

        Token body = source.parse();
        Node node = body.toNode();
        InterpretModuleNode global = (InterpretModuleNode) Configuration.create().moduleNode(source.getTitle());

        //global constants here
        if(defaults) {
            TypeDefaults.initGlobal(global);
            ValueDefaults.initGlobal(global);
            FileDefaults.initGlobal(global, input, output);
            ConvertDefaults.initGlobal(global);
        }

        global.add(node);

        global.matchVariables();
        global.matchTypes();
        global.setIsExpression(false);
        global.interpretValue();
    }

    public static void interpret(Source source) {
        interpret(source, true, Constants.getListener(), Constants.getOut());
    }

    public static void interpret(File sourceFile) {
        Source source = new SourceFile(sourceFile);
        interpret(source);
    }
    public static void interpret(String code) {
        interpret(code, true, Constants.getListener(), Constants.getOut());
    }
    public static void interpret(String code, boolean defaults, InputStream input, OutputStream output) {
        Source source = new SourceString(code);
        interpret(source, defaults, input, output);
    }
}
