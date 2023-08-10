package systems.monomer;

import lombok.*;
import systems.monomer.tokenizer.SourceString;
import systems.monomer.tokenizer.Token;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class Editor extends JFrame {

    private static Editor editor;
    private static final Highlighter.HighlightPainter RED = new DefaultHighlighter.DefaultHighlightPainter(Color.decode("#e06c75"));
    private static final Highlighter.HighlightPainter GREEN = new DefaultHighlighter.DefaultHighlightPainter(Color.decode("#98c379"));
    private static final Highlighter.HighlightPainter YELLOW = new DefaultHighlighter.DefaultHighlightPainter(Color.decode("#e5c07b"));
    private static final Highlighter.HighlightPainter BLUE = new DefaultHighlighter.DefaultHighlightPainter(Color.decode("#61afef"));
    private static final Highlighter.HighlightPainter PURPLE = new DefaultHighlighter.DefaultHighlightPainter(Color.decode("#c678dd"));
    private static final Highlighter.HighlightPainter ORANGE = new DefaultHighlighter.DefaultHighlightPainter(Color.decode("#d19a66"));
    private static final Highlighter.HighlightPainter GRAY = new DefaultHighlighter.DefaultHighlightPainter(Color.decode("#abb2bf"));
    private static final Highlighter.HighlightPainter CYAN = new DefaultHighlighter.DefaultHighlightPainter(Color.decode("#56b6c2"));
    private static Highlighter.HighlightPainter getPainterFor(Token.Usage usage) {
        if (usage == null) return GRAY;
        switch (usage) {
            case IDENTIFIER -> {
                return ORANGE;
            }
            case OPERATOR -> {
                return PURPLE;
            }
            case STRING, STRING_BUILDER -> {
                return GREEN;
            }
            case CHARACTER, CHARACTER_FROM_INT -> {
                return YELLOW;
            }
            case INTEGER, FLOAT -> {
                return RED;
            }
            case GROUP -> {
                return BLUE;
            }
        }
        return GRAY;
    }

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
                    color();
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

            InputMap inputMap = contents.getInputMap(JComponent.WHEN_FOCUSED);
            ActionMap actionMap = contents.getActionMap();
            KeyStroke enterStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
            inputMap.put(enterStroke, enterStroke.toString());
            actionMap.put(enterStroke.toString(), new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    int pos = contents.getCaretPosition();
                    int lineStart = 0;
                    try {
                        lineStart = contents.getLineStartOffset(contents.getLineOfOffset(pos));
                    } catch (BadLocationException ex) {
                        ex.printStackTrace();
                    }
                    String line = contents.getText().substring(lineStart, pos);
                    int tabs = 0;
                    while (tabs < line.length() && line.charAt(tabs) == '\t') tabs++;
                    contents.insert("\n", pos);
                    contents.insert("\t".repeat(tabs), pos + 1);
                }
            });

            enterStroke = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
            inputMap.put(enterStroke, enterStroke.toString());
            actionMap.put(enterStroke.toString(), new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    if (contents.getSelectedText() == null) {
                        int pos = contents.getCaretPosition();
                        contents.insert("\t", pos);
                    } else {
                        Action.getAction("Indent").action.run();
                    }
                }
            });

            // if down and it's the last line, go to the end of the line
            // if up and it's the first line, go to the start of the line

            this.contents.getDocument().addUndoableEditListener((event) -> {
                undoManager.addEdit(event.getEdit());
            });

            this.setLayout(new BorderLayout());
            this.add(new JScrollPane(contents), BorderLayout.CENTER);

            Box box = Box.createHorizontalBox();
            JLabel location = new JLabel("    1:1 / length: 0");

            contents.addCaretListener((event) -> {
                int pos = contents.getCaretPosition();
                int row = 0, col = 0;
                try {
                    row = contents.getLineOfOffset(pos);
                    col = pos - contents.getLineStartOffset(row);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
                location.setText("    " + (row + 1) + ":" + (col + 1) + " / length: " + contents.getText().length());
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

        private void color() {
            Highlighter highlighter = contents.getHighlighter();
            List<Token> tokens = new SourceString(contents.getText()).parse().markupBlock();
            highlighter.removeAllHighlights();
            for (Token token : tokens) {
                try {
                    highlighter.addHighlight(token.getStart().getPosition(), token.getStop().getPosition(), Editor.getPainterFor(token.getUsage()));
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            }
        }

        public void save() {
            source.setContents(contents.getText());
            if (source instanceof NewTabSource && ((NewTabSource) source).transform() != null) {
                source = ((NewTabSource) source).transform();
            }
            this.editedSinceLastSave = false;
        }

    }

    private final List<Tab> tabs = new ArrayList<>();
    private final JTabbedPane tabbedPane = new JTabbedPane();
    private final JPanel display = new JPanel();
    private final JMenuBar menuBar = new JMenuBar();

    public Editor() {
        editor = this;
        this.setLayout(new BorderLayout());
        this.setJMenuBar(menuBar);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 1000);
        this.setVisible(true);
        this.setResizable(true);
        this.setTitle("Monomer Editor");
        createDisplayPanel();
        this.add(display);
        populateActions();
        populateMenu();
    }

    private void createDisplayPanel() {
        JLabel comp1 = new JLabel("<html>Create a file with <font color='#b5ddff'>Ctrl + N</font></html>");
        comp1.setFont(new Font("Consolas", Font.PLAIN, 20));
        comp1.setHorizontalAlignment(SwingConstants.CENTER);
        comp1.setVerticalAlignment(SwingConstants.CENTER);
        JLabel comp3 = new JLabel("<html>Open a file with <font color='#b5ddff'>Ctrl + O</font></html>");
        comp3.setFont(new Font("Consolas", Font.PLAIN, 20));
        comp3.setHorizontalAlignment(SwingConstants.CENTER);
        comp3.setVerticalAlignment(SwingConstants.CENTER);
        JLabel comp2 = new JLabel("<html>Search for actions with <font color='#b5ddff'>Ctrl + Shift + A</font></html>");
        comp2.setFont(new Font("Consolas", Font.PLAIN, 20));
        comp2.setHorizontalAlignment(SwingConstants.CENTER);
        comp2.setVerticalAlignment(SwingConstants.CENTER);


        display.setLayout(new BorderLayout());
        Box panel = Box.createVerticalBox();
        panel.add(Box.createVerticalGlue());
        panel.add(comp1);
        panel.add(Box.createVerticalStrut(20));
        panel.add(comp3);
        panel.add(Box.createVerticalStrut(20));
        panel.add(comp2);
        panel.add(Box.createVerticalGlue());
        display.add(panel, BorderLayout.CENTER);
    }

    public void addTab(Tab tab) {
        if (!hasAnyTabs()) {
            this.remove(display);
            this.add(tabbedPane, BorderLayout.CENTER);
        }
        tabs.add(tab);
        tabbedPane.addTab(tab.getName(), null, tab, tab.source.getToolTipText());
    }

    private boolean hasAnyTabs() {
        return !tabs.isEmpty();
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
                new Action("Close", () -> {
                    Tab tab = getSelectedTab();
                    closeTab(tab);
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
                new Action("Replace", () -> {
                    Tab tab = getSelectedTab();
                    JDialog dialog = new JDialog(this, "Replace", true);
                    JPanel panel = new JPanel();
                    dialog.setContentPane(panel);
                    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                    JPanel findPanel = new JPanel();
                    panel.add(findPanel);
                    findPanel.setLayout(new BoxLayout(findPanel, BoxLayout.X_AXIS));
                    findPanel.add(new JLabel("  Find what:  "));
                    JTextField findField = new JTextField();
                    findPanel.add(findField);
                    JPanel replacePanel = new JPanel();
                    panel.add(replacePanel);
                    panel.add(Box.createVerticalStrut(10));
                    replacePanel.setLayout(new BoxLayout(replacePanel, BoxLayout.X_AXIS));
                    replacePanel.add(new JLabel("  Replace with:  "));
                    JTextField replaceField = new JTextField();
                    replacePanel.add(replaceField);
                    JPanel buttonPanel = new JPanel();
                    panel.add(Box.createVerticalStrut(10));
                    panel.add(buttonPanel);
                    JButton replaceButton = new JButton("Replace");
                    replaceButton.addActionListener(e -> {
                        String findText = findField.getText();
                        String replaceText = replaceField.getText();
                        String newText = tab.contents.getText().replace(
                                findText,
                                replaceText);
                        tab.contents.setText(newText);
                    });
                    buttonPanel.add(replaceButton);
                    JButton replaceAllButton = new JButton("Replace All");
                    replaceAllButton.addActionListener(e -> {
                        String findText = findField.getText();
                        String replaceText = replaceField.getText();
                        String newText = tab.contents.getText().replaceAll(
                                findText,
                                replaceText);
                        tab.contents.setText(newText);
                    });
                    buttonPanel.add(replaceAllButton);
                    dialog.pack();
                    dialog.setVisible(true);
                }),
                new Action("Run File", () -> {
                    Tab tab = getSelectedTab();
                    String contents = tab.contents.getText();
                    Run.interpret(contents);
                }),
                new Action("Find Action", () -> {
                    JPanel panel = new JPanel();
                    JDialog dialog = new JDialog(this, "Find Action", true);
                    dialog.setContentPane(panel);
                    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                    JTextField field = new JTextField();
                    panel.add(field); // show a list of results as field is updated
                    JPanel buttonPanel = new JPanel();
                    JScrollPane scrollPane = new JScrollPane(buttonPanel);
                    panel.add(scrollPane);
                    Set<Map.Entry<String, Action>> entries = Action.actions.entrySet().stream()
                            .filter(entry -> !entry.getKey().equals("Find Action"))
                            .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey().toLowerCase(Locale.ROOT), entry.getValue()))
                            .collect(Collectors.toSet());
                    buttonPanel.setLayout(new GridLayout(entries.size(), 1));
                    Consumer<String> onKeyTyped = (contents) -> {
                        List<Action> actions = new ArrayList<>(entries.stream()
                                .filter(entry -> entry.getKey().contains(contents))
                                .map(Map.Entry::getValue)
                                .toList());
                        buttonPanel.removeAll();
                        actions.sort(Comparator.comparing(action -> action.name));
                        GridBagConstraints gbc = new GridBagConstraints();
                        gbc.gridx = 0;
                        gbc.gridy = 0;
                        gbc.fill = GridBagConstraints.HORIZONTAL;
                        gbc.ipadx = 20;
                        gbc.ipady = 20;
                        for (Action action : actions) {
                            JButton button = new JButton(action.name);
                            button.setHorizontalTextPosition(SwingConstants.CENTER);
                            button.setVerticalTextPosition(SwingConstants.CENTER);
                            button.setToolTipText("Run action \"" + action.name + "\"");

                            button.addActionListener(e1 -> {
                                dialog.dispose();
                                action.action.run();
                            });
                            button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                            buttonPanel.add(button, gbc);
                            gbc.gridy++;
                        }
                        buttonPanel.revalidate();
                        buttonPanel.repaint();
                    };
                    field.addKeyListener(new KeyListener() {
                        @Override
                        public void keyTyped(KeyEvent e) {
                            String s;
                            if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
                                s = field.getText();
                            } else {
                                s = field.getText() + e.getKeyChar();
                            }
                            String contents = s.toLowerCase(Locale.ROOT);
                            onKeyTyped.accept(contents);
                        }

                        @Override
                        public void keyPressed(KeyEvent e) {
                        }

                        @Override
                        public void keyReleased(KeyEvent e) {
                        }
                    });
                    onKeyTyped.accept("");
                    dialog.setSize(400, 400);
                    dialog.setLocationRelativeTo(this);
                    dialog.setVisible(true);
                }),
                new Action("Indent", () -> {

                    // get the selected lines
                    Tab tab = getSelectedTab();
                    String str;

                    boolean hasSelection = tab.contents.getSelectedText() != null;
                    if (!hasSelection) {
                        str = tab.contents.getText();
                        String[] lines = str.split("\n");
                        lines = Arrays.stream(lines)
                                .map(line -> "\t" + line)
                                .toArray(String[]::new);
                        String newStr = String.join("\n", lines);
                        tab.contents.setText(newStr);
                    } else {
                        int start = tab.contents.getSelectionStart();
                        int lineStart = start;
                        while (lineStart > 0 && tab.contents.getText().charAt(lineStart - 1) != '\n') {
                            lineStart--;
                        }
                        str = tab.contents.getText().substring(lineStart, tab.contents.getSelectionEnd());
                        String[] lines = str.split("\n");
                        lines = Arrays.stream(lines)
                                .map(line -> "\t" + line)
                                .toArray(String[]::new);
                        String newStr = String.join("\n", lines);
                        tab.contents.replaceRange(newStr, lineStart, tab.contents.getSelectionEnd());
                    }
                }),
                new Action("Dedent", () -> {

                    // get the selected lines
                    Tab tab = getSelectedTab();
                    String str;

                    boolean hasSelection = tab.contents.getSelectedText() != null;
                    if (!hasSelection) {
                        str = tab.contents.getText();
                        String[] lines = str.split("\n");
                        for (int i = 0; i < lines.length; i++) {
                            String line = lines[i];
                            if (line.startsWith("\t")) {
                                lines[i] = line.substring(1);
                            }
                        }
                        String newStr = String.join("\n", lines);
                        tab.contents.setText(newStr);
                    } else {
                        int start = tab.contents.getSelectionStart();
                        int lineStart = start;
                        while (lineStart > 0 && tab.contents.getText().charAt(lineStart - 1) != '\n') {
                            lineStart--;
                        }
                        str = tab.contents.getText().substring(lineStart, tab.contents.getSelectionEnd());
                        String[] lines = str.split("\n");
                        for (int i = 0; i < lines.length; i++) {
                            String line = lines[i];
                            if (line.startsWith("\t")) {
                                lines[i] = line.substring(1);
                            }
                        }
                        String newStr = String.join("\n", lines);
                        tab.contents.replaceRange(newStr, lineStart, tab.contents.getSelectionEnd());
                    }
                }),
                new Action("Comment", () -> {

                    // get the selected lines
                    Tab tab = getSelectedTab();
                    if (tab.contents.getSelectedText() == null) {
                        // comment the current line
                        int caretPosition = tab.contents.getCaretPosition();
                        int lineStart = caretPosition;
                        while (lineStart > 0 && tab.contents.getText().charAt(lineStart - 1) != '\n') {
                            lineStart--;
                        }
                        String str = tab.contents.getText().substring(lineStart, caretPosition);
                        if (str.contains("\\\\"))
                            tab.contents.replaceRange(str.replace("\\\\", ""), lineStart, caretPosition);
                        else
                            tab.contents.replaceRange("\\\\" + str, lineStart, caretPosition);
                    } else {
                        int start = tab.contents.getSelectionStart();
                        int lineStart = start;
                        while (lineStart > 0 && tab.contents.getText().charAt(lineStart - 1) != '\n') {
                            lineStart--;
                        }
                        String str = tab.contents.getText().substring(lineStart, tab.contents.getSelectionEnd());
                        String[] lines = str.split("\n");
                        for (int i = 0; i < lines.length; i++) {
                            String line = lines[i];
                            if (line.contains("\\\\ ")) {
                                lines[i] = line.replace("\\\\ ", "");
                            } else {
                                lines[i] = "\\\\ " + line;
                            }
                        }
                        String newStr = String.join("\n", lines);
                        tab.contents.replaceRange(newStr, lineStart, tab.contents.getSelectionEnd());
                    }
                })
        );
    }

    private void closeTab(Tab tab) {
        tabbedPane.remove(tab);
        tabs.remove(tab);
        if (!hasAnyTabs()) {
            remove(tabbedPane);
            add(display, BorderLayout.CENTER);
            repaint();
        }
    }

    private void populateMenu() {
        JMenu fileMenu = new JMenu("File");

        JMenuItem newFile = new JMenuItem("New");
        newFile.addActionListener(Action.getAction("New").asActionListener());
        newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        fileMenu.add(newFile);

        JMenuItem open = new JMenuItem("Open");
        open.addActionListener(Action.getAction("Open").asActionListener());
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        fileMenu.add(open);

        JMenuItem save = new JMenuItem("Save");
        save.addActionListener(Action.getAction("Save").asActionListener());
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        fileMenu.add(save);

        JMenuItem saveAs = new JMenuItem("Save As");
        saveAs.addActionListener(Action.getAction("Save As").asActionListener());
        saveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.ALT_DOWN_MASK));
        fileMenu.add(saveAs);

        JMenuItem close = new JMenuItem("Close");
        close.addActionListener(Action.getAction("Close").asActionListener());
        close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK));
        fileMenu.add(close);

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

        JMenuItem replace = new JMenuItem("Replace");
        replace.addActionListener(Action.getAction("Replace").asActionListener());
        replace.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));
        editMenu.add(replace);

        JMenu code = new JMenu("Code");
        JMenuItem indent = new JMenuItem("Indent");
        indent.addActionListener(Action.getAction("Indent").asActionListener());
        // Ctrl ]
        indent.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_CLOSE_BRACKET, InputEvent.CTRL_DOWN_MASK));
        code.add(indent);

        JMenuItem dedent = new JMenuItem("Dedent");
        dedent.addActionListener(Action.getAction("Dedent").asActionListener());
        // Ctrl [
        dedent.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_OPEN_BRACKET, InputEvent.CTRL_DOWN_MASK));
        code.add(dedent);

        JMenuItem comment = new JMenuItem("Comment");
        comment.addActionListener(Action.getAction("Comment").asActionListener());
        comment.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SLASH, InputEvent.CTRL_DOWN_MASK));
        code.add(comment);

        JMenu run = new JMenu("Run");
        JMenuItem runFile = new JMenuItem("Run File");
        runFile.addActionListener(Action.getAction("Run File").asActionListener());
        // shift f10
        runFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F10, InputEvent.SHIFT_DOWN_MASK));
        run.add(runFile);

        JMenu help = new JMenu("Help");
        JMenuItem findAction = new JMenuItem("Find Action");
        findAction.addActionListener(Action.getAction("Find Action").asActionListener());
        findAction.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        help.add(findAction);

        JMenuItem about = new JMenuItem("About & Usage");
        about.addActionListener((e) -> {
            JFrame frame = new JFrame("About & Usage");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(400, 400);
            frame.setLocationRelativeTo(this);
            frame.setVisible(true);
            frame.setLayout(new BorderLayout());
            JTextArea textArea = new JTextArea();
            textArea.setEditable(false);
            textArea.setText("""
                    This is a simple text editor for editing Monomer files, and is bundled with the 
                    Monomer compiler.
                                       
                    To visit the Monomer website, go to https://monomer.dev
                    The source code for this project can be found at:
                                       
                    https://github.com/Jzhyang1/Monomer
                                       
                    Using this editor is simple:
                     - Create a new file by clicking File -> New (or Ctrl+N)
                        
                        Once a file has been created, you can save it by clicking File -> Save (or Ctrl+S)
                        or save it as a new file by clicking File -> Save As (or Ctrl+Alt+S)
                     - Open a file by clicking File -> Open (or Ctrl+O)
                     
                     - Close a file by clicking File -> Close (or Ctrl+W)
                     - Exit the program by clicking File -> Exit
                      
                    Standard text editing shortcuts are also available:
                     
                     - Ctrl+Z: Undo
                     - Ctrl+Y: Redo
                     - Ctrl+X: Cut
                     - Ctrl+C: Copy
                     - Ctrl+V: Paste
                     - Ctrl+A: Select All
                     - Ctrl+F: Find
                     - Ctrl+R: Replace
                                       
                    Syntax highlighting is available for Monomer files (*.mm).
                                       
                    To run a file, click Run -> Run File (or Shift+F10).
                    To look at all the actions as a whole, you can click Help -> Find Action (or Ctrl+Shift+A).
                                       
                    """);
            frame.add(textArea, BorderLayout.CENTER);
            frame.revalidate();
            frame.setSize(756, 618);
        });
        help.add(about);


        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(code);
        menuBar.add(run);
        menuBar.add(help);
        revalidate();
        repaint();
    }
}
