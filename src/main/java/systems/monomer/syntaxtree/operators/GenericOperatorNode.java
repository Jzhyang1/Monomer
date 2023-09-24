package systems.monomer.syntaxtree.operators;

import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretFunction;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretTuple;
import systems.monomer.types.OverloadedFunction;
import systems.monomer.types.Signature;
import systems.monomer.types.TupleType;
import systems.monomer.types.Type;

import static systems.monomer.types.AnyType.ANY;

public class GenericOperatorNode extends OperatorNode {
    private final OverloadedFunction overloads;
    private int functionIndex = -1;

    public GenericOperatorNode(String name, OverloadedFunction overloads) {
        super(name);
        this.overloads = overloads;
    }

    public void matchTypes() {
        super.matchTypes();
        Type argType = size() == 1 ?
                getFirst().getType() :
                new TupleType(getChildren().stream().map((e)->e.getType()).toList());
        Type returnType = getType();

        functionIndex = overloads.randomAccessIndex(new Signature(returnType, argType));

        if(functionIndex == -1)
            throwError("No matching function found for " + argType + " -> " + returnType);

        InterpretFunction function = overloads.getFunction(functionIndex);

        Type actualReturnType = function.getReturnType();
        //TODO sus that actualReturnType is not checked against returnType
        if(actualReturnType == ANY)
            setType(function.testReturnType(argType));
        else if(returnType == ANY)
            setType(actualReturnType);
        else
            setType(returnType);
    }

    public CompileSize compileSize() {
        return getType().compileSize();
    }
    public CompileValue compileValue() {
        throw new Error("TODO unimplemented");
    }

    public InterpretResult interpretValue() {
        //TODO even Aidan would be ashamed of this
        return overloads
                .getFunction(functionIndex)
                .call(new InterpretTuple(getChildren().stream().map((e)->e.interpretValue().asValue()).toList()));
    }
}
