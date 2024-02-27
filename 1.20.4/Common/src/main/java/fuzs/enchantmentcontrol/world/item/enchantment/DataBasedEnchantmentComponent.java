package fuzs.enchantmentcontrol.world.item.enchantment;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fuzs.enchantmentcontrol.init.ModRegistry;
import fuzs.puzzleslib.api.config.v3.json.GsonEnumHelper;
import fuzs.puzzleslib.api.init.v3.registry.RegistryHelper;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public record DataBasedEnchantmentComponent(Enchantment enchantment, Enchantment.Rarity rarity, EquipmentSlot[] equipmentSlots, int minLevel, int maxLevel) implements EnchantmentComponent {

    public DataBasedEnchantmentComponent withRarity(Enchantment.Rarity rarity) {
        return new DataBasedEnchantmentComponent(this.enchantment, rarity, this.equipmentSlots, this.minLevel, this.maxLevel);
    }

    public DataBasedEnchantmentComponent withEquipmentSlots(EquipmentSlot... equipmentSlots) {
        return new DataBasedEnchantmentComponent(this.enchantment, this.rarity, equipmentSlots, this.minLevel, this.maxLevel);
    }

    public DataBasedEnchantmentComponent withMinLevel(int minLevel) {
        return new DataBasedEnchantmentComponent(this.enchantment, this.rarity, this.equipmentSlots, minLevel, this.maxLevel);
    }

    public DataBasedEnchantmentComponent withMaxLevel(int maxLevel) {
        return new DataBasedEnchantmentComponent(this.enchantment, this.rarity, this.equipmentSlots, this.minLevel, maxLevel);
    }

    @Override
    public EnchantmentCategory getEnchantmentCategory() {
        return null;
    }

    @Override
    public boolean canEnchant(ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean isCompatibleWith(Enchantment other) {
        return false;
    }

    @Override
    public boolean isTreasureOnly() {
        return this.is(ModRegistry.TREASURE_ENCHANTMENT_TAG);
    }

    @Override
    public boolean isCurse() {
        return this.is(ModRegistry.CURSES_ENCHANTMENT_TAG);
    }

    @Override
    public boolean isTradeable() {
        return this.is(ModRegistry.TRADEABLE_ENCHANTMENT_TAG);
    }

    @Override
    public boolean isDiscoverable() {
        return this.is(ModRegistry.DISCOVERABLE_ENCHANTMENT_TAG);
    }

    @Override
    public boolean isUnobtainable() {
        return this.is(ModRegistry.UNOBTAINABLE_ENCHANTMENT_TAG);
    }

    public boolean is(TagKey<Enchantment> tagKey) {
        return RegistryHelper.is(tagKey, this.enchantment);
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

    public static DataBasedEnchantmentComponent fromJson(Enchantment enchantment, JsonElement jsonElement) {
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
        return new DataBasedEnchantmentComponent(enchantment, rarity, equipmentSlots, minLevel, maxLevel);
    }

    public static DataBasedEnchantmentComponent fromEnchantment(Enchantment enchantment) {
        EnchantmentFeature.testHolderIsNull(enchantment);
        return new DataBasedEnchantmentComponent(enchantment, enchantment.rarity,
                enchantment.slots,
                enchantment.getMinLevel(),
                enchantment.getMaxLevel()
        );
    }
}
