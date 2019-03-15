package hotelboardgame;

public class Hotel {
    public String hotelOwner;
    public String name;
    public int number;
    public int plotCost;
    public int requiredPlotCost;
    public int entranceCost;
    public int[] buildCost;
    public int[] rentCost;
    public int maxUpgradeLevel;
    public int currentUpgradeLevel;
    public Plot plot;
    
    public Hotel(String n, int pc, int rpc, int ec, int[] bc, int[] br, int num) {
        name = n;
        plotCost = pc;
        requiredPlotCost = rpc;
        entranceCost = ec;
        buildCost = bc;
        rentCost = br;
        number = num;
        maxUpgradeLevel = bc.length;
        currentUpgradeLevel = 0;
        hotelOwner = "None";
        plot = new Plot();
    }
    
}
