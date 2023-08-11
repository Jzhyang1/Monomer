package systems.monomer.commandline;

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
        node.matchVariables();
        node.matchTypes();
        node.matchOverloads();
        node.compileValue();    //TODO <--- write result to file
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
