package systems.monomer.syntaxtree.operators;

import org.jetbrains.annotations.Nullable;
import systems.monomer.execution.environmentDefaults.ConvertDefaults;
import systems.monomer.syntaxtree.Node;
import systems.monomer.types.function.OverloadsType;
import systems.monomer.types.object.ObjectType;
import systems.monomer.types.signature.Signature;
import systems.monomer.types.Type;
import systems.monomer.variables.Key;
import systems.monomer.variables.VariableKey;


import static systems.monomer.execution.Handler.init;
import static systems.monomer.types.pseudo.AnyType.ANY;

public class AssertTypeNode extends OperatorNode {
    protected int convertBy = -1;

    public AssertTypeNode() {
        super(":");
    }

    public void matchTypes() {
        getFirst().matchTypes();
        Type type = getFirst().getType();

        setType(type);
        Node second = getSecond();
        if(second instanceof CallNode) {
            //TODO when else is requiresConvert not necessary other than in call?
            second.setType(type);
            second.matchTypes();
            return;
        }

        second.matchTypes();
        if(second.getType() == ANY) {
            second.setType(type);
            second.matchTypes();
        }
    }

    @Override
    public Node simplify() {
        Type to = getType();
        Type from = getSecond().getType();

        VariableKey convertFunc = getVariable(ConvertDefaults.NAME);
        if(convertFunc != null) {
            OverloadsType overloads = (OverloadsType) convertFunc.getType().getExpressed();
            convertBy = overloads.randomAccessIndex(new Signature(from, ObjectType.EMPTY, to));

            if(convertBy >= 0) return this;
        }

        if(to.equals(from))
            return getSecond();

        if(from.typeContains(to)) {
            CastNode ret = init.castNode();
            ret.add(getFirst()); ret.add(getSecond());
            ret.matchTypes();
            ret.simplify();
            return ret;
        }
        else
            throw syntaxError("Cannot convert type from " + from + " to " + to);
    }

    @Override
    public @Nullable Key getVariableKey() {
        return getSecond().getVariableKey();
    }
}
