package systems.monomer.errorhandling;


public class Index {
    private int x, y;
    private int position;

    public Index(int x, int y, int position) {
        this.x = x;
        this.y = y;
        this.position = position;
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

    public int getPosition() {
        return position;
    }
}
