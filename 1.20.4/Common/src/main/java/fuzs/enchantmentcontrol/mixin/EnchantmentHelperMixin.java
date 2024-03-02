package fuzs.enchantmentcontrol.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import fuzs.enchantmentcontrol.impl.world.item.enchantment.EnchantmentFeature;
import fuzs.enchantmentcontrol.impl.world.item.enchantment.EnchantmentHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentHelper.class)
abstract class EnchantmentHelperMixin {
    @Unique
    private static boolean enchantmentcontrol$recursiveItemEnchantmentLevelCall;

    @ModifyReturnValue(method = "getItemEnchantmentLevel", at = @At("TAIL"))
    private static int getItemEnchantmentLevel(int enchantmentLevel, Enchantment enchantment, ItemStack itemStack) {
        if (!((EnchantmentFeature) enchantment).enchantmentcontrol$isEnabled()) {
            enchantmentLevel = 0;
        }
        if (enchantmentLevel == 0 && !enchantmentcontrol$recursiveItemEnchantmentLevelCall) {
            EnchantmentHolder holder = ((EnchantmentFeature) enchantment).enchantmentcontrol$getHolder();
            if (holder != null) {
                for (Enchantment alias : holder.getEnchantmentData().aliases()) {
                    enchantmentcontrol$recursiveItemEnchantmentLevelCall = true;
                    enchantmentLevel = getItemEnchantmentLevel(alias, itemStack);
                    enchantmentcontrol$recursiveItemEnchantmentLevelCall = false;
                    if (enchantmentLevel > 0) {
                        // do not go above max level for original enchantment, some calculation based on level might run into issues
                        enchantmentLevel = Math.min(enchantment.getMaxLevel(), enchantmentLevel);
                        break;
                    }
                }
            }
        }
        return enchantmentLevel;
    }

    @Shadow
    private static int getItemEnchantmentLevel(Enchantment enchantment, ItemStack stack) {
        throw new RuntimeException();
    }
}
