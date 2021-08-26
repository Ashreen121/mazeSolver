package maze;

import java.lang.RuntimeException;


/**  Objects of this class is thrown when an invalid maze is found
*    @author Ashreen Kaur
*/
public class InvalidMazeException extends RuntimeException {

  /**  Constructor which calls RuntimeException constructor
  * of RuntimeException class
  */
  public InvalidMazeException() {
    super();
  }
}
