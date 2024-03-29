//------------------------------------------------------------------//
// Direction.java                                                   //
//                                                                  //
// Enumeration Type used to represent a Movement Direction          //
//  Each Direction object includes the vector of Direction          //
//                                                                  //
// Author:  Brandon Williams                                        //
// Date:    1/17/15                                                 //
//------------------------------------------------------------------//

/** DO NOT MODIFY THIS FILE */
package AriSnakeGame.ucsdKWang;
public enum Direction
{
    // The Definitions for UP, DOWN, LEFT, and RIGHT
    UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0), STILL(0, 0);

    private final int y;
    private final int x;

    Direction(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    // Retrieve the X component of direction
    public int getX()
    {
        return x;
    }
    
    // Retrieve the Y component of direction
    public int getY()
    {
        return y;
    }

    @Override
    public String toString()
    {
        return name() + "(" + x + ", " + y + ")";
    }
    
}
