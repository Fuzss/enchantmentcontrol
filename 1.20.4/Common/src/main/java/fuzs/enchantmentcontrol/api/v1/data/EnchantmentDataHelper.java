package fuzs.enchantmentcontrol.api.v1.data;

import fuzs.enchantmentcontrol.impl.world.item.enchantment.EnchantmentHolder;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * Provides some universal actions related to enchantment data.
 */
public final class EnchantmentDataHelper {

    private EnchantmentDataHelper() {
        // NO-OP
    }

    /**
     * Apply all enchantment holders to corresponding enchantments, meaning enchantment data is available in the mod
     * provided state.
     * <p>
     * This has no effect on {@link EnchantmentData} stored in holders, so it can run independently of data pack
     * reloads.
     */
    public static void bindAll() {
        EnchantmentHolder.bindAll();
    }

    /**
     * Remove all enchantment holders from corresponding enchantments, so that enchantment data is available in the
     * original state.
     * <p>
     * This has no effect on {@link EnchantmentData} stored in holders, so it can run independently of data pack
     * reloads.
     */
    public static void unbindAll() {
        EnchantmentHolder.unbindAll();
    }

    /**
     * Remove all enchantment holders from corresponding enchantments, so that enchantment data is available in the
     * original state.
     * <p>
     * All {@link EnchantmentData} stored in holders is cleared as well. This should ideally only be called before data
     * pack reloads when enchantment data will be recreated right after.
     */
    public static void clearAll() {
        EnchantmentHolder.clearAll();
    }

    /**
     * Checks if an enchantment provides data in the original state without any mod changes applied, useful to check
     * before data generation.
     *
     * @param enchantment the enchantment to test
     */
    public static void isOriginalState(Enchantment enchantment) {
        EnchantmentHolder.isOriginalState(enchantment);
    }
}
