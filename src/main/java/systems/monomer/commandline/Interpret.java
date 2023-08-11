package systems.monomer.commandline;

import systems.monomer.syntaxtree.Node;
import systems.monomer.tokenizer.Source;
import systems.monomer.tokenizer.SourceFile;
import systems.monomer.tokenizer.SourceString;
import systems.monomer.tokenizer.Token;

import java.io.File;


public class Interpret {
    public static void interpret(Source source) {
        Token body = source.parse();
        Node node = body.toNode();
        node.matchVariables();
        node.matchTypes();
        node.matchOverloads();
        node.interpretValue();
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
