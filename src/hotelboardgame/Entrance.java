package hotelboardgame;

public class Entrance {
    private int price;
    private int entranceX, entranceY;
    public String hName;
    private String owner;
    
    public Entrance(String s, int p, int x, int y) {
        hName = s;
        price = p;
        entranceX = x;
        entranceY = y;
    }
    public void setOwner(String s) {
        owner = s;
    }
    public void setPrice(int p) {
        price = p;
    }
    public void setEntranceX(int x) {
        entranceX = x;
    }
    public void setEntranceY(int y) {
        entranceY = y;
    }
    public int getPrice() {
        return price;
    }
    public int getEntranceX() {
        return entranceX;
    }
    public int getEntranceY() {
        return entranceY;
    }
    public String getOwner() {
        return owner;
    }
    
    
}
