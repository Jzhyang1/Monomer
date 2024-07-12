package systems.monomer.execution.commandline;

import systems.monomer.compiler.Compiler;
import systems.monomer.ide.MonomerIdle;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Option;
import systems.monomer.interpreter.Interpreter;
import systems.monomer.tokenizer.SourceFile;

import java.io.File;

@Command(name = "mono", mixinStandardHelpOptions = true, version = "1.0.2", description = "The Monomer programming language (more info at https://monomer.dev)")
public class CommandLineInterface implements Runnable {
    public static final String SHELL = "shell";
    @Option(names = {"-v", "--version"}, versionHelp = true, description = "Print version information")
    private boolean versionRequested;

    @Command(name = "interpret", aliases = {"int"}, description = "Interpret Monomer file(s)")
    void interpret(@Parameters(description = "File(s) to interpret") String[] files) {
        for (String file : files) {
            Interpreter.interpret(new SourceFile(new File(file)), true, System.in, System.out);
        }
    }

    @Command(name = "compile", aliases = {"comp"}, description = "Compile Monomer file(s)")
    void compile(
            @Parameters(description = "File(s) to compile") String[] files,
            @Option(names = {"-c", "--config"}, description = "Compile configuration file") String configFile
    ) {
        for (String file : files) {
            Compiler.compile(new SourceFile(new File(file)), true, System.in, System.out);    //TODO use config file
        }
    }

    @Command(name = SHELL, aliases = {"sh"}, description = "Start Monomer shell")
    void shell() {
        // Implement the shell logic here
        System.out.println("Starting the shell...");
    }

    @Parameters(paramLabel = "sources", description = "Files to open in the IDE")
    private String[] defaultFiles;

    @Override
    public void run() {
        MonomerIdle.main(defaultFiles);
    }
}
