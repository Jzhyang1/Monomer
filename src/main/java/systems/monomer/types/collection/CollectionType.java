package systems.monomer.types.collection;

import lombok.Getter;
import systems.monomer.types.Type;
import systems.monomer.types.pseudo.AnyType;
import systems.monomer.util.RepeatingList;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

//TODO add methods:
// index, sort, etc (copy from other languages)
// exclude length/size and concat because separate operators exist for those already
@Getter
public abstract class CollectionType implements Type {
    private Type elementType = AnyType.ANY;

    public CollectionType() {
    }

    public CollectionType(Type elementType) {
        this.elementType = elementType;
    }



    @Override
    public boolean hasDependencies() {
        return elementType.hasDependencies();
    }

    @Override
    public Type simplify() {
        elementType = elementType.simplify();
        return this;
    }

    @Override
    public Type testReplace(Map<Type, Type> replacements) {
        Type replacement = replacements.get(this);
        if (replacement != null) return replacement;

        try {
            return this.getClass().getDeclaredConstructor(Type.class).newInstance(elementType.testReplace(replacements));
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException("Internal error in " + this.getClass().getName());
        }
    }

    @Override
    public Map<Type, Type> assign(Type newT) {
        if (!(newT instanceof CollectionType ct))
            throw new RuntimeException("Can not assign " + newT.valueString() + " to " + valueString());

        Map<Type, Type> ret = new TreeMap<>();
        ret.put(elementType, ct.elementType);

        return ret;
    }

    @Override
    public List<Type> getChildren() {
        return new RepeatingList<>(elementType);
    }

    @Override
    public int serial() {
        return 0x10000000 + elementType.serial();
    }

    /**
     * typeContains on collections allow any type to "contain" another
     * collection in order for casts between collections to work freely
     */
    @Override
    public boolean typeContains(Type other) {
        return other instanceof CollectionType ct && elementType.typeContains(ct.elementType);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CollectionType ct && serial() == ct.serial() && elementType.equals(ct.elementType);
    }

    @Override
    public String valueString() {
        return "[" + elementType.valueString() + "]";
    }
}