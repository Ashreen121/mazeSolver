package maze;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
 


/**  Class to create Maze objects
*    @author Ashreen Kaur
*/
public class Maze implements Serializable{

  private Tile entrance;
  private Tile exit;
  private List<List<Tile>> tiles;



  private Maze() {
    tiles = new ArrayList<List<Tile>>();
  }

  /**  Reads in text file containing the maze structure
  *    @param mazeFile: the file containing the maze structure
  *    @throws java.io.FileNotFoundException if there no file is found
  *    @throws java.io.IOException if input format is invalid
  *    @throws maze.NoEntranceException if there is no entrance in maze
  *    @throws maze.NoExitException if there is no exit in maze
  *    @throws maze.RaggedMazeException
  *    @return Returns a Maze object with tiles attribute set to hold the maze structure
  */
  public static Maze fromTxt(String mazeFile) {
    String line;
    boolean entranceSet = false;
    boolean exitSet = false;
    List<Tile> tempList;
    Maze maze = new Maze();

    try (BufferedReader mazeReader = new BufferedReader(new FileReader(mazeFile))) {
      line = mazeReader.readLine();
      do {

        tempList = new ArrayList<Tile>();
        for (int i=0; i<line.length(); i++) {
          tempList.add(Tile.fromChar(line.charAt(i)));
        }

        maze.tiles.add(tempList);

        for (int i=0; i<tempList.size(); i++) {
          if (tempList.get(i).getType() == Tile.Type.ENTRANCE) {
            maze.setEntrance(tempList.get(i));
            entranceSet = true;

          } else if (tempList.get(i).getType() == Tile.Type.EXIT) {
            maze.setExit(tempList.get(i));
            exitSet = true;

          } else {
            continue;
          }
        }

        line = mazeReader.readLine();
      } while (line != null);

      if (entranceSet==false) {throw new NoEntranceException();}
      if (exitSet==false) {throw new NoExitException();}

      int setLength = maze.tiles.get(0).size();
      for (int i=0; i<maze.tiles.size(); i++) {
        if (maze.tiles.get(i).size() != setLength) {
          throw new RaggedMazeException();
        }
      }
    } catch (FileNotFoundException e) {
      System.out.println("File was not found");
    } catch (IOException e) {
      System.out.println("Something went wrong while reading the file");
    }

    return maze;
  }

  /**  Gets Tile adjacent to the specified tile in a given direction
  *    @param tile: specified tile to find an adjacent Tile
  *    @param direction: direction of adjacent Tile
  *    @return return the adjacent Tile
  */
  public Tile getAdjacentTile(Tile tile, Direction direction) {
    Coordinate tileLocation = this.getTileLocation(tile);
    int x = tileLocation.getX();
    int y = tileLocation.getY();
    if (direction == Direction.NORTH) {
      y = y + 1;
    } else if (direction == Direction.SOUTH) {
      y = y - 1;
    } else if (direction == Direction.EAST) {
      x = x + 1;
    } else if (direction == Direction.WEST) {
      x = x - 1;
    }
    Coordinate coordinate = new Coordinate(x, y);
    Tile adjacentTile = this.getTileAtLocation(coordinate);
    return adjacentTile;
  }

  /**  Gets Tile corresponding to the entrance of the maze
  *    @return Returns Tile corresponding to the entrance of the maze
  */
  public Tile getEntrance() {
    return entrance;

  }

  /**  Gets Tile corresponding to the exit of the maze
  *    @return Returns Tile corresponding to the exit of the maze
  */
  public Tile getExit() {
    return exit;
  }



  /**  Accesses the tile attribute of this class
  *    @return Returns a List object consisting of List objects holding objects of type Tile
  */
  public List<List<Tile>> getTiles() {
    return tiles;
  }


  private void setEntrance(Tile tile) {
    boolean inMaze = false;
    if (entrance != null) {
      throw new MultipleEntranceException();

    } else {
      for (int i=0; i<tiles.size(); i++) {
        if (tiles.get(i).contains(tile)) {
          inMaze = true;
        }
      }

      if (tile.getType() == Tile.Type.ENTRANCE && inMaze == true) {
        entrance = tile;
      }
    }
  }


  private void setExit(Tile tile) {
    boolean inMaze = false;
    if (exit != null) {
      throw new MultipleExitException();

    } else {
      for (int i=0; i<tiles.size(); i++) {
        if (tiles.get(i).contains(tile)) {
          inMaze = true;
        }
      }

      if (tile.getType() == Tile.Type.EXIT && inMaze == true) {
        exit = tile;
      }
    }
  }

  /**  Gets Tile at specified location
  *    @param coordinate: location to get Tile from
  *    @return Returns retrieved Tile object
  */
  public Tile getTileAtLocation(Coordinate coordinate) {
    int x = coordinate.getX();
    int y = coordinate.getY();
    y = (tiles.size()-1) - y;
    Tile tile = tiles.get(y).get(x);
    return tile;
  }

  /**  Gets location of specified Tile
  *    @param tile: Tile to retrieve location of
  *    @return Returns Coordinate of Tile object
  */
  public Coordinate getTileLocation(Tile tile) {
    Coordinate coordinate = null;


    for (int i=0; i<tiles.size(); i++) {
      int y = (tiles.size()-1) - i;
      for (int j=0; j<tiles.get(0).size(); j++) {
        if (tiles.get(i).get(j) == tile) {
          coordinate = new Coordinate(j, y);
        }
      }
    }
    return coordinate;
  }

  /**  Visualises Maze object
  *    @return Returns a string representation of the maze structure
  */
  public String toString() {

    String mazeString = "";
    int gridNumber = 0;
    String bottomAxis = "";


    for (int i=0; i < tiles.size(); i++) {
      String line = "";
      List<Tile> innerList = tiles.get(i);

      for (int j=0; j<innerList.size(); j++) {
        if (j == 0) {
          line =  String.valueOf(gridNumber) + "  " + (innerList.get(j).toString());
        } else {
          line = line + " " + (innerList.get(j).toString());
        }
      }
      gridNumber = gridNumber + 1;
      line = line + "\n";
      mazeString = mazeString + line;
    }

    for (int i=0; i<tiles.get(0).size(); i++){
      bottomAxis = bottomAxis + String.valueOf(i) + " ";
    }

    mazeString = mazeString + "\n" + "   " + bottomAxis + "\n";
    return mazeString;
  }


  /**  Inner class to create Coordinate objects  */
  public class Coordinate {
    private int x;
    private int y;


    /**  Constructor to create Coordinate objects
    *    @param xValue: x value for the coordinate
    *    @param yValue: y value for the coordinate
    */
    public Coordinate(int xValue, int yValue) {
      x = xValue;
      y = yValue;
    }

    /**  Gets x value of the Coordinate Object
    *    @return Reuerns integer value of x value of Coordinate object
    */
    public int getX() {
      return x;
    }

    /**  Gets y value of the Coordinate Object
    *    @return Returns integer value of y value of Coordinate object
    */
    public int getY() {
      return y;
    }


    /**  Displays String representation of Coordinate object
    *    @return Returns String representation of Coordinate object
    */
    public String toString() {
      String coordinateString = "(" + x + "," + " " + y + ")";
      return coordinateString;
    }
  }

  /**  Inner enum class containing Directions that can be used  */
  public enum Direction {
    /** North direction */
    NORTH,

    /** South direction */
    SOUTH,

    /** East direction */
    EAST,

    /** West direction */
    WEST;
  }



}
