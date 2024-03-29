package systems.monomer;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;

import java.io.InputStream;
import java.io.OutputStream;

@UtilityClass
public class Constants {
    public final int TAB_SIZE = 4;
    public final String TAB = " ".repeat(TAB_SIZE);

    public final String OS = System.getProperty("os.name").toLowerCase();
    public final boolean IS_MAC = OS.contains("mac");
    public final boolean IS_WINDOWS = OS.contains("win");
    public final boolean IS_UNIX = OS.contains("nix") || OS.contains("nux") || OS.contains("aix") || OS.contains("sunos") || IS_MAC;

    public final int RECURSIVE_LIMIT = 200;

    public boolean isIdentifierChar(char c) {
        return ('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z') ||  //common english letters
                ('0' <= c && c <= '9') ||   //numbers
                c == '_' ||                 //underscore
                ('\u0391' <= c && c <= '\u03ff') ||  //greek characters
                ('\u0400' <= c && c <= '\u04ff');  //cryllic characters
    }

    @Getter @Setter
    public OutputStream out = System.out;
    @Getter @Setter
    public OutputStream err = System.err;
    @Getter @Setter
    public InputStream listener = System.in;
}
