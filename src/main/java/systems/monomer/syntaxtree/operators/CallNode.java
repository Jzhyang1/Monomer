package systems.monomer.syntaxtree.operators;

import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.CompileSize;
import systems.monomer.interpreter.InterpretObject;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.syntaxtree.Node;
import systems.monomer.types.ObjectType;
import systems.monomer.types.Signature;
import systems.monomer.types.Type;

import static systems.monomer.compiler.Assembly.Instruction.CALL;
import static systems.monomer.compiler.Assembly.Instruction.MOV;
import static systems.monomer.compiler.Assembly.Register.EAX;

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
    public CallNode() {
        super("call");
    }

    public InterpretValue interpretValue() {
        InterpretValue overload = getFirst().interpretValue().asValue();
        InterpretValue second = getSecond().interpretValue().asValue();
        InterpretValue third = size() > 2 ? get(2).interpretValue().asValue() : InterpretObject.EMPTY;
        return overload.call(second, third);
    }

    @Override
    public void matchTypes() {
        super.matchTypes();
        Type argType = getSecond().getType();
        Type returnType = getType();
        Type namedArgType = size() > 2 ? get(2).getType() : new ObjectType();
        Signature signature = new Signature(returnType, argType, namedArgType);

        CastToFunctionNode function = new CastToFunctionNode();
        function.with(getContext()).with(getFirst()).with(signature).matchTypes();
        set(0, function);
        setType(((Signature)function.getType()).getReturnType());
    }

    public Operand compileValue(AssemblyFile file) {
        file.add(MOV, getSecond().compileValue(file), EAX.toOperand())
                .add(CALL, getFirst().compileValue(file), null);

        return EAX.toOperand();
    }

    public CompileSize compileSize() {
        throw new Error("TODO unimplemented");
    }
}
