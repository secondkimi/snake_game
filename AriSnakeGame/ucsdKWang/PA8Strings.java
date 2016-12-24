package AriSnakeGame.ucsdKWang;
public class PA8Strings {
    public static final String USAGE = "Usage: java -cp ./Acme.jar:" + 
        "./objectdraw.jar:. SnakeController DIMENSIONS SEGMENT_SIZE DELAY\n";
    public static final String USAGE_EC = "Usage: java -cp ./Acme.jar:" + 
        "./objectdraw.jar:. SnakeControllerEC DIMENSIONS SEGMENT_SIZE DELAY\n";
    public static final String OUT_OF_RANGE = "Error: value %d is out of " + 
        "range. It should be between %d and %d\n";
    public static final String SEG_DOES_NOT_FIT = "Snake segment size of %d " +
        "does not evenly fit inside the given dimension %d x %d\n";
    public static final String SEG_TOO_LARGE = "Snake segment size of %d " +
        "is too large for the given dimension %d x %d\n";
    public static final String SCORE = "Score: %d";
    public static final String HIGH_SCORE = "High Score: %d";
    public static final String NEW_GAME = "New Game";
    public static final String GAME_OVER = "GAME OVER";
    public static final String WIN = "YOU WON!";
    public static final String PAUSED = "PAUSED";
}
