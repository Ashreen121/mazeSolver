import maze.Maze;
import maze.Tile;
import maze.InvalidMazeException;
import maze.MultipleEntranceException;
import maze.MultipleExitException;
import maze.NoEntranceException;
import maze.NoExitException;
import maze.RaggedMazeException;


import java.io.FileNotFoundException;
import java.io.IOException;

public class MazeDriver {
  public static void main(String args[]) {
    Maze maze1 = null;
    Maze maze2 = null;

    try {
      maze1 = Maze.fromTxt("../resources/mazes/maze1.txt");
    } catch (RaggedMazeException e) {
      System.out.println("Maze file contianed ragged maze. Ragged maze not allowed");
    } catch (MultipleEntranceException e) {
      System.out.println("Multiple entrances were found in the maze file");
    } catch (MultipleExitException e) {
      System.out.println("Multiple exits were found in the maze file");
    } catch (NoEntranceException e) {
      System.out.println("No entrance was found in the maze file");
    } catch (NoExitException e) {
      System.out.println("No exit was found in the maze file");
    } catch (InvalidMazeException e) {
      System.out.println("Invalid maze");
    }

    System.out.println(maze1.toString());

    try {
      maze2 = Maze.fromTxt("../resources/mazes/maze2.txt");
    } catch (RaggedMazeException e) {
      System.out.println("Maze file contianed ragged maze. Ragged maze not allowed");
    } catch (MultipleEntranceException e) {
      System.out.println("Multiple entrances were found in the maze file");
    } catch (MultipleExitException e) {
      System.out.println("Multiple exits were found in the maze file");
    } catch (NoEntranceException e) {
      System.out.println("No entrance was found in the maze file");
    } catch (NoExitException e) {
      System.out.println("No exit was found in the maze file");
    } catch (InvalidMazeException e) {
      System.out.println("Invalid maze");
    }

    System.out.println(maze2.toString());


  }
}
