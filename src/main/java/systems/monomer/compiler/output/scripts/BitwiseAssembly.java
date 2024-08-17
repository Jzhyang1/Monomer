package systems.monomer.compiler.output.scripts;

import systems.monomer.compiler.operators.CompileOperatorNode;
import systems.monomer.compiler.output.CompileOutput;
import systems.monomer.compiler.output.CompileValue;

public abstract class BitwiseAssembly {

    //TODO replace (self) -> BoolType.BOOL with a named function that also handles non-bool
    public abstract CompileValue noti(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue notb(CompileOperatorNode node, CompileOutput out);

    public abstract CompileValue isb(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue isi(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue isf(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue isl(CompileOperatorNode node, CompileOutput out);

    public abstract CompileValue andi(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue andb(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue nandi(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue nandb(CompileOperatorNode node, CompileOutput out);

    public abstract CompileValue ori(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue orb(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue xori(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue xorb(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue nori(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue norb(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue nxori(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue nxorb(CompileOperatorNode node, CompileOutput out);
}
