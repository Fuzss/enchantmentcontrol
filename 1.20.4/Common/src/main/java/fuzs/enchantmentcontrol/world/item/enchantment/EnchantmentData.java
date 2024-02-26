package fuzs.enchantmentcontrol.world.item.enchantment;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fuzs.puzzleslib.api.config.v3.json.GsonEnumHelper;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public record EnchantmentData(Enchantment.Rarity rarity, EquipmentSlot[] equipmentSlots, int minLevel, int maxLevel) {

    public EnchantmentData withRarity(Enchantment.Rarity rarity) {
        return new EnchantmentData(rarity, this.equipmentSlots, this.minLevel, this.maxLevel);
    }

    public EnchantmentData withEquipmentSlots(EquipmentSlot... equipmentSlots) {
        return new EnchantmentData(this.rarity, equipmentSlots, this.minLevel, this.maxLevel);
    }

    public EnchantmentData withMinLevel(int minLevel) {
        return new EnchantmentData(this.rarity, this.equipmentSlots, minLevel, this.maxLevel);
    }

    public EnchantmentData withMaxLevel(int maxLevel) {
        return new EnchantmentData(this.rarity, this.equipmentSlots, this.minLevel, maxLevel);
    }

    public JsonElement toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("rarity", this.rarity.name());
        JsonArray jsonArray = new JsonArray();
        for (EquipmentSlot equipmentSlot : this.equipmentSlots) {
            jsonArray.add(equipmentSlot.name());
        }
        jsonObject.add("slots", jsonArray);
        jsonObject.addProperty("min_level", this.minLevel);
        jsonObject.addProperty("max_level", this.maxLevel);
        return jsonObject;
    }

    public static EnchantmentData fromJson(JsonElement jsonElement) {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Enchantment.Rarity rarity = GsonEnumHelper.getAsEnum(jsonObject, "rarity", Enchantment.Rarity.class);
        JsonArray jsonArray = GsonHelper.getAsJsonArray(jsonObject, "slots");
        EquipmentSlot[] equipmentSlots = new EquipmentSlot[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); i++) {
            String equipmentSlot = GsonHelper.convertToString(jsonArray.get(i), "equipment_slot");
            equipmentSlots[i] = GsonEnumHelper.convertToEnum(equipmentSlot, EquipmentSlot.class);
        }
        int minLevel = GsonHelper.getAsInt(jsonObject, "min_level");
        int maxLevel = GsonHelper.getAsInt(jsonObject, "max_level");
        return new EnchantmentData(rarity, equipmentSlots, minLevel, maxLevel);
    }

    public static EnchantmentData fromEnchantment(Enchantment enchantment) {
        EnchantmentFeature.testHolderIsNull(enchantment);
        return new EnchantmentData(enchantment.rarity,
                enchantment.slots,
                enchantment.getMinLevel(),
                enchantment.getMaxLevel()
        );
    }
}
