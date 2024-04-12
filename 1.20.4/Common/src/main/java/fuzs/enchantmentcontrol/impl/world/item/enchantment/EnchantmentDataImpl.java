package fuzs.enchantmentcontrol.impl.world.item.enchantment;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fuzs.enchantmentcontrol.api.v1.data.EnchantmentCost;
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
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public record EnchantmentDataImpl(EnchantmentCategory enchantmentCategory,
                                  Enchantment.Rarity rarity,
                                  EquipmentSlot[] equipmentSlots,
                                  int minLevel,
                                  int maxLevel,
                                  @Nullable EnchantmentCost minCost,
                                  @Nullable EnchantmentCost maxCost,
                                  List<Enchantment> aliases) implements EnchantmentDataBuilder {

    @Override
    public EnchantmentDataImpl withRarity(Enchantment.Rarity rarity) {
        return new EnchantmentDataImpl(this.enchantmentCategory,
                rarity,
                this.equipmentSlots,
                this.minLevel,
                this.maxLevel,
                this.minCost,
                this.maxCost,
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
                this.minCost,
                this.maxCost,
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
                this.minCost,
                this.maxCost,
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
                this.minCost,
                this.maxCost,
                this.aliases
        );
    }

    @Override
    public EnchantmentDataBuilder withMinCost(@Nullable EnchantmentCost minCost) {
        return new EnchantmentDataImpl(this.enchantmentCategory,
                this.rarity,
                this.equipmentSlots,
                this.minLevel,
                this.maxLevel,
                minCost,
                this.maxCost,
                this.aliases
        );
    }

    @Override
    public EnchantmentDataBuilder withMaxCost(@Nullable EnchantmentCost maxCost) {
        return new EnchantmentDataImpl(this.enchantmentCategory,
                this.rarity,
                this.equipmentSlots,
                this.minLevel,
                this.maxLevel,
                this.minCost,
                maxCost,
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
                this.minCost,
                this.maxCost,
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
        if (!EnchantmentControlMod.CONFIG.getHolder(CommonConfig.class).isAvailable() ||
                EnchantmentControlMod.CONFIG.get(CommonConfig.class).abstractEquipmentSlots) {
            jsonObject.addProperty("type", EnchantmentType.fromSlots(this.equipmentSlots).name());
        } else {
            JsonArray equipmentSlots = new JsonArray();
            for (EquipmentSlot equipmentSlot : this.equipmentSlots) {
                equipmentSlots.add(equipmentSlot.name());
            }
            jsonObject.add("slots", equipmentSlots);
        }
        if (this.minLevel > 1) {
            jsonObject.addProperty("min_level", this.minLevel);
        }
        if (this.maxLevel > 1) {
            jsonObject.addProperty("max_level", this.maxLevel);
        }
        if (this.minCost != null) {
            jsonObject.add("min_cost", ((EnchantmentCostImpl) this.minCost).toJson());
        }
        if (this.maxCost != null) {
            jsonObject.add("max_cost", ((EnchantmentCostImpl) this.maxCost).toJson());
        }
        if (!this.aliases.isEmpty()) {
            JsonArray aliases = new JsonArray();
            for (Enchantment enchantment : this.aliases) {
                aliases.add(BuiltInRegistries.ENCHANTMENT.getKey(enchantment).toString());
            }
            jsonObject.add("aliases", aliases);
        }

        return jsonObject;
    }

    public static EnchantmentData fromJson(EnchantmentHolder holder, JsonElement jsonElement) {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Enchantment.Rarity rarity = GsonEnumHelper.getAsEnum(jsonObject, "rarity", Enchantment.Rarity.class);
        EquipmentSlot[] equipmentSlots;
        // prefer defining individual slots over slot types
        if (jsonObject.has("slots")) {
            JsonArray equipmentSlotsArray = GsonHelper.getAsJsonArray(jsonObject, "slots");
            equipmentSlots = new EquipmentSlot[equipmentSlotsArray.size()];
            for (int i = 0; i < equipmentSlotsArray.size(); i++) {
                String equipmentSlot = GsonHelper.convertToString(equipmentSlotsArray.get(i), "equipment_slot");
                equipmentSlots[i] = GsonEnumHelper.convertToEnum(equipmentSlot, EquipmentSlot.class);
            }
        } else {
            equipmentSlots = GsonEnumHelper.getAsEnum(jsonObject, "type", EnchantmentType.class).getSlots();
        }
        int minLevel = GsonHelper.getAsInt(jsonObject, "min_level", 1);
        int maxLevel = GsonHelper.getAsInt(jsonObject, "max_level", 1);
        EnchantmentCost minCost = EnchantmentCostImpl.fromJson(jsonObject, "min_cost");
        EnchantmentCost maxCost = EnchantmentCostImpl.fromJson(jsonObject, "max_cost");
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
                minCost,
                maxCost,
                ImmutableList.copyOf(aliases)
        );
    }

    public static EnchantmentDataBuilder fromEnchantment(Enchantment enchantment) {
        return fromEnchantment(enchantment, false);
    }

    public static EnchantmentDataBuilder fromEnchantment(Enchantment enchantment, boolean skipHolderValidation) {
        if (!skipHolderValidation) {
            EnchantmentHolder.isOriginalState(enchantment);
        }
        return new EnchantmentDataImpl(enchantment.category,
                enchantment.rarity,
                enchantment.slots,
                enchantment.getMinLevel(),
                enchantment.getMaxLevel(),
                null,
                null,
                Collections.emptyList()
        );
    }
}
