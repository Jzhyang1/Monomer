package systems.monomer.compiler;

import systems.monomer.compiler.controls.*;
import systems.monomer.compiler.literals.*;
import systems.monomer.compiler.operators.*;
import systems.monomer.compiler.output.CompileOutput;
import systems.monomer.compiler.operators.CompileOperatorNode;
import systems.monomer.compiler.output.CompileUtil;
import systems.monomer.compiler.output.CompileValue;
import systems.monomer.errorhandling.ErrorBlock;
import systems.monomer.execution.Constants;
import systems.monomer.execution.Initializer;
import systems.monomer.execution.environmentDefaults.ConvertDefaults;
import systems.monomer.execution.environmentDefaults.FileDefaults;
import systems.monomer.execution.environmentDefaults.TypeDefaults;
import systems.monomer.execution.environmentDefaults.ValueDefaults;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.syntaxtree.Node;
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
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static systems.monomer.compiler.output.CompileUtil.*;
import static systems.monomer.compiler.output.CompileUtil.asmc;

public class Compiler extends Initializer<Function<GenericOperatorNode, BiFunction<CompileOperatorNode, CompileOutput, CompileValue>>> {
    public static void compile(Source source, boolean defaults, InputStream input, OutputStream output) {
        Initializer init = new Compiler();
        source.with(init);

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


    private final Map<String, Function<GenericOperatorNode, BiFunction<CompileOperatorNode, CompileOutput, CompileValue>>> operatorBodies = new HashMap<>();
    public Compiler() {
        //purposefully omitted are the control operators, assignment, cast, convert, and with/then
        operatorBodies.put("+", CompileUtil.unBiOp(CompileUtil.intFloatUniOp(asma::posi, asma::posf), CompileUtil.intFloatOp(asma::addi, asma::addf)));
        operatorBodies.put("-", CompileUtil.unBiOp(CompileUtil.intFloatUniOp(asma::negi, asma::negf), CompileUtil.intFloatOp(asma::subi, asma::subf)));
        operatorBodies.put("*", CompileUtil.intFloatOp(asma::muli, asma::mulf));
        operatorBodies.put("/", CompileUtil.intFloatOp(asma::divi, asma::divf));
        operatorBodies.put("%", CompileUtil.intFloatOp(asma::modi, asma::modf));
        operatorBodies.put("||", CompileUtil.intFloatOp(asma::plli, asma::pllf));
        operatorBodies.put("**", CompileUtil.intFloatOp(asma::powi, asma::powf));
        operatorBodies.put("*/", CompileUtil.intFloatOp(asma::rooti, asma::rootf));

        operatorBodies.put("!", CompileUtil.intBoolOp(asmb::noti, asmb::notb));
        operatorBodies.put("?", CompileUtil.intFloatBoolColUniOp(asmb::isi, asmb::isf, asmb::isb, asmb::isl));
        operatorBodies.put("&", CompileUtil.intBoolOp(asmb::andi, asmb::andb));
        operatorBodies.put("|", CompileUtil.intBoolOp(asmb::ori, asmb::orb));
        operatorBodies.put("^", CompileUtil.intBoolOp(asmb::xori, asmb::xorb));
        operatorBodies.put("~&", CompileUtil.intBoolOp(asmb::nandi, asmb::nandb));
        operatorBodies.put("~|", CompileUtil.intBoolOp(asmb::nori, asmb::norb));
        operatorBodies.put("~^", CompileUtil.intBoolOp(asmb::nxori, asmb::nxorb));

        operatorBodies.put("==", CompileUtil.intFloatBoolColOp(asmc::eqi, asmc::eqf, asmc::eqb, asmc::eql));
        operatorBodies.put("!=", CompileUtil.intFloatBoolColOp(asmc::neqi, asmc::neqf, asmc::neqb, asmc::neql));
        operatorBodies.put(">", CompileUtil.intFloatBoolColOp(asmc::gti, asmc::gtf, asmc::gtb, asmc::gtl));
        operatorBodies.put("<", CompileUtil.intFloatBoolColOp(asmc::lti, asmc::ltf, asmc::ltb, asmc::ltl));
        operatorBodies.put(">=", CompileUtil.intFloatBoolColOp(asmc::gteqi, asmc::gteqf, asmc::gteqb, asmc::gteql));
        operatorBodies.put("<=", CompileUtil.intFloatBoolColOp(asmc::lteqi, asmc::lteqf, asmc::lteqb, asmc::lteql));
        operatorBodies.put("?=", CompileUtil.intFloatBoolColUniOp(asmc::cmpi, asmc::cmpf, asmc::cmpb, asmc::cmpl));

//        operatorBodies.put(".", null); //TODO all of these need to be implemented
//        operatorBodies.put("...", null);
//        operatorBodies.put("in", null);
//        operatorBodies.put("#", null);

//        operatorBodies.put("break", null); //TODO and these too
//        operatorBodies.put("continue", null);
//        operatorBodies.put("return", null);
    }


    //TODO have a way of storing the direct compile implementation for the defined value
    @Override
    public Node definedValueNode(Supplier<InterpretResult> interpret) {
        throw new RuntimeException("TODO unimplemented");
    }

    @Override
    public Node controlGroupNode() {
        return new CompileControlGroupNode().with(this);
    }

    @Override
    public Node ifNode() {
        return new CompileIfNode().with(this);
    }

    @Override
    public Node allNode() {
        return new CompileAllNode().with(this);
    }

    @Override
    public Node anyNode() {
        return new CompileAnyNode().with(this);
    }

    @Override
    public Node elseNode() {
        return new CompileElseNode().with(this);
    }

    @Override
    public Node repeatNode() {
        return new CompileRepeatNode().with(this);
    }

    @Override
    public Node whileNode() {
        return new CompileWhileNode().with(this);
    }

    @Override
    public Node forNode() {
        return new CompileForNode().with(this);
    }

    @Override
    public Node returnNode() {
        return new CompileReturnNode().with(this);
    }

    @Override
    public Node boolNode(boolean value) {
        return new CompileBoolNode(value).with(this);
    }

    @Override
    public Node charNode(Character c) {
        return new CompileCharNode(c);
    }

    @Override
    public Node floatNode(Double f) {
        return new CompileFloatNode(f).with(this);
    }

    @Override
    public Node intNode(Integer i) {
        return new CompileIntNode(i).with(this);
    }

    @Override
    public Node stringBuilderNode(Collection<? extends Node> list) {
        return new CompileStringBuilderNode(list).with(this);
    }

    @Override
    public Node stringNode(String s) {
        return new CompileStringNode(s).with(this);
    }

    @Override
    public Node listNode() {
        return new CompileListNode().with(this);
    }

    @Override
    public Node structureNode() {
        return new CompileStructureNode().with(this);
    }

    @Override
    public Node tupleNode() {
        return new CompileTupleNode().with(this);
    }

    @Override
    public Node blockNode() {
        return new CompileTupleNode("block").with(this);
    }

    @Override
    public Node linesNode() {
        return new CompileTupleNode(";").with(this);
    }

    @Override
    public Node mapNode() {
        return new CompileMapNode().with(this);
    }

    @Override
    public Node setNode() {
        throw new RuntimeException("Set has not been implemented");
    }

    @Override
    public Node rangeNode(boolean startInclusive, boolean stopInclusive) {
        return new CompileRangeNode(startInclusive, stopInclusive).with(this);
    }

    @Override
    public Node assertTypeNode() {
        return new CompileAssertTypeNode().with(this);
    }

    @Override
    public Node castNode() {
        throw new RuntimeException("Cast has not been implemented");
    }

    @Override
    public Node convertNode() {
        throw new RuntimeException("Convert has not been implemented");
    }

    @Override
    public Node assignNode() {
        return new CompileAssignNode().with(this);
    }

    @Override
    public Node assignModifyNode() {
        throw new RuntimeException("AssignModify has not been implemented");
    }

    @Override
    public Node callNode() {
        return new CompileCallNode().with(this);
    }

    @Override
    public Node castToFunctionNode() {
        return new CompileCastToFunctionNode().with(this);
    }

    @Override
    public Node fieldNode() {
        return new CompileFieldNode().with(this);
    }

    @Override
    public Node indexNode() {
        return new CompileIndexNode().with(this);
    }

    @Override
    public Node spreadNode() {
        throw new RuntimeException("Convert has not been implemented");
    }

    @Override
    public Node withNode() {
        return new CompileWithNode().with(this);
    }

    @Override
    public Node thenNode() {
        return new CompileThenNode().with(this);
    }

    @Override
    public Node genericOperatorNode(String name, Function<OperatorNode, Type> type) {
        return new CompileOperatorNode(name, type).with(this);
    }

    @Override
    public Node moduleNode(String name) {
        return new CompileModuleNode(name).with(this);
    }

    @Override
    public Node variableNode(String name) {
        return new CompileVariableNode(name).with(this);
    }


    //TODO create Compile Equivalents to VariableKey, FieldKey, IndexKey
    public Key variableKey() {
        return new VariableKey().with(this);
    }
    public Key fieldKey(String name, Key parent) {
        return new FieldKey(name, parent).with(this);
    }
    public Key indexKey(IndexNode owner) {
        return new IndexKey(owner).with(this);
    }

    @Override
    public Function<GenericOperatorNode, BiFunction<CompileOperatorNode, CompileOutput, CompileValue>> getOperatorBody(String name) {
        return operatorBodies.get(name);
    }
}
