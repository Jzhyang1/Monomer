package systems.monomer.interpreter.values;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import systems.monomer.errorhandling.ErrorBlock;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.object.ObjectType;

import java.io.File;
import java.util.Objects;

import static systems.monomer.errorhandling.ErrorBlock.programError;

//TODO fix this code
@Getter
public class InterpretURI extends ObjectType implements InterpretValue {
    public static final InterpretURI URI = new InterpretURI();

    private final @Nullable File uri;

    public InterpretURI(String uri) {
        this.uri = new File(uri);
    }

    public InterpretURI() {
        this.uri = null;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof InterpretURI otherURI &&
                Objects.equals(otherURI.uri, uri);
    }

    public String toString() {
        return "uri";
    }

    @Override
    public InterpretValue getField(String field) {
        return (InterpretValue) super.getField(field);
    }

    @Override
    public InterpretURI clone() {
        try {
            return (InterpretURI) super.clone();
        } catch (CloneNotSupportedException e) {
            throw programError(e.getMessage(), ErrorBlock.Reason.OTHER);
        }
    }

    @Override
    public InterpretValue defaultValue() {
        return this;
    }

    @Override
    public int compareValueTo(InterpretValue other) {
        //TODO propagate compareValueTo to ObjectType
        //TODO also propagate compareValueTo to overloaded function "compare" when two values are not of the same type
        // with the exception of int-float-bool-etc. comparisons
        if (!(other instanceof InterpretURI otherURI)) {
            return compareTo(other);
        }

        if (uri == null) {
            return otherURI.uri == null ? 0 : -1;
        }

        return uri.compareTo(otherURI.uri);
    }
}
