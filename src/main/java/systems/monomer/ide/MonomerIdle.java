package systems.monomer.ide;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.util.Enumeration;

public class MonomerIdle {
    public static void main(String[] args) {
        FlatDarkLaf.setup();
        setFont();
        Editor editor = new Editor();
    }

    private static void setFont() {
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, new FontUIResource(Editor.FONT, FontUIResource.PLAIN, 14));
            }
        }
    }
}
