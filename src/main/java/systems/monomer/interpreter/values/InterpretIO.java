package systems.monomer.interpreter.values;

import org.jetbrains.annotations.Nullable;
import systems.monomer.Constants;
import systems.monomer.interpreter.IOType;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.InterpretVariableNode;
import systems.monomer.syntaxtree.literals.LiteralNode;
import systems.monomer.types.CharType;
import systems.monomer.types.NumberType;
import systems.monomer.types.OverloadedFunctionType;
import systems.monomer.types.StringType;

import java.io.*;
import java.util.List;
import java.util.function.Supplier;

import static systems.monomer.errorhandling.ErrorBlock.programError;

public class InterpretIO extends IOType implements InterpretValue {
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
            throw programError(e.getMessage() + " (" + source.getPath() + ")");
        }
    }
    private static OutputStream safeWriter(File source) {
        try {
            return new FileOutputStream(source);
        } catch (FileNotFoundException e) {
            throw programError(e.getMessage() + " (" + source.getPath() + ")");
        }
    }
    private static void safeClose(@Nullable Closeable closeable) {
        if (closeable == null) return;

        try {
            closeable.close();
        } catch (IOException e) {
            throw programError(e.getMessage());
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
        //io read
        OverloadedFunctionType readFunction = new OverloadedFunctionType();
        readFunction.putInterpretOverload(List.of(), (args) -> new InterpretIO.CharReader(() -> InterpretIO.this.readIntent()));
        readFunction.putInterpretOverload(List.of(), (args) -> new InterpretIO.StringReader(() -> InterpretIO.this.readIntent()));
        readFunction.putInterpretOverload(List.of(), (args) -> new InterpretIO.IntReader(() -> InterpretIO.this.readIntent()));
        readFunction.putInterpretOverload(List.of(NumberType.INTEGER), (args) -> new InterpretIO.MultiCharReader(() -> InterpretIO.this.readIntent(), (InterpretVariableNode) args.get(0)));

        setField("read", readFunction);

        //io write
        OverloadedFunctionType writeFunction = new OverloadedFunctionType();
        writeFunction.putInterpretOverload(List.of(CharType.CHAR), (args) -> new InterpretIO.CharWriter(() -> InterpretIO.this.writeIntent(), (InterpretVariableNode) args.get(0)));
        writeFunction.putInterpretOverload(List.of(StringType.STRING), (args) -> new InterpretIO.StringWriter(() -> InterpretIO.this.writeIntent(), (InterpretVariableNode) args.get(0)));
        writeFunction.putInterpretOverload(List.of(NumberType.INTEGER), (args) -> new InterpretIO.IntWriter(() -> InterpretIO.this.writeIntent(), (InterpretVariableNode) args.get(0)));

        setField("write", writeFunction);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof InterpretIO otherIO && otherIO.reader == reader && otherIO.writer == writer;
    }

    private Reader readIntent() {
        if (state == InterpretIO.IOState.READ || state == InterpretIO.IOState.BOTH) return reader;
        else if (state == InterpretIO.IOState.WRITE) safeClose(writer);
        else if (state == InterpretIO.IOState.CLOSED) throw programError("IO is closed");

        reader = new BufferedReader(new InputStreamReader(safeReader(source)));
        state = InterpretIO.IOState.READ;
        return reader;
    }

    private Writer writeIntent() {
        if (state == InterpretIO.IOState.WRITE || state == InterpretIO.IOState.BOTH) return writer;
        else if (state == InterpretIO.IOState.READ) safeClose(reader);
        else if (state == InterpretIO.IOState.CLOSED) throw programError("IO is closed");

        writer = new BufferedWriter(new OutputStreamWriter(safeWriter(source)));
        state = InterpretIO.IOState.WRITE;
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
                throw programError(e.getMessage());
            }
        }

        @Override
        public void matchTypes() {
            setType(CharType.CHAR);
        }
    }

    public static class MultiCharReader extends LiteralNode {
        private final Supplier<Reader> reader;
        private final InterpretVariableNode count;

        public MultiCharReader(Supplier<Reader> reader, InterpretVariableNode count) {
            this.reader = reader;
            this.count = count;
        }

        public InterpretValue interpretValue() {
            InterpretValue countValue = count.interpretValue();
            if(!NumberType.INTEGER.typeContains(countValue.getType()))
                throw syntaxError("expected " + countValue + " to be int, but was " + countValue.getType());

            try {
                Reader cachedReader = reader.get();
                //return new InterpretString(reader.read(count.interpretValue()));
                int number = ((InterpretNumber) countValue).getValue().intValue();
                StringBuilder ret = new StringBuilder();
                for (int i = 0; i < number; i++) {
                    ret.append((char) cachedReader.read());
                }
                return new InterpretString(ret.toString());
            } catch (IOException e) {
                throw programError(e.getMessage());
            }
        }

        @Override
        public void matchTypes() {
            setType(StringType.STRING);
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
                while ((c = cachedReader.read()) != -1 && c != '\n') {
                    ret.append((char) c);
                }
                return new InterpretString(ret.toString());
            } catch (IOException e) {
                throw programError(e.getMessage());
            }
        }

        @Override
        public void matchTypes() {
            setType(StringType.STRING);
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
                while ((c = cachedReader.read()) != -1 && c != '\n') {
                    ret.append((char) c);
                }
                return new InterpretNumber<>(Integer.valueOf(ret.toString()));
            } catch (IOException e) {
                throw programError(e.getMessage());
            }
        }

        @Override
        public void matchTypes() {
            setType(NumberType.INTEGER);
        }
    }

    public static class IntWriter extends LiteralNode {
        private final Supplier<Writer> writer;
        private final InterpretVariableNode val;

        public IntWriter(Supplier<Writer> writer, InterpretVariableNode val) {
            this.writer = writer;
            this.val = val;
        }

        public InterpretValue interpretValue() {
            InterpretValue iValue = val.interpretValue();
            if(!NumberType.INTEGER.typeContains(iValue.getType()))
                throw syntaxError("expected " + iValue + " to be int, but was " + iValue.getType());
            int number = ((InterpretNumber) iValue).getValue().intValue();

            Writer cachedWriter = writer.get();
            try {
                cachedWriter.write(String.valueOf(number));
                cachedWriter.flush();
            } catch (IOException e) {
                throw programError(e.getMessage());
            }
            return iValue;
        }

        @Override
        public void matchTypes() {
            setType(NumberType.INTEGER);
        }
    }

    public static class CharWriter extends LiteralNode {
        private final Supplier<Writer> writer;
        private final InterpretVariableNode val;

        public CharWriter(Supplier<Writer> writer, InterpretVariableNode val) {
            this.writer = writer;
            this.val = val;
        }

        public InterpretValue interpretValue() {
            InterpretValue cValue = val.interpretValue();
            if(!CharType.CHAR.typeContains(cValue.getType()))
                throw syntaxError("expected " + cValue + " to be char, but was " + cValue.getType());

            Writer cachedWriter = writer.get();
            InterpretChar chr = (InterpretChar) cValue;

            try {
                cachedWriter.write(chr.getValue());
                cachedWriter.flush();
            } catch (IOException e) {
                throw programError(e.getMessage());
            }
            return cValue;
        }

        @Override
        public void matchTypes() {
            setType(CharType.CHAR);
        }
    }

    public static class StringWriter extends LiteralNode {
        private final Supplier<Writer> writer;
        private final InterpretVariableNode val;

        public StringWriter(Supplier<Writer> writer, InterpretVariableNode val) {
            this.writer = writer;
            this.val = val;
        }

        public InterpretValue interpretValue() {
            InterpretValue sValue = val.interpretValue();
            if(!StringType.STRING.typeContains(sValue.getType()))
                throw syntaxError("expected " + sValue + " to be string, but was " + sValue.getType());

            Writer cachedWriter = writer.get();
            InterpretString str = (InterpretString) sValue;
            try {
                cachedWriter.write(str.getValue());
                cachedWriter.flush();
            } catch (IOException e) {
                throw programError(e.getMessage());
            }
            return sValue;
        }

        @Override
        public void matchTypes() {
            setType(StringType.STRING);
        }
    }
}
