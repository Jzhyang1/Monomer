package systems.monomer.compiler.output.scripts;

import systems.monomer.compiler.operators.CompileOperatorNode;
import systems.monomer.compiler.output.CompileOutput;
import systems.monomer.compiler.output.CompileValue;

public abstract class ArithmeticAssembly {

    //TODO optimize paired div and remainder
    public abstract CompileValue posi(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue posf(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue negi(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue negf(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue addi(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue addf(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue subi(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue subf(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue muli(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue mulf(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue divi(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue divf(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue modi(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue modf(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue plli(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue pllf(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue powi(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue powf(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue rooti(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue rootf(CompileOperatorNode node, CompileOutput out);
}
