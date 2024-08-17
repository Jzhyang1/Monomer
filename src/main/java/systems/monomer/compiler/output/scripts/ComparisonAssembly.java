package systems.monomer.compiler.output.scripts;

import systems.monomer.compiler.operators.CompileOperatorNode;
import systems.monomer.compiler.output.CompileOutput;
import systems.monomer.compiler.output.CompileValue;

public abstract class ComparisonAssembly {
    public abstract CompileValue eqi(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue eqf(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue eqb(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue eql(CompileOperatorNode node, CompileOutput out);

    public abstract CompileValue neqi(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue neqf(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue neqb(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue neql(CompileOperatorNode node, CompileOutput out);

    public abstract CompileValue gti(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue gtf(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue gtb(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue gtl(CompileOperatorNode node, CompileOutput out);

    public abstract CompileValue lti(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue ltf(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue ltb(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue ltl(CompileOperatorNode node, CompileOutput out);

    public abstract CompileValue gteqi(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue gteqf(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue gteqb(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue gteql(CompileOperatorNode node, CompileOutput out);

    public abstract CompileValue lteqi(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue lteqf(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue lteqb(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue lteql(CompileOperatorNode node, CompileOutput out);

    public abstract CompileValue cmpi(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue cmpf(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue cmpb(CompileOperatorNode node, CompileOutput out);
    public abstract CompileValue cmpl(CompileOperatorNode node, CompileOutput out);
}
