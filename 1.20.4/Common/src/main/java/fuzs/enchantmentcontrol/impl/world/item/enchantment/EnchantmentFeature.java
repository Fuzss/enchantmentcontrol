package fuzs.enchantmentcontrol.impl.world.item.enchantment;

import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import org.jetbrains.annotations.Nullable;

public interface EnchantmentFeature extends FeatureElement {

    @Nullable EnchantmentHolder enchantmentcontrol$getHolder();

    void enchantmentcontrol$setHolder(@Nullable EnchantmentHolder enchantmentHolder);

    @Override
    default FeatureFlagSet requiredFeatures() {
        // we don't care about the feature flag set, the vanilla FeatureElement interface is only used to easily support features
        // like registry filtering (like disabled enchantments not showing up as command suggestions)
        // and for other mods to be able to check if an enchantment is enabled without introducing a hard dependency on this mod
        return FeatureFlags.DEFAULT_FLAGS;
    }

    default boolean enchantmentcontrol$isEnabled() {
        EnchantmentHolder holder = this.enchantmentcontrol$getHolder();
        return holder == null || !holder.isUnobtainable();
    }

    @Override
    default boolean isEnabled(FeatureFlagSet enabledFeatures) {
        // we don't care about the feature flag set, the vanilla FeatureElement interface is only used to easily support features
        // like registry filtering (like disabled enchantments not showing up as command suggestions)
        // and for other mods to be able to check if an enchantment is enabled without introducing a hard dependency on this mod
        return this.enchantmentcontrol$isEnabled();
    }
}
