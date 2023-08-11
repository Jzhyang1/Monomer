package systems.monomer.ide;

import lombok.Getter;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

@Getter
public class Action {
    private final String name;
    private final Runnable action;

    Action(String name, Runnable action) {
        this.name = name;
        this.action = () -> {
            try {
                action.run();
            } catch (IndexOutOfBoundsException e) {
                JOptionPane.showMessageDialog(Editor.EDITOR_INSTANCE, "This action requires a tab to be selected.", "No tab selected", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

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

    public void run() {
        action.run();
    }
}