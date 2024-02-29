package fuzs.enchantmentcontrol.world.item.enchantment;

import com.google.common.base.Preconditions;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;

public interface EnchantmentFeature extends FeatureElement {

    @Nullable EnchantmentHolder getHolder();

    static void testHolderIsNull(Enchantment enchantment) {
        Preconditions.checkState(((EnchantmentFeature) enchantment).getHolder() == null, "holder is still set");
    }

    void setHolder(@Nullable EnchantmentHolder enchantmentHolder);

    default <T> void ifHolderPresent(Function<EnchantmentHolder, T> valueExtractor, Consumer<T> valueConsumer) {
        this.ifHolderPresent(valueExtractor, valueConsumer, null);
    }

    default <T> void ifHolderPresent(Function<EnchantmentHolder, T> valueExtractor, Consumer<T> valueConsumer, @Nullable T fallback) {
        EnchantmentHolder holder = this.getHolder();
        if (holder != null && (this.isEnabled() || fallback != null)) {
            valueConsumer.accept(this.isEnabled() ? valueExtractor.apply(holder) : fallback);
        }
    }

    @Override
    default FeatureFlagSet requiredFeatures() {
        // we don't care about the feature flag set, the vanilla FeatureElement interface is only used to easily support features
        // like registry filtering (like disabled enchantments not showing up as command suggestions)
        // and for other mods to be able to check if an enchantment is enabled without introducing a hard dependency on this mod
        return FeatureFlags.DEFAULT_FLAGS;
    }

    default boolean isEnabled() {
        EnchantmentHolder holder = this.getHolder();
        return holder == null || !holder.isUnobtainable();
    }

    @Override
    default boolean isEnabled(FeatureFlagSet enabledFeatures) {
        // we don't care about the feature flag set, the vanilla FeatureElement interface is only used to easily support features
        // like registry filtering (like disabled enchantments not showing up as command suggestions)
        // and for other mods to be able to check if an enchantment is enabled without introducing a hard dependency on this mod
        return this.isEnabled();
    }
}
