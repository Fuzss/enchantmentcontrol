package fuzs.enchantmentcontrol.impl.data;

import fuzs.enchantmentcontrol.api.v1.tags.EnchantmentTags;
import fuzs.enchantmentcontrol.impl.world.item.enchantment.EnchantmentHolder;
import fuzs.puzzleslib.api.core.v1.CommonAbstractions;
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
        this.buildEnchantmentTag(EnchantmentTags.IS_TREASURE, Enchantment::isTreasureOnly);
        this.buildEnchantmentTag(EnchantmentTags.IS_CURSE, Enchantment::isCurse);
        this.buildEnchantmentTag(EnchantmentTags.IS_TRADEABLE, Enchantment::isTradeable);
        this.buildEnchantmentTag(EnchantmentTags.IS_DISCOVERABLE, Enchantment::isDiscoverable);
        this.buildEnchantmentTag(EnchantmentTags.IS_ALLOWED_ON_BOOKS, CommonAbstractions.INSTANCE::isAllowedOnBooks);
        for (EnchantmentHolder holder : EnchantmentHolder.values()) {
            Enchantment enchantment = holder.getEnchantment();
            if (!this.skipHolderValidation) {
                EnchantmentHolder.isOriginalState(enchantment);
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
                EnchantmentHolder.isOriginalState(enchantment);
            }
            if (predicate.test(enchantment)) {
                tagAppender.add(enchantment);
            }
        }
    }

    public static DataProviderContext.Factory create(boolean skipHolderValidation) {
        return context -> new DynamicEnchantmentTagProvider(context, skipHolderValidation);
    }
}
