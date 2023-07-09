package systems.merl.monomer.errorHandling.error;

public class SourceIndex {
    private final int x, y;

    public SourceIndex(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }
    public int getY() {
        return this.y;
    }

    public int getRow(){
        return y+1;
    }
    public int getCol(){
        return x+1;
    }
}