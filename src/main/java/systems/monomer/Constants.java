package systems.monomer;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NonNls;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class Constants {
    public final int TAB_SIZE = 4;
    public final String TAB = " ".repeat(TAB_SIZE);
    public final boolean IS_MAC = System.getProperty("os.name").toLowerCase().contains("mac");
    public final int RECURSIVE_LIMIT = 200;

    public boolean isIdentifierChar(char c) {
        return ('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z') ||  //common english letters
                ('0' <= c && c <= '9') ||   //numbers
                c == '_' ||                 //underscore
                ('\u0391' <= c && c <= '\u03ff') ||  //greek characters
                ('\u0400' <= c && c <= '\u04ff');  //cryllic characters
    }

    public static ConsoleWriter out;
    public static ConsoleWriter err;

    public interface ConsoleWriter {
        public void write(String s);

        default void writeln(@NonNls String s) {
            write(s + "\n");
        }
    }

    public static List<ConsoleListener> listeners = new ArrayList<>();

    public interface ConsoleListener {
        public void onInput(String input);
    }
}
