package systems.monomer.execution.environmentDefaults;

import lombok.experimental.UtilityClass;
import systems.monomer.interpreter.values.*;
import systems.monomer.syntaxtree.Node;
import systems.monomer.types.primitive.BoolType;
import systems.monomer.types.primitive.CharType;
import systems.monomer.types.collection.StringType;
import systems.monomer.types.primitive.FloatType;
import systems.monomer.types.primitive.IntType;
import systems.monomer.util.DefaultInterpretKey;

@UtilityClass
public class TypeDefaults {
    public void initGlobal(Node global) {
        global.putVariable("bool", new DefaultInterpretKey(InterpretBool.FALSE, BoolType.BOOL));
        global.putVariable("int", new DefaultInterpretKey(new InterpretInt(0), IntType.INT));
        global.putVariable("float", new DefaultInterpretKey(new InterpretFloat(0.0), FloatType.FLOAT));
        global.putVariable("char", new DefaultInterpretKey(new InterpretChar('\0'), CharType.CHAR));
        global.putVariable("string", new DefaultInterpretKey(new InterpretString(""), StringType.STRING));
    }
}
