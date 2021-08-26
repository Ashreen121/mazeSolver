package maze;

/**  Objects of this class is thrown when no entrances are found in the maze
*    @author Ashreen Kaur
*/
public class NoEntranceException extends InvalidMazeException {

  /**  Constructor which calls InvalidMazeException constructor
  * of RuntimeException class
  */
  public NoEntranceException() {
    super();
  }
}
