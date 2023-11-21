package systems.monomer.interpreter;

import systems.monomer.types.Type;

public abstract class InterpretVariable implements Type, Cloneable {

    /**
     * Returns a reference to the value of the variable.
     * Use clone() to get a copy of the value.
     * @return a reference to the value of the variable
     */
    public abstract InterpretValue getValue();
    public abstract void setValue(InterpretValue value);

    @Override
    public boolean typeContains(Type type) {
        return getType().typeContains(type);
    }

    @Override
    public boolean hasField(String field) {
        return getType().hasField(field);
    }

    @Override
    public void assertField(String field, Type value) {
        getType().assertField(field, value);
    }

    @Override
    public Type getField(String field) {
        return getType().getField(field);
    }

    @Override
    public InterpretValue defaultValue() {
        return getValue();
    }

    @Override
    public InterpretVariable clone() {
        try {
            InterpretVariable clone = (InterpretVariable) super.clone();
            if(getValue() != null)
                clone.setValue(getValue().clone());
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }
}