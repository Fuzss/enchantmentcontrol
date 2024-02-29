package fuzs.enchantmentcontrol.data;

import fuzs.enchantmentcontrol.CommonAbstractions;
import fuzs.enchantmentcontrol.api.v1.tags.EnchantmentCategoryTags;
import fuzs.enchantmentcontrol.init.EnchantmentCategoryTagsImpl;
import fuzs.enchantmentcontrol.world.item.enchantment.EnchantmentFeature;
import fuzs.enchantmentcontrol.world.item.enchantment.EnchantmentHolder;
import fuzs.puzzleslib.api.data.v2.AbstractTagProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.function.Predicate;

public class DynamicItemTagProvider extends AbstractTagProvider.Items {
    private final boolean skipHolderValidation;

    private DynamicItemTagProvider(DataProviderContext context, boolean skipHolderValidation) {
        super(context);
        this.skipHolderValidation = skipHolderValidation;
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        for (EnchantmentHolder holder : EnchantmentHolder.values()) {
            Enchantment enchantment = holder.getEnchantment();
            if (!this.skipHolderValidation) {
                EnchantmentFeature.testHolderIsNull(enchantment);
            }
            addAllMatchingItems(this.tag(holder.getEnchantingTableItemTag()), (Item item) -> {
                // we do not use our dynamic enchantment category tags here since Forge overrides that with a method which allows for additional items
                return CommonAbstractions.canApplyAtEnchantingTable(enchantment, item.getDefaultInstance());
            });
            addAllMatchingItems(this.tag(holder.getAnvilItemTag()), (Item item) -> {
                return enchantment.canEnchant(item.getDefaultInstance());
            });
        }
        for (EnchantmentCategory enchantmentCategory : EnchantmentCategory.values()) {
            TagKey<Item> tagKey = EnchantmentCategoryTagsImpl.getTagKey(enchantmentCategory);
            if (tagKey != null) {
                addAllMatchingItems(this.tag(tagKey), enchantmentCategory::canEnchant);
            }
        }
    }

    public static void addAllMatchingItems(IntrinsicTagAppender<Item> tagAppender, Predicate<Item> predicate) {
        for (Item item : BuiltInRegistries.ITEM) {
            if (predicate.test(item)) {
                tagAppender.add(item);
            }
        }
    }

    public static DataProviderContext.Factory create(boolean skipHolderValidation) {
        return context -> new DynamicItemTagProvider(context, skipHolderValidation);
    }
}
