package systems.monomer.commandline;

import systems.monomer.Constants;
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
import java.io.FileWriter;
import java.io.IOException;

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
        node.compileValue(file);

        //write result to file
        try {
            File outputFile = new File(source.getTitle() + ".asm");
            FileWriter writer = new FileWriter(outputFile);
            file.writeAssembly(writer);
            writer.close();
//            link(source.getTitle(), "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void compile(File sourceFile) {
        Source source = new SourceFile(sourceFile);
        compile(source);
    }

    public static void compile(String code) {
        Source source = new SourceString(code);
        compile(source);
    }


    //TODO currently only supports UNIX and windows
    public static void link(String fileName, String folderPath) {
        String filePath = folderPath + fileName;
        String objectFilePath = filePath + ".o";
        String assemblyFilePath = filePath + ".asm";
        String executableFilePath = filePath + ".exe";


        // Assemble the assembly file
        String assembleCommand = (
                Constants.IS_WINDOWS ?
                "ml /c " + assemblyFilePath :   //WINDOWS
                "as -msyntax=intel -mnaked-reg -o " + objectFilePath + " " + assemblyFilePath  //UNIX
        );

        // Link the object file to create the executable
        String linkCommand = (
                Constants.IS_WINDOWS ?
                "link /subsystem:console /nodefaultlib /lib:kernel32.lib " + objectFilePath + " -OUT:" + executableFilePath :   //WINDOWS
                "ld -o " + executableFilePath + " " + objectFilePath  //UNIX
                );

        try {
            // Execute the assemble command
            Process assembleProcess = Runtime.getRuntime().exec(assembleCommand);
            int assembleExitCode = assembleProcess.waitFor();

            if (assembleExitCode == 0) {
                Constants.getOut().write("Assembly successful".getBytes());
            } else {
                Constants.getErr().write(("Assembly failed with exit code: " + assembleExitCode).getBytes());
                return;
            }

            // Execute the link command
            Process linkProcess = Runtime.getRuntime().exec(linkCommand);
            int linkExitCode = linkProcess.waitFor();

            if (linkExitCode == 0) {
                Constants.getOut().write("Linking successful".getBytes());
            } else {
                Constants.getErr().write(("Linking failed with exit code: " + linkExitCode).getBytes());
                return;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
