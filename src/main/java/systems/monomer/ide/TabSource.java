package systems.monomer.ide;

public interface TabSource {

    boolean isEditable();

    String getName();

    void setName(String name);

    default String getToolTipText() {
        return getName();
    }

    String getContents();

    void setContents(String contents);
    String desc();
}
