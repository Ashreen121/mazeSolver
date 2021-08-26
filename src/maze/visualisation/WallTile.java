package maze.visualisation;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

/**  Class to create WallTile objects
*    @author Ashreen Kaur
*/
public class WallTile {

  private Rectangle wall;
  private Color color = Color.BLUE;

  /**  Constructor to create WallTile object */
  public WallTile() {
    wall = new Rectangle(10, 10, 45, 45);
    wall.setFill(color);
  }

  /**  Gets Rectangle object associated to the WallTile
  *    @return Returns rectangle object associated to the WallTile
  */
  public Rectangle getWall() {
    return wall;
  }
}
