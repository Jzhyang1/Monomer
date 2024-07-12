package systems.monomer.types;

import systems.monomer.errorhandling.ErrorBlock;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.pseudo.AnyType;
import systems.monomer.util.Util;

import java.util.List;
import java.util.Map;

import static systems.monomer.errorhandling.ErrorBlock.programError;

public interface Type extends Comparable<Type> {
    /**
     * replaces all instances (including nested instances) of a type with a new type
     * and returns an IMMUTABLE COPY (which may be equal to this if this does not need
     * to be modified in order to replace t with newT); uses .equals(t).
     * testReplace will also return this if the type-wise behaviour remains the same
     * with the replacement (ie. fixed signature)
     *
     * @param replacements a map of all original PlaceholderTypes to their replacement types
     * @return this, or the replacement
     */
    //TODO testReplace needs to be overwritten in OverloadedFunctionType
    default Type testReplace(Map<Type, Type> replacements) {
        Type ret = replacements.get(this);
        return ret == null ? this : ret;
    }


    /**
     * replaces _this_ as if a value of type _this_ is subject to assignment (which
     * includes destructuring) from another value of type _newT_.
     * Eg. A.assign(B) will return {A:B} and (A,B).assign((C,D)) will return {A:C,B:D}
     * where A,B,C,D are PlaceholderTypes.
     * _assign_ will be overridden in Placeholder, Tuple, Object, and collection types.
     * simplify will not be called during execution.
     *
     * @param newT the type of the other value
     * @return a map of type replacements necessary when reassigning (this should be a
     * map of {PlaceholderType:Type})
     */
    default Map<Type, Type> assign(Type newT) {
        if (!newT.typeContains(this))
            throw programError("Cannot assign " + newT.valueString() + " to " + valueString(), ErrorBlock.Reason.SYNTAX);

        return Util.EMPTY_MAP;
    }

    /**
     * returns the type if a value of type _this_ were called in a function-manner
     * with the below arguments. This includes evaluating Signature, OverloadedFunction,
     * Collections (in the form of indexing). returnFor will return an OperatedType by
     * default.
     *
     * @return the return type from the function-manner call
     */
    default Type returnFor(Type arg, Type namedArg) {
        //create a new OperatedType ret whose types-to-return function
        // searches for a matching overload to the global CALL
        // OverloadedFunctionType and performs a returnFor operation
        // on the SignatureType stored there
        //TODO
        return AnyType.ANY;
    }


    /**
     * optimizes the type; necessary for pseudotypes
     *
     * @return this if the type is already simplified, otherwise a simplified version of this
     * @throws RuntimeException if a pseudotype cannot be fully simplified
     */
    default Type simplify() {
        return this;
    }

    /**
     * determines whether there are any PlaceholderTypes that may determine the
     * Type of _this_. hasDependencies is written to operate after simplify.
     *
     * @return whether this Type is dependent on any PlaceholderTypes
     */
    default boolean hasDependencies() {
        return false;
    }

    /**
     * used by signature to get a function's return type. Only implemented in ReturnType and UnionType.
     * Does not perform simplify which will need to be called explicitly in order to achieve the
     * same functionality. This happens in place so a copy will need to be made manually.
     *
     * @return the result of having all ReturnTypes replaced with their internal types
     */
    default Type unwrapReturns() {
        return this;
    }


    /**
     * returns the value that this type, after considering all generics, will use.
     * function bodies, actual values, generating lists, etc. do not count as defaultValues.
     * pseudoTypes will throw an error if this is called on them.
     *
     * @return the defaultValue of the type
     */
    InterpretValue defaultValue();


    /**
     * returns whether the other type can be converted to this type without any loss.
     * eg. FLOAT.typeContains(INT) would be true. However, if type A is to be cast
     * to type B, A.typeContains(B) should be true
     *
     * @param other the other type
     * @return this type contains the other type
     */
    boolean typeContains(Type other);

    /**
     * true if the types are equal
     *
     * @param other the other type
     * @return if the types are exactly equal
     */
    boolean equals(Object other);


    /**
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
        throw programError("Can not set field of " + this.valueString(), ErrorBlock.Reason.SYNTAX);
    }

    /**
     * @param field the name of the field
     * @return the type of the field with the given name
     */
    default Type getField(String field) {
        throw programError("Can not get field of " + this.valueString(), ErrorBlock.Reason.SYNTAX);
    }

    /**
     * an abstract method that is used to get all children
     * of a children-containing type. Note that Union is not
     * children-containing. Collection and Tuple are.
     * Do not iterate over the returned list as it may be
     * infinite.
     */
    default List<Type> getChildren() {
        return List.of();
    }


    /**
     * a sortable index over which similar types are grouped together, with most
     * inclusive types returning smaller serial numbers. The same serial does not
     * guarantee the same value for Unions, Tuples, and Objects. <br/>
     * the bits operate as follows:
     * <ul>
     *     <li>0x7f000000 is the general type (listed further below)</li>
     *     <li>0x00ff0000 is the subtype (eg. int vs float)</li>
     *     <li>0x0000ffff includes more data</li>
     * </ul>
     *
     *
     * <ul>
     *     <li>0x00000000 - ANY</li>
     *     <li>0x01000000 - unions</li>
     *     <li>0x02000000 - primitives</li>
     *     <li>0x04000000 - tuples</li>
     *     <li>0x08000000 - objects</li>
     *     <li>0x10000000 - collections</li>
     *     <li>0x20000000 - "dead" pseudotypes</li>
     * </ul>
     *
     * @return the sortable index
     */
    int serial();


    /**
     * the more inclusive type will be smaller. This will not simplify before performing
     * the comparison so simplify should be called before compareTo if the functionality is
     * desired (eg. for Unions)
     */
    @Override
    default int compareTo(Type o) {
        return serial() - o.serial();
    }

    /**
     * @return a string representation of the value of this type
     */
    String valueString();
}
