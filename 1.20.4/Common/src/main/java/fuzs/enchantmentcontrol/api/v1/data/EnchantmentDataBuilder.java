package fuzs.enchantmentcontrol.api.v1.data;

import fuzs.enchantmentcontrol.impl.world.item.enchantment.EnchantmentDataImpl;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.ApiStatus;

/**
 * An extension to {@link EnchantmentData} providing a builder format for data generation via
 * {@link AbstractEnchantmentDataProvider}.
 */
@ApiStatus.NonExtendable
public interface EnchantmentDataBuilder extends EnchantmentData {

    /**
     * @param rarity the new rarity
     * @return the new enchantment data
     */
    EnchantmentDataBuilder withRarity(Enchantment.Rarity rarity);

    /**
     * @param equipmentSlots the new slots
     * @return the new enchantment data
     */
    EnchantmentDataBuilder withEquipmentSlots(EquipmentSlot... equipmentSlots);

    /**
     * @param minLevel the new minimum level
     * @return the new enchantment data
     */
    EnchantmentDataBuilder withMinLevel(int minLevel);

    /**
     * @param maxLevel the new maximum level
     * @return the new enchantment data
     */
    EnchantmentDataBuilder withMaxLevel(int maxLevel);

    /**
     * @param aliases the new enchantment aliases
     * @return the new enchantment data
     */
    EnchantmentDataBuilder withAliases(Enchantment... aliases);

    /**
     * Create a new enchantment data instance from an enchantment.
     * <p>
     * It is checked that enchantment values are in their original state (without having any changes applied from this
     * mod). To ensure the state is reset properly {@link EnchantmentDataHelper#clearAll()} should be run before data generation happens.
     *
     * @param enchantment the enchantment
     * @return the new enchantment data
     */
    static EnchantmentDataBuilder fromEnchantment(Enchantment enchantment) {
        return EnchantmentDataImpl.fromEnchantment(enchantment);
    }
}
