package systems.monomer.compiler;

import systems.monomer.compiler.controls.*;
import systems.monomer.compiler.literals.*;
import systems.monomer.compiler.operators.*;
import systems.monomer.compiler.output.CompileOutput;
import systems.monomer.compiler.operators.CompileOperatorNode;
import systems.monomer.compiler.output.CompileValue;
import systems.monomer.errorhandling.ErrorBlock;
import systems.monomer.execution.Constants;
import systems.monomer.execution.Handler;
import systems.monomer.execution.environmentDefaults.ConvertDefaults;
import systems.monomer.execution.environmentDefaults.FileDefaults;
import systems.monomer.execution.environmentDefaults.TypeDefaults;
import systems.monomer.execution.environmentDefaults.ValueDefaults;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.syntaxtree.ModuleNode;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.VariableNode;
import systems.monomer.syntaxtree.controls.*;
import systems.monomer.syntaxtree.literals.*;
import systems.monomer.syntaxtree.operators.*;
import systems.monomer.tokenizer.Source;
import systems.monomer.tokenizer.Token;
import systems.monomer.types.Type;
import systems.monomer.variables.FieldKey;
import systems.monomer.variables.IndexKey;
import systems.monomer.variables.Key;
import systems.monomer.variables.VariableKey;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

//TODO
public class Compiler extends Handler {
    public static void compile(Source source, boolean defaults, InputStream input, OutputStream output) {
        init = new Compiler();

        Token body = source.parse();
        Node node = body.toNode();
        CompileModuleNode global = (CompileModuleNode) init.moduleNode(source.getTitle());

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

        CompileOutput callback = new CompileOutput();
        global.compile(callback);

        try {
            output.write(callback.getAssembly().getBytes());
        } catch (IOException e) {
            throw ErrorBlock.programError("Can not write output to file (" + e.getMessage() + ")", ErrorBlock.Reason.OTHER);
        }
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




    //TODO have a way of storing the direct compile implementation for the defined value
    @Override
    public Node definedValueNode(Supplier<InterpretResult> interpret) {
        throw new RuntimeException("TODO unimplemented");
    }

    @Override
    public ControlGroupNode controlGroupNode() {
        return new CompileControlGroupNode();
    }

    @Override
    public IfNode ifNode() {
        return new CompileIfNode();
    }

    @Override
    public AllNode allNode() {
        return new CompileAllNode();
    }

    @Override
    public AnyNode anyNode() {
        return new CompileAnyNode();
    }

    @Override
    public ElseNode elseNode() {
        return new CompileElseNode();
    }

    @Override
    public RepeatNode repeatNode() {
        return new CompileRepeatNode();
    }

    @Override
    public WhileNode whileNode() {
        return new CompileWhileNode();
    }

    @Override
    public ForNode forNode() {
        return new CompileForNode();
    }

    @Override
    public ReturnNode returnNode() {
        return new CompileReturnNode();
    }

    @Override
    public BoolNode boolNode(boolean value) {
        return new CompileBoolNode(value);
    }

    @Override
    public CharNode charNode(Character c) {
        return new CompileCharNode(c);
    }

    @Override
    public FloatNode floatNode(Double f) {
        return new CompileFloatNode(f);
    }

    @Override
    public IntNode intNode(Integer i) {
        return new CompileIntNode(i);
    }

    @Override
    public StringBuilderNode stringBuilderNode(Collection<? extends Node> list) {
        return new CompileStringBuilderNode(list);
    }

    @Override
    public StringNode stringNode(String s) {
        return new CompileStringNode(s);
    }

    @Override
    public ListNode listNode() {
        return new CompileListNode();
    }

    @Override
    public StructureNode structureNode() {
        return new CompileStructureNode();
    }

    @Override
    public TupleNode tupleNode() {
        return new CompileTupleNode();
    }

    @Override
    public TupleNode blockNode() {
        return new CompileTupleNode("block");
    }

    @Override
    public TupleNode linesNode() {
        return new CompileTupleNode(";");
    }

    @Override
    public MapNode mapNode() {
        return new CompileMapNode();
    }

    @Override
    public SetNode setNode() {
        throw new RuntimeException("Set has not been implemented");
    }

    @Override
    public RangeNode rangeNode(boolean startInclusive, boolean stopInclusive) {
        return new CompileRangeNode(startInclusive, stopInclusive);
    }

    @Override
    public AssertTypeNode assertTypeNode() {
        return new CompileAssertTypeNode();
    }

    @Override
    public CastNode castNode() {
        throw new RuntimeException("Cast has not been implemented");
    }

    @Override
    public ConvertNode convertNode() {
        throw new RuntimeException("Convert has not been implemented");
    }

    @Override
    public AssignNode assignNode() {
        return new CompileAssignNode();
    }

    @Override
    public AssignModifyNode assignModifyNode() {
        throw new RuntimeException("AssignModify has not been implemented");
    }

    @Override
    public CallNode callNode() {
        return new CompileCallNode();
    }

    @Override
    public CastToFunctionNode castToFunctionNode() {
        return new CompileCastToFunctionNode();
    }

    @Override
    public FieldNode fieldNode() {
        return new CompileFieldNode();
    }

    @Override
    public IndexNode indexNode() {
        return new CompileIndexNode();
    }

    @Override
    public SpreadNode spreadNode() {
        throw new RuntimeException("Convert has not been implemented");
    }

    @Override
    public WithNode withNode() {
        return new CompileWithNode();
    }

    @Override
    public ThenNode thenNode() {
        return new CompileThenNode();
    }

    @Override
    public GenericOperatorNode genericOperatorNode(String name, Function<OperatorNode, Type> type,
                                                   Function<GenericOperatorNode, BiFunction<CompileOperatorNode, CompileOutput, CompileValue>> compile,
                                                   Function<GenericOperatorNode, Function<Iterator<InterpretValue>, ? extends InterpretResult>> interpret) {
        CompileOperatorNode ret = new CompileOperatorNode(name, type);
        ret.setCompileGenerator(compile);
        return ret;
    }

    @Override
    public ModuleNode moduleNode(String name) {
        return new CompileModuleNode(name);
    }

    @Override
    public VariableNode variableNode(String name) {
        return new CompileVariableNode(name);
    }


    //TODO create Compile Equivalents to VariableKey, FieldKey, IndexKey
    public VariableKey variableKey() {
        return new VariableKey();
    }
    public FieldKey fieldKey(String name, Key parent) {
        return new FieldKey(name, parent);
    }
    public IndexKey indexKey(IndexNode owner) {
        return new IndexKey(owner);
    }
}
