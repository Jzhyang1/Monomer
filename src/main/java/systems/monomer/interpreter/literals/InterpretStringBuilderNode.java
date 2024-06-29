package systems.monomer.interpreter.literals;

import systems.monomer.interpreter.InterpretNode;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.InterpretVariable;
import systems.monomer.interpreter.values.InterpretString;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.literals.StringBuilderNode;

import java.util.Collection;
import java.util.stream.Collectors;

public class InterpretStringBuilderNode extends StringBuilderNode implements InterpretNode {
    public InterpretStringBuilderNode(Collection<? extends Node> list) {
        super(list);
    }

    @Override
    public InterpretVariable interpretVariable() {
        throw runtimeError("Cannot interpret string value as variable");
    }

    public InterpretValue interpretValue() {
        return new InterpretString(
                getChildrenInterpretNodes().stream()
                        .map(x -> x.interpretValue().asValue().valueString())
                        .collect(Collectors.joining()));
    }


}
