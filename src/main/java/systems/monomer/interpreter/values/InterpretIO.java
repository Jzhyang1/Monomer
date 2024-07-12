package systems.monomer.interpreter.values;

import org.jetbrains.annotations.Nullable;
import systems.monomer.errorhandling.ErrorBlock;
import systems.monomer.execution.Constants;
import systems.monomer.types.system.IOType;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.variables.OverloadedFunction;

import java.io.*;

import static systems.monomer.errorhandling.ErrorBlock.programError;
import static systems.monomer.types.primitive.CharType.CHAR;
import static systems.monomer.types.primitive.IntType.INT;
import static systems.monomer.types.collection.StringType.STRING;

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
            throw programError(e.getMessage() + " (" + source.getPath() + ")", ErrorBlock.Reason.RUNTIME);
        }
    }
    private static OutputStream safeWriter(File source) {
        try {
            return new FileOutputStream(source);
        } catch (FileNotFoundException e) {
            throw programError(e.getMessage() + " (" + source.getPath() + ")", ErrorBlock.Reason.RUNTIME);
        }
    }
    private static void safeClose(@Nullable Closeable closeable) {
        if (closeable == null) return;

        try {
            closeable.close();
        } catch (IOException e) {
            throw programError(e.getMessage(), ErrorBlock.Reason.RUNTIME);
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
        OverloadedFunction readFunction = new OverloadedFunction();
        readFunction.putSupplierInterpretOverload(CHAR, this::readChar);
        readFunction.putSupplierInterpretOverload(STRING, this::readString);
        readFunction.putSupplierInterpretOverload(INT, this::readInt);
        readFunction.putSingleInterpretOverload(INT, STRING, this::readMultichar);
        setField("read", readFunction);

        //io write
        OverloadedFunction writeFunction = new OverloadedFunction();
        writeFunction.putSingleInterpretOverload(CHAR, CHAR, this::writeChar);
        writeFunction.putSingleInterpretOverload(STRING, STRING, this::writeString);
        writeFunction.putSingleInterpretOverload(INT, INT, this::writeInt);
        setField("write", writeFunction);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof InterpretIO otherIO && otherIO.reader == reader && otherIO.writer == writer;
    }

    @Override
    public InterpretIO clone() {
        return (InterpretIO) super.clone();
    }

    private Reader readIntent() {
        if (state == InterpretIO.IOState.READ || state == InterpretIO.IOState.BOTH) return reader;
        else if (state == InterpretIO.IOState.WRITE) safeClose(writer);
        else if (state == InterpretIO.IOState.CLOSED) throw programError("IO is closed", ErrorBlock.Reason.RUNTIME);

        reader = new BufferedReader(new InputStreamReader(safeReader(source)));
        state = InterpretIO.IOState.READ;
        return reader;
    }

    private Writer writeIntent() {
        if (state == InterpretIO.IOState.WRITE || state == InterpretIO.IOState.BOTH) return writer;
        else if (state == InterpretIO.IOState.READ) safeClose(reader);
        else if (state == InterpretIO.IOState.CLOSED) throw programError("IO is closed", ErrorBlock.Reason.RUNTIME);

        writer = new BufferedWriter(new OutputStreamWriter(safeWriter(source)));
        state = InterpretIO.IOState.WRITE;
        return writer;
    }

    public String toString() {
        return "io";
    }

    private InterpretValue readChar() {
        try {
            return new InterpretChar((char) readIntent().read());
        } catch (IOException e) {
            throw programError(e.getMessage(), ErrorBlock.Reason.RUNTIME);
        }
    }

    private InterpretValue readMultichar(InterpretValue countValue) {
//        if(!INTEGER.typeContains(countValue.getType()))
//            throw programError("expected " + countValue + " to be int, but was " + countValue.getType(), ErrorBlock.Reason.RUNTIME);

        try {
            Reader cachedReader = readIntent();
            //return new InterpretString(reader.read(count.interpretValue()));
            int number = ((InterpretNumber) countValue).getValue().intValue();
            StringBuilder ret = new StringBuilder();
            for (int i = 0; i < number; i++) {
                ret.append((char) cachedReader.read());
            }
            return new InterpretString(ret.toString());
        } catch (IOException e) {
            throw programError(e.getMessage(), ErrorBlock.Reason.RUNTIME);
        }
    }

    private InterpretValue readString() {
        try {
            Reader cachedReader = readIntent();
            StringBuilder ret = new StringBuilder();
            int c;
            while ((c = cachedReader.read()) != -1 && c != '\n') {
                ret.append((char) c);
            }
            return new InterpretString(ret.toString());
        } catch (IOException e) {
            throw programError(e.getMessage(), ErrorBlock.Reason.RUNTIME);
        }
    }

    private InterpretValue readInt() {
        try {
            Reader cachedReader = readIntent();
            StringBuilder ret = new StringBuilder();
            int c;
            while ((c = cachedReader.read()) != -1 && c != '\n') {
                ret.append((char) c);
            }
            return new InterpretNumber<>(Integer.valueOf(ret.toString()));
        } catch (IOException e) {
            throw programError(e.getMessage(), ErrorBlock.Reason.RUNTIME);
        }
    }



    private InterpretValue writeInt(InterpretValue iValue) {
//        if(!INTEGER.typeContains(iValue.getType()))
//            throw programError("expected " + iValue + " to be int, but was " + iValue.getType(), ErrorBlock.Reason.RUNTIME);
        int number = ((InterpretNumber) iValue).getValue().intValue();

        Writer cachedWriter = writeIntent();
        try {
            cachedWriter.write(String.valueOf(number));
            cachedWriter.flush();
        } catch (IOException e) {
            throw programError(e.getMessage(), ErrorBlock.Reason.RUNTIME);
        }
        return iValue;
    }

    private InterpretValue writeChar(InterpretValue cValue) {
//        if(!CHAR.typeContains(cValue.getType()))
//            throw programError("expected " + cValue + " to be char, but was " + cValue.getType(), ErrorBlock.Reason.RUNTIME);

        Writer cachedWriter = writeIntent();
        InterpretChar chr = (InterpretChar) cValue;

        try {
            cachedWriter.write(chr.getValue());
            cachedWriter.flush();
        } catch (IOException e) {
            throw programError(e.getMessage(), ErrorBlock.Reason.RUNTIME);
        }
        return cValue;
    }

    private InterpretValue writeString(InterpretValue sValue) {
//        if(!STRING.typeContains(sValue.getType()))
//            throw programError("expected " + sValue + " to be string, but was " + sValue.getType(), ErrorBlock.Reason.RUNTIME);

        Writer cachedWriter = writeIntent();
        InterpretString str = (InterpretString) sValue;
        try {
            cachedWriter.write(str.getValue());
            cachedWriter.flush();
        } catch (IOException e) {
            throw programError(e.getMessage(), ErrorBlock.Reason.RUNTIME);
        }
        return sValue;
    }
}
