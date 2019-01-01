/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelboardgame;

import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Player {
    
    public int credits;
    public String color;
    public String name;
    public int maxProfit;
    public ArrayList<Hotel> hotels = new ArrayList<Hotel>();
    public ArrayList<Entrance> entrances = new ArrayList<Entrance>();
    public int positionX;
    public int positionY;
    public int prevPositionX;
    public int prevPositionY;
    public Circle pawn = new Circle(25);
    public boolean passedBank;
    public boolean passedTownHall;
    public boolean changedRoundForTownHall;
    public boolean changedRoundForBank;
    
    Player(String n, String co, int cr, int px, int py, Color f) {
        name = n;
        color  = co;
        credits = cr;
        maxProfit = cr;
        positionX = px;
        positionY = py;
        prevPositionX = px;
        prevPositionY = py;
        passedBank = false;
        passedTownHall = false;
        changedRoundForTownHall = false;
        changedRoundForBank = false;
        pawn.setFill(f);
    }
    
}
