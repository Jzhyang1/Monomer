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
import systems.monomer.types.Signature;
import systems.monomer.types.Type;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter @Setter
public class VariableKey extends InterpretVariable {
    private VariableKey parent;
    private InterpretValue value;
    private Type type = AnyType.ANY;
    private Map<Signature, InterpretFunction> overloads;    //TODO multikey map with Signature broken into returnType and args

    public VariableKey(){}
    public VariableKey(VariableKey parent) {
        this.parent = parent;
    }

    public boolean isMultivar() {
        return false;   //TODO
    }

    public void put(String field, Type value) {
        setField(field, value);   //uses the Type class's setField to store the value as a field
        if(value instanceof VariableKey key)
            key.setParent(this);
        else
            throw new Error("TODO unimplemented");  //TODO throwError
    }

    public VariableKey get(String field) {
        return (VariableKey) getField(field); //TODO check and throwError
    }

    public void setValue(InterpretValue value) {
        this.type = this.value = value;
    }

    /**
     * gets the exact overload matching _signature_
     * @param signature
     * @return the exact overload or null
     */
    public @Nullable InterpretFunction getOverload(Signature signature) {
        if(overloads == null) return null;  //TODO forward references
        return overloads.get(signature);
    }
    public void putOverload(Signature signature, InterpretFunction function) {
        if(overloads == null) overloads  = new HashMap<>();
        overloads.put(signature, function);

        //These handle unknown return types and unknown argument types
        if(!signature.getReturnType().equals(AnyType.ANY))
            overloads.put(new Signature(AnyType.ANY, signature.getArgs()), function);
        if(!signature.getArgs().equals(AnyType.ANY))
            overloads.put(new Signature(signature.getReturnType(), AnyType.ANY), function);
//        if(signature.getReturnType() != AnyType.ANY && signature.getArgs() != AnyType.ANY)
//            overloads.put(new Signature(AnyType.ANY, AnyType.ANY), function);
    }

    public void putOverload(Node args, Node body, ModuleNode wrapper) {
        putOverload(new Signature(body.getType(), args.getType()), new InterpretFunction(TupleNode.asTuple(args), body, wrapper));
    }

    public void putOverload(List<Type> argTypes, Function<List<VariableNode>, Node> bodyCallback) {
        List<VariableNode> args = IntStream.range(0, argTypes.size())
                .mapToObj(i -> {
                    VariableNode ret = new VariableNode("arg"+i);
                    ret.setType(argTypes.get(i));
                    return ret;
                }) //TODO type is wrong
                .collect(Collectors.toList());
        Node body = bodyCallback.apply(args);
        TupleNode argsTuple = new TupleNode(args);

        ModuleNode wrapper = new ModuleNode("function");
        wrapper.with(argsTuple).with(body).matchVariables();
        wrapper.matchTypes();

        putOverload(argsTuple, body, wrapper);
    }

    @Override
    public InterpretValue call(InterpretValue args) {
        Signature signature = new Signature(AnyType.ANY, args);
        //TODO fix this (currently doesn't handle type-matching)
        //TODO this might need to be replaced in favor of matchingOverload
        return matchingOverload(signature).call(args);
    }

    /**
     * returns the matching overload (ie _signature_ is contained by an existing signature)
     * @param signature
     * @return the matching overload
     */
    public @SneakyThrows InterpretFunction matchingOverload(Signature signature) {
        //TODO fix this (currently doesn't handle type-matching)
        if(overloads.containsKey(signature)) {
            return overloads.get(signature);
        } else{
            boolean needsRet = !signature.getReturnType().equals(AnyType.ANY);
            boolean needsArg = !signature.getArgs().equals(AnyType.ANY);
            Signature tempRetSignature = new Signature(AnyType.ANY, signature.getArgs());
            Signature tempArgSignature = new Signature(signature.getReturnType(), AnyType.ANY);

            if(needsRet && needsArg &&
                    overloads.containsKey(tempRetSignature) && overloads.containsKey(tempArgSignature)) {
                throw new Error("Ambiguous overload for " + signature);  //TODO throwError
            } else if(needsRet && overloads.containsKey(tempRetSignature)) {
                return overloads.get(tempRetSignature);
            } else if(needsArg && overloads.containsKey(tempArgSignature)) {
                return overloads.get(tempArgSignature);
            } else {
                throw new Error("No matching signature found for " + signature);  //TODO throwError
            }
        }
    }

    public InterpretValue getValue() {
        return value == null ? this : value;
    }

    public String valueString() {
        return value == null ? super.valueString() : value.valueString();
    }

    @Override
    public boolean typeContains(Type type) {
        return false;
    }

    @Override
    public boolean hasField(String field) {
        return super.hasField(field);
    }

    @Override
    public void assertField(String field, Type value) {
        super.assertField(field, value);
    }

    @Override
    public Type getField(String field) {
        return super.getField(field);
    }

    @Override
    public VariableKey clone() {
        VariableKey key = (VariableKey) super.clone();  //TODO also clone value, overloads, etc
        return key;
    }
}
