package systems.monomer.compiler;

import systems.monomer.compiler.output.CompileOutput;
import systems.monomer.compiler.output.CompileValue;

public interface CompileNode {
    /**
     * adds processes to output and returns where the result can be found
     * @param output the accumulator for outputs
     * @return where the output of this operation can be found
     * after all operations in output have been processed
     */
    CompileValue compile(CompileOutput output);
}
