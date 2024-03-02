package fuzs.enchantmentcontrol.impl.data;

import fuzs.enchantmentcontrol.impl.CommonAbstractions;
import fuzs.enchantmentcontrol.impl.init.EnchantmentCategoryTagsImpl;
import fuzs.enchantmentcontrol.impl.world.item.enchantment.EnchantmentHolder;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.api.data.v2.tags.AbstractTagAppender;
import fuzs.puzzleslib.api.data.v2.tags.AbstractTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.function.Predicate;

public class DynamicItemTagProvider extends AbstractTagProvider<Item> {
    private final boolean skipHolderValidation;

    private DynamicItemTagProvider(DataProviderContext context, boolean skipHolderValidation) {
        super(Registries.ITEM, context);
        this.skipHolderValidation = skipHolderValidation;
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        for (EnchantmentHolder holder : EnchantmentHolder.values()) {
            Enchantment enchantment = holder.getEnchantment();
            if (!this.skipHolderValidation) {
                EnchantmentHolder.testIsNull(enchantment);
            }
            addAllMatchingItems(this.add(holder.getEnchantingTableItemTag()), (Item item) -> {
                // we do not use our dynamic enchantment category tags here since Forge overrides that with a method which allows for additional items
                return CommonAbstractions.canApplyAtEnchantingTable(enchantment, item.getDefaultInstance());
            });
            addAllMatchingItems(this.add(holder.getAnvilItemTag()), (Item item) -> {
                return enchantment.canEnchant(item.getDefaultInstance());
            });
        }
        for (EnchantmentCategory enchantmentCategory : EnchantmentCategory.values()) {
            TagKey<Item> tagKey = EnchantmentCategoryTagsImpl.getTagKey(enchantmentCategory);
            if (tagKey != null) {
                addAllMatchingItems(this.add(tagKey), enchantmentCategory::canEnchant);
            }
        }
    }

    @Override
    public AbstractTagAppender<Item> add(TagKey<Item> tagKey) {
        return super.add(tagKey).setReplace(this.skipHolderValidation);
    }

    public static void addAllMatchingItems(AbstractTagAppender<Item> tagAppender, Predicate<Item> predicate) {
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
