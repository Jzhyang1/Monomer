package systems.monomer.interpreter.controls;

import systems.monomer.interpreter.InterpretNode;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretVariable;
import systems.monomer.interpreter.values.InterpretBreaking;
import systems.monomer.interpreter.values.InterpretTuple;
import systems.monomer.syntaxtree.controls.ReturnNode;

public class InterpretReturnNode extends ReturnNode implements InterpretNode {

    @Override
    public InterpretVariable interpretVariable() {
        throw runtimeError("Cannot interpret return statement as variable");
    }

    @Override
    public InterpretResult interpretValue() {
        return new InterpretBreaking("return",
                size() == 0 ? InterpretTuple.EMPTY : getFirstInterpretNode().interpretValue().asValue()
        );
    }
}
