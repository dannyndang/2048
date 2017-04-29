package g2048;

/* 

 *  Name: Danny Dang, A12780186
 *  Login: cs8bwamx
 *  Date: February 3, 2016
 *  File: Board.java
 *  Sources of Help: Java docs, discussion sections, tutors
 */

/*
 * Name:    Board.java
 * Purpose: Board.java is used by the GameManager.java to create a 2048 board.
 * Board.java takes in the following paramters: random, boardSize, and 
 * outputfilename. It creates a random board based on boardSize and random
 * and gets the outputfilename ready for the board to be saved. It also adds
 * tiles in the beginning and after each valid turn. It also checks whether
 * the board cane move in a certain direction, if so, it will move in that 
 * direction. If there are no more moves, it will show a game over message. 
 */

/**
 * Sample Board
 * <p/>
 * 0   1   2   3
 * 0   -   -   -   -
 * 1   -   -   -   -
 * 2   -   -   -   -
 * 3   -   -   -   -
 * <p/>
 * The sample board shows the index values for the columns and rows
 * Remember that you access a 2D array by first specifying the row
 * and then the column: grid[row][column]
 */

import java.util.*;
import java.io.*;


/*
 * The board class is the main class that Game Manager uses to work 2048.
 * It consists of all the tools the game needs to run. It creates the board
 * and manipulates the game based on user inputs.
 */
public class Board {
  public final int NUM_START_TILES = 2;
  public final int TWO_PROBABILITY = 90;
  public final int GRID_SIZE;
  public final int DEFAULT_SIZE = 4;
  
  private final Random random;
  private int[][] grid;
  private int score;
  
  
  //Constructs a fresh board with random tiles based on the parameters of
  //GameManager.java and checks whether or not a board can be made
  public Board(int boardSize, Random random) {
    
    //The random seed of 2015 is hard coded in because it is the seed used
    //in the PSA3Tester.java 
    this.random = random; 
    
    //Checks to see if GameManager passed in a valid board size
    if(boardSize >= 2)
    {
      GRID_SIZE = boardSize; 
    }
    //If the boardSize is not valid, assign a default size of 4 as the board
    //size and notify the user
    else
    {
      System.out.println("The given board size is not valid, a default board"
                           + " of size 4 will be created instead.");
      GRID_SIZE = DEFAULT_SIZE;               
    }      
    
    //Assigns the 2D array its size based on GRID_SIZE
    grid = new int[GRID_SIZE][GRID_SIZE];
    //Assign the score to 0 
    score = 0;
    
    //For a completely new board, generate random tiles based on 
    //NUM_START_TILES (in this case 2) using the addRandomTile method
    for(int i = 0; i < NUM_START_TILES; i++)
    {
      addRandomTile();
    }
    
  }
  
  
  //Construct a board based off of an input file
  //The input file needs to be scanned and values copied into a playable
  //game board
  public Board(String inputBoard, Random random) throws IOException {
    
    //Creates a file that will be scanned based on the string of the 
    //inputboard
    File gameFile = new File(inputBoard);
    
    //scanner to read values of the grid size, score, and board tiles
    Scanner gameFileScanner = new Scanner(gameFile);
    
    //Read in the boardsize first
    int boardSize = gameFileScanner.nextInt();
    
    //Double checks to see if it is a valid boardsize
    //if valid assigns the board size to GRID_SIZE
    if(boardSize >= 2)
    {
      GRID_SIZE = boardSize; 
    }
    else
    {
      System.out.print("The given board size is not valid, a default board"
                         + " of size 4 will be created instead.");
      GRID_SIZE = DEFAULT_SIZE;               
    }    
    
    //Assigns the 2D array its size based on GRID_SIZE
    grid = new int[GRID_SIZE][GRID_SIZE];
    //The next read value is the score
    score = gameFileScanner.nextInt();
    
    //Loop through the remainder values and assign it row by column
    for(int row = 0; row < GRID_SIZE; row++)
    {
      for(int column = 0; column < GRID_SIZE; column++)
      {
        grid[row][column] = gameFileScanner.nextInt();
      }
    }
    
    //Generate a random seed, in this case, 2015 is the one used to test
    this.random = new Random(2015); 
    
  }
  
  /* 
   * Name: saveBoard
   * Purpose: The purpose of this method is to save the game board 
   * when the user requests for it or the game is over. 
   * The saved board will be saved to an input file.
   * Parameters: String outputBoard - the name of the file that will be created
   * Return: void
   */
  public void saveBoard(String outputBoard) throws IOException {
    
    //Create the the file that will be used to save the values based on the
    //parameterr
    File outputFile = new File(outputBoard);
    //Create a PrintWriter to write to the new file
    PrintWriter writer = new PrintWriter(outputFile);
    
    //Print the first two lines which will be GRID_SIZE and the score
    writer.println(GRID_SIZE);
    writer.println(getScore());
    
    //Loop through the 2D array printing by row and column 
    for(int row = 0; row < GRID_SIZE; row++)
    {
      for(int column = 0; column < GRID_SIZE; column++)
      {
        writer.print(grid[row][column]+ " ");
      }
      //Moves onto next line after each row is complete to keep format correct
      writer.println();
    }
    
    //Closes and saves the new file
    writer.close();
    
  }
  
  /* 
   * Name: addRandomTile
   * Purpose: The purpose of this method is to generate a new tile
   * for the game. The method checks to see whether if a tile is empty or not. 
   * Generates which tile it wants to fill in and whether a value 
   * of 2 or 4 should be inputed.
   * Parameters: none
   * Return: void
   */
  public void addRandomTile() {
    
    //Sets a counter for the empty tiles
    int count = 0;
    
    //Loops though the grid and counts the number of empty tiles
    for(int row = 0; row < GRID_SIZE; row++)
    {
      for(int column = 0; column < GRID_SIZE; column++)
      {
        //if the tile is empty add to the counter
        if(grid[row][column] == 0)
        {
          count++;
        }
      }
    }
    
    //if count is 0 then return to the calling method
    if(count == 0)
    {
      return;
    }
    
    //Makes a second counter, but for the index instead
    int indexCount = 0;
    
    //generates which tile will be used to assign a value to
    int location = random.nextInt(count);
    
    //Generates the value used to compare to TWO_PROBABILITY to choose
    //whether the new tile is a 2 or a 4
    int value = random.nextInt(100);
    
    //Loops through the grid once again
    for(int row = 0; row < GRID_SIZE; row++)
    {
      for(int column = 0; column < GRID_SIZE; column++)
      {
        //If it is an empty tile add to the indexCounter
        if(grid[row][column] == 0)
        {
          indexCount++;
          
          //Checks to see if location is the correct index, because location 
          //starts at 0 and indexCount at 1, there is a need to put 
          //indexCount - 1. If both are the same, then it is the right location
          //to add the tile
          if(location == indexCount - 1)
          {
            //Compares the previously generated value to TWO_PROBABLITY
            //Determines whether a 2 or 4 is generated
            if(value < TWO_PROBABILITY)
            {
              grid[row][column] = 2;
            }
            else
            {
              grid[row][column] = 4;
            }
          }
        }
      }
    }
  }
  
  /* 
   * Name: rotate
   * Purpose: The purpose of this method is to rotate the grid either
   * clockwise or counterclockwise. This is determined by the parameter, 
   * rotateClockwise. If true, rotate clockwise, if false, rotate counter 
   * clockwise.
   * Parameters: boolean rotateClockwise
   * Return: void
   */
  public void rotate(boolean rotateClockwise) {
    
    //if true rotate clockwise
    if(rotateClockwise == true)
    {
      //Creates a temporary 2D array with the same size as the original
      int[][] clockwiseArray = new int[GRID_SIZE][GRID_SIZE];
      
      //Loops through the row and columns
      for(int row = 0; row < GRID_SIZE; row++)
      {
        for(int column = 0; column < GRID_SIZE; column++)
        {
          //rotating clockwise means the row will become the column position
          //and the column will move towards the row position
          clockwiseArray[row][column] = grid[GRID_SIZE - column - 1][row];
        }
      }
      //Reassign the grid as the new clockwiseArray
      grid = clockwiseArray;
    }
    
    //If false, counterclockwise array, it operates the same as clockwise
    //except for when the values are switched
    else
    {
      int[][] counterClockArray = new int[GRID_SIZE][GRID_SIZE];
      
      for(int row = 0; row < GRID_SIZE; row++)
      {
        for(int column = 0; column < GRID_SIZE; column++)
        {
          //The values are switched, row is changed to column and column to row
          //in the opposite direction as clockwise
          counterClockArray[row][column] = grid[column][GRID_SIZE - row - 1];
        }
      }
      grid = counterClockArray;
    }
    
  }
  
  //Complete this method ONLY if you want to attempt at getting the extra credit
  //Returns true if the file to be read is in the correct format, else return
  //false
  public static boolean isInputFileCorrectFormat(String inputFile) {
    //The try and catch block are used to handle any exceptions
    //Do not worry about the details, just write all your conditions inside the
    //try block
    try {
      //write your code to check for all conditions and return
      //true if it satisfies
      //all conditions else return false
      return true;
    } catch (Exception e) {
      return false;
    }
  }
  
  
  /* 
   * Name: move
   * Purpose: The purpose of this method is to check if a move can be made,
   * and if it, execute the move by calling on the method dependent on the
   * given direction. Returns false if no moves are possible.
   * Parameters: Direction direction
   * Return: boolean
   */
  public boolean move(Direction direction) {
    
    //Creates a direction for the passed in parameter
    Direction moveDirection = direction;
    //Creates the boolean that will be returned if a move is possible or not
    boolean moveSuccess = false;
    
    //Calls canMove method with the direction parameter, if canMove returns 
    //true begin to operate the move methods, if not then a move can not be
    //executed
    if(canMove(moveDirection) == true)
    {
      //The following if statements checks for which direction to move
      //Calls the move method based on direction and returns the boolean
      if(moveDirection == Direction.UP)
      {
        moveSuccess = moveUp();
      }
      
      else if(moveDirection == Direction.DOWN)
      {
        moveSuccess = moveDown();
      }
      
      else if(moveDirection == Direction.LEFT)
      {
        moveSuccess = moveLeft();
      }
      
      else if(moveDirection == Direction.RIGHT)
      {
        moveSuccess = moveRight();
      }
      //return to the calling method with a value of true
      return moveSuccess;
    }
    
    //If a move can not be made in the inputed direction, tell the user 
    //it can not be moved and to input a new direction
    //return false
    System.out.println("Cannot move in the given direction."
                         + " Please enter another direction.");
    return moveSuccess;
  }
  
  //Helper method for the move method, operates for when the user
  //inputs the up direction
  private boolean moveUp(){
    
    //Because the direction is moving vertically and up, columns of the 
    //2D array will be the basis of the for loop
    for(int column = 0; column < GRID_SIZE; column++)
    {
      //Creates an arraylist to manipulate each column
      ArrayList<Integer> tempArray = new ArrayList<Integer>();
      //When two tiles combine they are doubled using this multiplier
      int tileMultiplier = 2;
      //Holds the value of the tile that gets combined
      int tileValue = 0;
      
      //Fills the arraylist of the first column with all the rows 
      //excludes zeroes as they are not needed in manipulation
      for(int row = 0; row < GRID_SIZE; row++)
      {
        if(grid[row][column] != 0)
        {
          tempArray.add(grid[row][column]);
        }
      }
      
      //Check to see tiles can be combined, if possible, combine the tiles
      //and set the next tile to be zero. 
      for(int i = 0; i < (tempArray.size() - 1); i++)
      {
        if(tempArray.get(i).equals(tempArray.get(i + 1)))
        {
          tileValue = (tileMultiplier * tempArray.get(i + 1));
          tempArray.set(i, tileValue);
          //When tiles merge, add to the score
          score = score + tileValue;
          tempArray.set(i + 1, 0);
        }
      }
      
      //Remove zeroes that are created from combining tiles that may be 
      //in between numbers
      for(int j = 0; j < tempArray.size(); j++)
      {
        if(tempArray.get(j) == 0)
        {
          tempArray.remove(j);
        }
      }
      
      //The number of zeroes that need to be added into the arraylist after
      //the shift has been finished
      int addZero = (GRID_SIZE - tempArray.size());
      
      //Depending on whether it is shifted up or down, add zeroes to the 
      //beginning or the end of the arraylist
      for(int k = 0; k < addZero; k++)
      {
        tempArray.add(0);
      }
      
      //Reassign the board column of the game with the new arraylist values
      for(int row = 0; row < GRID_SIZE; row++)
      {
        grid[row][column] = tempArray.get(row);
      }
      
    }
    
    return true;
    
  }
  
  //Helper method for the move method, operates for when the user
  //inputs the down direction. Mostly operates the same as the moveUp method
  private boolean moveDown(){
    
    for(int column = 0; column < GRID_SIZE; column++)
    {
      ArrayList<Integer> tempArray = new ArrayList<Integer>();
      int tileMultiplier = 2;
      int tileValue = 0;
      
      for(int row = 0; row < GRID_SIZE; row++)
      {
        if(grid[row][column] != 0)
        {
          tempArray.add(grid[row][column]);
        }
      }
      
      //Operates similar to the moveUp, but iterates in the opposite direction
      for(int i = (tempArray.size() - 1); i > 0; i--)
      {
        if(tempArray.get(i).equals(tempArray.get(i - 1)))
        {
          tileValue = (tileMultiplier * tempArray.get(i - 1));
          tempArray.set(i, tileValue);
          score = score + tileValue;
          tempArray.set(i - 1, 0);
        }
      }
      
      for(int j = 0; j < tempArray.size(); j++)
      {
        if(tempArray.get(j) == 0)
        {
          tempArray.remove(j);
        }
      }
      
      int addZero = (GRID_SIZE - tempArray.size());
      
      //Because it is shifting in the opposite direction as up, add zeroes
      //to the beginning of the arraylist rather than the end
      for(int k = 0; k < addZero; k++)
      {
        tempArray.add(0,0);
      }
      
      for(int row = 0; row < GRID_SIZE; row++)
      {
        grid[row][column] = tempArray.get(row);
      }
      
    }  
    
    return true;
    
  }
  
  //Helper method for the move method, operates for when the user
  //inputs the left direction. 
  //Mostly operates the same as the moveUp method
  private boolean moveLeft(){
    
    //The main for loop operates with rows as it is moving left
    for(int row = 0; row < GRID_SIZE; row++)
    {
      ArrayList<Integer> tempArray = new ArrayList<Integer>();
      int tileMultiplier = 2;
      int tileValue = 0;
      
      for(int column = 0; column < GRID_SIZE; column++)
      {
        if(grid[row][column] != 0)
        {
          tempArray.add(grid[row][column]);
        }
      }
      
      for(int i = 0; i < (tempArray.size() - 1); i++)
      {
        if(tempArray.get(i).equals(tempArray.get(i + 1)))
        {
          tileValue = (tileMultiplier * tempArray.get(i + 1));
          tempArray.set(i, tileValue);
          score = score + tileValue;
          tempArray.set(i + 1, 0);
        }
      }
      
      for(int j = 0; j < tempArray.size(); j++)
      {
        if(tempArray.get(j) == 0)
        {
          tempArray.remove(j);
        }
      }
      
      int addZero = (GRID_SIZE - tempArray.size());
      
      for(int k = 0; k < addZero; k++)
      {
        tempArray.add(0);
      }
      
      for(int column = 0; column < GRID_SIZE; column++)
      {
        grid[row][column] = tempArray.get(column);
      }
      
    } 
    
    return true;
    
  }
  
  //Helper method for the move method, operates for when the user
  //inputs the right direction. 
  //Mostly operates the same as the moveLeft method
  private boolean moveRight(){
    
    for(int row = 0; row < GRID_SIZE; row++)
    {
      ArrayList<Integer> tempArray = new ArrayList<Integer>();
      int tileMultiplier = 2;
      int tileValue = 0;
      
      for(int column = 0; column < GRID_SIZE; column++)
      {
        if(grid[row][column] != 0)
        {
          tempArray.add(grid[row][column]);
        }
      }
      
      for(int i = (tempArray.size() - 1); i > 0; i--)
      {
        if(tempArray.get(i).equals(tempArray.get(i - 1)))
        {
          tileValue = (tileMultiplier * tempArray.get(i-1));
          tempArray.set(i, tileValue);
          score = score + tileValue;
          tempArray.set(i-1, 0);
        }
      }
      
      for(int j = 0; j < tempArray.size(); j++)
      {
        if(tempArray.get(j) == 0)
        {
          tempArray.remove(j);
        }
      }
      
      int addZero = (GRID_SIZE - tempArray.size());
      
      for(int k = 0; k < addZero; k++)
      {
        tempArray.add(0,0);
      }
      
      for(int column = 0; column < GRID_SIZE; column++)
      {
        grid[row][column] = tempArray.get(column);
      }
      
    } 
    
    return true;
    
  }
  
  
  /* 
   * Name: isGameOver
   * Purpose: The purpose of this method is to check if the game is over or 
   * not. This method checks if a move can be made in any direction after
   * each move is made.
   * Parameters: none
   * Return: boolean
   */
  public boolean isGameOver() {
    
    //If all canMove directions return false, return true and tell the user
    //it is game over and that there are no more moves
    if(canMoveUp() == false && canMoveDown() == false && 
       canMoveLeft() == false && canMoveRight() == false)
    {
      System.out.println("GAME OVER!");
      System.out.println("You are out of moves!");
      return true;
    }
    
    return false;
  }
  
  
  /* 
   * Name: canMove
   * Purpose: The purpose of this method is to check if a move can be made.
   * If it can be made, return true to move method.
   * Parameters: Direction direction
   * Return: boolean
   */
  public boolean canMove(Direction direction) {
    
    //Creates a direction based on parameter 
    Direction checkDirection = direction;
    //Creates a boolean that is returned to move method
    boolean moveCheck = false;
    
    //Check for the inputted direction for the following if statements
    //each direction has a helper method associated with it and 
    //returns true or false after checking if a move can be made
    if(checkDirection == Direction.UP)
    {
      moveCheck = canMoveUp();
    }
    
    else if(checkDirection == Direction.DOWN)
    {
      moveCheck = canMoveDown();
    }
    
    else if(checkDirection == Direction.LEFT)
    {
      moveCheck = canMoveLeft();
    }
    
    else if(checkDirection == Direction.RIGHT)
    {
      moveCheck = canMoveRight();
    }
    
    return moveCheck;
  }
  
  
  //A helper method for canMove, operates for the up direction
  private boolean canMoveUp() {
    
    //Because it shifts up, ignores the last row
    for(int row = 0; row < (GRID_SIZE - 1); row++)
    {
      for(int column = 0; column < GRID_SIZE; column++)
      {
        //If the above tile is zero and the below is a value other than zero
        //return true as a move can be made
        if(grid[row][column] == 0 && grid[row + 1][column] != 0)
        {
          return true;
        }
        
        //If two adjacent tiles are the same value and neither are zero
        //return true as a move can be made
        if(grid[row][column] != 0 && grid[row + 1][column] != 0
             && grid[row][column] == grid[row + 1][column])
        {
          return true;
        }
      }
    }
    //if both checks fail, return false as a move can not be made
    return false;
  }
  
  
  //A helper method for canMove, operates for the down direction and is mostly
  //the same as the up direction canMove check
  private boolean canMoveDown() {
    
    //Shifting down means iterating beginning from the second row
    for(int row = 1; row < GRID_SIZE; row++)
    {
      for(int column = 0; column < GRID_SIZE; column++)
      {
        if(grid[row][column] == 0 && grid[row - 1][column] != 0)
        {
          return true;
        }
        
        if(grid[row][column] != 0 && grid[row - 1][column] != 0 
             && grid[row][column] == grid[row - 1][column])
        {
          return true;
        }
      }
    }
    return false;
  }
  
  
  //A helper method for canMove, operates for the left direction and is mostly
  //the same as the up direction canMove check
  private boolean canMoveLeft() {
    
    for(int row = 0; row < GRID_SIZE; row++)
    {
      //Shifting left is dependent on columns and
      //shifts without the last column
      for(int column = 0; column < (GRID_SIZE - 1); column++)
      {
        if(grid[row][column] == 0 && grid[row][column + 1] != 0)
        {
          return true;
        }
        
        if(grid[row][column] != 0 && grid[row][column + 1] != 0
             && grid[row][column] == grid[row][column + 1])
        {
          return true;
        }
      }
    }
    return false;
  }
  
  
  //A helper method for canMove, operates for the right direction and is mostly
  //the same as the up direction canMove check
  private boolean canMoveRight() {
    
    for(int row = 0; row < GRID_SIZE; row++)
    {
      //For shifting right, the first column is not used in shifting
      for(int column = 1; column < GRID_SIZE; column++)
      {
        if(grid[row][column] == 0 && grid[row][column - 1] != 0)
        {
          return true;
        }
        
        if(grid[row][column] != 0 && grid[row][column - 1] != 0
             && grid[row][column] == grid[row][column - 1])
        {
          return true;
        }
      }
    }
    return false;
  }
  
  
  // Return the reference to the 2048 Grid
  public int[][] getGrid() {
    return grid;
  }
  
  // Return the score
  public int getScore() {
    return score;
  }
  
  @Override
  public String toString() {
    StringBuilder outputString = new StringBuilder();
    outputString.append(String.format("Score: %d\n", score));
    for (int row = 0; row < GRID_SIZE; row++) {
      for (int column = 0; column < GRID_SIZE; column++)
        outputString.append(grid[row][column] == 0 ? "    -" :
                              String.format("%5d", grid[row][column]));
      
      outputString.append("\n");
    }
    return outputString.toString();
  }
}
