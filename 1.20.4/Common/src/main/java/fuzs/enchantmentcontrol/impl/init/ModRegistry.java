package fuzs.enchantmentcontrol.impl.init;

import fuzs.enchantmentcontrol.impl.EnchantmentControl;
import fuzs.puzzleslib.api.init.v3.tags.BoundTagFactory;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;

public class ModRegistry {
    private static final String ENCHANTING_TABLE_ITEM_TAG_PREFIX = "enchantable_at_enchanting_table";
    private static final String ANVIL_ITEM_TAG_PREFIX = "enchantable_at_anvil";
    private static final String INCOMPATIBLE_ENCHANTMENT_TAG_PREFIX = "incompatible";
    static final BoundTagFactory TAGS = BoundTagFactory.make(EnchantmentControl.MOD_ID);
    public static final TagKey<Enchantment> IS_TRADEABLE_ENCHANTMENT_TAG = TAGS.registerEnchantmentTag("is_tradeable");
    public static final TagKey<Enchantment> IS_TREASURE_ENCHANTMENT_TAG = TAGS.registerEnchantmentTag("is_treasure");
    public static final TagKey<Enchantment> IS_CURSE_ENCHANTMENT_TAG = TAGS.registerEnchantmentTag("is_curse");
    public static final TagKey<Enchantment> IS_DISCOVERABLE_ENCHANTMENT_TAG = TAGS.registerEnchantmentTag("is_discoverable");
    public static final TagKey<Enchantment> IS_ALLOWED_ON_BOOKS_ENCHANTMENT_TAG = TAGS.registerEnchantmentTag("is_allowed_on_books");
    public static final TagKey<Enchantment> IS_UNOBTAINABLE_ENCHANTMENT_TAG = TAGS.registerEnchantmentTag("is_unobtainable");
    public static final TagKey<Enchantment> IS_UNTOUCHED_ENCHANTMENT_TAG = TAGS.registerEnchantmentTag("is_untouched");

    public static void touch() {
        // NO-OP
    }

    public static TagKey<Item> createEnchantingTableItemTag(Enchantment enchantment) {
        return TagKey.create(Registries.ITEM, BuiltInRegistries.ENCHANTMENT.getKey(enchantment).withPrefix(
                ENCHANTING_TABLE_ITEM_TAG_PREFIX + "/"));
    }

    public static TagKey<Item> createAnvilItemTag(Enchantment enchantment) {
        return TagKey.create(Registries.ITEM, BuiltInRegistries.ENCHANTMENT.getKey(enchantment).withPrefix(
                ANVIL_ITEM_TAG_PREFIX + "/"));
    }

    public static TagKey<Enchantment> createIncompatibleEnchantmentTag(Enchantment enchantment) {
        return TagKey.create(Registries.ENCHANTMENT, BuiltInRegistries.ENCHANTMENT.getKey(enchantment).withPrefix(
                INCOMPATIBLE_ENCHANTMENT_TAG_PREFIX + "/"));
    }
}
