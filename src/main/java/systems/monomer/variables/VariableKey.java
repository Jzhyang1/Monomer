package systems.monomer.variables;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Nullable;
import systems.monomer.interpreter.InterpretFunction;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.InterpretVariable;
import systems.monomer.syntaxtree.ModuleNode;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.VariableNode;
import systems.monomer.syntaxtree.literals.TupleNode;
import systems.monomer.types.AnyType;
import systems.monomer.types.ObjectType;
import systems.monomer.types.Signature;
import systems.monomer.types.Type;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter @Setter
public class VariableKey extends Key {
    private InterpretValue value;
    private Type type = AnyType.ANY;

    public VariableKey(){}

    public InterpretValue getValue() {
//        assert value != null;   //TODO
        return value;
    }

    public String valueString() {
//        assert value != null; //TODO
        return value.valueString();
    }

    @Override
    public VariableKey clone() {
        VariableKey key = (VariableKey) super.clone();  //TODO also clone value, overloads, etc
        return key;
    }
}
