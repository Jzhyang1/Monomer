package systems.monomer.interpreter.operators;

import systems.monomer.interpreter.InterpretNode;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.InterpretVariable;
import systems.monomer.interpreter.values.InterpretObject;
import systems.monomer.syntaxtree.operators.CallNode;

public class InterpretCallNode extends CallNode implements InterpretNode {
    public InterpretResult interpretValue() {
        InterpretNode first = getFirstInterpretNode();
        InterpretNode second = getSecondInterpretNode();

        InterpretResult firstResult = first.interpretValue();
        if(!firstResult.isValue())
            return firstResult;

        InterpretResult secondResult = second.interpretValue();
        if(!secondResult.isValue())
            return secondResult;

        InterpretValue overload = firstResult.asValue();
        InterpretValue positionalArgs = secondResult.asValue();
        InterpretValue namedArgs = InterpretObject.EMPTY;
        if(size() > 2) {
            InterpretNode third = getInterpretNode(2);
            namedArgs = third.interpretValue().asValue(); //this direct asValue is intentional
        }

        return overload.call(positionalArgs, namedArgs);
    }

    public InterpretVariable interpretVariable() {
        throw runtimeError("Cannot assign to result of operator call");
    }
}
