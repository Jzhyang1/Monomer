package systems.merl.monomer.tokenizer;

import java.util.Scanner;
import java.util.Deque;
import java.util.LinkedList;
import java.io.File;

public class Source {
    private int row;
    private Deque<SourceLine> buffer;
    private Scanner input;

    public Source(String source) {
        input = new Scanner(source);
        row = 1;
        buffer = new LinkedList<SourceLine>();
    }

    public Source(File source) {
        try {
            input = new Scanner(source);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        row = 1;
        buffer = new LinkedList<SourceLine>();
    }

    public SourceLine getLine() {
        if (buffer.isEmpty()) {
            String line = input.nextLine();
            SourceLine sourceLine = new SourceLine(line, row, this);
            row++;
            return sourceLine;
        } else {
            return buffer.remove();
        }
    }

    public void ungetLine(SourceLine sourceLine) {
        buffer.push(sourceLine);
        row--;
    }

    public boolean eof() {
        return !input.hasNextLine() && buffer.isEmpty();
    }

    public int getRow() {
        return row;
    }
}