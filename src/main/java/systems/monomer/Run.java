package systems.monomer;

import java.util.*;
import java.util.stream.Collectors;

import static systems.monomer.Config.*;

public class Run {
    public static void main(String[] args) {
        System.out.println();
        System.exit(
                checkIDE(args) ||
                checkVersionInfo(args) ||
                checkHelp(args) ||
                checkCompile(args) ||
                checkInterpret(args) ||
                checkShell(args) ?
                0 : -1);
    }
    private static boolean checkIDE(String[] args){
        if(args.length == 0) {
            //TODO
            return true;
        }
        return false;
    }
    private static boolean checkShell(String[] args){
        if(commands.get("shell").aliases.contains(args[0])) {
            //TODO
            return true;
        }
        return false;
    }
    private static boolean checkInterpret(String[] args){
        if(commands.get("int").aliases.contains(args[0])) {
            //TODO
            return true;
        }
        return false;
    }
    public static void interpret(String code) {
        System.out.println("running " + code);
        try {
            // actually implement your code here

        } catch (Exception e) {

        }
    }

    private static boolean checkCompile(String[] args){
        if(commands.get("comp").aliases.contains(args[0])) {
            //TODO
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

    private static String stripQuotes(String path) {
        return path.charAt(0) == '"' || path.charAt(0) == '\'' ? path.substring(1, path.length() - 1) : path;
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
        put("int", new CommandInfo(List.of("interpret", "-interpret", "int", "-int"), "interprets a Monomer file", "interprets Monomer files\n\tint [...paths]\tinterprets the files specified by a space separated list of paths. Paths may be quote enclosed"));
        put("comp", new CommandInfo(List.of("compile", "-compile", "comp", "-comp"), "compiles a Monomer file", "compiles Monomer files\n\tcomp [...paths]\tcompiles the files specified by a space separated list of paths. Paths may be quote enclosed\n\tcomp [...paths] -out [path]\tcompiles the files and outputs in the specified path"));
        put("shell", new CommandInfo(List.of("shell", "-shell", "sh", "-sh"), "starts Monomer shell", "starts Monomer shell\n\tshell\tturns the command line into a shell"));
    }};
}
