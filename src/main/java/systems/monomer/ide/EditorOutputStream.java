package systems.monomer.ide;

import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.io.OutputStream;

import static systems.monomer.errorhandling.ErrorBlock.programError;

public class EditorOutputStream extends OutputStream {
    private final StringBuilder stringBuilder = new StringBuilder();
    private final JTextPane console;
    private final @Nullable AttributeSet attributeSet;

    EditorOutputStream(JTextPane console, @Nullable AttributeSet attributeSet) {
        this.console = console;
        this.attributeSet = attributeSet;
    }

    @Override
    public void write(int b) {
        stringBuilder.append((char) b);
        if(b == '\n') {
            String s = stringBuilder.toString();
            stringBuilder.setLength(0);
            SwingUtilities.invokeLater(() -> {
                try {
                    Document document = console.getDocument();
                    document.insertString(document.getLength(), s, attributeSet);
                } catch (BadLocationException e) {
                    throw programError(e.getMessage());
                }
            });
        }
    }

    @Override
    public void flush(){
        String s = stringBuilder.toString();
        stringBuilder.setLength(0);
        SwingUtilities.invokeLater(() -> {
            try {
                Document document = console.getDocument();
                document.insertString(document.getLength(), s, attributeSet);
            } catch (BadLocationException e) {
                throw programError(e.getMessage());
            }
        });
    }
}
