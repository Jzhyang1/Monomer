package systems.monomer.commandline;

import systems.monomer.commandline.EnvironmentDefaults.ConvertDefaults;
import systems.monomer.commandline.EnvironmentDefaults.FileDefaults;
import systems.monomer.commandline.EnvironmentDefaults.TypeDefaults;
import systems.monomer.commandline.EnvironmentDefaults.ValueDefaults;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.syntaxtree.ModuleNode;
import systems.monomer.syntaxtree.Node;
import systems.monomer.tokenizer.Source;
import systems.monomer.tokenizer.SourceFile;
import systems.monomer.tokenizer.SourceString;
import systems.monomer.tokenizer.Token;

import java.io.File;

public class Compile {
    public static void compile(Source source) {
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

        AssemblyFile file = new AssemblyFile();
        node.compileVariables(file);
        node.compileValue(file);    //TODO <--- write result to file
        System.out.println(file);
    }
    public static void compile(File sourceFile) {
        Source source = new SourceFile(sourceFile);
        compile(source);
    }
    public static void compile(String code) {
        Source source = new SourceString(code);
        compile(source);
    }
}
