package g2048;

/* 
 *  Name: Danny Dang, A12780186
 *  Login: cs8bwamx
 *  Date: March 2, 2016
 *  File: Gui2048.java
 *  Sources of Help: Java docs, discussion sections, tutors
 */

/** Gui2048.java */
/** PSA8 Release */

import javafx.application.*;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import java.util.*;
import java.io.*;


/*
 * Name:    Guia2048
 * Purpose: Gui2048 is the game 2048. It uses methods from the board class 
 * in conjunction with javafx to generate a board to play the game. The class
 * first generates a default board with empty tiles, then it reads the grid
 * by using board class's getGrid getter. Using this information, the class
 * fills the board with the starting two tiles and numbers. Each tile and 
 * number gets its own specific value and graphic. This class also uses
 * EventHandler to manage the key presses. The class can take in five key 
 * presses: the four directional keys to move and S to save the game. 
 * After each valid key press, the game will update the board by calling on
 * board class methods. Each tile will be updated according. When game over
 * occurs, an overlay displaying Game Over! will appear and the users can no
 * longer input any keys. The program will exit when the user closes it.
 */
public class Gui2048 extends Application
{
  private String outputBoard; // The filename for where to save the Board
  private Board board; // The 2048 Game Board
  
  private static final int TILE_WIDTH = 106;
  
  private static final int TEXT_SIZE_LOW = 55; // Low value tiles (2,4,8,etc)
  private static final int TEXT_SIZE_MID = 45; // Mid value tiles 
  //(128, 256, 512)
  private static final int TEXT_SIZE_HIGH = 35; // High value tiles 
  //(1024, 2048, Higher)
  
  //Fill colors for each of the Tile values
  private static final Color COLOR_EMPTY = Color.rgb(238, 224, 218, 0.35);
  private static final Color COLOR_2 = Color.rgb(255, 225, 255);
  private static final Color COLOR_4 = Color.rgb(205, 181, 205);
  private static final Color COLOR_8 = Color.rgb(139, 123, 139);
  private static final Color COLOR_16 = Color.rgb(255, 187, 255);
  private static final Color COLOR_32 = Color.rgb(205, 150, 205);
  private static final Color COLOR_64 = Color.rgb(139, 102, 139);
  private static final Color COLOR_128 = Color.rgb(224, 102, 255);
  private static final Color COLOR_256 = Color.rgb(180, 82, 205);
  private static final Color COLOR_512 = Color.rgb(122, 55, 139);
  private static final Color COLOR_1024 = Color.rgb(104, 34, 139);
  private static final Color COLOR_2048 = Color.rgb(75, 0, 130);
  private static final Color COLOR_OTHER = Color.BLACK;
  private static final Color COLOR_GAME_OVER = Color.rgb(238, 228, 218, 0.73);
  
  private static final Color COLOR_VALUE_LIGHT = Color.rgb(249, 246, 242); 
  // For tiles >= 8
  
  private static final Color COLOR_VALUE_DARK = Color.rgb(119, 110, 101); 
  // For tiles < 8
  
  private GridPane pane;
  
  /** Add your own Instance Variables here */
  
  //2D arrays for rectangle and text as the game is in grid form
  //boardSize and sceneSize to hold values based on the board size
  //boolean gameOver to stop key inputs after game over 
  private Rectangle[][] rectangleArray;
  private Text[][] textArray;
  private Text gameName;
  private Text gameScore;
  private int boardSize;
  private int sceneSize;
  private boolean gameOver;
  
  //StackPane allows for multiple child nodes to stack
  //used for to add the main game graphics and the game over overlay
  private StackPane gameOverlay;
  
  /* 
   * Name: start
   * Purpose: This method is the main play method of the game. This method
   * calls on the rest of the class's method to create the game screen along
   * with the game title and game score. The base board is generated with 
   * the default starter tiles and an eventhandler is used to take in user
   * inputs.
   * Parameters: Stage primaryStage
   * Return: void
   */
  @Override
  public void start(Stage primaryStage)
  {
    // Process Arguments and Initialize the Game Board
    processArgs(getParameters().getRaw().toArray(new String[0]));
    
    // Create the pane that will hold all of the visual objects
    pane = new GridPane();
    pane.setAlignment(Pos.CENTER);
    pane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
    pane.setStyle("-fx-background-color: rgb(187, 173, 160)");
    // Set the spacing between the Tiles
    pane.setHgap(15); 
    pane.setVgap(15);
    
    /** Add your Code for the GUI Here */
    
    //Initiates stack pane and adds the first pane
    gameOverlay = new StackPane();
    gameOverlay.getChildren().add(pane);
    
    //Sets gameOver to false
    gameOver = false;
    //Uses board.GRID_SIZE to scale the rectangle, text arrays, and
    //scene size correctly
    boardSize = board.GRID_SIZE;
    rectangleArray = new Rectangle[boardSize][boardSize];
    textArray = new Text[boardSize][boardSize];
    sceneSize = 150 * boardSize;
    
    //Creates a new scene with the correct parameters and shows the 
    //primary stage of the game with its name
    Scene scene = new Scene(gameOverlay, sceneSize, sceneSize);
    primaryStage.setTitle("Gui2048");
    primaryStage.setScene(scene);
    primaryStage.show();
    
    //Calls method to display 2048 properly in the game
    displayGameName();
    
    //Calls method to display the game score properly in the game
    displayScore();
    
    //Creates an empty board along with the blank tiles and text that
    //are later modified
    makeBoard();
    
    //Updates the existing board with the initial two starting tiles
    updateBoard();
    
    //Creates a EventHandler that will take in the user's keypresses to 
    //play the game
    scene.setOnKeyPressed(new myKeyHandler());
    
  }
  
  
  /** Add your own Instance Methods Here */
  
   /* 
   * Name: displayGameName
   * Purpose: This method creates the game name and adds it to the game window.
   * Parameters: none
   * Return: void
   */
  private void displayGameName() {
    
    //Initializes the gameName, sets the name of the game and font
    gameName = new Text();
    gameName.setText("2048");
    gameName.setFont(Font.font("Helvetica", FontWeight.BOLD, 30));
    gameName.setFill(Color.WHITE);
    
    //Adds it to pane and assigns coordinates, column span, and position
    pane.add(gameName, 0, 0);  
    pane.setColumnSpan(gameName, 2);
    GridPane.setHalignment(gameName, HPos.CENTER);
    
  }
  
   /* 
   * Name: displayScore
   * Purpose: This method creates the score text and adds it to the
   * game window. This text will later be edited by other methods.
   * Parameters: none
   * Return: void
   */
  private void displayScore() {
    
    //Creates the Score text layout, font, and color
    gameScore = new Text();
    gameScore.setText("Score: " + board.getScore());
    gameScore.setFont(Font.font("Helvetica", FontWeight.BOLD, 30));
    gameScore.setFill(Color.WHITE);
    
    //Adds it to the pane at the specific coordinates 
    pane.add(gameScore, 2, 0);
    pane.setColumnSpan(gameScore, 2);
    GridPane.setHalignment(gameScore, HPos.CENTER);
    
  }
  
   /* 
   * Name: updateScore
   * Purpose: This method is called after each succesful user input and 
   * it updates the score by calling the getScore method from Board.java.
   * Parameters: none
   * Return: void
   */
  private void updateScore() {
    
    gameScore.setText("Score: " + board.getScore());
    
  }
  
   /* 
   * Name: makeBoard
   * Purpose: This method creates the initial default board. It adds the 
   * empty tiles and empty text to the game grid that will be later updated
   * once the user starts to play the game. 
   * Parameters: none
   * Return: void
   */
  private void makeBoard() {
    
    //Nested loops are used to fill in the 2D array
    for(int row = 0; row < rectangleArray.length; row++)
    {
      for(int col = 0; col < rectangleArray.length; col++)
      {
        //Creates a new rectangle for each array element
        Rectangle tile = new Rectangle();
        
        //Uses the same width for all tiles and the empty color for empty tiles
        tile.setWidth(TILE_WIDTH);
        tile.setHeight(TILE_WIDTH);
        tile.setFill(COLOR_EMPTY);
        
        //Adds the rectangle to the array
        rectangleArray[row][col] = tile;
        
        //Because the game title and score is located on the top row,
        //the grid begins on row 1, therefore the rectangle is added
        //one row more
        pane.add(tile, col, row + 1);
        
        //Creates tile text in the same process as rectangles
        Text tileText = new Text();
        
        //Sets the text to be blank with default settings
        tileText.setText("");
        tileText.setFont(Font.font("Helvetica", FontWeight.BOLD, 30));
        tileText.setFill(Color.WHITE);
        
        //Adds text object to the array 
        textArray[row][col] = tileText;
        
        //Adds the text to the pane and allign to the center of the grid
        pane.add(tileText, col, row + 1);
        GridPane.setHalignment(tileText, HPos.CENTER);
        
      }
    }
  }
  
   /* 
   * Name: updateBoard
   * Purpose: This method updates the game board/GUI after every successful
   * user input. It calls on the getGrid method from board class and updates
   * each tile one by one. 
   * Parameters: none
   * Return: void
   */
  private void updateBoard() {
    
    //Initializes tileNumber that later holds the value of each tile
    int tileNumber = 0;
    
    //Uses a nested loop to modify the 2D rectangle and text arrays
    for(int row = 0; row < board.getGrid().length; row++)
    {
      for(int col = 0; col < board.getGrid().length; col++)
      {
        //Gets the tileNumber from the grid
        tileNumber = board.getGrid()[row][col];
        
        //Calls the updateTileColor and TileText methods
        updateTileColor(row, col, tileNumber);
        updateTileText(row, col, tileNumber);
        
      }
    }
    
  }
  
   /* 
   * Name: updateTileColor
   * Purpose: This method modifies the existing color of the tiles on the 
   * board based on the value given from the getGrid method. Using the row
   * and columns of rectangle array, it properly changes each color
   * accordingly. Each tile number has its own specific color that it uses.
   * Parameters: int row, int col, int tileNumber
   * Return: void
   */
  private void updateTileColor(int row, int col, int tileNumber) {
    
    //The entire method is an if-else using tileNumber as its condition
    //and uses colors established as global variables
    if(tileNumber == 0)
    {
      rectangleArray[row][col].setFill(COLOR_EMPTY);
    }
    
    else if(tileNumber == 2)
    {
      rectangleArray[row][col].setFill(COLOR_2);
    }
    
    else if(tileNumber == 4)
    {
      rectangleArray[row][col].setFill(COLOR_4);
    }
    
    else if(tileNumber == 8)
    {
      rectangleArray[row][col].setFill(COLOR_8);
    }
    
    else if(tileNumber == 16)
    {
      rectangleArray[row][col].setFill(COLOR_16);
    }
    
    else if(tileNumber == 32)
    {
      rectangleArray[row][col].setFill(COLOR_32);
    }
    
    else if(tileNumber == 64)
    {
      rectangleArray[row][col].setFill(COLOR_64);
    }
    
    else if(tileNumber == 128)
    {
      rectangleArray[row][col].setFill(COLOR_128);
    }
    
    else if(tileNumber == 256)
    {
      rectangleArray[row][col].setFill(COLOR_256);
    }
    
    else if(tileNumber == 512)
    {
      rectangleArray[row][col].setFill(COLOR_512);
    }
    
    else if(tileNumber == 1024)
    {
      rectangleArray[row][col].setFill(COLOR_1024);
    }
    
    else if(tileNumber == 2048)
    {
      rectangleArray[row][col].setFill(COLOR_2048);
    }
    
    else
    {
      rectangleArray[row][col].setFill(COLOR_OTHER);
    }
    
  }
  
   /* 
   * Name: updateTileText
   * Purpose: This method updates the existing tile text based on the 
   * tileNumber after each turn. It uses the 2D text array to fill in the
   * numbers according. Each set of numbers has its own font conditions
   * and color
   * Parameters: int row, int col, int tileNumber
   * Return: void
   */
  private void updateTileText(int row, int col, int tileNumber) {
    
    //The first set of if-else statements sets the text and color based on the 
    //value of the tileNumber
    if(tileNumber == 0)
    {
      //If its an empty tile, set the text to be blank
      textArray[row][col].setText("");
    }
    
    else if(tileNumber == 2 || tileNumber == 4)
    { 
      //If 2 or 4, convert the tileNumber to string and display it
      textArray[row][col].setText(Integer.toString(tileNumber));
      //Sets the text fill color to a dark hue
      textArray[row][col].setFill(COLOR_VALUE_DARK);
    }
    
    else
    {
      //For the remainder elements, set the text fill color to a lighter hue
      textArray[row][col].setText(Integer.toString(tileNumber));
      textArray[row][col].setFill(COLOR_VALUE_LIGHT);
    }
    
    //The second if-else statements readjusts the font for each number
    //For numbers 2-64, uses a larger font size based on a global variable
    if(tileNumber == 2 || tileNumber == 4 || tileNumber == 8 ||
       tileNumber == 16 || tileNumber == 32 || tileNumber == 64)
    {
      textArray[row][col].setFont(Font.font("Helvetica", 
                                            FontWeight.BOLD, TEXT_SIZE_LOW)); 
    }
    
    //Numbers 128, 256, and 512 uses a medium font size
    else if(tileNumber == 128 || tileNumber == 256 || tileNumber == 512)
    {
      textArray[row][col].setFont(Font.font("Helvetica", 
                                            FontWeight.BOLD, TEXT_SIZE_MID));
    }
    
    //Numbers 1024 or greater uses a small text size
    else
    {
      textArray[row][col].setFont(Font.font("Helvetica", 
                                            FontWeight.BOLD, TEXT_SIZE_HIGH));
    }
    
  }
  
  /* 
   * Name: myKeyHandler
   * Purpose: The purpose of this class is to function as an EventHandler for
   * the 2048 game. It takes in valid key presses from the users and operates
   * the game. It calls on the according functions for each of the key presses.
   */
  private class myKeyHandler implements EventHandler<KeyEvent>
  {
    
    /* 
     * Name: handle
     * Purpose: This method takes in each key presses and runs the according
     * steps based on which key is pressed. It does not run if the user
     * reaches a gameOver
     * inputs.
     * Parameters: KeyEvent e
     * Return: void
     */
    @Override
    public void handle(KeyEvent e)
    {
      //Game over boolean that checks whether or not the following statements
      //will be ran or not. This will prevent the following code to be ran
      //if the user loses the game. 
      if(gameOver == false)
      {
        //For when the user inputs the up key
        if(e.getCode() == KeyCode.UP)
        {
          //Calls board.move method with the up direction parameter
          if(board.move(Direction.UP) == true)
          {
            //If true the game will print moving up in console and 
            //Adds a new tile to the board, updates the board, updates
            //the score, and check for game over
            System.out.println("Moving Up");
            board.addRandomTile();
            updateBoard();
            updateScore();
            checkGameOver();
          }
          
        }
        
        //For when the user inputs the down key
        else if(e.getCode() == KeyCode.DOWN)
        {
          if(board.move(Direction.DOWN) == true)
          {
            System.out.println("Moving Down");
            board.addRandomTile();
            updateBoard();
            updateScore();
            checkGameOver();
            
          }
        }
        
        //For when the user inputs the left key
        else if(e.getCode() == KeyCode.LEFT)
        {
          if(board.move(Direction.LEFT) == true)
          {
            System.out.println("Moving Left");
            board.addRandomTile();
            updateBoard();
            updateScore();
            checkGameOver();
          }
        }
        
        //For when the user inputs the right key
        else if(e.getCode() == KeyCode.RIGHT)
        {
          if(board.move(Direction.RIGHT) == true)
          {
            System.out.println("Moving Roght");
            board.addRandomTile();
            updateBoard();
            updateScore();
            checkGameOver();
          }
        }
        
        //For when the user inputs the S key
        //This statement is when the user wants to save the game board
        else if(e.getCode() == KeyCode.S)
        {
          //Uses an catch statement to properly save the game
          try 
          {
            board.saveBoard("Gui2048Save");
          } 
          catch (IOException i) 
          { 
            System.out.println("saveBoard threw an Exception");
          }
          
          //Prints to console after the game is saved
          System.out.println("Saving Board to Gui2048Save");
          
        }    
      }
    }
  }
  
   /* 
   * Name: checkGameOver
   * Purpose: This method is ran after each successful move to check if the 
   * user still has any left over moves. If there are available moves, this
   * method does nothing. If there are no more moves, then this method will
   * create and print a transparent game over screen for the user and stops
   * the game from acting from key presses. 
   * Parameters: none
   * Return: void
   */
  private void checkGameOver() {
    
    //Calls board.isGameOver() to check if the game is over or not
    if(board.isGameOver() == true)
    {
      //If true...Creates a new rectangle as the game over overlay
      Rectangle gameOverScreen = new Rectangle();
      
      //Sets the color and set the size to be the same as the game pane
      gameOverScreen.setWidth(sceneSize);
      gameOverScreen.setHeight(sceneSize);
      gameOverScreen.setFill(COLOR_GAME_OVER);
      
      //Add it to the stackpane
      gameOverlay.getChildren().add(gameOverScreen);
      
      //Aligns the grid to be at the center
      GridPane.setHalignment(gameOverScreen, HPos.CENTER);
      GridPane.setValignment(gameOverScreen, VPos.CENTER);
      
      //Creates the game over text that displays over the transparent
      //game over overlay
      Text gameOverText = new Text();
      
      gameOverText.setText("GAME OVER!");
      gameOverText.setFont(Font.font("Helvetica", FontWeight.BOLD, 70));
      gameOverText.setFill(Color.BLACK);
      
      gameOverlay.getChildren().add(gameOverText);
      
      GridPane.setHalignment(gameOverText, HPos.CENTER);
      GridPane.setValignment(gameOverText, VPos.CENTER);
      
      //Sets gameOver boolean to true so the keypresses will not run 
      //unnecessary code after the game is over
      gameOver = true;
      
    }
    
  }
  

  /** DO NOT EDIT BELOW */
  
  // The method used to process the command line arguments
  private void processArgs(String[] args)
  {
    String inputBoard = null;   // The filename for where to load the Board
    int boardSize = 0;          // The Size of the Board
    
    // Arguments must come in pairs
    if((args.length % 2) != 0)
    {
      printUsage();
      System.exit(-1);
    }
    
    // Process all the arguments 
    for(int i = 0; i < args.length; i += 2)
    {
      if(args[i].equals("-i"))
      {   // We are processing the argument that specifies
        // the input file to be used to set the board
        inputBoard = args[i + 1];
      }
      else if(args[i].equals("-o"))
      {   // We are processing the argument that specifies
        // the output file to be used to save the board
        outputBoard = args[i + 1];
      }
      else if(args[i].equals("-s"))
      {   // We are processing the argument that specifies
        // the size of the Board
        boardSize = Integer.parseInt(args[i + 1]);
      }
      else
      {   // Incorrect Argument 
        printUsage();
        System.exit(-1);
      }
    }
    
    // Set the default output file if none specified
    if(outputBoard == null)
      outputBoard = "2048.board";
    // Set the default Board size if none specified or less than 2
    if(boardSize < 2)
      boardSize = 4;
    
    // Initialize the Game Board
    try{
      if(inputBoard != null)
        board = new Board(inputBoard, new Random());
      else
        board = new Board(boardSize, new Random());
    }
    catch (Exception e)
    {
      System.out.println(e.getClass().getName() + 
                         " was thrown while creating a " +
                         "Board from file " + inputBoard);
      System.out.println("Either your Board(String, Random) " +
                         "Constructor is broken or the file isn't " +
                         "formated correctly");
      System.exit(-1);
    }
  }
  
  // Print the Usage Message 
  private static void printUsage()
  {
    System.out.println("Gui2048");
    System.out.println("Usage:  Gui2048 [-i|o file ...]");
    System.out.println();
    System.out.println("  Command line arguments come in pairs of the "+ 
                       "form: <command> <argument>");
    System.out.println();
    System.out.println("  -i [file]  -> Specifies a 2048 board that " + 
                       "should be loaded");
    System.out.println();
    System.out.println("  -o [file]  -> Specifies a file that should be " + 
                       "used to save the 2048 board");
    System.out.println("                If none specified then the " + 
                       "default \"2048.board\" file will be used");  
    System.out.println("  -s [size]  -> Specifies the size of the 2048" + 
                       "board if an input file hasn't been"); 
    System.out.println("                specified.  If both -s and -i" + 
                       "are used, then the size of the board"); 
    System.out.println("                will be determined by the input" +
                       " file. The default size is 4.");
  }
  
  
  
  public static void main(String[] args)
  {
	  
	  Application.launch(args);
  }
  
}
