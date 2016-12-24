package AriSnakeGame.ucsdKWang;
public class Coordinate {
    int x;
    int y;
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate(Coordinate other) {
        this.x = other.x;
        this.y = other.y;
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;    
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Coordinate)) {
            return false;
        }
        Coordinate other = (Coordinate) o;
        return this.getX() == other.getX() && this.getY() == other.getY();
    }

    @Override
    public String toString() {
        return "X: " + getX() + ", Y: " + getY();
    }
}
