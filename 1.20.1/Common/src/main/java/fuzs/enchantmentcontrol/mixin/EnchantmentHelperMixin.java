package fuzs.enchantmentcontrol.mixin;

import fuzs.puzzleslib.api.core.v1.CommonAbstractions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.flag.FeatureFlagSet;
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
    private static void getItemEnchantmentLevel(Enchantment enchantment, ItemStack stack, CallbackInfoReturnable<Integer> callback) {
        MinecraftServer minecraftServer = CommonAbstractions.INSTANCE.getGameServer();
        if (minecraftServer != null) {
            FeatureFlagSet featureFlagSet = minecraftServer.getWorldData().enabledFeatures();
            if (!((FeatureElement) enchantment).isEnabled(featureFlagSet)) callback.setReturnValue(0);
        }
    }
}
