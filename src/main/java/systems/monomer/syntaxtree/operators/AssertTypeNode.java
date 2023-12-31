package systems.monomer.syntaxtree.operators;

import org.jetbrains.annotations.Nullable;
import systems.monomer.commandline.EnvironmentDefaults.ConvertDefaults;
import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.CompileSize;
import systems.monomer.interpreter.*;
import systems.monomer.syntaxtree.Node;
import systems.monomer.types.OverloadedFunction;
import systems.monomer.types.Signature;
import systems.monomer.variables.Key;
import systems.monomer.variables.VariableKey;

import static systems.monomer.types.AnyType.ANY;

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
            second.setType(getType());
            second.matchTypes();
        }
        else {
            second.matchTypes();
            if(getType().equals(second.getType())) return;  //TODO also include subtypes/masking
            else if(second.getType() == ANY) {
                second.setType(getType());
                second.matchTypes();
                return;
            }

            VariableKey convertFunc = getVariable(ConvertDefaults.NAME);
            if(convertFunc != null) {
                OverloadedFunction overloads = (OverloadedFunction) convertFunc.getType();
                convertBy = overloads.getOverload(new Signature(getType(), second.getType()));
            }
            if(convertFunc == null || convertBy == null) {
                throwError("Cannot convert from " + second.getType() + " to " + getType());
            }
        }
    }

    public InterpretResult interpretValue() {
        //TODO check that the type is a subtype of the type
        return convertBy == null ? getSecond().interpretValue() : convertBy.call(getSecond().interpretValue().asValue(), InterpretObject.EMPTY);
    }

    @Override
    public InterpretVariable interpretVariable() {
        return getSecond().interpretVariable();
    }

    @Override
    public @Nullable Key getVariableKey() {
        return getSecond().getVariableKey();
    }

    public Operand compileValue(AssemblyFile file) {
        throw new Error("TODO unimplemented");
    }

    public CompileSize compileSize() {
        throw new Error("TODO unimplemented");
    }
}
