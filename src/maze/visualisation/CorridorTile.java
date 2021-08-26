package maze.visualisation;

import javafx.scene.text.Text;
import javafx.scene.control.TextField;

/**  Class to create CorridorTile objects
*    @author Ashreen Kaur
*/
public class CorridorTile {

  private TextField corridor;
  private String tileSymbol;

  /**  Constructor to create CorridorTile object */
  public CorridorTile(String symbol) {
    tileSymbol = symbol;
    corridor = new TextField();
    corridor.setText(symbol);
    corridor.setPrefSize(45, 45);
  }

  /**  Gets TextField object associated to the CorridorTile
  *    @return Returns TextField object associated to the CorridorTile
  */
  public TextField getCorridor() {
    return corridor;
  }

}
