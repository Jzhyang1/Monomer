package systems.monomer.commandLine;

import picocli.CommandLine;
import systems.monomer.syntaxTree.Node;
import systems.monomer.tokenizer.Source;
import systems.monomer.tokenizer.SourceFile;
import systems.monomer.tokenizer.SourceString;
import systems.monomer.tokenizer.Token;

import java.io.File;

import static picocli.CommandLine.*;

@Command(name = "interpret", aliases = {"int"}, description = "Interpret a Monomer file", mixinStandardHelpOptions = true)
public class Interpret implements Runnable{
    @Parameters(paramLabel = "<files>", description = "Paths to the files to be interpreted.")
    private File[] files;

    @Override
    public void run() {
        for(File file : files) {
            interpret(file);
        }
    }


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
