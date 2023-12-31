package systems.monomer.types;

import systems.monomer.compiler.CompileSize;
import systems.monomer.interpreter.InterpretBool;
import systems.monomer.interpreter.InterpretValue;

public class BoolType extends AnyType {
    public static final BoolType BOOL = new BoolType();

    @Override
    public String valueString() {
        return "bool";
    }

    @Override
    public boolean typeContains(Type type) {
        return type instanceof BoolType;
    }

    @Override
    public BoolType clone() {
        return (BoolType) super.clone();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof BoolType;
    }

    @Override
    public InterpretValue defaultValue() {
        return InterpretBool.FALSE;
    }

    public CompileSize compileSize() {
        return new CompileSize(1);
    }
}
