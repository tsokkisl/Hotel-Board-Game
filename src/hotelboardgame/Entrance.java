/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelboardgame;

public class Entrance {
    public int price;
    public int entranceX, entranceY;
    public String hName;
    public Entrance(String s, int p, int x, int y) {
        hName = s;
        price = p;
        entranceX = x;
        entranceY = y;
    }
    
}
