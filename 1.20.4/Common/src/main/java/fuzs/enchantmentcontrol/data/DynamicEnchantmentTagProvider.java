package fuzs.enchantmentcontrol.data;

import fuzs.enchantmentcontrol.init.ModRegistry;
import fuzs.enchantmentcontrol.world.item.enchantment.EnchantmentFeature;
import fuzs.enchantmentcontrol.world.item.enchantment.EnchantmentHolder;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.api.data.v2.tags.AbstractTagAppender;
import fuzs.puzzleslib.api.data.v2.tags.AbstractTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.function.Predicate;

public class DynamicEnchantmentTagProvider extends AbstractTagProvider<Enchantment> {
    private final boolean skipHolderValidation;

    private DynamicEnchantmentTagProvider(DataProviderContext context, boolean skipHolderValidation) {
        super(Registries.ENCHANTMENT, context);
        this.skipHolderValidation = skipHolderValidation;
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        this.buildEnchantmentTag(ModRegistry.DISCOVERABLE_ENCHANTMENT_TAG, Enchantment::isDiscoverable);
        this.buildEnchantmentTag(ModRegistry.TREASURE_ENCHANTMENT_TAG, Enchantment::isTreasureOnly);
        this.buildEnchantmentTag(ModRegistry.TRADEABLE_ENCHANTMENT_TAG, Enchantment::isTradeable);
        this.buildEnchantmentTag(ModRegistry.CURSES_ENCHANTMENT_TAG, Enchantment::isCurse);
        for (EnchantmentHolder holder : EnchantmentHolder.values()) {
            Enchantment enchantment = holder.getEnchantment();
            if (!this.skipHolderValidation) {
                EnchantmentFeature.testHolderIsNull(enchantment);
            }
            this.buildEnchantmentTag(holder.getIncompatibleEnchantmentTag(), (Enchantment other) -> {
                return enchantment != other && !enchantment.isCompatibleWith(other);
            }, false);
        }
    }

    private void buildEnchantmentTag(TagKey<Enchantment> tagKey, Predicate<Enchantment> predicate) {
        this.buildEnchantmentTag(tagKey, predicate, true);
    }

    private void buildEnchantmentTag(TagKey<Enchantment> tagKey, Predicate<Enchantment> predicate, boolean testHolder) {
        AbstractTagAppender<Enchantment> tagAppender = this.add(tagKey);
        for (Enchantment enchantment : BuiltInRegistries.ENCHANTMENT) {
            if (testHolder && !this.skipHolderValidation) {
                EnchantmentFeature.testHolderIsNull(enchantment);
            }
            if (predicate.test(enchantment)) {
                tagAppender.add(enchantment);
            }
        }
    }

    @Override
    public AbstractTagAppender<Enchantment> add(TagKey<Enchantment> tagKey) {
        return super.add(tagKey).setReplace(this.skipHolderValidation);
    }

    public static DataProviderContext.Factory create(boolean skipHolderValidation) {
        return context -> new DynamicEnchantmentTagProvider(context, skipHolderValidation);
    }
}
