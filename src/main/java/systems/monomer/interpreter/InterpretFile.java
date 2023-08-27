package systems.monomer.interpreter;

import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.syntaxtree.ModuleNode;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.VariableNode;
import systems.monomer.syntaxtree.literals.LiteralNode;
import systems.monomer.syntaxtree.literals.TupleNode;
import systems.monomer.types.*;
import systems.monomer.variables.FunctionKey;

import java.io.*;
import java.util.List;

public class InterpretFile extends ObjectType implements InterpretValue {
    private Reader reader;
    private Writer writer;

    private static Reader safeReader(File source) {
        try {
            return new BufferedReader(new FileReader(source));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    private static Writer safeWriter(File source) {
        try {
            return new BufferedWriter(new FileWriter(source));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public InterpretFile(File source) {
        this(new BufferedReader(safeReader(source)), new BufferedWriter(safeWriter(source)));
    }
    public InterpretFile(Reader reader, Writer writer){
        setField("read", new FunctionKey(){{
            putOverload(new TupleNode(), new CharReader(reader), new ModuleNode("function"));
            putOverload(new TupleNode(), new StringReader(reader), new ModuleNode("function"));

            putOverload(List.of(NumberType.INTEGER), (args) -> new MultiCharReader(reader, args.get(0)));
        }});

        setField("write", new FunctionKey(){{
            putOverload(List.of(NumberType.INTEGER), (args) -> new IntWriter(writer, args.get(0)));
            putOverload(List.of(CharType.CHAR), (args) -> new CharWriter(writer, args.get(0)));
            putOverload(List.of(StringType.STRING), (args) -> new StringWriter(writer, args.get(0)));
        }});
    }


    private static class CharReader extends LiteralNode {
        private Reader reader;
        public CharReader(Reader reader) {
            this.reader = reader;
        }
        public InterpretValue interpretValue() {
            try {
                return new InterpretChar((char) reader.read());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void matchTypes() {
            setType(CharType.CHAR);
        }

        @Override
        public CompileValue compileValue() {
            return null;
        }

        @Override
        public CompileSize compileSize() {
            return null;
        }
    }

    private static class MultiCharReader extends LiteralNode {
        private Reader reader;
        private VariableNode count;

        public MultiCharReader(Reader reader, VariableNode count) {
            this.reader = reader;
            this.count = count;
        }

        public InterpretValue interpretValue() {
            try {
                //return new InterpretString(reader.read(count.interpretValue()));
                InterpretValue countValue = count.interpretValue();
                if(countValue instanceof InterpretNumber number) {
                    StringBuilder ret = new StringBuilder();
                    for(int i = 0; i < number.getValue().intValue(); i++) {
                        ret.append((char) reader.read());
                    }
                    return new InterpretString(ret.toString());
                } else {
                    throw new Error("TODO unimplemented");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void matchTypes() {
            setType(StringType.STRING);
        }

        @Override
        public CompileValue compileValue() {
            return null;
        }

        @Override
        public CompileSize compileSize() {
            return null;
        }
    }

    private static class StringReader extends LiteralNode {
        private Reader reader;

        public StringReader(Reader reader) {
            this.reader = reader;
        }

        public InterpretValue interpretValue() {
            try {
                StringBuilder ret = new StringBuilder();
                int c;
                while((c = reader.read()) != -1 && c != '\n') {
                    ret.append((char) c);
                }
                return new InterpretString(ret.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void matchTypes() {
            setType(StringType.STRING);
        }

        @Override
        public CompileValue compileValue() {
            return null;
        }

        @Override
        public CompileSize compileSize() {
            return null;
        }
    }

    private static class IntWriter extends LiteralNode {
        private Writer writer;
        private VariableNode val;

        public IntWriter(Writer writer, VariableNode val) {
            this.writer = writer;
            this.val = val;
        }

        public InterpretValue interpretValue() {
            InterpretValue iValue = val.interpretValue();
            if(iValue instanceof InterpretNumber number) {
                try {
                    writer.write(String.valueOf(number.getValue().intValue()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return iValue;
            } else {
                throw new Error("bad argument " + iValue + " to write (expected int)");
            }
        }

        @Override
        public void matchTypes() {
            setType(NumberType.INTEGER);
        }

        @Override
        public CompileValue compileValue() {
            return null;
        }

        @Override
        public CompileSize compileSize() {
            return null;
        }
    }

    private static class CharWriter extends LiteralNode {
        private Writer writer;
        private VariableNode val;

        public CharWriter(Writer writer, VariableNode val) {
            this.writer = writer;
            this.val = val;
        }

        public InterpretValue interpretValue() {
            InterpretValue cValue = val.interpretValue();
            if(cValue instanceof InterpretChar chr) {
                try {
                    writer.write(chr.getValue());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return cValue;
            } else {
                throw new Error("bad argument " + cValue + " to write (expected char)");
            }
        }

        @Override
        public void matchTypes() {
            setType(CharType.CHAR);
        }

        @Override
        public CompileValue compileValue() {
            return null;
        }

        @Override
        public CompileSize compileSize() {
            return null;
        }
    }

    private static class StringWriter extends LiteralNode {
        private Writer writer;
        private VariableNode val;

        public StringWriter(Writer writer, VariableNode val) {
            this.writer = writer;
            this.val = val;
        }

        public InterpretValue interpretValue() {
            InterpretValue sValue = val.interpretValue();
            if(sValue instanceof InterpretString str) {
                try {
                    writer.write(str.getValue());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return sValue;
            } else {
                throw new Error("bad argument " + sValue + " to write (expected string)");
            }
        }

        @Override
        public void matchTypes() {
            setType(StringType.STRING);
        }

        @Override
        public CompileValue compileValue() {
            return null;
        }

        @Override
        public CompileSize compileSize() {
            return null;
        }
    }
}
