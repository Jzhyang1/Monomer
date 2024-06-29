package systems.monomer.interpreter;

import systems.monomer.Constants;
import systems.monomer.interpreter.values.InterpretIO;
import systems.monomer.types.*;

import java.util.List;


public class IOType extends ObjectType {
    public IOType() {
        initFields();
    }


    private void initFields() {
        //io read
        OverloadedFunctionType readFunction = new OverloadedFunctionType();
        readFunction.putTypeOverload(List.of(), (args) -> new InterpretIO.CharReader(() -> InterpretIO.this.readIntent()));
        readFunction.putTypeOverload(List.of(), (args) -> new InterpretIO.StringReader(() -> InterpretIO.this.readIntent()));
        readFunction.putTypeOverload(List.of(), (args) -> new InterpretIO.IntReader(() -> InterpretIO.this.readIntent()));
        readFunction.putTypeOverload(List.of(NumberType.INTEGER), (args) -> new InterpretIO.MultiCharReader(() -> InterpretIO.this.readIntent(), (InterpretVariableNode) args.get(0)));

        setField("read", readFunction);

        //io write
        OverloadedFunctionType writeFunction = new OverloadedFunctionType();
        writeFunction.putTypeOverload(List.of(CharType.CHAR), (args) -> new InterpretIO.CharWriter(() -> InterpretIO.this.writeIntent(), (InterpretVariableNode) args.get(0)));
        writeFunction.putTypeOverload(List.of(StringType.STRING), (args) -> new InterpretIO.StringWriter(() -> InterpretIO.this.writeIntent(), (InterpretVariableNode) args.get(0)));
        writeFunction.putTypeOverload(List.of(NumberType.INTEGER), (args) -> new InterpretIO.IntWriter(() -> InterpretIO.this.writeIntent(), (InterpretVariableNode) args.get(0)));

        setField("write", writeFunction);
    }

    @Override
    public InterpretValue defaultValue() {
        return new InterpretIO(Constants.getListener(), Constants.getOut());
    }
}
