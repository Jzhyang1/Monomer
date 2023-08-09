package systems.monomer;

import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Editor extends JFrame {

    private static final Logger logger = LoggerFactory.getLogger(Editor.class);
    private static Editor editor;

    public interface TabSource {

        boolean isEditable();

        String getName();

        void setName(String name);

        default String getToolTipText() {
            return getName();
        }

        String getContents();

        void setContents(String contents);
    }


    @RequiredArgsConstructor
    @Getter
    public static class FileTabSource implements TabSource {
        private final File file;
        private final boolean editable;
        private String contents;

        @Override
        public String getName() {
            return file.getName();
        }

        @Override
        public void setName(String name) {
            if (!isEditable()) return;
            file.renameTo(new File(file.getParent(), name));
        }

        @Override
        public String getToolTipText() {
            return file.getAbsolutePath();
        }

        @Override
        @SneakyThrows
        public String getContents() {
            if (contents != null) return contents;
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }
            return (contents = builder.toString());
        }

        @Override
        @SneakyThrows
        public void setContents(String contents) {
            if (!isEditable()) return;
            this.contents = contents;
            try (PrintStream stream = new PrintStream(file)) {
                stream.print(contents);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Getter
    @Setter
    public static class DefaultTabSource implements TabSource {
        private boolean editable = true;
        private String name;
        private String contents;

        @Override
        public void setName(String name) {
            this.name = name;
        }
    }

    public static class NewTabSource extends DefaultTabSource implements TabSource {
        public NewTabSource() {
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
    }

    static class Tab extends JPanel {
        private TabSource source;
        private final JTextArea contents;
        private final UndoManager undoManager = new UndoManager();
        private boolean editedSinceLastSave = false;

        public Tab(TabSource source) {
            this.source = source;
            boolean isEditable = source.isEditable();
            this.contents = new JTextArea(source.getContents());
            this.contents.setEditable(isEditable);

            this.contents.getDocument().addDocumentListener(new DocumentListener() {
                private void update() {
                    if (editedSinceLastSave) return;
                    editedSinceLastSave = true;
                    editor.refreshTab(Tab.this);
                }

                @Override
                public void insertUpdate(DocumentEvent e) {
                    update();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    update();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    update();
                }
            });

            this.contents.getDocument().addUndoableEditListener((event) -> {
                undoManager.addEdit(event.getEdit());
            });

            this.setLayout(new BorderLayout());
            this.add(new JScrollPane(contents), BorderLayout.CENTER);

            Box box = Box.createHorizontalBox();
            JLabel location = new JLabel("    1:1");

            contents.addCaretListener((event) -> {
                int pos = contents.getCaretPosition();
                int row = 0, col = 0;
                try {
                    row = contents.getLineOfOffset(pos);
                    col = pos - contents.getLineStartOffset(row);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
                location.setText("    " + (row + 1) + ":" + (col + 1));
            });
            contents.setTabSize(4);
            box.add(location);
            this.add(box, BorderLayout.SOUTH);
        }

        public String getName() {
            if (editedSinceLastSave) {
                return "*" + source.getName();
            }
            return source.getName();
        }

        public void save() {
            source.setContents(contents.getText());
            if (source instanceof NewTabSource) {
                source = ((NewTabSource) source).transform();
            }
            this.editedSinceLastSave = false;
            logger.info("Saved tab {}", source.getName());
        }
    }

    private final List<Tab> tabs = new ArrayList<>();
    private final JTabbedPane tabbedPane = new JTabbedPane();
    private final JMenuBar menuBar = new JMenuBar();

    public Editor() {
        editor = this;
        this.setJMenuBar(menuBar);
        this.add(tabbedPane);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 1000);
        this.setVisible(true);
        this.setResizable(true);
        this.setTitle("Monomer Editor");
        populateActions();
        populateMenu();
        sampleTab();
    }

    public void addTab(Tab tab) {
        tabs.add(tab);
        tabbedPane.addTab(tab.getName(), null, tab, tab.source.getToolTipText());
    }

    private void sampleTab() {
        DefaultTabSource source = new DefaultTabSource();
        source.setContents("Hello, world!");
        source.setName("Hello");
        addTab(new Tab(source));
    }

    public Tab getSelectedTab() {
        return tabs.get(tabbedPane.getSelectedIndex());
    }

    void refreshTab(Tab tab) {
        int index = tabs.indexOf(tab);
        tabbedPane.setTitleAt(index, tab.getName());
        tabbedPane.setToolTipTextAt(index, tab.source.getToolTipText());
    }

    @Data
    static class Action {
        private final String name;
        private final Runnable action;

        public ActionListener asActionListener() {
            return (e) -> action.run();
        }

        static Map<String, Action> actions = new HashMap<>();

        public static Action getAction(String name) {
            return actions.get(name);
        }

        public static void addAction(Action... action) {
            for (Action a : action) {
                actions.put(a.getName(), a);
            }
        }
    }

    void populateActions() {
        Action.addAction(
                new Action("New", () -> {
                    TabSource source = new NewTabSource();
                    addTab(new Tab(source));
                    tabbedPane.setSelectedIndex(tabs.size() - 1);
                }),
                new Action("Open", () -> {
                    JFileChooser chooser = new JFileChooser();
                    int result = chooser.showOpenDialog(this);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File file = chooser.getSelectedFile();
                        if (file.isDirectory()) {
                            JOptionPane.showMessageDialog(this,
                                    "Cannot open a directory.",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        addTab(new Tab(new FileTabSource(file, true)));
                        tabbedPane.setSelectedIndex(tabs.size() - 1);
                    }
                }),
                new Action("Save", () -> {
                    Tab tab = getSelectedTab();
                    tab.save();
                    refreshTab(tab);
                }),
                new Action("Save As", () -> {
                    Tab tab = getSelectedTab();
                    JFileChooser chooser = new JFileChooser();
                    int result = chooser.showSaveDialog(this);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File file = chooser.getSelectedFile();
                        if (file.isDirectory()) {
                            JOptionPane.showMessageDialog(this,
                                    "Cannot save to a directory.",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        tab.source = new FileTabSource(file, true);
                        tab.save();
                        refreshTab(tab);
                    }
                }),
                new Action("Exit", () -> {
                    for (Tab tab : tabs) {
                        tab.save();
                    }
                    this.dispose();
                    System.exit(0);
                }),
                new Action("Undo", () -> {
                    Tab tab = getSelectedTab();
                    if (tab.undoManager.canUndo()) {
                        tab.undoManager.undo();
                    }
                }),
                new Action("Redo", () -> {
                    Tab tab = getSelectedTab();
                    if (tab.undoManager.canRedo()) {
                        tab.undoManager.redo();
                    }
                }),
                new Action("Cut", () -> {
                    Tab tab = getSelectedTab();
                    tab.contents.cut();
                }),
                new Action("Copy", () -> {
                    Tab tab = getSelectedTab();
                    tab.contents.copy();
                }),
                new Action("Paste", () -> {
                    Tab tab = getSelectedTab();
                    tab.contents.paste();
                }),
                new Action("Select All", () -> {
                    Tab tab = getSelectedTab();
                    tab.contents.selectAll();
                }),
                new Action("Find", () -> {
                    Tab tab = getSelectedTab();
                    String text = JOptionPane.showInputDialog(this, "Find what:");
                    if (text == null) {
                        return;
                    }
                    String contents = tab.contents.getText();
                    int index = contents.indexOf(text);
                    if (index == -1) {
                        JOptionPane.showMessageDialog(this,
                                "Cannot find \"" + text + "\"",
                                "Could not find text",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    tab.contents.select(index, index + text.length());
                }),
                new Action("Find Next", () -> {
                    Tab tab = getSelectedTab();
                    String text = JOptionPane.showInputDialog(this, "Find what:");
                    if (text == null) {
                        return;
                    }
                    String contents = tab.contents.getText();
                    int index = contents.indexOf(text, tab.contents.getCaretPosition());
                    if (index == -1) {
                        JOptionPane.showMessageDialog(this,
                                "Cannot find \"" + text + "\"",
                                "Could not find text",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    tab.contents.select(index, index + text.length());
                }),
                new Action("Run File", () -> {
                    Tab tab = getSelectedTab();
                    String contents = tab.contents.getText();
                    Run.interpret(contents);
                })
        );
    }

    private void populateMenu() {
        JMenu fileMenu = new JMenu("File");

        JMenuItem newFile = new JMenuItem("New");
        newFile.addActionListener(Action.getAction("New").asActionListener());
        fileMenu.add(newFile);

        JMenuItem open = new JMenuItem("Open");
        open.addActionListener(Action.getAction("Open").asActionListener());
        fileMenu.add(open);

        JMenuItem save = new JMenuItem("Save");
        save.addActionListener(Action.getAction("Save").asActionListener());
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        fileMenu.add(save);

        JMenuItem saveAs = new JMenuItem("Save As");
        saveAs.addActionListener(Action.getAction("Save As").asActionListener());
        saveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.ALT_DOWN_MASK));
        fileMenu.add(saveAs);

        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(Action.getAction("Exit").asActionListener());
        fileMenu.add(exit);

        JMenu editMenu = new JMenu("Edit");
        JMenuItem undo = new JMenuItem("Undo");
        undo.addActionListener(Action.getAction("Undo").asActionListener());
        undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
        editMenu.add(undo);

        JMenuItem redo = new JMenuItem("Redo");
        redo.addActionListener(Action.getAction("Redo").asActionListener());
        redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK));
        editMenu.add(redo);

        JMenuItem cut = new JMenuItem("Cut");
        cut.addActionListener(Action.getAction("Cut").asActionListener());
        cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
        editMenu.add(cut);

        JMenuItem copy = new JMenuItem("Copy");
        copy.addActionListener(Action.getAction("Copy").asActionListener());
        editMenu.add(copy);

        JMenuItem paste = new JMenuItem("Paste");
        paste.addActionListener(Action.getAction("Paste").asActionListener());
        editMenu.add(paste);

        JMenuItem selectAll = new JMenuItem("Select All");
        selectAll.addActionListener(Action.getAction("Select All").asActionListener());
        selectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
        editMenu.add(selectAll);

        JMenuItem find = new JMenuItem("Find");
        find.addActionListener(Action.getAction("Find").asActionListener());
        find.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK));
        editMenu.add(find);

        JMenu run = new JMenu("Run");
        JMenuItem runFile = new JMenuItem("Run File");
        runFile.addActionListener(Action.getAction("Run File").asActionListener());
        // shift f10
        runFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F10, InputEvent.SHIFT_DOWN_MASK));
        run.add(runFile);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(run);

    }
}
