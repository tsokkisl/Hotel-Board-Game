/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelboardgame;

public class Hotel {
    public String name;
    public int plotCost;
    public int requiredPlotCost;
    public int entranceCost;
    public int basicBuildCost;
    public int basicBuildRent;
    public int[] upgradeBuildCost;
    public int[] upgradeBuildRent;
    public int outsideBuildCost;
    public int maxRent;
    
    Hotel(String n, int pc, int rpc, int ec, int bc, int br, int[] ubc, int[] ubr,int obc, int mr) {
        name = n;
        plotCost = pc;
        requiredPlotCost = rpc;
        entranceCost = ec;
        basicBuildCost = bc;
        basicBuildRent = br;
        upgradeBuildCost = ubc;
        upgradeBuildRent = ubr;
        outsideBuildCost = obc;
        maxRent = mr;
    }
    
}
