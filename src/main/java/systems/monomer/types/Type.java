package systems.monomer.types;

import systems.monomer.compiler.CompileSize;
import systems.monomer.interpreter.InterpretValue;

public interface Type extends Cloneable {
    Type clone();

    /**
     * @return a string representation of the value of this type
     */
    String valueString();

    /**
     * true if the type given can be obtained as a part of this type
     * @param type the other type
     * @return this type contains the other type
     */
    boolean typeContains(Type type);

    /**
     * true if the types are equal
     * @param other the other type
     * @return if the types are exactly equal
     */
    boolean equals(Object other);

    /**
     *
     * @param field the name of the field
     * @return whether this type has a field with the given name
     */
    default boolean hasField(String field) {
        return false;
    }

    /**
     * @param field the name of the field
     * @param value the type of the field with the given name
     */
    default void setField(String field, Type value) {
        throw new Error("TODO unimplemented");
    }

    /**
     * checks if the field exists and if it contains the given type
     * if the field does not exist, sets the field to the given type
     * if the field does exist, checks if the type contains the given type, and if not, throws an error
     * @param field the name of the field
     * @param value the type of the field with the given name
     */
    default void assertField(String field, Type value) {
        throw new Error("TODO unimplemented");
    }

    /**
     * @param field the name of the field
     * @return the type of the field with the given name
     */
    default Type getField(String field) {
        throw new Error("TODO unimplemented");
    }

    /**
     * @return the size of the type in memory
     */
    default CompileSize compileSize() {
        throw new Error("TODO unimplemented");  //TODO make this not default
    }

    /**
     * @return the type as a Type
     */
    default Type getType() {
        return this;
    }

    /**
     * @return the default value of the type
     */
    InterpretValue defaultValue();
}
