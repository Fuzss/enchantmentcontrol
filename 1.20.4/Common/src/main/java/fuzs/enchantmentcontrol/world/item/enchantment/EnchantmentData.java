package fuzs.enchantmentcontrol.world.item.enchantment;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fuzs.puzzleslib.api.config.v3.json.GsonEnumHelper;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

public record EnchantmentData(EnchantmentCategory enchantmentCategory,
                              Enchantment.Rarity rarity,
                              EquipmentSlot[] equipmentSlots,
                              int minLevel,
                              int maxLevel) {

    public EnchantmentData withRarity(Enchantment.Rarity rarity) {
        return new EnchantmentData(this.enchantmentCategory, rarity, this.equipmentSlots, this.minLevel, this.maxLevel);
    }

    public EnchantmentData withEquipmentSlots(EquipmentSlot... equipmentSlots) {
        return new EnchantmentData(this.enchantmentCategory, this.rarity, equipmentSlots, this.minLevel, this.maxLevel);
    }

    public EnchantmentData withMinLevel(int minLevel) {
        return new EnchantmentData(this.enchantmentCategory, this.rarity, this.equipmentSlots, minLevel, this.maxLevel);
    }

    public EnchantmentData withMaxLevel(int maxLevel) {
        return new EnchantmentData(this.enchantmentCategory, this.rarity, this.equipmentSlots, this.minLevel, maxLevel);
    }

    public void apply(Enchantment enchantment) {
        enchantment.category = this.enchantmentCategory;
        enchantment.rarity = this.rarity;
        enchantment.slots = this.equipmentSlots;
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

    public static EnchantmentData fromJson(EnchantmentHolder holder, JsonElement jsonElement) {
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
        return new EnchantmentData(holder.getTagBasedEnchantmentCategory(), rarity, equipmentSlots, minLevel, maxLevel);
    }

    public static EnchantmentData fromEnchantment(Enchantment enchantment) {
        EnchantmentFeature.testHolderIsNull(enchantment);
        return new EnchantmentData(enchantment.category,
                enchantment.rarity,
                getAndExpandSlots(enchantment),
                enchantment.getMinLevel(),
                enchantment.getMaxLevel()
        );
    }

    private static EquipmentSlot[] getAndExpandSlots(Enchantment enchantment) {
        EquipmentSlot[] slots = enchantment.slots;
        int armorSlots = 0;
        for (EquipmentSlot slot : slots) {
            if (slot.isArmor()) armorSlots++;
        }
        // some armor enchantments are locked to a single equipment slot,
        // which has no effect in vanilla since they usually can only go on items for that slot
        // but when a user adds other armor items to the enchantment it will not work due to the slot restriction,
        // so lift the restriction by default as it will not have any effect in-game with the vanilla configuration
        if (armorSlots > 0 && armorSlots < 4) {
            Set<EquipmentSlot> set = EnumSet.copyOf(Arrays.asList(slots));
            for (EquipmentSlot slot : slots) {
                if (slot.isArmor()) set.add(slot);
            }
            slots = set.toArray(EquipmentSlot[]::new);
        }
        return slots;
    }
}
