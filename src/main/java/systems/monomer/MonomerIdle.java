package systems.monomer;

import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.util.Enumeration;

public class MonomerIdle {
    public static void main(String[] args) {
        FlatDarculaLaf.setup();
        setFont();
        Editor editor = new Editor();
    }

    public static void setFont() {
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, new FontUIResource("Consolas", FontUIResource.PLAIN, 14));
            }
        }
    }
}
