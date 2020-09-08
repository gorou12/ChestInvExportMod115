
package dev.pokete.chestinvexport.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Book {

    @SerializedName("item_type")
    @Expose
    private String itemType;
    @SerializedName("repair_times")
    @Expose
    private Integer repairTimes;
    @SerializedName("enchantments")
    @Expose
    private List<Enchantment> enchantments = null;
    @SerializedName("count")
    @Expose
    private Integer count;

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}
