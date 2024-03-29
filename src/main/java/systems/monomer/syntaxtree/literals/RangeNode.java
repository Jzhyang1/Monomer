package systems.monomer.syntaxtree.literals;

import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.CompileSize;
import systems.monomer.interpreter.InterpretNumber;
import systems.monomer.interpreter.InterpretRange;
import systems.monomer.interpreter.InterpretRanges;
import systems.monomer.syntaxtree.Node;


public class RangeNode extends LiteralNode {
    private final boolean startInclusive;
    private final boolean stopInclusive;

    public RangeNode(boolean startInclusive, boolean stopInclusive) {
        this.startInclusive = startInclusive;
        this.stopInclusive = stopInclusive;
    }
    public RangeNode(Node start, Node stop, boolean startInclusive, boolean stopInclusive) {
        this(startInclusive, stopInclusive);
        super.add(start);
        super.add(stop);
    }

    @Override
    public void matchTypes() {
        super.matchTypes();
        setType(new InterpretRanges(get(0).getType())); //TODO first and second could be different types
    }

    @Override
    public InterpretRanges interpretValue() {
        return new InterpretRanges(
                new InterpretRange(
                        get(0).interpretValue().asValue(),
                        get(1).interpretValue().asValue(),
                        new InterpretNumber<Integer>(1),
                        startInclusive,
                        stopInclusive
                )
        );
    }

    @Override
    public Operand compileValue(AssemblyFile file) {
        return null;
    }

    @Override
    public CompileSize compileSize() {
        return null;
    }
}
