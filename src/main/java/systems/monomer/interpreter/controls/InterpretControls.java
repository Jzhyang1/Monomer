package systems.monomer.interpreter.controls;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Nullable;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.values.InterpretBreaking;
import systems.monomer.interpreter.values.InterpretTuple;

@UtilityClass
public class InterpretControls {
    public static class InterpretControlResult {
        public final boolean isSuccess;
        public final boolean isBroken;
        public final InterpretResult value;

        public InterpretControlResult(boolean isSuccess, InterpretResult value) {
            this.isSuccess = isSuccess;
            this.isBroken = !value.isValue();
            this.value = value;
        }
    }

    public static @Nullable InterpretControlResult resultOfBreak(InterpretBreaking breaking, boolean isExpression) {
        if("continue".equals(breaking.getName())) return null;
        else if("break".equals(breaking.getName()))
            return new InterpretControlResult(false, isExpression ? breaking.asValue() : InterpretTuple.EMPTY);
        return new InterpretControlResult(false, isExpression ? breaking : InterpretTuple.EMPTY);
    }


}
