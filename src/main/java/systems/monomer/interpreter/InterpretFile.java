package systems.monomer.interpreter;

import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.syntaxtree.ModuleNode;
import systems.monomer.syntaxtree.VariableNode;
import systems.monomer.syntaxtree.literals.LiteralNode;
import systems.monomer.syntaxtree.literals.TupleNode;
import systems.monomer.types.*;
import systems.monomer.variables.VariableKey;

import java.io.*;
import java.util.List;

public class InterpretFile extends VariableKey {
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
        setField("read", new VariableKey(){{
            putOverload(List.of(), (args)->new CharReader(reader));
            putOverload(List.of(), (args)->new StringReader(reader));
            putOverload(List.of(), (args)->new IntReader(reader));

            putOverload(List.of(NumberType.INTEGER), (args) -> new MultiCharReader(reader, args.get(0)));
        }});

        setField("write", new VariableKey(){{
            putOverload(List.of(NumberType.INTEGER), (args) -> new IntWriter(writer, args.get(0)));
            putOverload(List.of(CharType.CHAR), (args) -> new CharWriter(writer, args.get(0)));
            putOverload(List.of(StringType.STRING), (args) -> new StringWriter(writer, args.get(0)));
        }});
    }


    public static class CharReader extends LiteralNode {
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

    public static class MultiCharReader extends LiteralNode {
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

    public static class StringReader extends LiteralNode {
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

    public static class IntReader extends LiteralNode {
        private Reader reader;

        public IntReader(Reader reader) {
            this.reader = reader;
        }

        public InterpretValue interpretValue() {
            try {
                StringBuilder ret = new StringBuilder();
                int c;
                while((c = reader.read()) != -1 && c != '\n') {
                    ret.append((char) c);
                }
                return new InterpretNumber<>(Integer.valueOf(ret.toString()));
            } catch (IOException e) {
                throw new RuntimeException(e);
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

    public static class IntWriter extends LiteralNode {
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
                    writer.flush();
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

    public static class CharWriter extends LiteralNode {
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
                    writer.flush();
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

    public static class StringWriter extends LiteralNode {
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
                    writer.flush();
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