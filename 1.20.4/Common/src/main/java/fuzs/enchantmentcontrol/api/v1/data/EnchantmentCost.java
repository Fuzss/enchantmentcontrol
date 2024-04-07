package fuzs.enchantmentcontrol.api.v1.data;

import fuzs.enchantmentcontrol.impl.world.item.enchantment.EnchantmentCostImpl;

/**
 * A calculation implementation for {@link net.minecraft.world.item.enchantment.Enchantment#getMinCost(int)} and
 * {@link net.minecraft.world.item.enchantment.Enchantment#getMaxCost(int)} pulled from Minecraft 1.20.5.
 * <p>
 * Allows for making cost calculations controlled via our {@link EnchantmentData} implementation.
 */
@FunctionalInterface
public interface EnchantmentCost {

    /**
     * Calculates the enchantment cost based on the provided enchantment level.
     *
     * @param enchantmentLevel the current enchantment level
     * @return the calculated experience cost for the player
     */
    int calculate(int enchantmentLevel);

    /**
     * @param baseCost     minimum player enchantment level cost
     * @return new enchantment cost instance
     */
    static EnchantmentCost constantCost(int baseCost) {
        return dynamicCost(baseCost, 0);
    }

    /**
     * @param baseCost     minimum player enchantment level cost
     * @param perLevelCost additional player levels per enchantment level
     * @return new enchantment cost instance
     */
    static EnchantmentCost dynamicCost(int baseCost, int perLevelCost) {
        return new EnchantmentCostImpl(baseCost, perLevelCost);
    }
}
