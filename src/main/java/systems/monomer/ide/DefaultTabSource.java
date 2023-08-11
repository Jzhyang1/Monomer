package systems.monomer.ide;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DefaultTabSource implements TabSource {
    private boolean editable = true;
    private String name;
    private String contents;

    @Override
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String desc() {
        return "virtual";
    }
}