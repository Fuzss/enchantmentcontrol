package fuzs.enchantmentcontrol;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public final class CommonAbstractions {

    @ExpectPlatform
    public static boolean canApplyAtEnchantingTable(Enchantment enchantment, ItemStack itemStack) {
        throw new RuntimeException();
    }
}
