package maze;

import java.io.Serializable;

/**  Class to create Tile objects
*    @author Ashreen Kaur
*/
public class Tile implements Serializable{
  private Type type;
  private boolean isDeadEnd;
  private boolean alreadyVisited;


  private Tile(Type tileType) {
    alreadyVisited = false;
    type = tileType;
  }

  /**  Creates Tile object with input char
  *    @param c: a char value to dtermine the Type of the Tile
  *    @throws maze.InvalidMazeException when invalid character is in maze file
  *    @return Returns Tile object with a set Type attribute
  */
  protected static Tile fromChar(char c) {
    Tile newTile;
    if (c == '.') {
      newTile = new Tile(Type.CORRIDOR);
    } else if (c == 'e') {
      newTile = new Tile(Type.ENTRANCE);
    } else if (c == 'x') {
      newTile = new Tile(Type.EXIT);
    } else if (c == '#'){
      newTile = new Tile(Type.WALL);
    } else {
      throw new InvalidMazeException();
    }
    return newTile;
  }


  /**  Gets Type of a Tile object
  *    @return Returns Type of the Tile object
  */
  public Type getType() {
    return type;
  }

  /**  Checks if a Tile object is Navigable (only Type WALL is not navigable)
  *    @return true if Tile object is navigable, and false if it is not navigable
  */
  public boolean isNavigable() {
    if (type == Type.WALL) {
      return false;
    } else {
      return true;
    }
  }

  /**  Checks if a Tile is a dead end Tile with respect to a particular maze structure
  *    @return true if Tile object is a dead end, and false if it is not a dead end
  */
  public boolean isDeadEnd() {
    return isDeadEnd;
  }

  /**  Sets a boolean value to the attribute isDeadEnd
  *    @param b indicates if the tile is a dead end
  */
  public void setDeadEnd(boolean b) {
    isDeadEnd = b;
  }

  /**  Checks if a Tile has already been visited
  *    @return true if Tile object has been visited, and false if it has not been visited
  */
  public boolean alreadyVisited() {
    return alreadyVisited;
  }

  /**  Sets a boolean value to the attribute alreadyVisited
  *    @param b indicates if the tile has already been visited
  */
  public void setVisited(boolean b) {
    alreadyVisited = b;
  }


  /**  Displays String representation of Tile object
  *    @return Returns String representation of tile object
  */
  public String toString() {
    String tileString;

    if (type == Type.CORRIDOR) {
      tileString = ".";
    } else if (type == Type.ENTRANCE) {
      tileString = "e";
    } else if (type == Type.EXIT) {
      tileString = "x";
    } else {
      tileString = "#";
    }
    return tileString;
  }

  /**  Inner enum class containing Types that can be used  */
  public enum Type {

    /** Corridor type */
    CORRIDOR,

    /** Entrance type */
    ENTRANCE,

    /** Exit type */
    EXIT,

    /** Wall type */
    WALL;
  }
}
