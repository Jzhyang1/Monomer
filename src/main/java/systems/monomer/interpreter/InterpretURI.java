package systems.monomer.interpreter;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import systems.monomer.types.ObjectType;
import systems.monomer.types.Type;
import systems.monomer.variables.VariableKey;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

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
    public InterpretURI clone() {
        return (InterpretURI) super.clone();
    }

    @Override
    public InterpretValue defaultValue() {
        return this;
    }
}
