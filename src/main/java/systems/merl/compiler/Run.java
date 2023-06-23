package systems.merl.compiler;

import systems.merl.compiler.error.InternalErrorRenderer;

public class Run {
    public static void main(String[] args) {
        try {
            Integer.parseInt("a");
        } catch (Exception e) {
            System.err.println(InternalErrorRenderer.render(e));
        }
    }
}
