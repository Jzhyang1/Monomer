package systems.monomer.execution.environmentDefaults;

import lombok.experimental.UtilityClass;
import systems.monomer.interpreter.values.InterpretBool;
import systems.monomer.syntaxtree.Node;
import systems.monomer.types.primitive.BoolType;
import systems.monomer.util.DefaultInterpretKey;

@UtilityClass
public class ValueDefaults {
    public void initGlobal(Node global) {
        global.putVariable("true", new DefaultInterpretKey(InterpretBool.TRUE, BoolType.BOOL));
        global.putVariable("false", new DefaultInterpretKey(InterpretBool.FALSE, BoolType.BOOL));
    }
}
