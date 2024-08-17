package systems.monomer.compiler.output.size;

public class SystemSize extends FixedSize {
    public static SystemSize   B1 = new SystemSize(1), B2 = new SystemSize(2),
            B4 = new SystemSize(4), B8 = new SystemSize(8), B16 = new SystemSize(16),
            B32 = new SystemSize(32), B64 = new SystemSize(64), B128 = new SystemSize(128),
            B256 = new SystemSize(256);

    public static SystemSize getSystemSize(int byteSize) {
        return switch (byteSize) {
            case 1 -> B1;
            case 2 -> B2;
            case 4 -> B4;
            case 8 -> B8;
            case 16 -> B16;
            case 32 -> B32;
            case 64 -> B64;
            case 128 -> B128;
            case 256 -> B256;
            default -> throw new IllegalStateException("Unexpected value: " + byteSize);
        };
    }


    private SystemSize(int byteSize) {
        super(byteSize);
    }

    @Override
    public CompileSize simplify() {
        return this;
    }
}
