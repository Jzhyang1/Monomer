package systems.monomer.interpreter;

import org.jetbrains.annotations.Nullable;
import systems.monomer.Constants;
import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.CompileSize;
import systems.monomer.syntaxtree.VariableNode;
import systems.monomer.syntaxtree.literals.LiteralNode;
import systems.monomer.types.*;
import systems.monomer.variables.VariableKey;

import java.io.*;
import java.util.List;
import java.util.function.Supplier;

//TODO fix this code
public class InterpretIO extends ObjectType implements InterpretValue {
    public static final InterpretIO STDIO = new InterpretIO(Constants.getListener(), Constants.getOut());
    private enum IOState {
        READ, WRITE, NEITHER, BOTH, CLOSED
    }

    private IOState state = IOState.NEITHER;
    private final @Nullable File source;
    private @Nullable Reader reader;
    private @Nullable Writer writer;

    private static InputStream safeReader(File source) {
        try {
            return new FileInputStream(source);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage() + " (" + source.getPath() + ")");
        }
    }
    private static OutputStream safeWriter(File source) {
        try {
            return new FileOutputStream(source);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage() + " (" + source.getPath() + ")");
        }
    }
    private static void safeClose(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public InterpretIO(File source) {
        this.source = source;
        initFields();
    }
    public InterpretIO(InputStream inputStream, OutputStream outputStream) {
        this.source = null;
        reader = new BufferedReader(new InputStreamReader(inputStream));
        writer = new BufferedWriter(new OutputStreamWriter(outputStream));

        state = IOState.BOTH;
        initFields();
    }

    private void initFields() {
        setField("read", new VariableKey(){{
            setType(new OverloadedFunctionType(){{
                putOverload(List.of(), (args)->new CharReader(()->InterpretIO.this.readIntent()));
                putOverload(List.of(), (args)->new StringReader(()->InterpretIO.this.readIntent()));
                putOverload(List.of(), (args)->new IntReader(()->InterpretIO.this.readIntent()));

                putOverload(List.of(NumberType.INTEGER), (args) -> new MultiCharReader(()->InterpretIO.this.readIntent(), args.get(0)));
            }});
        }});

        setField("write", new VariableKey(){{
            setType(new OverloadedFunctionType(){{
                putOverload(List.of(CharType.CHAR), (args) -> new CharWriter(()->InterpretIO.this.writeIntent(), args.get(0)));
                putOverload(List.of(StringType.STRING), (args) -> new StringWriter(()->InterpretIO.this.writeIntent(), args.get(0)));
                putOverload(List.of(NumberType.INTEGER), (args) -> new IntWriter(()->InterpretIO.this.writeIntent(), args.get(0)));
            }});
        }});
    }

    @Override
    public InterpretValue defaultValue() {
        return this;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof InterpretIO otherIO &&
                otherIO.reader == reader &&
                otherIO.writer == writer;
    }

    @Override
    public InterpretIO clone() {
        return (InterpretIO) super.clone(); //TODO clone reader and writer
    }

    private Reader readIntent() {
        if(state == IOState.READ || state == IOState.BOTH) return reader;
        else if(state == IOState.WRITE) safeClose(writer);
        else if(state == IOState.CLOSED) throw new Error("IO is closed");

        reader = new BufferedReader(new InputStreamReader(safeReader(source)));
        state = IOState.READ;
        return reader;
    }
    private Writer writeIntent() {
        if(state == IOState.WRITE || state == IOState.BOTH) return writer;
        else if(state == IOState.READ) safeClose(reader);
        else if(state == IOState.CLOSED) throw new Error("IO is closed");

        writer = new BufferedWriter(new OutputStreamWriter(safeWriter(source)));
        state = IOState.WRITE;
        return writer;
    }

    public String toString() {
        return "io";
    }

    public static class CharReader extends LiteralNode {
        private final Supplier<Reader> reader;
        public CharReader(Supplier<Reader> reader) {
            this.reader = reader;
        }
        public InterpretValue interpretValue() {
            try {
                return new InterpretChar((char) reader.get().read());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void matchTypes() {
            setType(CharType.CHAR);
        }

        @Override
        public Operand compileValue(AssemblyFile file) {
            return null;
        }

        @Override
        public CompileSize compileSize() {
            return null;
        }
    }

    public static class MultiCharReader extends LiteralNode {
        private final Supplier<Reader> reader;
        private final VariableNode count;

        public MultiCharReader(Supplier<Reader> reader, VariableNode count) {
            this.reader = reader;
            this.count = count;
        }

        public InterpretValue interpretValue() {
            try {
                Reader cachedReader = reader.get();
                //return new InterpretString(reader.read(count.interpretValue()));
                InterpretValue countValue = count.interpretValue();
                if(countValue instanceof InterpretNumber number) {
                    StringBuilder ret = new StringBuilder();
                    for(int i = 0; i < number.getValue().intValue(); i++) {
                        ret.append((char) cachedReader.read());
                    }
                    return new InterpretString(ret.toString());
                } else {
                    throw new Error("TODO unimplemented");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void matchTypes() {
            setType(StringType.STRING);
        }

        @Override
        public Operand compileValue(AssemblyFile file) {
            return null;
        }

        @Override
        public CompileSize compileSize() {
            return null;
        }
    }

    public static class StringReader extends LiteralNode {
        private final Supplier<Reader> reader;

        public StringReader(Supplier<Reader> reader) {
            this.reader = reader;
        }

        public InterpretValue interpretValue() {
            try {
                Reader cachedReader = reader.get();
                StringBuilder ret = new StringBuilder();
                int c;
                while((c = cachedReader.read()) != -1 && c != '\n') {
                    ret.append((char) c);
                }
                return new InterpretString(ret.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void matchTypes() {
            setType(StringType.STRING);
        }

        @Override
        public Operand compileValue(AssemblyFile file) {
            return null;
        }

        @Override
        public CompileSize compileSize() {
            return null;
        }
    }

    public static class IntReader extends LiteralNode {
        private final Supplier<Reader> reader;

        public IntReader(Supplier<Reader> reader) {
            this.reader = reader;
        }

        public InterpretValue interpretValue() {
            try {
                Reader cachedReader = reader.get();
                StringBuilder ret = new StringBuilder();
                int c;
                while((c = cachedReader.read()) != -1 && c != '\n') {
                    ret.append((char) c);
                }
                return new InterpretNumber<>(Integer.valueOf(ret.toString()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void matchTypes() {
            setType(NumberType.INTEGER);
        }

        @Override
        public Operand compileValue(AssemblyFile file) {
            return null;
        }

        @Override
        public CompileSize compileSize() {
            return null;
        }
    }

    public static class IntWriter extends LiteralNode {
        private final Supplier<Writer> writer;
        private final VariableNode val;

        public IntWriter(Supplier<Writer> writer, VariableNode val) {
            this.writer = writer;
            this.val = val;
        }

        public InterpretValue interpretValue() {
            Writer cachedWriter = writer.get();
            InterpretValue iValue = val.interpretValue();
            if(iValue instanceof InterpretNumber number) {
                try {
                    cachedWriter.write(String.valueOf(number.getValue().intValue()));
                    cachedWriter.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return iValue;
            } else {
                throw new Error("bad argument " + iValue + " to write (expected int)");
            }
        }

        @Override
        public void matchTypes() {
            setType(NumberType.INTEGER);
        }

        @Override
        public Operand compileValue(AssemblyFile file) {
            return null;
        }

        @Override
        public CompileSize compileSize() {
            return null;
        }
    }

    public static class CharWriter extends LiteralNode {
        private final Supplier<Writer> writer;
        private final VariableNode val;

        public CharWriter(Supplier<Writer> writer, VariableNode val) {
            this.writer = writer;
            this.val = val;
        }

        public InterpretValue interpretValue() {
            Writer cachedWriter = writer.get();
            InterpretValue cValue = val.interpretValue();
            if(cValue instanceof InterpretChar chr) {
                try {
                    cachedWriter.write(chr.getValue());
                    cachedWriter.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return cValue;
            } else {
                throw new Error("bad argument " + cValue + " to write (expected char)");
            }
        }

        @Override
        public void matchTypes() {
            setType(CharType.CHAR);
        }

        @Override
        public Operand compileValue(AssemblyFile file) {
            return null;
        }

        @Override
        public CompileSize compileSize() {
            return null;
        }
    }

    public static class StringWriter extends LiteralNode {
        private final Supplier<Writer> writer;
        private final VariableNode val;

        public StringWriter(Supplier<Writer> writer, VariableNode val) {
            this.writer = writer;
            this.val = val;
        }

        public InterpretValue interpretValue() {
            Writer cachedWriter = writer.get();
            InterpretValue sValue = val.interpretValue();
            if(sValue instanceof InterpretString str) {
                try {
                    cachedWriter.write(str.getValue());
                    cachedWriter.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return sValue;
            } else {
                throw new Error("bad argument " + sValue + " to write (expected string)");
            }
        }

        @Override
        public void matchTypes() {
            setType(StringType.STRING);
        }

        @Override
        public Operand compileValue(AssemblyFile file) {
            return null;
        }

        @Override
        public CompileSize compileSize() {
            return null;
        }
    }
}
