package systems.monomer.commandline;

import systems.monomer.syntaxtree.Node;
import systems.monomer.tokenizer.Source;
import systems.monomer.tokenizer.SourceFile;
import systems.monomer.tokenizer.SourceString;
import systems.monomer.tokenizer.Token;

import java.io.File;

import static picocli.CommandLine.*;


@Command(name = "compile", aliases = {"comp"}, description = "Compiles a Monomer file", mixinStandardHelpOptions = true)
public class Compile implements Runnable {
    @Option(names = {"-c", "--config"}, description = "Configuration file for compilation.")
    private String configs;

    @Parameters(paramLabel = "<files>", description = "Paths to the files to be compiled.")
    private File[] files;

    @Override
    public void run() {
        //TODO parse configs & pass as second parameter to compile
        //  if config file is not specified, use default config
        for(File file : files) {
            compile(file);

        }
    }

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
