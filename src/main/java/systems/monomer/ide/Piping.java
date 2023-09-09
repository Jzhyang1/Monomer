package systems.monomer.ide;

import java.io.*;

public class Piping {

    private final PipedOutputStream fromConsoleOS = new PipedOutputStream();
    private final PipedInputStream consoleInput = new PipedInputStream(fromConsoleOS);
    private final OutputStream displayOutputStream; // for use in console code to write to display

    public PipedInputStream getConsoleInput() {
        return consoleInput;
    }

    public PipedOutputStream getConsoleOutputStream() { // for use in console code to write to interpreter
        return fromConsoleOS;
    }

    public Piping(OutputStream stream) throws IOException {
        displayOutputStream = stream;
    }

    public void registerSystem() {
        System.setIn(consoleInput);
        System.setOut(new PrintStream(displayOutputStream, true));
        System.setErr(System.out);
    }
}
