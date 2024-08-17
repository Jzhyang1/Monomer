package systems.monomer.compiler.output;

import lombok.Getter;
import lombok.Setter;
import systems.monomer.compiler.output.operations.CompileOperation;
import systems.monomer.compiler.output.operations.TerminatorCompileOperation;
import systems.monomer.compiler.output.size.CompileSize;

import java.util.*;
import java.util.function.Function;

public class CompileOutput {
    //adder and getter
    private final Deque<CompileOperation> main = new LinkedList<>();
    private final Map<String, CompileVariable> variables = new HashMap<>();
    private final Map<String, CompileOutput> functions = new HashMap<>();
    private final Map<
            CompileOperation,
            List<Function<Deque<CompileOperation>,Boolean>>
            /* takes the deque and returns whether the match executed */
            > patternMatchers = new HashMap<>();

    //TODO incrementer
    @Getter
    private CompileSize vTop, vBottom, sCurrent;

    public CompileOutput add(CompileOperation op) {
        main.add(op);
        return this;
    }
    public CompileOutput addVariable(String name, CompileVariable var) {
        variables.put(name, var);
        return this;
    }
    public CompileOutput addFunction(String uniqueName, CompileOutput func) {
        functions.put(uniqueName, func);
        return this;
    }
    public CompileOutput addPattern(CompileOperation trigger, Function<Deque<CompileOperation>,Boolean> handler) {
        if(!patternMatchers.containsKey(trigger)) {
            patternMatchers.put(trigger, new ArrayList<>());
        }
        patternMatchers.get(trigger).add(handler);
        return this;
    }

    @Getter @Setter private Function<String, String> formatLabel;
    @Getter @Setter private Function<Byte[], String> bytesToAssembly;
    @Getter @Setter private String head, tail;
    @Getter @Setter private String mainHeader, globalHeader, functionsHeader;

    public CompileOutput simplify() {

        int operationChanges;
        do {
            operationChanges = 0;
            main.add(TerminatorCompileOperation.TERMINATE);

            while(main.getFirst() != TerminatorCompileOperation.TERMINATE) {
                CompileOperation next = main.getFirst();
                List<Function<Deque<CompileOperation>,Boolean>> matchers = patternMatchers.get(next);
                if (matchers == null) continue;

                boolean executed = false;
                for (var matcher : matchers) {
                    if(matcher.apply(main)) {
                        executed = true;
                        ++operationChanges;
                        break;
                    }
                }

                if(!executed) main.push(main.poll());
            }

            main.poll();    //remove TERMINATE
        } while(operationChanges > 0);

        return this;
    }

    public String getAssembly() {
        StringBuilder out = new StringBuilder();
        out.append(this.head);

        out.append(this.globalHeader);
        for(Map.Entry<String, CompileVariable> entry : variables.entrySet()) {
            out.append(formatLabel.apply(entry.getKey()));
            out.append(bytesToAssembly.apply(entry.getValue().getByteValue()));
        }

        out.append(this.functionsHeader);
        for(Map.Entry<String, CompileOutput> entry : functions.entrySet()) {
            out.append(formatLabel.apply(entry.getKey()));
            out.append(entry.getValue().getAssembly());
        }

        out.append(this.mainHeader);
        for(CompileOperation op : main) out.append(op.getAssemblyString());

        out.append(this.tail);
        return out.toString();
    }
}
