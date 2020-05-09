
package dev.pokete.chestinvexport.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Book {

    @SerializedName("repair_times")
    @Expose
    private Integer repairTimes;
    @SerializedName("enchantments")
    @Expose
    private List<Enchantment> enchantments = null;

    public Integer getRepairTimes() {
        return repairTimes;
    }

    public void setRepairTimes(Integer repairTimes) {
        this.repairTimes = repairTimes;
    }

    public List<Enchantment> getEnchantments() {
        return enchantments;
    }

    public void setEnchantments(List<Enchantment> enchantments) {
        this.enchantments = enchantments;
    }

}
