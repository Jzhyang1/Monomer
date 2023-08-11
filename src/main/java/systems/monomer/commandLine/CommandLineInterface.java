package systems.monomer.commandLine;

import systems.monomer.ide.MonomerIdle;

import java.util.*;
import java.util.stream.Collectors;

import static systems.monomer.Config.*;

public class CommandLineInterface {

    private static boolean checkIDE(String[] args){
        if(args.length == 0) {
            MonomerIdle.main(args);
            return true;
        }
        return false;
    }
    private static boolean checkVersionInfo(String[] args){
        if(commands.get("version").aliases.contains(args[0])) {
            System.out.printf("Monomer version %d.%d.%d\nFor help, type:\n\tmono help%n", MAJOR, MINOR, REVISION);
            return true;
        }
        return false;
    }
    private static boolean checkHelp(String[] args){
        if(commands.get("help").aliases.contains(args[0])) {
            if(args.length == 1) {
                System.out.println("The commands are as follows. For more detail about a command, type:\n\tmono help [command]\n");
                System.out.println(commands.entrySet().stream().map((e)->"mono %s\t\t%s\n".formatted(e.getKey(),e.getValue().desc)).collect(Collectors.joining()));
            }
            else {
                for(int i = 1; i < args.length; ++i) {
                    System.out.println(commands.get(args[i]).aliases);
                    System.out.printf("\t%s\n", commands.get(args[i]).fullDesc);
                }
            }
            return true;
        }
        return false;
    }

    private static class CommandInfo {
        List<String> aliases;
        String desc;
        String fullDesc;

        CommandInfo(List<String> aliases, String desc, String fullDesc) {
            this.aliases = aliases;
            this.desc = desc;
            this.fullDesc = fullDesc;
        }
    }
    private static Map<String, CommandInfo> commands = new TreeMap<>(){{
        put("version", new CommandInfo(List.of("version", "-version", "v", "-v"), "displays the installed Monomer version", "displays the version\n\tversion\t\tdisplays M.m.r where M is the compatibility version, m is the feature version, and r is the revision version"));
        put("help", new CommandInfo(List.of("help", "-help", "h", "-h"), "displays the help menu", "displays the help menu\n\thelp\t\tdisplays a list of all commands and a general description\n\thelp [command]\tdisplays a list of all options for a command"));
    }};
}
