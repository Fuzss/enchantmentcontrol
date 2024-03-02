package fuzs.enchantmentcontrol.impl.fabric;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public final class CommonAbstractionsImpl {

    public static boolean canApplyAtEnchantingTable(Enchantment enchantment, ItemStack itemStack) {
        return enchantment.category.canEnchant(itemStack.getItem());
    }
}
