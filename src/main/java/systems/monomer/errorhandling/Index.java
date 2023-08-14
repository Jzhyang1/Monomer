package systems.monomer.errorhandling;


import lombok.Getter;

@Getter
public class Index {
    private int x, y;
    private int position;

    public Index(int x, int y, int position) {
        this.x = x;
        this.y = y;
        this.position = position;
    }

    public int getRow() {
        return y + 1;
    }
    public int getCol() {
        return x + 1;
    }

    public String toString() {
        return "(%d,%d)@%d".formatted(x,y,position);
    }
}
