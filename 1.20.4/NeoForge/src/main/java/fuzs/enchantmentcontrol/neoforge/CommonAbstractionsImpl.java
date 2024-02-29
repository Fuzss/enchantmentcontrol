package fuzs.enchantmentcontrol.neoforge;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public final class CommonAbstractionsImpl {

    public static boolean canApplyAtEnchantingTable(Enchantment enchantment, ItemStack itemStack) {
        return enchantment.canApplyAtEnchantingTable(itemStack);
    }
}
