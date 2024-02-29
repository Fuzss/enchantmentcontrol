package fuzs.enchantmentcontrol.api.v1.data;

import fuzs.enchantmentcontrol.world.item.enchantment.EnchantmentDataImpl;
import fuzs.enchantmentcontrol.world.item.enchantment.EnchantmentHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface EnchantmentDataBuilder extends EnchantmentData {
    
    EnchantmentDataBuilder withRarity(Enchantment.Rarity rarity);

    EnchantmentDataBuilder withEquipmentSlots(EquipmentSlot... equipmentSlots);

    EnchantmentDataBuilder withMinLevel(int minLevel);

    EnchantmentDataBuilder withMaxLevel(int maxLevel);

    EnchantmentDataBuilder withAliases(Enchantment... aliases);

    static EnchantmentDataBuilder fromEnchantment(Enchantment enchantment) {
        return EnchantmentDataImpl.fromEnchantment(enchantment, true);
    }

    static void clearAll() {
        EnchantmentHolder.clearAll();
    }
}
