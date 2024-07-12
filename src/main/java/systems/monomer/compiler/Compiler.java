package systems.monomer.compiler;

import systems.monomer.compiler.assembly.Operand;
import systems.monomer.compiler.operators.CompileOperatorNode;
import systems.monomer.execution.Constants;
import systems.monomer.execution.Initializer;
import systems.monomer.execution.environmentDefaults.ConvertDefaults;
import systems.monomer.execution.environmentDefaults.FileDefaults;
import systems.monomer.execution.environmentDefaults.TypeDefaults;
import systems.monomer.execution.environmentDefaults.ValueDefaults;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.operators.InterpretOperatorNode;
import systems.monomer.syntaxtree.ModuleNode;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.VariableNode;
import systems.monomer.syntaxtree.controls.*;
import systems.monomer.syntaxtree.literals.*;
import systems.monomer.syntaxtree.operators.*;
import systems.monomer.tokenizer.Source;
import systems.monomer.tokenizer.Token;
import systems.monomer.types.Type;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

//TODO
public class Compiler implements Initializer {
    public static void compile(Source source, boolean defaults, InputStream input, OutputStream output) {
        Node.init = new Compiler();

        Token body = source.parse();
        Node node = body.toNode();
        CompileModuleNode global = (CompileModuleNode) Node.init.moduleNode(source.getTitle());

        //global constants here
        if(defaults) {
            TypeDefaults.initGlobal(global);
            ValueDefaults.initGlobal(global);
            FileDefaults.initGlobal(global, input, output);
            ConvertDefaults.initGlobal(global);
        }

        global.add(node);

        global.matchVariables();
        global.matchTypes();
        global.setIsExpression(false);
        global.compileValue(null);  //TODO
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



    @Override
    public Node definedValueNode(Supplier<InterpretResult> interpret) {
        return null;
    }

    @Override
    public ControlGroupNode controlGroupNode() {
        return null;
    }

    @Override
    public IfNode ifNode() {
        return null;
    }

    @Override
    public AllNode allNode() {
        return null;
    }

    @Override
    public AnyNode anyNode() {
        return null;
    }

    @Override
    public ElseNode elseNode() {
        return null;
    }

    @Override
    public RepeatNode repeatNode() {
        return null;
    }

    @Override
    public WhileNode whileNode() {
        return null;
    }

    @Override
    public ForNode forNode() {
        return null;
    }

    @Override
    public ReturnNode returnNode() {
        return null;
    }

    @Override
    public BoolNode boolNode(boolean value) {
        return null;
    }

    @Override
    public CharNode charNode(Character c) {
        return null;
    }

    @Override
    public FloatNode floatNode(Double f) {
        return null;
    }

    @Override
    public IntNode intNode(Integer i) {
        return null;
    }

    @Override
    public StringBuilderNode stringBuilderNode(Collection<? extends Node> list) {
        return null;
    }

    @Override
    public StringNode stringNode(String s) {
        return null;
    }

    @Override
    public ListNode listNode() {
        return null;
    }

    @Override
    public StructureNode structureNode() {
        return null;
    }

    @Override
    public TupleNode tupleNode() {
        return null;
    }

    @Override
    public TupleNode linesNode() {
        return null;
    }

    @Override
    public TupleNode blockNode() {
        return null;
    }

    @Override
    public MapNode mapNode() {
        return null;
    }

    @Override
    public SetNode setNode() {
        return null;
    }

    @Override
    public RangeNode rangeNode(boolean startInclusive, boolean stopInclusive) {
        return null;
    }

    @Override
    public AssertTypeNode assertTypeNode() {
        return null;
    }

    @Override
    public CastNode castNode() {
        return null;
    }

    @Override
    public ConvertNode convertNode() {
        return null;
    }

    @Override
    public AssignNode assignNode() {
        return null;
    }

    @Override
    public AssignModifyNode assignModifyNode() {
        return null;
    }

    @Override
    public CallNode callNode() {
        return null;
    }

    @Override
    public CastToFunctionNode castToFunctionNode() {
        return null;
    }

    @Override
    public FieldNode fieldNode() {
        return null;
    }

    @Override
    public IndexNode indexNode() {
        return null;
    }

    @Override
    public SpreadNode spreadNode() {
        return null;
    }

    @Override
    public WithThenNode withThenNode(String name) {
        return null;
    }

    @Override
    public GenericOperatorNode genericOperatorNode(String name, Function<OperatorNode, Type> type,
                                                   BiFunction<CompileOperatorNode, AssemblyFile, Operand> compile,
                                                   Function<InterpretOperatorNode, ? extends InterpretResult> interpret) {
        return null;
    }

    @Override
    public ModuleNode moduleNode(String name) {
        return null;
    }

    @Override
    public VariableNode variableNode(String name) {
        return null;
    }
}
