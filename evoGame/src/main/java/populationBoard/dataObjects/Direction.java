package populationBoard.dataObjects;

public enum Direction {
    UP(0, 1),
    DOWN(0, -1),
    RIGHT(1, 0),
    LEFT(-1, 0);


    private final int x;
    private final int y;

    Direction(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getX(int speed){
        return x*speed;
    }

    public int getY(int speed){
        return y*speed;
    }
}
