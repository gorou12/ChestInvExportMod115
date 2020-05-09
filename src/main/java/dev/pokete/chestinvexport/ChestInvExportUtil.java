package dev.pokete.chestinvexport;

public class ChestInvExportUtil {
    public static int getRepairTimes(int repairCost){
        if(repairCost == 0) return 0;
        return (int) Math.round(1 / (Math.log(2) / Math.log(repairCost + 1)));
    }
}
