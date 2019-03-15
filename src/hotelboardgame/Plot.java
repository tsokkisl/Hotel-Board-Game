package hotelboardgame;

public class Plot {
    private boolean isOwned;
    private boolean isConstructed;
    private String owner = "";
    
    public Plot() {
        isOwned = false;
        isConstructed = false;
    }
    public void setOwner(String s) {
        owner = s;
    }
    public void setIsOwned(boolean t) {
        isOwned = t;
    }
    public void setIsConstructed(boolean t) {
        isConstructed = t;
    }
    public boolean getIsOwned() {
        return isOwned;
    }
    public boolean getIsConstructed() {
        return isConstructed;
    }
    public String getOwner() {
        return owner;
    }
}
