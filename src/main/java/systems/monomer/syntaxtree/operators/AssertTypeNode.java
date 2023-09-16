package systems.monomer.syntaxtree.operators;

import org.jetbrains.annotations.Nullable;
import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretFunction;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.InterpretVariable;
import systems.monomer.syntaxtree.Node;
import systems.monomer.types.OverloadedFunction;
import systems.monomer.types.Signature;
import systems.monomer.types.TupleType;
import systems.monomer.variables.Key;
import systems.monomer.variables.VariableKey;

public class AssertTypeNode extends OperatorNode {
    private InterpretFunction convertBy = null;

    public AssertTypeNode() {
        super(":");
    }

    public void matchTypes() {
        getFirst().matchTypes();
        setType(getFirst().getType());
        Node second = getSecond();
        if(second instanceof CallNode) {
            //TODO when else is requiresConvert not necessary other than in call?
            getSecond().setType(getType());
            getSecond().matchTypes();
        }
        else {
            second.matchTypes();
            VariableKey convertFunc = getVariable("convert");
            if(convertFunc != null) {
                OverloadedFunction overloads = (OverloadedFunction) convertFunc.getType();
                convertBy = overloads.getOverload(new Signature(getType(), TupleType.asTuple(second.getType())));
            }
            if(convertFunc == null || convertBy == null) {
                throwError("Cannot convert from " + second.getType() + " to " + getType());
            }
        }
    }

    public InterpretResult interpretValue() {
        //TODO check that the type is a subtype of the type
        return convertBy == null ? getSecond().interpretValue() : convertBy.call(getSecond().interpretValue().asValue());
    }

    @Override
    public InterpretVariable interpretVariable() {
        return getSecond().interpretVariable();
    }

    @Override
    public @Nullable Key getVariableKey() {
        return getSecond().getVariableKey();
    }

    public CompileValue compileValue() {
        throw new Error("TODO unimplemented");
    }
    public CompileSize compileSize() {
        throw new Error("TODO unimplemented");
    }
}
