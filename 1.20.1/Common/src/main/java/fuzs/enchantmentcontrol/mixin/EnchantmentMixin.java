package fuzs.enchantmentcontrol.mixin;

import fuzs.enchantmentcontrol.world.item.enchantment.EnchantmentFeature;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Enchantment.class)
abstract class EnchantmentMixin implements EnchantmentFeature {
    @Unique
    private FeatureFlagSet universalenchants$requiredFeatures = FeatureFlags.DEFAULT_FLAGS;

    @ModifyVariable(method = "getFullname", at = @At(value = "TAIL", shift = At.Shift.BEFORE))
    public MutableComponent getFullname(MutableComponent component, int level) {
        if (!this.universalenchants$isEnabled()) component.withStyle(ChatFormatting.STRIKETHROUGH);
        return component;
    }

    @Override
    public FeatureFlagSet requiredFeatures() {
        return this.universalenchants$requiredFeatures;
    }

    @Override
    public void universalenchants$setRequiredFeatures(FeatureFlagSet requiredFeatures) {
        this.universalenchants$requiredFeatures = requiredFeatures;
    }
}
