package AriSnakeGame.ucsdKWang;
/**
 * Author: Kimii Wang
 * Date: Nov 26, 2015
 * File Name: SnakeEC.java
 * This file is a part of snake game program
 */

/*
 * Class Name: SnakeEC
 * This class defines SnakeEC as an ArrayList of SnakeECSegments and
 * some methods about snake actions.
 */
import objectdraw.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import AriSnakeGame.ucsdKWang.Coordinate;
import AriSnakeGame.ucsdKWang.Direction;
import AriSnakeGame.ucsdKWang.PA8Strings;

public class SnakeEC extends ActiveObject implements KeyListener {                  // How much the SnakeEC grows by when it eats an apple.                       
  private static final int GROW_BY = 1; 
  private static final int CUT_POINT = -200;
  // The number of cells the snake has left to grow.
  private int leftToGrow;
  private int dimensions;
  private int initialLeftToGrow;
  // The diameter of each SnakeECSegment.
  private int size;                   
  // The delay between each pause in run.        
  private int delay;    
  // the score lost in cut off mode
  private int scoreLose;
  private DrawingCanvas canvas;                  
  // Which way the snake is going.
  private Direction currentDir;                  
  // Whether the game is activated or not.       
  private boolean isRunning = false, paused = false, isGameOver = false;
  private boolean isPointLost;
  // The coordinate the snake needs to go to in order to grow                 
  private Coordinate nextApple;                  
  // The snake is a collection of segments.      
  ArrayList<SnakeECSegment> snake; 
  ArrayList<SnakeECSegment> initialSnakeEC;                
  // We need to know where the head is for apple eating and crashing          
  SnakeECSegment head;
  SnakeECSegment initialHead;                             
  SnakeControllerEC controller;
  // the Coordinate that a SnakeECSegment is added
  private Coordinate addSnakeECCoord;
  private Coordinate initialAddSnakeECCoord;
  private Coordinate startCoord;
  // determine if the apple is eaten in a move method
  private boolean isAppleEaten;
  private Color snakeColor;
  // default color of the snake when the game begins
  private Color defaultColor;
  private static final int INT_TWO = 2;
  /*
   * normal mode: 0;
   * no walls mode: 1;
   * cuts off mode: 2;
   */
  private int gameMode;

  /**
   * This is the constructor of the class
   * We add an "int dimensions" parameter to the constructor 
   * to avoid the problem of canvas size.
   */
  public SnakeEC(Coordinate coord, int size, int delay, DrawingCanvas 
    canvas, SnakeControllerEC controller, Color color, int dimensions) { 
    this.startCoord = coord;
    this.dimensions = dimensions;
    snakeColor = color;
    defaultColor = color;
    this.controller = controller;
    this.canvas = canvas;
    this.size = size;
    this.delay = delay;
    currentDir = Direction.RIGHT;
    // initialize the SnakeEC head when the game starts
    head = new SnakeECSegment( coord, size, true, canvas, snakeColor );
    //initialHead = new SnakeECSegment( coord, size, true, canvas );
    snake = new ArrayList<SnakeECSegment>(size * size);
    //initialSnakeEC = new ArrayList<SnakeECSegment>(size * size);
    snake.add( 0, head );
    //initialSnakeEC.add( 0, initialHead );
    leftToGrow = (dimensions / size) * 
      (dimensions / size) - 1;
    initialLeftToGrow = leftToGrow;
    // initialize the Coordinate that a SnakeECSegnment 
    // might be added
    addSnakeECCoord = new Coordinate( coord.getX() - size, coord.getY() );
    initialAddSnakeECCoord = new Coordinate( coord.getX()
      - size, coord.getY() );
    canvas.addKeyListener( this );
    start();
  }     
  
  /**
   * Method Name: run
   * This method starts the animation of the snake
   */
  public void run() {
    // create an infinite while loop
    while ( true ) {
      // if the game is restarted, reset everything
      // into the initial value;
      if ( controller.getStartNew() ) { 
        leftToGrow = initialLeftToGrow;
        setcurrentDir(Direction.RIGHT);
	      // reset the head to initial position
        head.moveTo(startCoord);
        head.setStartAngle();
	      snakeColor = defaultColor;
	      head.setColor( snakeColor );
	      // remove the body of snake one by one
	      int snakeSize = snake.size();
        if ( snakeSize > 1) {
	        int count = 1;
	        while ( count < snakeSize ) {
	          snake.get(1).removeFromCanvas();
	          snake.remove( snake.get(1) );
	          count++;
	        }
	      }       
        addSnakeECCoord = initialAddSnakeECCoord;
        setisRunning( false );
      }
      // the game is not over and not paused
      else if ( !controller.getPaused() && !controller.getGameOver()
        && getisRunning() && !controller.getWon()  ) {
        boolean isValidMove = move();
	      if ( !isValidMove ) {
          controller.setGameOver(true);
	      }
      }
      pause( getdelay() );
    }
  }

  public synchronized int getdelay(){

    return this.delay;    
  }

  public synchronized void setdelay( int t){
    if ( t >= 100){
      this.delay = t;
    }
    else {
      this.delay = 100;
    }

  }
  /** 
   * Method Name: keyTyped
   * This method handles the key typed event. 
   * We leave this method empty
   */
  public void keyTyped( KeyEvent e ) {
        
  }
  /** 
   * Method Name: keyPressed
   * This method handles the key pressed event. 
   */

  public void keyPressed( KeyEvent e ) {
    if ( !controller.getGameOver() && !controller.getWon()
      && !controller.getPaused() ) {
      if ( e.getKeyCode() == KeyEvent.VK_UP ){
	      setcurrentDir( Direction.UP );
	      head.setStartAngle();
	      setisRunning(true);
      }
      if ( e.getKeyCode() == KeyEvent.VK_DOWN ){
	      setcurrentDir( Direction.DOWN );
	      head.setStartAngle();
	      setisRunning(true);
      }

      if ( e.getKeyCode() == KeyEvent.VK_LEFT ){
	      setcurrentDir( Direction.LEFT );
	      head.setStartAngle();
	      setisRunning(true);
      }
      if ( e.getKeyCode() == KeyEvent.VK_RIGHT ){
	      setcurrentDir( Direction.RIGHT );
	      head.setStartAngle();
	      setisRunning(true);
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
   * Method Name: setcurrentDir
   * This method will set the value of currentDir according to 
   * the actual argument
   * @param Direction dir - a Direction object
   */
  public synchronized void setcurrentDir( Direction dir ) {
    currentDir = dir;
  }
  /**
   * Method Name: getcurrentDir()
   * This method will return the currentDir 
   * @return Direction currentDir - the Direction of 
   * the current snake movement
   */
  public synchronized Direction getcurrentDir() {
    return this.currentDir;
  }

  /**
   * Method Name: setisRunning
   * This method will set the value of isRunning
   * @param boolean bool - determine if the game should start
   */
  public synchronized void setisRunning( boolean bool ) {
    this.isRunning = bool;
  }
  /**
   * Method Name: getisRunning
   * This method will return the value of the variable isRunning
   * @return boolean true if the game has started and false otherwise
   */
  public synchronized boolean getisRunning() {
    return this.isRunning;
  }
  /**
   * Method Name: setisGameOver
   * This method will set the value of isGameOver
   * @param boolean bool - determine if the game is over
   */
  public synchronized void setisGameOver( boolean bool ) {
    this.isGameOver = bool;
  }
  /**
   * Method Name: getisGameOver
   * This method will return the value of the variable isGameOver
   * @return boolean true if the game is over and false otherwise
   */
  public synchronized boolean getisGameOver() {
    return this.isGameOver;
  }
  /**
   * Method Name: getSnakeECCoordArray
   * This method will return the coordinate array of snake arrayList
   */
  public synchronized ArrayList<Coordinate> getSnakeCoordArray() {
    ArrayList<Coordinate> snakeCoord = 
      new ArrayList<Coordinate>(snake.size());
    // add the Coordinate of each SnakeECSegment to the 
    // sankeCoord ArrayList
    int snakeSize = snake.size();
    for ( int i = 0; i < snakeSize; i++ ) {
      snakeCoord.add(snake.get(i).getSnakeECSegmentCoord());
    }
    return snakeCoord;
  }
           
  /**
   * Method Name: getGameMode
   * This method returns the code to the game mode
   * @return int gameMode - the code to the game mode
   */
  public synchronized int getGameMode() {
    return gameMode;
  }
  /**
   * Method Name: setGameMode
   * This method set the value of gameMode
   * according to the actual argument
   * @param int modeCode - the code of the game
   */
  public synchronized void setGameMode( int modeCode ) {
    gameMode = modeCode;
  } 

 
  private class SnakeECSegment {                   
    // visible appearance of the snake.        
    private final Color SNAKE_COLOR = Color.GREEN;                          
    private final Color SNAKE_OUTLINE = Color.BLACK;                        
    private FilledArc segment;       
    private FramedArc frame;        
    // the location of each snake segment.             
    private Coordinate coord; 
    private Color color;
    // head constants                          
    private static final double UP_ANGLE = 90 + 22.5;  
    private static final double LEFT_ANGLE = 90 + UP_ANGLE;                   
    private static final double DOWN_ANGLE = 90 + LEFT_ANGLE;                 
    private static final double RIGHT_ANGLE = 90 + DOWN_ANGLE;                
    private static final double HEAD_ARC_ANGLE = 360 - 45;
    private double BODY_ARC_ANGLE = 360;       
    /**                                        
     * Creates a snakeSegment, by putting its parts on the canvas.          
     *                                         
     */                                        
    public SnakeECSegment( Coordinate coord, int size, boolean isHead,         
      DrawingCanvas canvas, Color color ) { 
      this.color = color;
      this.coord = coord;
      double xLoc = (double) coord.getX();
      double yLoc = (double) coord.getY();
      double segWidth = (double) size;
      double segHeight = segWidth;

      // the segment is a snake head
      if ( isHead ) {
        // the head is towards right
        if ( getcurrentDir() == Direction.RIGHT ){
          segment = new FilledArc( xLoc,yLoc,segWidth, segHeight,
	          RIGHT_ANGLE, HEAD_ARC_ANGLE, canvas );
          frame = new FramedArc(xLoc,yLoc,segWidth, segHeight,
            RIGHT_ANGLE, HEAD_ARC_ANGLE, canvas);

	    }
        // the head is towards right
        else if ( getcurrentDir() == Direction.DOWN ){
	
          segment = new FilledArc( xLoc,yLoc,segWidth, segHeight,
	          DOWN_ANGLE, HEAD_ARC_ANGLE, canvas );
          frame = new FramedArc( xLoc,yLoc,segWidth, segHeight,
            DOWN_ANGLE, HEAD_ARC_ANGLE, canvas );

	      }
        // the head is towards right
        if ( getcurrentDir() == Direction.LEFT ){
	
          segment = new FilledArc( xLoc,yLoc,segWidth, segHeight,
	          LEFT_ANGLE, HEAD_ARC_ANGLE, canvas );
          frame = new FramedArc( xLoc,yLoc,segWidth, segHeight,
            LEFT_ANGLE, HEAD_ARC_ANGLE, canvas );

	      }
        // the head is towards right
        if ( getcurrentDir() == Direction.UP ){
	
          segment = new FilledArc( xLoc,yLoc,segWidth, segHeight,
	          UP_ANGLE, HEAD_ARC_ANGLE, canvas );
          frame = new FramedArc( xLoc,yLoc,segWidth, segHeight,
            UP_ANGLE, HEAD_ARC_ANGLE, canvas ); 

        }
	      segment.setColor( color );
        frame.setColor( Color.BLACK );

      }
      // the segment is a snake body
      else {
        segment = new FilledArc( xLoc, yLoc, segWidth, segHeight,
	        0, BODY_ARC_ANGLE, canvas );
        frame = new FramedArc( xLoc, yLoc, segWidth, segHeight,
          0, BODY_ARC_ANGLE, canvas );

      }
      segment.setColor( color );
      frame.setColor( Color.BLACK);

    }

    /**
     * Method Name: getSnakeECSegmentCoord
     * This method will return the coordinate of the SnakeECSegment
     * @return Coordinate coord - the Coordinate of the SnakeECSegment
     */
    public Coordinate getSnakeECSegmentCoord() {
      return this.coord;
    }

    /**
     * Method Name: setSnakeECSegmentCoord
     * This method sets the Coordinate of the SnakeECSegment
     * according to the actual argument
     * @param Coordinate c - the new Coordinate of the 
     * SnakeECSegment
     */
    public void setSnakeECSegmentCoord( Coordinate c ) {
      this.coord = c;
    }

    /**
     * Method Name: moveTo
     * This method will move the SnakeECSegment to a Location
     * based on the actual argument
     * @param Coordinate c - the Coordinate where the 
     * SnakeECSegment will move to
     */
    public void moveTo( Coordinate c ) {
      double xLoc = (double) c.getX();
      double yLoc = (double) c.getY();
      segment.moveTo( xLoc, yLoc );
      frame.moveTo(xLoc,yLoc);
      setSnakeECSegmentCoord(c);
    }
    /**
     * Method Name: removeFromCanvas
     * This method remove the SnakeECSegment on the canvas
     */
    public void removeFromCanvas() {
      segment.removeFromCanvas();
      frame.removeFromCanvas();
    }

    public void sendForward(){
     segment.sendForward();
     frame.sendForward();
    }
    /**
     * Method Name: setColor
     * THis method set the color of the SnakeECSegment
     * @param Color color - the color of the snake segment
     */
    public void setColor( Color color ) {
      segment.setColor( color );
    }
    /**
     * Method Name: setStartAngle
     * This method overrides to setStartAngle method in FilledArc
     */
    public void setStartAngle(){
      if ( getcurrentDir() == Direction.UP ) {
	      segment.setStartAngle(UP_ANGLE);
        frame.setStartAngle(UP_ANGLE);
  
      }
      else if ( getcurrentDir() == Direction.DOWN ) {
	      segment.setStartAngle(DOWN_ANGLE);
        frame.setStartAngle(DOWN_ANGLE);
      
      }
      else if ( getcurrentDir() == Direction.RIGHT ) {
	      segment.setStartAngle(RIGHT_ANGLE);
        frame.setStartAngle(RIGHT_ANGLE);

      }
      else if ( getcurrentDir() == Direction.LEFT ) {
	      segment.setStartAngle(LEFT_ANGLE);
        frame.setStartAngle(LEFT_ANGLE);
      }
     
    }
  }
  /**
   * Method Name: move
   * This method takes care of moving the snake.
   * @return boolean bool - true if it is a valid move or false if
   * the snake crashes
   */
  private boolean move() {
    setScoreLose(0);
    // the return type of this method.
    boolean isValid = true;
    boolean snakeCutOff = false;
    int cutOffIndex = 0;
    // the Coordinate of different parts of the snake
    Coordinate prevCoord = head.getSnakeECSegmentCoord();
    Coordinate newCoord = prevCoord;
    // the Coordinate of the head of the SnakeEC.
    Coordinate headCoord = head.getSnakeECSegmentCoord();
    // update the coordinate to add a snake segment
    addSnakeECCoord = snake.get(snake.size()-1).getSnakeECSegmentCoord();

    // head right
    if ( getcurrentDir() == Direction.RIGHT ) {
      newCoord = new Coordinate( prevCoord.getX()+size,
        prevCoord.getY() );
    }
    // head left
    else if ( getcurrentDir() == Direction.LEFT ){
      newCoord = new Coordinate( prevCoord.getX()-size,
        prevCoord.getY() );	
    }
    // head up
    else if ( getcurrentDir() == Direction.UP ){
      newCoord = new Coordinate( prevCoord.getX(),
        prevCoord.getY()-size );	
    }
    // head down
    else if ( getcurrentDir() == Direction.DOWN ){
      newCoord = new Coordinate( prevCoord.getX(),
        prevCoord.getY() + size );	
    }
    headCoord = newCoord;
    // if it is no walls mode
    if ( getGameMode() == 1 ) {
      if ( headCoord.getX() < 0 ){
        // now the snake hits the left wall
	      newCoord.set( (int)(dimensions) - size, headCoord.getY());
	      headCoord = newCoord;
      }
      else if ( headCoord.getX() >= dimensions ) {
        // now the snake hits the right wall
	      newCoord.set( 0, headCoord.getY() );
	      headCoord = newCoord;
      }
      else if ( headCoord.getY() < 0 ){
        // now the snake hits the upper wall
	     newCoord.set(headCoord.getX(), (int)(dimensions)- size);
	     headCoord = newCoord;
      }
      else if ( headCoord.getY() >= dimensions ) {
        // now the snake hits the lower wall
	      newCoord.set(headCoord.getX(),0);
	      headCoord = newCoord;
      }
      
    }

    // normal mode or cut off mode
    else {
      // check if the snake hits the wall
      if ( headCoord.getX() < 0 || headCoord.getX() >= dimensions
        || headCoord.getY() < 0 || headCoord.getY() >= 
        dimensions ) {
        isValid = false;
      }
    }

    // move the head and the body of the snake
    // move the SnakeECSegment one by one
    for ( int i = 0; i < snake.size(); i++ ) {
      // the snake hits its own body
      if (snake.get(i).getSnakeECSegmentCoord().equals(headCoord)) {
	      if ( getGameMode() == 0 || getGameMode() == 1 ) {
	      // in normal mode or no walls mode
	      isValid = false;
	      }
	     else {
	      // in cuts off mode
	      snakeCutOff = true;
	      cutOffIndex = i + 1;
	     }
     }

      snake.get(i).moveTo( newCoord );
      snake.get(i).sendForward();
      if (i < snake.size() - 1 ) {
	      // update the previous and new coordinate of the snake
	      newCoord = prevCoord;
	      prevCoord = snake.get(i+1).getSnakeECSegmentCoord();	
      }      
    }

    // remove the snake segment from the ArrayList and canvas
    if ( snakeCutOff ){
      int snakeSize = snake.size();
      int count = cutOffIndex;
      for ( int j = cutOffIndex; j < snakeSize; j++ ) {
        snake.get(count).removeFromCanvas();
	      snake.remove(snake.get(count));
      }
  
      int segmentLost = snakeSize-cutOffIndex;
      setScoreLose( CUT_POINT * segmentLost );
      leftToGrow += segmentLost;
      controller.setLosePointUpdate(true);
    }

    // add a new snake segment if an apple is eaten
    // in the previous move
    if ( getisAppleEaten() && !snakeCutOff ) {
      SnakeECSegment body = new SnakeECSegment( addSnakeECCoord, size,
	      false, canvas, snakeColor );
      snake.add( snake.size(), body );
      leftToGrow--;
      setisAppleEaten( false );
    }

    // get the coordinate of the next apple 
    nextApple = controller.getCurrentAppCoord();
    // if the snake eats an apple, send this message to the 
    // controller and the SnakeEC
    if ( head.getSnakeECSegmentCoord().equals(nextApple) ) {
      setdelay(getdelay()-20);
      // change the color of the snake.
      snakeColor = controller.getAppColor();
      for ( int i = 0; i < snake.size(); i++ ) {
        snake.get(i).setColor( controller.getAppColor() );
      }
      // check whether the player wins the game
      if ( leftToGrow == 0 ) {
	      controller.setWon(true);
      }
      // if the player does not win, send the add apple message
      // to the next call to move() method
      else {
        setisAppleEaten( true );
        controller.setisAppleEaten( true );
      }
    }
    return isValid; 
  }

  /**
   * Method Name: getScoreLose
   * This method return the value of scoreLose
   * @return int scoreLose  - the score lost only in cut off mode
   */
  public synchronized int getScoreLose() {
    return scoreLose;
  }
  /**
   * Method Name: setScoreLose
   * THis method sets teh value of scoreLose
   * @param int pts - the point lost 
   */
  public synchronized void setScoreLose( int pts ) {
    scoreLose = pts;
  }


}  
