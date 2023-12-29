package systems.monomer.syntaxtree.operators;

import org.jetbrains.annotations.Nullable;
import systems.monomer.commandline.EnvironmentDefaults.ConvertDefaults;
import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.CompileSize;
import systems.monomer.interpreter.*;
import systems.monomer.syntaxtree.Node;
import systems.monomer.types.ObjectType;
import systems.monomer.types.OverloadedFunction;
import systems.monomer.types.Signature;
import systems.monomer.types.Type;
import systems.monomer.variables.Key;
import systems.monomer.variables.VariableKey;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static systems.monomer.types.AnyType.ANY;

public class AssertTypeNode extends OperatorNode {
    private InterpretFunction convertBy = null;
    private BiFunction<ObjectType, InterpretObject, InterpretObject> castBy = null;

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
            OverloadedFunction overloads = (OverloadedFunction) convertFunc.getType();
            convertBy = overloads.getOverload(new Signature(type, second.getType()));

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

    public InterpretResult interpretValue() {
        if(convertBy != null)
            return convertBy.call(getSecond().interpretValue().asValue(), InterpretObject.EMPTY);
        if(castBy != null) {
            InterpretValue from = getSecond().interpretValue().asValue();
            Type to = getType();

            if(from instanceof InterpretObject ofrom && to instanceof ObjectType oto)
                return castBy.apply(oto, ofrom);
            else
                throw syntaxError("Cannot cast object from " + from + " to " + to);
        }

        InterpretResult result = getSecond().interpretValue();
        if(!result.isValue())
            return result;

        if(result.asValue().getType().equals(getType()))
            return result;
        else
            throw syntaxError("Cannot convert value from " + result.asValue() + " to " + getType());
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
