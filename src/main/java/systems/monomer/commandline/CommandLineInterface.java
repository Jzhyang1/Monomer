package systems.monomer.commandline;

import systems.monomer.ide.MonomerIdle;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Option;

import java.io.File;

@Command(name = "mono", mixinStandardHelpOptions = true, version = "1.0.0", description = "The Monomer programming language (more info at https://monomer.dev)")
public class CommandLineInterface implements Runnable {
    @Option(names = {"-v", "--version"}, versionHelp = true, description = "Print version information")
    private boolean versionRequested;

    @Command(name = "interpret", aliases = {"int"}, description = "Interpret Monomer file(s)")
    void interpret(@Parameters(description = "File(s) to interpret") String[] files) {
        for (String file : files) {
            Interpret.interpret(new File(file));
        }
    }

    @Command(name = "compile", aliases = {"comp"}, description = "Compile Monomer file(s)")
    void compile(
            @Parameters(description = "File(s) to compile") String[] files,
            @Option(names = {"-c", "--config"}, description = "Compile configuration file") String configFile
    ) {
        for (String file : files) {
            Compile.compile(new File(file));    //TODO use config file
        }
    }

    @Command(name = "shell", aliases = {"sh"}, description = "Start Monomer shell")
    void shell() {
        // Implement the shell logic here
        System.out.println("Starting the shell...");
    }

    public static void main(String[] args) {
        CommandLine.run(new CommandLineInterface(), args);
    }

    @Parameters(paramLabel = "sources", description = "Files to open in the IDE")
    private String[] defaultFiles;

    @Override
    public void run() {
        MonomerIdle.main(defaultFiles);
    }
}
