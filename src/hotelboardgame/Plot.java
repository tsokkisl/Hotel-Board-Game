/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelboardgame;

public class Plot {
    public boolean isOwned;
    public boolean isConstructed;
    public String owner = "";
    
    public Plot() {
        isOwned = false;
        isConstructed = false;
    }
}
