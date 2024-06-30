package systems.monomer.syntaxtree.operators;

import org.jetbrains.annotations.Nullable;
import systems.monomer.commandline.EnvironmentDefaults.ConvertDefaults;
import systems.monomer.interpreter.values.InterpretObject;
import systems.monomer.syntaxtree.Node;
import systems.monomer.types.ObjectType;
import systems.monomer.types.OverloadedFunctionType;
import systems.monomer.types.Signature;
import systems.monomer.types.Type;
import systems.monomer.variables.Key;
import systems.monomer.variables.VariableKey;

import java.util.function.BiFunction;

import static systems.monomer.types.AnyType.ANY;

public class AssertTypeNode extends OperatorNode {
    protected Signature convertBy = null;
    protected BiFunction<ObjectType, InterpretObject, InterpretObject> castBy = null;

    public AssertTypeNode() {
        super(":");
    }

    public AssertTypeNode(Node parent, Node child) {
        this();
        add(parent);
        add(child);
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
            return;
        }

        VariableKey convertFunc = getVariable(ConvertDefaults.NAME);
        if(convertFunc != null) {
            OverloadedFunctionType overloads = (OverloadedFunctionType) convertFunc.getType();
            convertBy = overloads.getOverload(new Signature(second.getType(), type));

            if(convertBy != null) return;
        }

        if(type.equals(second.getType()))
            return;
        if(second.getType() instanceof ObjectType secondType && secondType.typeConvertsTo(type)) {
            //TODO consider types during matchTypes
            castBy = (to, from) -> {
                InterpretObject ret = new InterpretObject();
                to.getFields().forEach((e, t) -> ret.set(e, from.get(e)));
                return ret;
            };
        }
        else
            throw syntaxError("Cannot convert type from " + second.getType() + " to " + getType());
    }

    @Override
    public @Nullable Key getVariableKey() {
        return getSecond().getVariableKey();
    }
}
