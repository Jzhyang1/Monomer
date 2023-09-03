package systems.monomer;

public enum Constants {
    ;
    public static final int TAB_SIZE = 4;
    public static final String TAB = " ".repeat(TAB_SIZE);
    public static final boolean IS_MAC = System.getProperty("os.name").toLowerCase().contains("mac");
    public static final int RECURSIVE_LIMIT = 200;

    public static boolean isIdentifierChar(char c) {
        return ('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z') ||  //common english letters
                ('0' <= c && c <= '9') ||   //numbers
                c == '_' ||                 //underscore
                ('\u0391' <= c && c <= '\u03ff') ||  //greek characters
                ('\u0400' <= c && c <= '\u04ff');  //cryllic characters
    }
}
