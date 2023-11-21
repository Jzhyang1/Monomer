package systems.monomer.ide;

import javax.swing.*;
import java.io.File;

public class NewTabSource extends DefaultTabSource {
    private final Editor editor;

    public NewTabSource(Editor editor) {
        this.editor = editor;
        setName("untitled");
        super.setContents("");
    }

    private FileTabSource fileTabSource;

    @Override
    public void setContents(String contents) {
        super.setContents(contents);
        String fileName = (String) JOptionPane.showInputDialog(editor,
                "Save the file as:",
                "Save New File As",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null, "untitled.mm");
        if (fileName == null) return;
        File f = new File(fileName);
        if (f.exists() && !f.isDirectory()) {
            int result = JOptionPane.showConfirmDialog(editor,
                    "File already exists. Overwrite?",
                    "Existing File",
                    JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.NO_OPTION) {
                return;
            }
            fileTabSource = new FileTabSource(f, true);
            fileTabSource.setContents(contents);
        } else if (f.isDirectory()) {
            JOptionPane.showMessageDialog(editor,
                    "Cannot save to a directory.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            fileTabSource = new FileTabSource(f, true);
            fileTabSource.setContents(contents);
        }
    }

    public FileTabSource transform() {
        return fileTabSource;
    }

    @Override
    public String desc() {
        return "new (virtual)";
    }
}