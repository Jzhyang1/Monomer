package systems.monomer.syntaxtree.operators;

import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.CompileSize;
import systems.monomer.interpreter.InterpretFunction;
import systems.monomer.interpreter.InterpretIO;
import systems.monomer.variables.FunctionBody;
import systems.monomer.interpreter.InterpretObject;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.*;

import static systems.monomer.compiler.Assembly.Instruction.CALL;
import static systems.monomer.compiler.Assembly.Instruction.MOV;
import static systems.monomer.compiler.Assembly.Register.EAX;
import static systems.monomer.types.AnyType.ANY;

/**
 * A node representing a function call.
 *     <ul>
 *         <li>First child: the function to call</li>
 *         <li>Second child: the argument to pass to the function</li>
 *         <li>Third child: optional, the Structure representing named arguments</li>
 *     </ul>
 *     <br/>
 *     The type of the node is the return type of the function.
 *     the <b>getSignature</b> method returns the signature of the function call.
 */
public class CallNode extends OperatorNode {
    private int functionIndex = -1;

    public CallNode() {
        super("call");
    }

    public InterpretValue interpretValue() {
        InterpretValue overload = functionIndex == -1 ?
                getFirst().interpretValue().asValue() :
                ((OverloadedFunctionType) getFirst().getType()).getFunction(functionIndex);
        InterpretValue second = getSecond().interpretValue().asValue();
        InterpretValue third = size() > 2 ? get(2).interpretValue().asValue() : InterpretObject.EMPTY;
        //TODO why are functions defined in OverloadedFunction type instead of a value?
        return overload.call(second, third);
    }

    @Override
    public void matchTypes() {
        //recursion guard
        if(functionIndex != -1) return;

        super.matchTypes();
        Type funcType = getFirst().getType();
        Type argType = getSecond().getType();
        Type returnType = getType();
        Type namedArgType = size() > 2 ? get(2).getType() : new ObjectType();
//        if(returnType == null) returnType = ANY;

        if(funcType instanceof OverloadedFunctionType overload) {
            FunctionBody function = overload.getOverload(new Signature(returnType, argType, namedArgType));
            if(function == null)
               throw syntaxError("No matching function found for " + argType + " -> " + returnType);

            Type actualReturnType = function.getReturnType();
            if(actualReturnType == ANY)
                setType(function.testReturnType(argType));
            else if(returnType == ANY)
                setType(actualReturnType);
        } else if (funcType instanceof Signature signature) {
            setType(signature.getReturnType());
        } else {
            throw getFirst().syntaxError("Expected function, got " + funcType);
        }
    }

    public Operand compileValue(AssemblyFile file) {
        if(functionIndex == -1) {
            file.add(MOV, getSecond().compileValue(file), EAX.toOperand())
                    .add(CALL, getFirst().compileValue(file), null);

            return EAX.toOperand(); //TODO handle non-integer return types

        }
        else {
            InterpretFunction function = ((OverloadedFunction) getFirst().getType()).getFunction(functionIndex);
            return function.compileValue(file);
        }
    }

    public CompileSize compileSize() {
        throw new Error("TODO unimplemented");
    }
}
