package systems.monomer.commandline;

import static picocli.CommandLine.*;

@Command(name = CommandLineInterface.SHELL, aliases = {"sh"}, description = "Start a Monomer shell", mixinStandardHelpOptions = true)
public class Shell implements Runnable {

    @Override
    public void run() {
        //TODO
    }
}
