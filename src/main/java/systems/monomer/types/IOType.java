package systems.monomer.types;

import systems.monomer.Constants;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.values.InterpretIO;

import java.util.List;


public class IOType extends ObjectType {
    public IOType() {
        initFields();
    }


    private void initFields() {
        //io read
        OverloadedFunctionType readFunction = new OverloadedFunctionType();
        readFunction.putTypeOverload(List.of(), CharType.CHAR);
        readFunction.putTypeOverload(List.of(), StringType.STRING);
        readFunction.putTypeOverload(List.of(), NumberType.INTEGER);
        readFunction.putTypeOverload(List.of(NumberType.INTEGER), StringType.STRING);

        setField("read", readFunction);

        //io write
        OverloadedFunctionType writeFunction = new OverloadedFunctionType();
        writeFunction.putTypeOverload(List.of(CharType.CHAR), CharType.CHAR);
        writeFunction.putTypeOverload(List.of(StringType.STRING), StringType.STRING);
        writeFunction.putTypeOverload(List.of(NumberType.INTEGER), NumberType.INTEGER);

        setField("write", writeFunction);
    }

    @Override
    public InterpretValue defaultValue() {
        return new InterpretIO(Constants.getListener(), Constants.getOut());
    }
}
