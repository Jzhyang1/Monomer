package systems.monomer;

public class Constants {
    public static final int TAB_SIZE = 4;   //TODO set these
    public static final String TAB = " ".repeat(TAB_SIZE);  //TODO use this
    public static final boolean IS_MAC = System.getProperty("os.name").toLowerCase().contains("mac");
}
