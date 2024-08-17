package systems.monomer.interpreter;

import systems.monomer.interpreter.values.InterpretObject;
import systems.monomer.types.Type;
import systems.monomer.types.pseudo.AnyType;
import systems.monomer.types.signature.Signature;

public interface InterpretFunction {
    public InterpretValue call(InterpretValue args, InterpretValue namedArgs);

    public Type getReturnType();

    public Type getArgsType();

    public Type getNamedArgsType();

    public Signature getType();
}
