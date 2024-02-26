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
        EnchantmentHolder holder = this.getHolder();
        if (holder != null) valueConsumer.accept(valueExtractor.apply(holder));
    }

    @Override
    default FeatureFlagSet requiredFeatures() {
        return FeatureFlags.DEFAULT_FLAGS;
    }

    default boolean isEnabled() {
        EnchantmentHolder holder = this.getHolder();
        return holder == null || !holder.isUnobtainable();
    }

    @Override
    default boolean isEnabled(FeatureFlagSet enabledFeatures) {
        return this.isEnabled();
    }
}
