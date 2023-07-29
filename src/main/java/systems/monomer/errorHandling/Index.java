package systems.monomer.errorHandling;


public class Index {
    private int x, y;

    public Index(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRow() {
        return y + 1;
    }

    public int getCol() {
        return x + 1;
    }
}
