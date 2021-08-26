package maze;

/**  Objects of this class is thrown when no exits are found in the maze
*    @author Ashreen Kaur
*/
public class NoExitException extends InvalidMazeException {

  /**  Constructor which calls InvalidMazeException constructor
  * of RuntimeException class
  */
  public NoExitException() {
    super();
  }
}
