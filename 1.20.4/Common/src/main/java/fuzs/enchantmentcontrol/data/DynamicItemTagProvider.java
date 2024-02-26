package fuzs.enchantmentcontrol.data;

import fuzs.enchantmentcontrol.init.ModRegistry;
import fuzs.enchantmentcontrol.world.item.enchantment.EnchantmentFeature;
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
        for (Enchantment enchantment : BuiltInRegistries.ENCHANTMENT) {
            EnchantmentFeature.testHolderIsNull(enchantment);
            this.populateTag(ModRegistry.createEnchantingTableItemTag(enchantment), (Item item) -> {
                return enchantment.category.canEnchant(item);
            });
            this.populateTag(ModRegistry.createAnvilItemTag(enchantment), (Item item) -> {
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
