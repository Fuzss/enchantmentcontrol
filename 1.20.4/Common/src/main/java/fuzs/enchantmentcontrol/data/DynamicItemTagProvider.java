package fuzs.enchantmentcontrol.data;

import fuzs.enchantmentcontrol.CommonAbstractions;
import fuzs.enchantmentcontrol.world.item.enchantment.EnchantmentFeature;
import fuzs.enchantmentcontrol.world.item.enchantment.EnchantmentHolder;
import fuzs.puzzleslib.api.data.v2.AbstractTagProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.function.Predicate;

public class DynamicItemTagProvider extends AbstractTagProvider.Items {

    public DynamicItemTagProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        for (EnchantmentHolder holder : EnchantmentHolder.values()) {
            Enchantment enchantment = holder.getEnchantment();
            EnchantmentFeature.testHolderIsNull(enchantment);
            this.populateTag(holder.getEnchantingTableItemTag(), (Item item) -> {
                return CommonAbstractions.canApplyAtEnchantingTable(enchantment, item.getDefaultInstance());
            });
            this.populateTag(holder.getAnvilItemTag(), (Item item) -> {
                return enchantment.canEnchant(item.getDefaultInstance());
            });
        }
    }

    private void populateTag(TagKey<Item> tagKey, Predicate<Item> predicate) {
        IntrinsicTagAppender<Item> tagAppender = this.tag(tagKey);
        for (Item item : BuiltInRegistries.ITEM) {
            if (predicate.test(item)) {
                tagAppender.add(item);
            }
        }
    }
}
