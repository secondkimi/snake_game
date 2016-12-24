package AriSnakeGame.ucsdKWang;
/**
 * Author: Kimi Wang
 * Date: Nov 26, 2015
 * File Name: SnakeControllerEC.java
 * This file is a part of the AriSnake Game program.
 */

/*
 * Class Name: SnakeControllerEC
 * This class controls all the UI of the game.
 */
import objectdraw.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import AriSnakeGame.ucsdKWang.Coordinate;
import AriSnakeGame.ucsdKWang.Direction;
import AriSnakeGame.ucsdKWang.PA8Strings;

public class SnakeControllerEC extends WindowController
                             implements ActionListener, KeyListener {   
  private static final int Y_PADDING = 50;
  private static final int X_PADDING = 6;
  private static final int LINUX_MIGHT_HAVE_THIS_EXTRA_WEIRD_PADDING = 6;                               
  private static final int MIN_DIM = 500;        
  private static final int MAX_DIM = 800;        
  private static final int MAX_SPEED = 100;      
  private static final int MIN_SPEED = 1000;     
  private static final int MIN_SIZE = 20;        
  private static final int MAX_SIZE = 400;       
  private static final int NUM_TOP_COLUMNS = 2;  
  private static final int NUM_TOP_ROWS = 1;  
  private static final int FONT_SIZE = 40;
  private static final double TWO = 2;
  private static final int INT_TWO = 2;
  // the score got when an apple is eaten.
  private static final int EAT_SCORE = 100;
  // the number of arguments in the command line
  private static final int THREE = 3;
  private static int dimensions;                               
  private int size;                              
  private int delay;                                                          
  private int score;                             
  private int highScore;                         
  private JLabel scoreLabel;                     
  private JLabel highScoreLabel;                 
  private JButton newgame;
  private JButton normal;
  private JButton noWalls;
  private JButton cutsOff;
  private JButton review;
  private JPanel northPanel;
  private JPanel northPanel1;
  private JPanel northPanel2;
  private JPanel southPanel;
  private Text gameOverText;                     
  private Text winText;                          
  private Text pauseText;    
  private int count;                    
  private boolean gameOver;                      
  private boolean won;                           
  private boolean paused;  
  private boolean startNew;   
  private boolean isAppleEaten;
  private boolean isScoreUpdated;
  private boolean losePointUpdate; 
  private boolean canReview;
  private SnakeEC snake;                           
  private FramedRect apple;
  private FramedRect apple2;
  private FramedRect apple3;
  private VisibleImage winImg;
  private VisibleImage loseImg;
  private Random randomIndexGenerator = new Random();
  private Random randomIndexGenerator1 = new Random();
  // number of grids in the game.
  private int gridNum;
  private VisibleImage we;
  private ArrayList<Coordinate> openSpaces;
  private Coordinate currentAppCoord;
  private SnakeControllerEC controller;
  // Color array used to change the color of the snake and fruits
  private Color[] colors = { Color.RED, Color.ORANGE, 
    Color.GREEN, Color.CYAN, Color.MAGENTA };
  // the color of the fruit
  private Color appColor;
  private int colorLength = colors.length;
  private static Image[] img = new Image[20]; 

  /**
   * This is a constructor of the SnakeControllerEC class
   * @param String[] args - a string array based on user
   * input in the command line
   */
  public SnakeControllerEC( String[] args ) {
    dimensions = Integer.parseInt( args[0] );
    this.size = Integer.parseInt( args[1] );
    this.delay = Integer.parseInt( args[INT_TWO] );
  }

  /**
   * Method Name: actionPerformed
   * this method implements the abstract method in ActionListener
   * @param ActionEvent evt - An ActionEvent Object
   */
  public void actionPerformed ( ActionEvent evt ) {
    // clicks newgame button
    if ( evt.getSource() == newgame ) {
	  controller.setStartNew( true );
	  snake.setdelay(delay);
    }
    // click normal button
    else if ( evt.getSource() == normal ) {
      if ( !controller.getGameOver() && !snake.getisRunning() ) {
        snake.setGameMode(0);
      }
    }
    // click noWalls button
    else if ( evt.getSource() == noWalls ) {
      if ( !controller.getGameOver() && !snake.getisRunning() ) {
        snake.setGameMode(1);
      }     
    }
    else if ( evt.getSource() == review ) {
      // only effective when the game is over or won
      if ( controller.getGameOver() || controller.getWon() ) {
      	if ( getCount() % 2 == 0 ){
          setCanReview( true );
          winImg.hide();
          loseImg.hide();
        }
        else {
          winImg.show();
          winImg.show();
          setCanReview(false);
        }
        setCount(getCount()+1);
      }

    }
    // click cutsOff button
    else if ( evt.getSource() == cutsOff ) {
      if ( !controller.getGameOver() && !snake.getisRunning() ) {
        snake.setGameMode(INT_TWO);
      }    
    }

    canvas.requestFocusInWindow();
  }

  /**
   * Method Name: begin
   * This method begins the game and assign values 
   * to many objects when the game get started
   */
  public void begin() {
    scoreLabel = new JLabel( "Score: "+score );
    highScoreLabel = new JLabel( "High Score: "+highScore );
    newgame = new JButton(PA8Strings.NEW_GAME);
    normal = new JButton("Normal");
    noWalls = new JButton("No Walls");
    cutsOff = new JButton("Cuts Off");
    review = new JButton("Review");
    northPanel = new JPanel( new GridLayout(1,INT_TWO));
    northPanel1 = new JPanel(new BorderLayout());
    northPanel2 = new JPanel(new BorderLayout());
    southPanel = new JPanel();

    newgame.addActionListener( this );
    normal.addActionListener( this );
    noWalls.addActionListener( this );
    cutsOff.addActionListener( this );
    review.addActionListener( this );
    northPanel1.add( scoreLabel, BorderLayout.WEST );
    northPanel2.add( highScoreLabel, BorderLayout.WEST );
    northPanel.add(northPanel1);
    northPanel.add(northPanel2);

    southPanel.add( newgame );
    southPanel.add( normal );
    southPanel.add( noWalls );
    southPanel.add( cutsOff );
    southPanel.add( review );

    // create a GUI container
    Container contentPane = getContentPane();
    // add the JPanel and JButton to the container
    contentPane.add( northPanel, BorderLayout.NORTH );
    contentPane.add( southPanel, BorderLayout.SOUTH );
    contentPane.validate();
    this.validate();  
    System.out.println("width "+canvas.getWidth()+" height "+
      canvas.getHeight());
    img[0] = getImage("1.jpg");
    img[1] = getImage("2.JPG");
    img[2] = getImage("3.JPG");
    img[3] = getImage("4.JPG");
    img[4] = getImage("5.JPG");
    img[5] = getImage("6.JPG");
    img[6] = getImage("7.JPG");
    img[7] = getImage("8.JPG");
    img[8] = getImage("9.JPG");

    img[9] = getImage("10.JPG");
    img[10] = getImage("11.JPG");
    img[11] = getImage("12.JPG");
    img[12] = getImage("13.JPG");
    img[13] = getImage("14.JPG");
    img[14] = getImage("15.JPG");
    img[15] = getImage("20.JPG");
    img[16] = getImage("21.JPG");
    img[17] = getImage("22.JPG");
    img[18] = getImage("23.JPG");
    img[19] = getImage("24.JPG");

    this.addKeyListener( this );
    canvas.addKeyListener( this );
    String[] mainArgs = { Integer.toString(dimensions),
      Integer.toString(size), Integer.toString(delay) };
    controller = new SnakeControllerEC(mainArgs);
    snake = new SnakeEC( new Coordinate(size,size), size, delay,
    	canvas, controller, Color.GREEN, dimensions ); 
    controller.setAppColor( colors[INT_TWO] );

    we = new VisibleImage(getImage("2.JPG"),1,1,size,size,canvas);
    winImg = new VisibleImage(getImage("17.JPG"),0,0,dimensions,dimensions,
      canvas);
    winImg.hide();
    loseImg = new VisibleImage(getImage("16.JPG"),0,0,dimensions,dimensions,
      canvas);
    loseImg.hide();
    we.hide();
    apple = new FramedRect(0, 0, size, size, canvas);
    apple2 = new FramedRect(1,1,size-2,size-2,canvas);
    apple3 = new FramedRect(2,2,size-4,size-4,canvas);
    apple.hide();
    apple2.hide();
    apple3.hide();
    placeApple( canvas );
    // set the text content
    gameOverText = new Text(PA8Strings.GAME_OVER, 1, 1, canvas );
    winText = new Text(PA8Strings.WIN, 1, 1, canvas );
    pauseText = new Text(PA8Strings.PAUSED, 1, 1, canvas );
    gameOverText.setFontSize( FONT_SIZE );
    winText.setFontSize( FONT_SIZE );
    pauseText.setFontSize( FONT_SIZE );
    winText.hide();
    pauseText.hide();
    gameOverText.hide();
    // the x and y coordinate of each Text 
    double winTextX = ( dimensions - winText.getWidth() ) / TWO; 
    double gameOverTextX = ( dimensions - gameOverText.getWidth()
      ) / TWO; 
    double pauseTextX = ( dimensions - pauseText.getWidth() )
      / TWO; 
    double textY = ( dimensions - winText.getHeight() ) / TWO;
    winText.moveTo( winTextX, textY );
    gameOverText.moveTo( gameOverTextX, textY );
    pauseText.moveTo( pauseTextX, textY );
    canvas.requestFocusInWindow();
    // create a infinite while loop to keep track of the game
    while ( true ) {
      if ( controller.getStartNew() ){
        if (controller.getGameOver() || controller.getWon() ) {
	      // check if the canvas should update the score
          if ( getScore() > getHighScore() ) {
            setHighScore(score);
            highScoreLabel.setText("High Score: "+getHighScore());
          }
	    }
    
        controller.setGameOver( false );
		    controller.setWon( false );
		    controller.setPaused( false );
		    controller.setAppColor(colors[INT_TWO]);
		    setisScoreUpdated( false );
		    setCanReview(false);
		    setCount(0);
    	  setScore( 0 );
    	  scoreLabel.setText("Score: "+getScore());
    	  winText.hide();
    	  pauseText.hide();
    	  gameOverText.hide();
    	  winImg.hide();
    	  loseImg.hide();
        if ( !snake.getisRunning() ) {
          placeApple(canvas);
	        controller.setStartNew( false );
        }          	
      } 
      
      else if ( controller.getPaused() ) {
	    pauseText.show();
        pauseText.sendForward();  
      }

      else {
        if ( controller.getLosePointUpdate() ) {
          setScore( getScore() + snake.getScoreLose() );
	      scoreLabel.setText("Score: "+getScore());
	      controller.setLosePointUpdate( false );
        }

	    if ( controller.getisAppleEaten() ) {	      
          placeApple( canvas );
	      setScore( getScore() + EAT_SCORE );
	      scoreLabel.setText("Score: "+getScore());
	      controller.setisAppleEaten( false );
	    }

	    // if the player wins after eat the apple,
		// display the win message
		if ( controller.getWon() ) {
	  	  this.gameWinning();
		}

		else if ( controller.getGameOver() ) {
	  	  this.gameOver();
		}
      } 
    }

  }

  // used for review button
  public synchronized void setCount( int c ) {
    this.count = c;

  }
  public synchronized int getCount() {
    return this.count;
  }
  public synchronized void setCanReview( boolean c ) {
    this.canReview = c;

  }
  public synchronized boolean getCanReview() {
    return this.canReview;
  }

  /** 
   * Method Name: keyTyped
   * This method handles the key typed event. 
   * we leave this method empty
   */
  public void keyTyped( KeyEvent e ) {
        
  }
  /** 
   * Method Name: keyPressed
   * This method handles the key pressed event. Here we care about
   * space bar press 
   */

  public void keyPressed( KeyEvent e ) {
    // if the user presses the space bar, pause the game
    if ( e.getKeyCode() == KeyEvent.VK_SPACE ){
      if ( !controller.getGameOver() && !controller.getWon() ) {
        // reverse the value of paused
        controller.setPaused();
        if ( !controller.getPaused() ){
          // the game starts again.
          pauseText.hide();
        }
      }
    }
  }

  /** 
   * Method Name: keyReleased
   * This method handles the key released event. 
   * we leave this method empty
   */
  public void keyReleased( KeyEvent e ) {
        
  }

  /**
   * Method Name: placeApple
   * This method will place an apple in an open space
   * @return Coordinate currentAppCoord - the current
   * Coordinate of the apple.
   */
  public Coordinate placeApple( DrawingCanvas canvas ) {
    ArrayList<Coordinate> snakeArr = snake.getSnakeCoordArray();
    // number of rows and columns
    int rowNum = dimensions / size;
    int colNum = dimensions / size;
    gridNum = rowNum * colNum;
    openSpaces = new ArrayList<Coordinate>(gridNum);
    // we will add the Coordinate to the ArrayList
    // First we create all the coordinates on the canvas
    for ( int i = 0; i < rowNum; i++ ) {
      for ( int j = 0; j < colNum; j++ ) {
        openSpaces.add(new Coordinate(size * i, size * j));	
      }
    }
    // then we remove all coordinates covered by the snake
    for ( int i = 0; i < snakeArr.size(); i++ ){
      for ( int j = 0; j < openSpaces.size(); j++ ) {
        if ( snakeArr.get(i).equals(openSpaces.get(j))) {
	      openSpaces.remove( snakeArr.get(i) );
	    }
      }
    }
    if ( openSpaces.size() == 0 ) {
      apple.hide();
      apple2.hide();
      apple3.hide();
      we.hide();
      return null;
    }
    else {
      int value = randomIndexGenerator.nextInt(openSpaces.size());
      controller.currentAppCoord = openSpaces.get( value );
      double xLoc = (double) controller.currentAppCoord.getX();
      double yLoc = (double) controller.currentAppCoord.getY();
      apple.moveTo( xLoc, yLoc );
      apple2.moveTo(xLoc+1,yLoc+1);
      apple3.moveTo(xLoc+2,yLoc+2);
      if ( getImg()!= null ) {
        we.setImage(getImg());}
        we.moveTo( xLoc, yLoc);
        we.show();
        apple.show();
        apple2.show();
        apple3.show();
        controller.setAppColor();
        apple.setColor( controller.getAppColor() );
        apple2.setColor( controller.getAppColor() );
        apple3.setColor( controller.getAppColor() );
      }
    return currentAppCoord;  

  }

  // return the image we want
  public Image getImg() {
    int val = randomIndexGenerator1.nextInt(20);
    Image i = img[val];
    return i;
  }

  /**
   * Method Name: getAppColor
   * This method will return the color of the apple
   * @return Color - the color of the apple
   */
  public synchronized Color getAppColor() {
    return appColor;
  }

  /**
   * Method Name: setAppColor
   * This method sets the variable appColor according to the actual
   * argument
   * @param Color color - the color to be set
   */
  public synchronized void setAppColor( Color color ) {
    appColor = color;
  }

  /**
   * Method Name: setAppColor
   * This method will change the color of the apple randomly.
   */
  public synchronized void setAppColor() {
    // make sure the old color is changed 
    Color oldColor = getAppColor();
    boolean keepLooping = true;
    /* 
     * if the random color generated equals the
     * old color, keep the while loop until we get
     * a different color
     */
    while ( keepLooping ) {
      int value = randomIndexGenerator.nextInt( colorLength );
      if ( !colors[value].equals( oldColor ) ) {
        appColor = colors[value];
        keepLooping = false;
      }
    }
  }

  /**
   * Method Name: getCurrentAppCoord
   * This method returns the value of currentAppCoord
   * @return Coordinate currentAppCoord
   */
  public synchronized Coordinate getCurrentAppCoord() {
    return currentAppCoord;
  }

  /**
   * Method Name: setCurrentAppCoord
   * This method will set the Coordinate of the current apple
   * on the canvas
   * @param Coordinate cd - the new Coordinate of the apple
   */
  public synchronized void setCurrentAppCoord( Coordinate cd ) {
    this.currentAppCoord = cd;
  }

  /**
   * Method Name: gameOver
   * This method will handle the game over situation
   */
  public synchronized void gameOver() {
    apple.hide();
    apple2.hide();
    apple3.hide();
    we.hide();
    if (!getCanReview() ){
      loseImg.sendForward();
      loseImg.show();
    } 
  }

  /**
   * Method Name: gameWinning
   * This method will handle the game winning situation
   */
  public synchronized void gameWinning() {
    if ( !getisScoreUpdated() ) {
      setScore( getScore() + EAT_SCORE );
      scoreLabel.setText("Score: "+getScore());   
      apple.hide();
      apple2.hide();
      apple3.hide();
      we.hide();

      setisScoreUpdated( true ); 
    }
    if (!getCanReview()){
      winImg.sendForward();
      winImg.show();
    }
  }

  /**
   * Method Name: setisScoreUpdated
   * This method set the value of the boolean variable isScoreUpdated
   * @param boolean bool - true if the score has been updated when 
   * the player won ( eat the last apple )
   */
  public synchronized void setisScoreUpdated( boolean bool ) {
    this.isScoreUpdated = bool;
  }

  /**
   * Method Name: getisScoreUpdated
   * THis method will return the value of the variable isScoreUpdated
   * @return boolean isScoreUpdated - A boolean variable
   */
  public synchronized boolean getisScoreUpdated(){
    return this.isScoreUpdated;
  }

  /**
   * Method Name: setLosePointUpdate
   * This method set the value of the boolean variable losePointUpdate
   * @param boolean bool - true if the score should be updated when
   * the snake eats its own body and false if not.
   */
  public synchronized void setLosePointUpdate( boolean bool ) {
    this.losePointUpdate = bool;
  }

  /**
   * Method Name: getLosePointUpdate
   * THis method will return the value of the variable losePointUpdate
   * @return boolean losePointUpdate - A boolean variable
   */
  public synchronized boolean getLosePointUpdate(){
    return losePointUpdate;
  }
   
  /**
   * Method Name: setWon
   * This method set the value of the boolean variable won
   * @param boolean bool - true if the player wins and false 
   * if the game is still going
   */
  public synchronized void setWon( boolean bool ) {
    this.won = bool;
  }

  /**
   * Method Name: getWon
   * THis method will return the value of the variable won
   * @return boolean won - A boolean variable
   */
  public synchronized boolean getWon(){
    return this.won;
  }

  /**
   * Method Name: setWon
   * This method set the value of the boolean variable startNew
   * @param boolean bool - true if new game button is clicked
   * and false when the game starts.
   */
  public synchronized void setStartNew( boolean bool ) {
    this.startNew = bool;
  }

  /**
   * Method Name: getStartNew
   * THis method will return the value of the variable startNew
   * @return boolean startNew - A boolean variable
   */
  public synchronized boolean getStartNew(){
    return startNew;
  }

  /**
   * Method Name: setPaused
   * This method will set the value of the boolean variable paused
   * based on the formal parameter
   * @param boolean bool - true if the game is paused and false 
   * if not.
   */
  public synchronized void setPaused( boolean bool ) {
    this.paused = bool;
  }


  /**
   * Method Name: setPaused
   * This method will reverse the value of the boolean variable paused
   * So when paused is true, the method will set the paused false.
   */
  public synchronized void setPaused() {
    boolean bool = getPaused();
    this.paused = !bool;
  }

  /**
   * Method Name: getPaused
   * THis method will return the value of the variable paused
   * @return boolean paused - A boolean variable
   */
  public synchronized boolean getPaused(){
    return this.paused;
  }

  /**
   * Method Name: setisAppleEaten
   * This method will set the value of isAppleEaten according to 
   * the actual argument
   * @param boolean bool - a boolean paramter that determines if the 
   * apple is eaten by the snake
   */
  public synchronized void setisAppleEaten( boolean bool ) {
    this.isAppleEaten = bool;
  }

  /**
   * Method Name: getisAppleEaten
   * This method will return the variable isAppleEaten 
   * @return boolean isAppleEaten - whether the apple is 
   * eaten by the snake
   */
  public synchronized boolean getisAppleEaten() {
    return this.isAppleEaten;
  }


  /**
   * Method Name: setGameOver
   * This method will reverse the boolean variable GameOver
   * @param boolean bool - true if the game is over and false otherwise
   */
  public synchronized void setGameOver( boolean bool ) {
    gameOver = bool;
  }

  /**
   * Method Name: getGameOver()
   * THis method will return the value of the variable gameOver
   * @return boolean gameOver - A boolean variable
   */
  public synchronized boolean getGameOver(){
    return gameOver;
  }

  /**
   * Method Name: setHighScore
   * This method set the value of the int variable highScore
   * @param int score - the score to be updated
   */
  public synchronized void setHighScore( int score ) {
    highScore = score;
  }

  /**
   * Method Name: getScore()
   * THis method will return the value of the variable score
   * @return int score - The current score of the player
   */
  public synchronized int getScore(){
    return score;
  }

  /**
   * Method Name: setScore
   * This method set the value of the int variable score
   * @param int score - the score to be updated
   */
  public synchronized void setScore( int score ) {
    this.score = score;
  }

  /**
   * Method Name: getHighScore()
   * THis method will return the value of the variable highScore
   * @return int highScore - The highest score of the player
   */
  public synchronized int getHighScore(){
    return highScore;
  }
    
  /**
   * Method Name: main
   * This is the main method of this class
   * Launch the program
   * @param args a string array 
   */
  public static void main ( String[] args ) {
    // error check whether the user puts
    // the correct number of arguments
    if ( args.length != THREE ) {
      System.out.print(PA8Strings.USAGE_EC);
      System.exit(1);
    } 
    else {
      dimensions = Integer.parseInt( args[0] );
      if ( dimensions < MIN_DIM || dimensions > MAX_DIM ) {
        System.out.printf( PA8Strings.OUT_OF_RANGE, dimensions,
	      MIN_DIM, MAX_DIM );
	    System.exit(1);
      }

      int segSize = Integer.parseInt( args[1] );
      if ( segSize > MAX_SIZE || segSize < MIN_SIZE ) {
        System.out.printf( PA8Strings.OUT_OF_RANGE, segSize,
	      MIN_SIZE, MAX_SIZE );
        System.out.print(PA8Strings.USAGE_EC);
		System.exit(1);
      }
      else if ( segSize > ( dimensions / INT_TWO ) ) {
        System.out.printf( PA8Strings.SEG_TOO_LARGE, segSize,
	  	  dimensions, dimensions );  
		System.out.print(PA8Strings.USAGE_EC);
		System.exit(1);
      }
      else if (dimensions % segSize != 0 ) {
        System.out.printf( PA8Strings.SEG_DOES_NOT_FIT, segSize,
	  	  dimensions, dimensions ); 
		System.out.print(PA8Strings.USAGE_EC);
		System.exit(1);
      }
      int delayTime = Integer.parseInt( args[INT_TWO] );
      if ( delayTime > MIN_SPEED || delayTime < MAX_SPEED ) {
      	System.out.print(PA8Strings.USAGE_EC);
		System.exit(1);
      }
    }
    // if the canvas width displayed in the command line is 606,
    // remove X_PADDING.
    new Acme.MainFrame( new SnakeControllerEC(args), args,
      dimensions, dimensions+Y_PADDING + 2);

  }

}
