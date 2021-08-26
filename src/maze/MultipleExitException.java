package maze;

/**  Objects of this class is thrown when mutliple exits are found in the maze
*    @author Ashreen Kaur
*/
public class MultipleExitException extends InvalidMazeException {

  /**  Constructor which calls InvalidMazeException constructor
  * of RuntimeException class
  */
  public MultipleExitException() {
    super();
  }
}
