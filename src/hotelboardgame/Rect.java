/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelboardgame;

import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Rect {
    public StackPane stack = new StackPane();
    public Rectangle rec = new Rectangle();
    public ImageView image;
    private Text text;
    private boolean hasEntrance;
    private boolean belongsToTheBank;
    
    public Rect() {
        text = new Text ("");
        hasEntrance = false;
        belongsToTheBank = false;
    }
    public void setText(String s) {
        text.setText(s);
    }
    public void setHasEntrance(boolean t) {
        hasEntrance = t;
    }
    public void setBelongsToTheBank(boolean t) {
        belongsToTheBank = t;
    }
    public Text getText() {
        return text;
    }
    public boolean getHasEntrance() {
        return hasEntrance;
    }
    public boolean getBelongsToTheBank() {
        return belongsToTheBank;
    }
}
