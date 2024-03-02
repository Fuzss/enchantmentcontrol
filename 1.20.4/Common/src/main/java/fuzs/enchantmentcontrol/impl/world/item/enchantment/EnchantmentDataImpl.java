package fuzs.enchantmentcontrol.impl.world.item.enchantment;

import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fuzs.enchantmentcontrol.api.v1.data.EnchantmentData;
import fuzs.enchantmentcontrol.api.v1.data.EnchantmentDataBuilder;
import fuzs.enchantmentcontrol.impl.EnchantmentControlMod;
import fuzs.enchantmentcontrol.impl.config.CommonConfig;
import fuzs.puzzleslib.api.config.v3.json.GsonEnumHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.*;

public record EnchantmentDataImpl(EnchantmentCategory enchantmentCategory,
                                  Enchantment.Rarity rarity,
                                  EquipmentSlot[] equipmentSlots,
                                  int minLevel,
                                  int maxLevel,
                                  List<Enchantment> aliases) implements EnchantmentDataBuilder {
    private static final Set<EquipmentSlot> ARMOR_SLOTS = Sets.immutableEnumSet(EquipmentSlot.FEET,
            EquipmentSlot.LEGS,
            EquipmentSlot.CHEST,
            EquipmentSlot.HEAD
    );

    @Override
    public EnchantmentDataImpl withRarity(Enchantment.Rarity rarity) {
        return new EnchantmentDataImpl(this.enchantmentCategory,
                rarity,
                this.equipmentSlots,
                this.minLevel,
                this.maxLevel,
                this.aliases
        );
    }

    @Override
    public EnchantmentDataImpl withEquipmentSlots(EquipmentSlot... equipmentSlots) {
        return new EnchantmentDataImpl(this.enchantmentCategory,
                this.rarity,
                equipmentSlots,
                this.minLevel,
                this.maxLevel,
                this.aliases
        );
    }

    @Override
    public EnchantmentDataImpl withMinLevel(int minLevel) {
        return new EnchantmentDataImpl(this.enchantmentCategory,
                this.rarity,
                this.equipmentSlots,
                minLevel,
                this.maxLevel,
                this.aliases
        );
    }

    @Override
    public EnchantmentDataImpl withMaxLevel(int maxLevel) {
        return new EnchantmentDataImpl(this.enchantmentCategory,
                this.rarity,
                this.equipmentSlots,
                this.minLevel,
                maxLevel,
                this.aliases
        );
    }

    @Override
    public EnchantmentDataImpl withAliases(Enchantment... aliases) {
        return new EnchantmentDataImpl(this.enchantmentCategory,
                this.rarity,
                this.equipmentSlots,
                this.minLevel,
                this.maxLevel,
                List.copyOf(Arrays.asList(aliases))
        );
    }

    public void apply(Enchantment enchantment) {
        enchantment.category = this.enchantmentCategory;
        enchantment.rarity = this.rarity;
        enchantment.slots = this.equipmentSlots;
    }

    public JsonElement toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("rarity", this.rarity.name());
        JsonArray equipmentSlots = new JsonArray();
        for (EquipmentSlot equipmentSlot : this.equipmentSlots) {
            equipmentSlots.add(equipmentSlot.name());
        }
        jsonObject.add("slots", equipmentSlots);
        jsonObject.addProperty("min_level", this.minLevel);
        jsonObject.addProperty("max_level", this.maxLevel);
        JsonArray aliases = new JsonArray();
        for (Enchantment enchantment : this.aliases) {
            aliases.add(BuiltInRegistries.ENCHANTMENT.getKey(enchantment).toString());
        }
        jsonObject.add("aliases", aliases);
        return jsonObject;
    }

    public static EnchantmentData fromJson(EnchantmentHolder holder, JsonElement jsonElement) {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Enchantment.Rarity rarity = GsonEnumHelper.getAsEnum(jsonObject, "rarity", Enchantment.Rarity.class);
        JsonArray equipmentSlotsArray = GsonHelper.getAsJsonArray(jsonObject, "slots");
        EquipmentSlot[] equipmentSlots = new EquipmentSlot[equipmentSlotsArray.size()];
        for (int i = 0; i < equipmentSlotsArray.size(); i++) {
            String equipmentSlot = GsonHelper.convertToString(equipmentSlotsArray.get(i), "equipment_slot");
            equipmentSlots[i] = GsonEnumHelper.convertToEnum(equipmentSlot, EquipmentSlot.class);
        }
        int minLevel = GsonHelper.getAsInt(jsonObject, "min_level");
        int maxLevel = GsonHelper.getAsInt(jsonObject, "max_level");
        JsonArray aliasesArray = GsonHelper.getAsJsonArray(jsonObject, "aliases", new JsonArray());
        List<Enchantment> aliases = new ArrayList<>();
        for (int i = 0; i < aliasesArray.size(); i++) {
            ResourceLocation resourceLocation = ResourceLocation.tryParse(aliasesArray.get(i).getAsString());
            if (BuiltInRegistries.ENCHANTMENT.containsKey(resourceLocation)) {
                aliases.add(BuiltInRegistries.ENCHANTMENT.get(resourceLocation));
            } else {
                throw new IllegalArgumentException("Missing reference: " + resourceLocation);
            }
        }
        return new EnchantmentDataImpl(holder.getTagBasedEnchantmentCategory(),
                rarity,
                equipmentSlots,
                minLevel,
                maxLevel,
                List.copyOf(aliases)
        );
    }

    public static EnchantmentDataBuilder fromEnchantment(Enchantment enchantment) {
        return fromEnchantment(enchantment, false);
    }

    public static EnchantmentDataBuilder fromEnchantment(Enchantment enchantment, boolean skipHolderValidation) {
        if (!skipHolderValidation) {
            EnchantmentHolder.testIsNull(enchantment);
        }
        return new EnchantmentDataImpl(enchantment.category,
                enchantment.rarity,
                getAndExpandSlots(enchantment),
                enchantment.getMinLevel(),
                enchantment.getMaxLevel(),
                Collections.emptyList()
        );
    }

    private static EquipmentSlot[] getAndExpandSlots(Enchantment enchantment) {
        EquipmentSlot[] slots = enchantment.slots;
        if (EnchantmentControlMod.CONFIG.get(CommonConfig.class).unlockArmorEquipmentSlots) {
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
                set.addAll(ARMOR_SLOTS);
                slots = set.toArray(EquipmentSlot[]::new);
            }
        }
        return slots;
    }
}
