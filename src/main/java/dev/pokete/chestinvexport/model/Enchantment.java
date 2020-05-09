
package dev.pokete.chestinvexport.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Enchantment {

    @SerializedName("enchantment")
    @Expose
    private String enchantment;
    @SerializedName("level")
    @Expose
    private Integer level;

    public String getEnchantment() {
        return enchantment;
    }

    public void setEnchantment(String enchantment) {
        this.enchantment = enchantment;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

}
