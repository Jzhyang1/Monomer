package systems.monomer.types.system;

import systems.monomer.execution.Constants;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.values.InterpretIO;
import systems.monomer.types.Type;
import systems.monomer.types.signature.Signature;
import systems.monomer.types.collection.StringType;
import systems.monomer.types.function.OverloadedFunctionType;
import systems.monomer.types.object.ObjectType;
import systems.monomer.types.primitive.CharType;
import systems.monomer.types.primitive.IntType;
import systems.monomer.types.tuple.TupleType;

import java.util.List;

public class IOType extends ObjectType {
    //accept namedArgs: stop delimiter, encoding, etc
    public IOType(){
        //io read
        List<Type> readOptions = List.of(
                new Signature(TupleType.EMPTY, ObjectType.EMPTY, CharType.CHAR),
                new Signature(TupleType.EMPTY, ObjectType.EMPTY, StringType.STRING),
                new Signature(TupleType.EMPTY, ObjectType.EMPTY, IntType.INT),
                new Signature(IntType.INT, ObjectType.EMPTY, StringType.STRING)
        );
        OverloadedFunctionType readFunction = new OverloadedFunctionType(readOptions);
        setField("read", readFunction);

        //io write
        List<Type> writeOptions = List.of(
                new Signature(CharType.CHAR, ObjectType.EMPTY, CharType.CHAR),
                new Signature(StringType.STRING, ObjectType.EMPTY, StringType.STRING),
                new Signature(IntType.INT, ObjectType.EMPTY, IntType.INT)
        );
        OverloadedFunctionType writeFunction = new OverloadedFunctionType(writeOptions);
        setField("write", writeFunction);
    }

    @Override
    public InterpretIO defaultValue() {
        return new InterpretIO(Constants.getListener(), Constants.getOut());
    }
}
