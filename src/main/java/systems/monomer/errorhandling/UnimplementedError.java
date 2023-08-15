package systems.monomer.errorhandling;

import lombok.SneakyThrows;

public class UnimplementedError extends Error {
    public String toString() {
        return "unimplemented";
    }
}
