package fuzs.enchantmentcontrol.impl.data;

import fuzs.enchantmentcontrol.api.v1.tags.EnchantmentTags;
import fuzs.enchantmentcontrol.impl.init.EnchantmentCategoryTagsImpl;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.api.data.v2.tags.AbstractTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class ModItemTagProvider extends AbstractTagProvider<Item> {

    public ModItemTagProvider(DataProviderContext context) {
        super(Registries.ITEM, context);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        // generate all those empty so other mods can use them for data generation
        // do not use holders, they are created on load complete which does not run during data generation
        for (Enchantment enchantment : BuiltInRegistries.ENCHANTMENT) {
            this.add(EnchantmentTags.getEnchantingTableTag(enchantment));
            this.add(EnchantmentTags.getAnvilTag(enchantment));
        }
        for (EnchantmentCategory enchantmentCategory : EnchantmentCategory.values()) {
            TagKey<Item> tagKey = EnchantmentCategoryTagsImpl.getTagKey(enchantmentCategory);
            if (tagKey != null) this.add(tagKey);
        }
    }
}
