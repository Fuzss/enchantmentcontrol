package fuzs.enchantmentcontrol.mixin;

import fuzs.enchantmentcontrol.world.item.enchantment.EnchantmentFeature;
import fuzs.enchantmentcontrol.world.item.enchantment.MaxLevelManager;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
abstract class DynamicEnchantmentMixin implements EnchantmentFeature {

    @Inject(method = "getMaxLevel", at = @At("HEAD"), cancellable = true, require = 0)
    public void getMaxLevel(CallbackInfoReturnable<Integer> callback) {
        int maxLevel = MaxLevelManager.getMaxLevel(Enchantment.class.cast(this));
        if (maxLevel != -1) callback.setReturnValue(maxLevel);
    }
}
