package fuzs.enchantmentcontrol.data;

import fuzs.enchantmentcontrol.init.ModRegistry;
import fuzs.enchantmentcontrol.world.item.enchantment.EnchantmentFeature;
import fuzs.puzzleslib.api.data.v2.AbstractTagProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.function.Predicate;

public class DynamicEnchantmentTagProvider extends AbstractTagProvider.Intrinsic<Enchantment> {

    public DynamicEnchantmentTagProvider(DataProviderContext context) {
        super(Registries.ENCHANTMENT, context);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        this.buildEnchantmentTag(ModRegistry.DISCOVERABLE_ENCHANTMENT_TAG, Enchantment::isDiscoverable);
        this.buildEnchantmentTag(ModRegistry.TREASURE_ENCHANTMENT_TAG, Enchantment::isTreasureOnly);
        this.buildEnchantmentTag(ModRegistry.TRADEABLE_ENCHANTMENT_TAG, Enchantment::isTradeable);
        this.buildEnchantmentTag(ModRegistry.CURSES_ENCHANTMENT_TAG, Enchantment::isCurse);
        for (Enchantment enchantment : BuiltInRegistries.ENCHANTMENT) {
            EnchantmentFeature.testHolderIsNull(enchantment);
            this.buildEnchantmentTag(ModRegistry.createIncompatibleEnchantmentTag(enchantment), (Enchantment other) -> {
                return enchantment != other && !enchantment.isCompatibleWith(other);
            }, false);
        }
    }

    private void buildEnchantmentTag(TagKey<Enchantment> tagKey, Predicate<Enchantment> predicate) {
        this.buildEnchantmentTag(tagKey, predicate, true);
    }

    private void buildEnchantmentTag(TagKey<Enchantment> tagKey, Predicate<Enchantment> predicate, boolean testHolder) {
        IntrinsicTagAppender<Enchantment> tagAppender = this.tag(tagKey);
        for (Enchantment enchantment : BuiltInRegistries.ENCHANTMENT) {
            if (testHolder) EnchantmentFeature.testHolderIsNull(enchantment);
            if (predicate.test(enchantment)) tagAppender.add(enchantment);
        }
    }
}
