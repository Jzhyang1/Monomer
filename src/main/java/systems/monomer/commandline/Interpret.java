package systems.monomer.commandline;

import systems.monomer.commandline.EnvironmentDefaults.ConvertDefaults;
import systems.monomer.commandline.EnvironmentDefaults.FileDefaults;
import systems.monomer.commandline.EnvironmentDefaults.TypeDefaults;
import systems.monomer.commandline.EnvironmentDefaults.ValueDefaults;
import systems.monomer.syntaxtree.ModuleNode;
import systems.monomer.syntaxtree.Node;
import systems.monomer.tokenizer.Source;
import systems.monomer.tokenizer.SourceFile;
import systems.monomer.tokenizer.SourceString;
import systems.monomer.tokenizer.Token;

import java.io.*;


public class Interpret {
    public static void interpret(Source source) {
        Token body = source.parse();
        Node node = body.toNode();
        Node global = new ModuleNode(source.getTitle());

        //global constants here
        TypeDefaults.initGlobal(global);
        ValueDefaults.initGlobal(global);
        FileDefaults.initGlobal(global);
        ConvertDefaults.initGlobal(global);

        global.add(node);

        global.matchVariables();
        global.matchTypes();
        global.setIsExpression(false);
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
