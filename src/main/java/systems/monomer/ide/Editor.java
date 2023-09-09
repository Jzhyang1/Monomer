package systems.monomer.ide;

import systems.monomer.Constants;
import systems.monomer.commandline.Interpret;
import systems.monomer.tokenizer.SourceString;
import systems.monomer.tokenizer.Token;
import systems.monomer.util.Pair;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Timer;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class Editor extends JFrame {
    public static final String TITLE = "Monomer Idle";
    public static String FONT;
    public static final String INDENT = Constants.TAB;
    static Editor EDITOR_INSTANCE;

    static {
        try {
            Font firaCode = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/java/systems/monomer/ide/FiraCode-Regular.ttf")).deriveFont(12f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(firaCode);
            FONT = "Fira Code";
        } catch (Exception e) {
            FONT = "Consolas";
        }
    }

    static final class Tab extends JPanel {
        //create a panel with line numbers and a content text area
        private TabSource source;
        private final JTextPane contents;
        private final JTextPane lineNumbers;
        private final UndoManager undoManager = new UndoManager();
        private boolean editedSinceLastSave = false;
        private boolean editedSinceLastColor = false;
        private final JTextPane console = new JTextPane();
        private final JTextField consoleInput = new JTextField();
        private int existingLineCount = 1;
        private final JLabel location = new JLabel();


        public Tab(TabSource source) {
            this.source = source;
            boolean isEditable = source.isEditable();

            this.lineNumbers = new JTextPane();
            this.lineNumbers.setFont(new Font(FONT, Font.PLAIN, 14));
            this.lineNumbers.setEditable(false);
            this.lineNumbers.setText("1");
            this.lineNumbers.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
            this.lineNumbers.setMargin(new Insets(0, 5, 0, 10));

            this.contents = new JTextPane();
            this.contents.setFont(new Font(FONT, Font.PLAIN, 14));
            this.contents.setText(source.getContents());
            this.contents.setEditable(isEditable);

            this.console.setFont(new Font(FONT, Font.PLAIN, 14));
            this.console.setEditable(false);

            this.consoleInput.setFont(new Font(FONT, Font.PLAIN, 14));
            this.consoleInput.setText("");

            this.contents.getDocument().addDocumentListener(new DocumentListener() {
                private void update() {
                    if (!editedSinceLastColor) {
                        editedSinceLastColor = true;
                    }
                    if (editedSinceLastSave) return;
                    editedSinceLastSave = true;
                    EDITOR_INSTANCE.refreshTab(Tab.this);
                }

                @Override
                public void insertUpdate(DocumentEvent e) {
                    update();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    updateLineCount();
                    update();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                } // ignore because coloring causes a
            });

            InputMap inputMap = contents.getInputMap(JComponent.WHEN_FOCUSED);
            ActionMap actionMap = contents.getActionMap();
            KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
            inputMap.put(keyStroke, keyStroke.toString());
            actionMap.put(keyStroke.toString(), new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    int pos = contents.getCaretPosition();
                    int lineStart, lineEnd;
                    try {
                        lineStart = Utilities.getRowStart(contents, pos);
                        lineEnd = Utilities.getRowEnd(contents, pos);
                    } catch (BadLocationException badLocationException) {
                        throw new RuntimeException(badLocationException);
                    }
                    String line = sanitizedText().substring(lineStart, lineEnd);
                    while (line.startsWith("\n")) {
                        line = line.substring(1);
                    }
                    int numSpaces = 0;
                    for (int i = 0; i < line.length(); i++) {
                        if (line.charAt(i) == ' ') {
                            numSpaces++;
                        } else {
                            break;
                        }
                    }
                    int indents = numSpaces / INDENT.length();
                    try {
                        contents.getDocument().insertString(pos, "\n", null);
                        contents.getDocument().insertString(pos + 1, INDENT.repeat(indents), null);
                        updateLineCount();
                    } catch (BadLocationException badLocationException) {
                        throw new RuntimeException(badLocationException);
                    }
                }
            });

            keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
            inputMap.put(keyStroke, keyStroke.toString());
            actionMap.put(keyStroke.toString(), new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    if (contents.getSelectedText() == null) {
                        int pos = contents.getCaretPosition();
                        try {
                            contents.getDocument().insertString(pos, INDENT, null);
                        } catch (BadLocationException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        Action.getAction("Indent").run();
                    }
                }
            });

            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK), "Copy");
            actionMap.put("Copy", new AbstractAction() { // override default action
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    Action.getAction("Copy").run();
                }
            });
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK), "Paste_");
            actionMap.put("Paste_", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    Action.getAction("Paste").run();
                }
            });
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK), "Cut");
            actionMap.put("Cut", new AbstractAction() { // override default action
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    Action.getAction("Cut").run();
                }
            });

            this.contents.getDocument().addUndoableEditListener((event) -> undoManager.addEdit(event.getEdit()));

            Constants.out = new OutputStream() {
                private final StringBuilder stringBuilder = new StringBuilder();

                @Override
                public void write(int b) {
                    stringBuilder.append((char) b);
                    if(b == '\n') {
                        String s = stringBuilder.toString();
                        stringBuilder.setLength(0);
                        SwingUtilities.invokeLater(() -> {
                            try {
                                Document document = console.getDocument();
                                document.insertString(document.getLength(), s, null);
                            } catch (BadLocationException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }
                }
            };

            Constants.err = new OutputStream() {
                private final StringBuilder stringBuilder = new StringBuilder();

                @Override
                public void write(int b) {
                    stringBuilder.append((char) b);
                    if(b == '\n') {
                        String s = stringBuilder.toString();
                        stringBuilder.setLength(0);
                        SwingUtilities.invokeLater(() -> {
                            try {
                                Document document = console.getDocument();
                                document.insertString(document.getLength(), s,
                                        COLOR_ATTRIBUTE_SET_HASH_MAP.get(Colors.RED.getColor()));

                            } catch (BadLocationException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }
                }
            };

            PipedOutputStream printStream;
            try {
                PipedInputStream inputStream = new PipedInputStream();
                Constants.listener = inputStream;
                printStream = new PipedOutputStream(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            consoleInput.addActionListener((event) -> {
                String text = consoleInput.getText();
                consoleInput.setText("");
                try {
                    int start = console.getDocument().getLength();
                    byte[] bytes = (text + "\n").getBytes(StandardCharsets.UTF_8);

                    printStream.write(bytes);
                    printStream.flush();

                    SwingUtilities.invokeLater(() -> {
                        try {
                            console.getDocument().insertString(
                                    start,
                                    text+"\n",
                                    COLOR_ATTRIBUTE_SET_HASH_MAP.get(Colors.GREEN.getColor()));
                        } catch (BadLocationException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            JScrollPane scrollPane = new JScrollPane(contents);
            scrollPane.setRowHeaderView(lineNumbers);
            this.setLayout(new BorderLayout());
            this.add(scrollPane, BorderLayout.CENTER);

            contents.addCaretListener((event) -> updateLocationLabelText());
            updateLocationLabelText();
            contents.getDocument().putProperty(PlainDocument.tabSizeAttribute, Constants.TAB_SIZE);
            this.add(location, BorderLayout.SOUTH);
            Box box = new Box(BoxLayout.PAGE_AXIS);
            JScrollPane consoleScrollPane = new JScrollPane(console);
            consoleScrollPane.setPreferredSize(new Dimension(300, Integer.MAX_VALUE));
            box.add(consoleScrollPane);
            box.add(consoleInput);

            this.add(box, BorderLayout.EAST);
        }

        private void updateLocationLabelText() {
            if (contents.getSelectedText() == null) {
                int positionOffset = contents.getCaretPosition();
                int row = getLineOfOffset(positionOffset);
                int col = positionOffset - getLineStartOffset(row);
                location.setText(
                        INDENT + (row + 1) + ":" + (col + 1) +
                                " / " + positionOffset +
                                " / length: " + sanitizedText().length() +
                                " / " + this.source.desc());
            } else {
                int start = contents.getSelectionStart();
                int end = contents.getSelectionEnd();
                int startRow = getLineOfOffset(start);
                int startCol = start - getLineStartOffset(startRow);
                int endRow = getLineOfOffset(end);
                int endCol = end - getLineStartOffset(endRow);
                int selectionLength = end - start;
                String selectionLengthString;
                if (selectionLength == 1) {
                    selectionLengthString = " (1 char)";
                } else {
                    selectionLengthString = " (" + selectionLength + " chars)";
                }
                location.setText(
                        INDENT + (startRow + 1) + ":" + (startCol + 1) + " - " + (endRow + 1) + ":" + (endCol + 1)
                                + selectionLengthString +
                                " / " + start + " - " + end +
                                " / length: " + sanitizedText().length() +
                                " / " + this.source.desc());
            }

        }

        public String getName() {
            if (editedSinceLastSave) {
                return "*" + source.getName();
            }
            return source.getName();
        }

        private int lineCount() {
            Element root = contents.getDocument().getDefaultRootElement();
            return root.getElementCount();
        }

        private void updateLineCount() {
            int lineNumber = lineCount();
            if (lineNumber == this.existingLineCount) return; // no need to update
            try {
                lineNumbers.getDocument().remove(0, lineNumbers.getDocument().getLength());
            } catch (BadLocationException badLocationException) {
                throw new RuntimeException(badLocationException);
            }
            this.existingLineCount = lineNumber;
            int offset = 0;
            for (int currentLine = 1; currentLine <= lineNumber; currentLine++) {
                String insert = currentLine + "\n";
                try {
                    lineNumbers.getDocument().insertString(offset, insert, null);
                } catch (BadLocationException e) {
                    throw new RuntimeException(e);
                }
                offset += insert.length();
            }
        }

        private void color() {
            if (!editedSinceLastColor) return;
            editedSinceLastColor = false;
            String text = sanitizedText();
            List<Token> tokens;
            try {
                tokens = new SourceString(text).parse().markupBlock();
            } catch (Exception exception) {
                throw new RuntimeException("failed to color parse source, " + source.desc(), exception);
            }
            final List<Token> finalTokens = tokens;
            SwingUtilities.invokeLater(() -> {
                for (Token token : finalTokens) {
                    try {
                        syntaxHighlight(
                                token.getStart().getPosition(),
                                token.getStop().getPosition(),
                                Colors.colorFor(token.getUsage())
                        );
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
                updateLineCount();
            });
            contents.setCharacterAttributes(SimpleAttributeSet.EMPTY, true);
        }

        private static final Map<Color, AttributeSet> COLOR_ATTRIBUTE_SET_HASH_MAP = new HashMap<>();

        static {
            for (Colors color : Colors.values()) {
                StyleContext styleContext = StyleContext.getDefaultStyleContext();
                AttributeSet attributeSet = styleContext.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, color.getColor());
                attributeSet = styleContext.addAttribute(attributeSet, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);
                COLOR_ATTRIBUTE_SET_HASH_MAP.put(color.getColor(), attributeSet);
            }
        }

        private void syntaxHighlight(int start, int end, Color color) {
            contents.getStyledDocument().setCharacterAttributes(
                    start,
                    end - start,
                    COLOR_ATTRIBUTE_SET_HASH_MAP.get(color),
                    false
            );
        }


        public void save() {
            source.setContents(sanitizedText());
            if (source instanceof NewTabSource && ((NewTabSource) source).transform() != null) {
                source = ((NewTabSource) source).transform();
            }
            this.editedSinceLastSave = false;
        }

        public int getLineStartOffset(int line) {
            Element map = contents.getDocument().getDefaultRootElement();
            Element lineElem = map.getElement(line);
            return lineElem.getStartOffset();
        }

        public int getLineOfOffset(int offset) {
            Element map = contents.getDocument().getDefaultRootElement();
            return map.getElementIndex(offset);
        }

        public void replaceRange(String str, int start, int end) {
            try {
                contents.getDocument().remove(start, end - start);
                contents.getDocument().insertString(start, str, null);
            } catch (BadLocationException e) {
                throw new RuntimeException(e);
            }
        }

        public String sanitizedText() {
            return contents.getText().replace("\r", "");
        }

    }

    private final List<Tab> tabs = new ArrayList<>();
    private final JTabbedPane tabbedPane = new JTabbedPane();
    private final JPanel display = new JPanel();
    private final JMenuBar menuBar = new JMenuBar();
    private final TimerTask colorTimerTask;
    private final Timer colorTimer = new Timer();

    public Editor() {
        EDITOR_INSTANCE = this;
        initFrame();
        createDisplayPanel();
        this.add(display);
        populateActions();
        populateMenu();
        this.colorTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (hasNoTabs()) return;
                Tab selectedTab = getSelectedTab();
                selectedTab.color();
                selectedTab.repaint();
            }
        };
        colorTimer.schedule(colorTimerTask, 0, 1000);
    }

    private void initFrame() {
        this.setLayout(new BorderLayout());
        this.setJMenuBar(menuBar);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 1000);
        this.setVisible(true);
        this.setResizable(true);
        this.setTitle(TITLE);
    }

    private JLabel createDisplayPanelLabel(String option, String commandWindows, String commandMac) {
        String commandText = Constants.IS_MAC ? commandMac : commandWindows;
        JLabel comp = new JLabel("<html>" + option + " with <font color='#b5ddff'>" + commandText + "</font></html>");
        comp.setFont(new Font(FONT, Font.PLAIN, 20));
        comp.setHorizontalAlignment(SwingConstants.CENTER);
        comp.setVerticalAlignment(SwingConstants.CENTER);
        return comp;
    }

    private void createDisplayPanel() {
        List<JLabel> comps = List.of(
                createDisplayPanelLabel("Create file", "Ctrl + N", "Cmd + N"),
                createDisplayPanelLabel("Search for actions", "Ctrl + Shift + A", "Cmd + Shift + A"),
                createDisplayPanelLabel("Open file", "Ctrl + O", "Cmd + O"));
        display.setLayout(new BorderLayout());
        Box panel = Box.createVerticalBox();
        panel.add(Box.createVerticalGlue());
        for (int index = 0; index < comps.size(); index++) {
            panel.add(comps.get(index));
            if (index != comps.size() - 1) {
                panel.add(Box.createVerticalStrut(20));
            }
        }
        panel.add(Box.createVerticalGlue());
        display.add(panel, BorderLayout.CENTER);
    }

    public void addTab(Tab tab) {
        if (hasNoTabs()) {
            this.remove(display);
            this.add(tabbedPane, BorderLayout.CENTER);
        }
        tabs.add(tab);
        tabbedPane.addTab(tab.getName(), null, tab, tab.source.getToolTipText());
        tabbedPane.setSelectedIndex(tabs.size() - 1);
    }

    private boolean hasNoTabs() {
        return tabs.isEmpty();
    }

    public Tab getSelectedTab() {
        return tabs.get(tabbedPane.getSelectedIndex());
    }

    void refreshTab(Tab tab) {
        int index = tabs.indexOf(tab);
        tabbedPane.setTitleAt(index, tab.getName());
        tabbedPane.setToolTipTextAt(index, tab.source.getToolTipText());
    }


    void populateActions() {
        Action.addAction(
                new Action("New", () -> {
                    TabSource source = new NewTabSource(EDITOR_INSTANCE);
                    addTab(new Tab(source));
                }),
                new Action("New Virtual", () -> {
                    TabSource source = new DefaultTabSource();
                    source.setName("virtual");
                    addTab(new Tab(source));
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
                        Tab tab;
                        addTab(tab = new Tab(new FileTabSource(file, true)));
                        tab.color();
                        tab.repaint();
                    }
                }),
                new Action("Save", () -> {
                    Tab tab = getSelectedTab();
                    tab.save();
                    refreshTab(tab);
                }),
                new Action("Save As", () -> {
                    Tab tab = getSelectedTab();
                    if (tab.source instanceof DefaultTabSource && !(tab.source instanceof NewTabSource)) {
                        // pure virtual
                        String text = JOptionPane.showInputDialog(this, "Change the name of the virtual file to:");
                        if (text == null) return;
                        tab.source.setName(text);
                        return;
                    }
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
                    copySelectedFromTab();
                    Tab tab = getSelectedTab();
                    tab.contents.replaceSelection(null);
                }),
                new Action("Copy", () -> {
                    copySelectedFromTab();
                }),
                new Action("Paste", () -> {
                    Tab tab = getSelectedTab();
                    tab.contents.paste();
                }),
                new Action("Select All", () -> {
                    Tab tab = getSelectedTab();
                    tab.contents.select(0, tab.sanitizedText().length());
                }),
                new Action("Find", () -> {
                    Tab tab = getSelectedTab();
                    String text = JOptionPane.showInputDialog(
                            this,
                            "Find what:",
                            "Find",
                            JOptionPane.QUESTION_MESSAGE
                    );
                    if (text == null) return;
                    String contents = tab.sanitizedText();
                    int index = contents.indexOf(text);
                    if (index == -1) {
                        JOptionPane.showMessageDialog(this,
                                "We couldn't find the text \"" + text + "\" in the file",
                                "Could not find input",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    tab.contents.select(index, index + text.length());
                }),
                new Action("Find Next", () -> {
                    Tab tab = getSelectedTab();
                    String text = JOptionPane.showInputDialog(
                            this,
                            "Find what:",
                            "Find Next",
                            JOptionPane.QUESTION_MESSAGE
                    );
                    if (text == null) return;
                    String contents = tab.sanitizedText();
                    int index = contents.indexOf(text, tab.contents.getCaretPosition());
                    if (index == -1) {
                        JOptionPane.showMessageDialog(this,
                                "We couldn't find the text \"" + text + "\" in the rest of the file",
                                "Could not find input",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    tab.contents.select(index, index + text.length());
                }),
                new Action("Replace", this::askForReplace),
                new Action("Run File", () -> {
                    Tab tab = getSelectedTab();
                    String contents = tab.sanitizedText();
                    new Thread(() -> {
                        try {
                            Interpret.interpret(contents);
                        } catch (RuntimeException e) {try {
                            Constants.err.write(e.getMessage().getBytes());
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }}
                    }).start();
                }),
                new Action("Find Action", this::showActionListWindow),
                new Action("Indent", () -> {
                    Tab tab = getSelectedTab();
                    if (tab.contents.getSelectedText() == null) {
                        String[] lines = tab.sanitizedText()
                                .lines()
                                .map(line -> INDENT + line)
                                .toArray(String[]::new);
                        tab.contents.setText(String.join("\n", lines));
                    } else {
                        int lineStart, lineEnd;
                        try {
                            lineStart = Utilities.getRowStart(tab.contents, tab.contents.getSelectionStart());
                            lineEnd = Utilities.getRowEnd(tab.contents, tab.contents.getSelectionEnd());
                        } catch (BadLocationException badLocationException) {
                            throw new RuntimeException(badLocationException);
                        }
                        String[] lines = tab.sanitizedText()
                                .substring(lineStart, lineEnd)
                                .lines()
                                .map(line -> INDENT + line)
                                .toArray(String[]::new);
                        String replacement = String.join("\n", lines);
                        tab.replaceRange(replacement, lineStart, lineEnd);
                    }
                }),
                new Action("Dedent", () -> {
                    Tab tab = getSelectedTab();
                    if (tab.contents.getSelectedText() == null) {
                        String[] lines = tab.sanitizedText()
                                .lines()
                                .toArray(String[]::new);
                        for (int index = 0; index < lines.length; index++) {
                            if (lines[index].startsWith(INDENT)) {
                                lines[index] = lines[index].substring(INDENT.length());
                            }
                        }
                        tab.contents.setText(String.join("\n", lines));
                    } else {
                        int lineStart, lineEnd;
                        try {
                            lineStart = Utilities.getRowStart(tab.contents, tab.contents.getSelectionStart());
                            lineEnd = Utilities.getRowEnd(tab.contents, tab.contents.getSelectionEnd());
                        } catch (BadLocationException badLocationException) {
                            throw new RuntimeException(badLocationException);
                        }
                        String[] lines = tab.sanitizedText()
                                .substring(lineStart, lineEnd)
                                .lines()
                                .toArray(String[]::new);
                        for (int index = 0; index < lines.length; index++) {
                            if (lines[index].startsWith(INDENT)) {
                                lines[index] = lines[index].substring(INDENT.length());
                            }
                        }
                        String replacement = String.join("\n", lines);
                        tab.replaceRange(replacement, lineStart, lineEnd);
                    }
                }),
                new Action("Comment", () -> {
                    Tab tab = getSelectedTab();
                    if (tab.contents.getSelectedText() == null) {
                        Pair<Integer, Integer> lineStartEnd = selectCurrentLine();
                        String line = tab.sanitizedText().substring(lineStartEnd.getFirst(), lineStartEnd.getSecond());
                        if (line.contains("\\\\")) {
                            tab.replaceRange(line.replace("\\\\", ""), lineStartEnd.getFirst(), lineStartEnd.getSecond());
                        } else {
                            tab.replaceRange("\\\\ " + line, lineStartEnd.getFirst(), lineStartEnd.getSecond());
                        }
                    } else {
                        int lineStart, lineEnd;
                        try {
                            lineStart = Utilities.getRowStart(tab.contents, tab.contents.getSelectionStart());
                            lineEnd = Utilities.getRowEnd(tab.contents, tab.contents.getSelectionEnd());
                        } catch (BadLocationException badLocationException) {
                            throw new RuntimeException(badLocationException);
                        }
                        String[] lines = tab.contents
                                .getText()
                                .substring(lineStart, lineEnd)
                                .lines()
                                .toArray(String[]::new);
                        for (int index = 0; index < lines.length; index++) {
                            if (lines[index].contains("\\\\")) {
                                lines[index] = lines[index].replace("\\\\", "");
                            } else {
                                lines[index] = "\\\\ " + lines[index];
                            }
                        }
                        String replacement = String.join("\n", lines);
                        tab.replaceRange(replacement, lineStart, lineEnd);
                    }
                })
        );
    }

    private void copySelectedFromTab() {
        Tab tab = getSelectedTab();
        if (tab.contents.getSelectedText() == null) {
            Pair<Integer, Integer> lineStartEnd = selectCurrentLine();
            tab.contents.select(lineStartEnd.getFirst(), lineStartEnd.getSecond());
        }
        String text = tab.contents.getSelectedText();
        StringSelection selection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }

    private Pair<Integer, Integer> selectCurrentLine() {
        Tab tab = getSelectedTab();
        int caret = tab.contents.getCaretPosition();
        int lineStart, lineEnd;
        try {
            lineStart = Utilities.getRowStart(tab.contents, caret);
            lineEnd = Utilities.getRowEnd(tab.contents, caret);
        } catch (BadLocationException badLocationException) {
            throw new RuntimeException(badLocationException);
        }
        return new Pair<>(lineStart, lineEnd);
    }

    private void askForReplace() {
        Tab tab = getSelectedTab();
        JDialog dialog = new JDialog(this, "Find and Replace", true);
        dialog.setLocationRelativeTo(this);
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
            String newText = tab.sanitizedText().replace(
                    findText,
                    replaceText);
            tab.contents.setText(newText);
        });
        buttonPanel.add(replaceButton);
        JButton replaceAllButton = new JButton("Replace All");
        replaceAllButton.addActionListener(e -> {
            String findText = findField.getText();
            String replaceText = replaceField.getText();
            String newText = tab.sanitizedText().replaceAll(
                    findText,
                    replaceText);
            tab.contents.setText(newText);
        });
        buttonPanel.add(replaceAllButton);
        dialog.pack();
        dialog.setVisible(true);
    }

    private void showActionListWindow() {
        JPanel panel = new JPanel();
        JDialog dialog = new JDialog(this, "Find Action", true);
        dialog.setContentPane(panel);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JTextField field = new JTextField();
        panel.add(field);
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
            actions.sort(Comparator.comparing(Action::getName));
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.ipadx = 20;
            gridBagConstraints.ipady = 20;
            for (Action action : actions) {
                JButton button = new JButton(action.getName());
                button.setHorizontalTextPosition(SwingConstants.CENTER);
                button.setVerticalTextPosition(SwingConstants.CENTER);
                button.setToolTipText("Run action \"" + action.getName() + "\"");
                button.addActionListener(actionEvent -> {
                    dialog.dispose();
                    action.run();
                });
                button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                buttonPanel.add(button, gridBagConstraints);
                gridBagConstraints.gridy++;
            }
            buttonPanel.revalidate();
            buttonPanel.repaint();
        };
        field.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                String actualTextInField;
                if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
                    actualTextInField = field.getText();
                } else {
                    actualTextInField = field.getText() + e.getKeyChar();
                } // field.getText() doesn't include the character we just typed right now
                String contents = actualTextInField.toLowerCase(Locale.ROOT);
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

    }

    private void closeTab(Tab tab) {
        tabbedPane.remove(tab);
        tabs.remove(tab);
        if (hasNoTabs()) {
            remove(tabbedPane);
            add(display, BorderLayout.CENTER);
            repaint();
        }
    }

    private JMenuItem createMenuItem(String name, int keyEvent, int inputEvent, int inputEventMac) {
        JMenuItem item = new JMenuItem(name);
        item.addActionListener(Action.getAction(name).asActionListener());
        item.setAccelerator(KeyStroke.getKeyStroke(keyEvent, Constants.IS_MAC ? inputEventMac : inputEvent));
        return item;
    }

    private void populateMenu() {
        JMenu fileMenu = new JMenu("File");

        fileMenu.add(createMenuItem("New", KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK, InputEvent.META_DOWN_MASK));
        fileMenu.add(createMenuItem("New Virtual", KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK, InputEvent.META_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        fileMenu.add(createMenuItem("Open", KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK, InputEvent.META_DOWN_MASK));
        fileMenu.add(createMenuItem("Save", KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK, InputEvent.META_DOWN_MASK));
        fileMenu.add(createMenuItem("Save As", KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK, InputEvent.META_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        fileMenu.add(createMenuItem("Close", KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK, InputEvent.META_DOWN_MASK));
        fileMenu.add(createMenuItem("Exit", KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK, InputEvent.META_DOWN_MASK));


        JMenu editMenu = new JMenu("Edit");
        editMenu.add(createMenuItem("Undo", KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK, InputEvent.META_DOWN_MASK));
        editMenu.add(createMenuItem("Redo", KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK, InputEvent.META_DOWN_MASK));
        editMenu.add(createMenuItem("Cut", KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK, InputEvent.META_DOWN_MASK));
        editMenu.add(createMenuItem("Copy", KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK, InputEvent.META_DOWN_MASK));
        editMenu.add(createMenuItem("Paste", KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK, InputEvent.META_DOWN_MASK));
        editMenu.add(createMenuItem("Select All", KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK, InputEvent.META_DOWN_MASK));
        editMenu.add(createMenuItem("Find", KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK, InputEvent.META_DOWN_MASK));
        editMenu.add(createMenuItem("Replace", KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK, InputEvent.META_DOWN_MASK));

        JMenu code = new JMenu("Code");
        code.add(createMenuItem("Indent", KeyEvent.VK_CLOSE_BRACKET, InputEvent.CTRL_DOWN_MASK, InputEvent.META_DOWN_MASK));
        code.add(createMenuItem("Dedent", KeyEvent.VK_OPEN_BRACKET, InputEvent.CTRL_DOWN_MASK, InputEvent.META_DOWN_MASK));
        code.add(createMenuItem("Comment", KeyEvent.VK_SLASH, InputEvent.CTRL_DOWN_MASK, InputEvent.META_DOWN_MASK));

        JMenu run = new JMenu("Run");
        run.add(createMenuItem("Run File", KeyEvent.VK_F10, InputEvent.SHIFT_DOWN_MASK, InputEvent.SHIFT_DOWN_MASK));
//        run.add(createMenuItem("Run Project", KeyEvent.VK_F10, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));

        JMenu help = new JMenu("Help");
        help.add(createMenuItem("Find Action", KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK, InputEvent.META_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));

        JMenuItem about = new JMenuItem("About & Usage");
        about.addActionListener((e) -> {
            JFrame frame = new JFrame("About & Usage");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
                                       
                    Monomer syntax highlighting is provided. 
                                       
                    To run a file, click Run -> Run File (or Shift+F10).
                    To run a specific action, you can click Help -> Find Action (or Ctrl+Shift+A).
                                       
                    """);
            frame.add(textArea, BorderLayout.CENTER);
            frame.revalidate();
            frame.setResizable(false);
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










