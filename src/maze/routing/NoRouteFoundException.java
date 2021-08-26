package maze.routing;

import java.lang.RuntimeException;

/**  Objects of this class are thrown when there is no route out of the maze
*    @author Ashreen Kaur
*/
public class NoRouteFoundException extends RuntimeException {

  /**  Constructor which calls RuntimeException constructor
  * of RuntimeException class
  */
  public NoRouteFoundException() {
    super();
  }
}
