package systems.monomer.types.object;


import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.values.InterpretObject;
import systems.monomer.types.Type;
import systems.monomer.util.Util;

import java.util.*;
import java.util.stream.Collectors;

public class ObjectType implements Type {
    public static final ObjectType EMPTY = new ObjectType();


    private final Map<String, Type> fields = new HashMap<>();
    private List<String> sortedKeys = null;


    @Override
    public boolean hasDependencies() {
        return fields.values().stream().anyMatch(Type::hasDependencies);
    }

    @Override
    public Type simplify() {
        for (Map.Entry<String, Type> fieldEntry : fields.entrySet()) {
            fieldEntry.setValue(fieldEntry.getValue().simplify());
        }

        return this;
    }

    @Override
    public Type testReplace(Map<Type, Type> replacements) {
        Type replacement = replacements.get(this);
        if (replacement != null) return replacement;

        ObjectType ret = new ObjectType();
        for (Map.Entry<String, Type> fieldEntry : fields.entrySet()) {
            ret.fields.put(fieldEntry.getKey(), fieldEntry.getValue().testReplace(replacements));
        }

        return ret;
    }

    @Override
    public Map<Type, Type> assign(Type newT) {
        //can not use typeContains here because of PlaceholderTypes
        if (!(newT instanceof ObjectType ot) || !fields.keySet().containsAll(ot.fields.keySet()))
            throw new RuntimeException("Can not assign " + newT.valueString() + " to " + valueString());

        Map<Type, Type> ret = new TreeMap<>();
        for (Map.Entry<String, Type> entry : fields.entrySet()) {
            Type otherField = ot.fields.get(entry.getKey());
            ret.putAll(entry.getValue().assign(otherField));
        }

        return ret;
    }

    @Override
    public InterpretValue defaultValue() {
        InterpretObject ret = new InterpretObject();
        fields.forEach((key, value) -> ret.set(key, value.defaultValue()));
        return ret;
    }

    @Override
    public boolean hasField(String field) {
        return fields.containsKey(field);
    }

    @Override
    public void setField(String field, Type value) {
        fields.put(field, value);
        sortedKeys = null;
    }

    @Override
    public Type getField(String field) {
        return fields.get(field);
    }

    public List<String> getSortedKeys() {
        if (sortedKeys == null) {
            sortedKeys = new ArrayList<>(fields.keySet());
            sortedKeys.sort(null);
        }
        return sortedKeys;
    }

    @Override
    public boolean typeContains(Type other) {
        if (!(other instanceof ObjectType ot)) return false;

        //other can not be contained in this if it has more fields
        if (ot.fields.size() > fields.size()) return false;

        for (Map.Entry<String, Type> entry : ot.fields.entrySet()) {
            Type thisField = fields.get(entry.getKey());
            if (thisField == null || !thisField.typeContains(entry.getValue())) return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ObjectType ot && fields.equals(ot.fields);
    }

    @Override
    public int serial() {
        return 0x0800ffff - fields.size();
    }

    @Override
    public int compareTo(Type o) {
        int c = Type.super.compareTo(o);
        if (c != 0) return c;

        //the other has to be UnionType now
        ObjectType ot = (ObjectType) o;

        //get constant ordering of keys so that the weight of an individual key
        // does not change between comparisons
        //TODO would using a TreeMap for the original fields map be better?
        // that would allow simply using two iterators between the two fields maps.
        // But whether to do that would depend on how frequently compareTo is used.
        // It might be more worthwhile to just cache sorted field keys
        List<String> keys = getSortedKeys();
        List<String> altKeys = ot.getSortedKeys();

        //compare the key lists first
        int keyDiff = Util.pairCheck(keys, altKeys, String::compareTo, 0);
        if (keyDiff != 0) return keyDiff;

        //compare element-by-element
        for (String key : keys) {
            int d = fields.get(key).compareTo(ot.fields.get(key));
            if (d != 0) return d;
        }

        return 0;
    }

    @Override
    public String valueString() {
        return "{" +
                fields.entrySet()
                        .stream()
                        .map((a) -> (a.getKey() + "=" + a.getValue().valueString()))
                        .collect(Collectors.joining(","))
                + "}";
    }
}
