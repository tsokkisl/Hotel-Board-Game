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
    
    public ArrayList<Hotel> hotels = new ArrayList<Hotel>();
    public ArrayList<Entrance> entrances = new ArrayList<Entrance>();
    public int positionX;
    public int positionY;
    public int prevPositionX;
    public int prevPositionY;
    public Circle pawn = new Circle(25);
    private boolean passedBank;
    private boolean passedTownHall;
    private boolean hasLost;
    private int credits;
    private String name;
    private String color;
    private int maxProfit;
    
    public Player(String n, String co, int cr, int px, int py, Color f) {
        name = n;
        color  = co;
        credits = cr;
        maxProfit = cr;
        positionX = px;
        positionY = py;
        prevPositionX = px;
        prevPositionY = py;
        passedBank = false;
        passedTownHall = true;
        hasLost = false;
        pawn.setFill(f);
    }
    public void setCredits(int c) {
        credits = c;
    }
    public void setName(String n) {
        name = n;
    }
    public void setPassedBank(boolean t) {
        passedBank = t;
    }
    public void setPassedTownHall(boolean t) {
        passedTownHall = t;
    }
    public void setHasLost(boolean t) {
        hasLost = t;
    }
    public void setMaxProfit(int m) {
        maxProfit = m;
    }
    public int getCredits() {
        return credits;
    }
    public String getName() {
        return name;
    }
    public boolean getPassedBank() {
        return passedBank;
    }
    public boolean getPassedTownHall() {
        return passedTownHall;
    }
    public boolean getHasLost() {
        return hasLost;
    }
    public int getMaxProfit() {
        return maxProfit;
    }
    
}
