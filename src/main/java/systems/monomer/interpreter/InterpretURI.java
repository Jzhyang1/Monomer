package systems.monomer.interpreter;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import systems.monomer.types.Type;
import systems.monomer.variables.VariableKey;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

//TODO fix this code
@Getter
public class InterpretURI extends VariableKey {
    private final @Nullable URI uri;

    public InterpretURI(String uri) {
        try {
            this.uri = new URI(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public InterpretURI() {
        this.uri = null;
    }

    @Override
    public Type getType() {
        return this;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof InterpretURI otherURI &&
                Objects.equals(otherURI.uri, uri);
    }

    public String toString() {
        return "uri";
    }
}
