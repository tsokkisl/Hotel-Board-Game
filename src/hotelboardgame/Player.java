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
    public Hotel[] hotels = {};
    public ArrayList<Entrance> entrances = new ArrayList<Entrance>();
    public int position;
    public Circle pawn = new Circle(25);
    
    Player(String n, String co, int cr, int p, Color f) {
        name = n;
        color  = co;
        credits = cr;
        maxProfit = cr;
        position = p;
        pawn.setFill(f);
    }
    
}
