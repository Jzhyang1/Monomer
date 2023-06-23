package systems.merl.compiler.error;

import lombok.experimental.UtilityClass;

import java.util.function.Predicate;

@UtilityClass
public class InternalErrorRenderer {

    public String render(Throwable throwable) {
        return render(throwable, s -> s.contains("merl"));
    }

    public String render(Throwable throwable, Predicate<String> importanceFilter) {
        StringBuilder builder = new StringBuilder();
        builder.append("the stack trace is shown below (for thread \"");
        builder.append(Thread.currentThread().getName());
        builder.append("\"):\n");
        builder.append("[*] -- ");
        builder.append(throwable.getClass().getName());
        builder.append(": ");
        builder.append(throwable.getMessage() != null ? throwable.getMessage() : "no message");
        builder.append('\n');
        for (StackTraceElement element : throwable.getStackTrace()) {
            renderStackTraceElement(builder, element, importanceFilter);
        }
        return builder.toString();
    }

    private void renderStackTraceElement(StringBuilder builder, StackTraceElement element, Predicate<String> importanceFilter) {
        String module = element.getModuleName();
        String prefix = importanceFilter.test(element.getClassName()) ? "(+)" : " + ";
        String classAndMethod = (element.getClassName() + '.' + element.getMethodName()).replace("$", ".");
        String modulePart = module != null ? "[" + module + "]" : "";
        builder.append(prefix);
        builder.append(" -- ");
        builder.append(classAndMethod);
        builder.append('(');
        builder.append(element.getFileName());
        builder.append(':');
        builder.append(element.getLineNumber());
        builder.append(')');
        builder.append(' ');
        builder.append(modulePart);
        builder.append('\n');
    }

}
