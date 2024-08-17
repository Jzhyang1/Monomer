package systems.monomer.variables;

import systems.monomer.types.Type;


public abstract class Key {
    public boolean isLocked(){ return false; }
    public void lock(){
        throw new UnsupportedOperationException("Can not lock " + this);
    }

    public abstract Type getType();
    public abstract void setType(Type type);

    public void setConstant(boolean constant) {
        getType().setConstant(constant);
    }
    public boolean isConstant() {
        return getType().isConstant();
    }
}
