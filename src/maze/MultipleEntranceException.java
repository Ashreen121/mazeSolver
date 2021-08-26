package maze;

/**  Objects of this class is thrown when mutliple entrances are found in the maze
*    @author Ashreen Kaur
*/
public class MultipleEntranceException extends InvalidMazeException {

  /**  Constructor which calls InvalidMazeException constructor
  * of RuntimeException class
  */
  public MultipleEntranceException() {
    super();
  }
}
