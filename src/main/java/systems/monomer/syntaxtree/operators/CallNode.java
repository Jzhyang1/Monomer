package systems.monomer.syntaxtree.operators;

import systems.monomer.types.primative.ObjectType;
import systems.monomer.types.Signature;
import systems.monomer.types.Type;

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

    @Override
    public void matchTypes() {
        super.matchTypes();
        Type argType = getSecond().getType();
        Type returnType = getType();
        Type namedArgType = size() > 2 ? get(2).getType() : new ObjectType();
        Signature signature = new Signature(argType, namedArgType, returnType);

        CastToFunctionNode function = init.castToFunctionNode();
        function.with(getContext()).with(getFirst()).with(signature).matchTypes();
        set(0, function);
        setType(((Signature)function.getType()).getReturnType());
    }

//    public Operand compileValue(AssemblyFile file) {
//        file.add(MOV, getSecond().compileValue(file), EAX.toOperand())
//                .add(CALL, getFirst().compileValue(file), null);
//
//        return EAX.toOperand();
//    }
//
//    public CompileSize compileSize() {
//    }
}
