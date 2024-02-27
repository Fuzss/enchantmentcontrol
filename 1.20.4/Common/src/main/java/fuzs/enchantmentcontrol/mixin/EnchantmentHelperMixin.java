package fuzs.enchantmentcontrol.mixin;

import fuzs.enchantmentcontrol.world.item.enchantment.EnchantmentFeature;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
abstract class EnchantmentHelperMixin {

    @Inject(method = "getItemEnchantmentLevel", at = @At("HEAD"), cancellable = true)
    private static void getItemEnchantmentLevel(Enchantment enchantment, ItemStack itemStack, CallbackInfoReturnable<Integer> callback) {
        if (!((EnchantmentFeature) enchantment).isEnabled()) callback.setReturnValue(0);
    }
}
