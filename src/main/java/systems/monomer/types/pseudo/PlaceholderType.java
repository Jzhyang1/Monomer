package systems.monomer.types.pseudo;

import types.Type;

import java.util.Map;
import java.util.TreeMap;

public class PlaceholderType extends PseudoType {
    private Type expressed = AnyType.ANY;
    boolean hasReplaced = false;

    public PlaceholderType() {
    }

    public PlaceholderType(Type expressed) {
        this.expressed = expressed;
    }


    /**
     * stores the replacement to _this_
     *
     * @param newT the replacement type
     * @return this
     */
    public Type replace(Type newT) {
        hasReplaced = true;
        expressed = newT;
        return this;
    }

    @Override
    public boolean hasDependencies() {
        return !hasReplaced || expressed.hasDependencies();
    }

    @Override
    public Type simplify() {
        if (!hasReplaced) return this;

        //to prevent infinite recursion, remove expressed while performing simplify
        Type temp = expressed;
        expressed = ErasedType.ERASED;
        return temp.simplify();
    }

    @Override
    public Map<Type, Type> assign(Type newT) {
        Map<Type, Type> ret = new TreeMap<>();
        ret.put(this, newT);

        return ret;
    }

//    @Override
//    public boolean typeContains(Type other) {
//        return (other instanceof PlaceholderType pt) ? expressed.typeContains(pt.expressed) : expressed.typeContains(other);
//    }

    @Override
    public String valueString() {
        return "@" + Integer.toString(this.hashCode(), 16);// + "(" + expressed.valueString() + ")";
    }
}
