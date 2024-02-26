package fuzs.enchantmentcontrol.init;

import fuzs.enchantmentcontrol.EnchantmentControl;
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
    public static final TagKey<Enchantment> TRADEABLE_ENCHANTMENT_TAG = TAGS.registerEnchantmentTag("tradeable");
    public static final TagKey<Enchantment> TREASURE_ENCHANTMENT_TAG = TAGS.registerEnchantmentTag("treasure");
    public static final TagKey<Enchantment> CURSES_ENCHANTMENT_TAG = TAGS.registerEnchantmentTag("curses");
    public static final TagKey<Enchantment> DISCOVERABLE_ENCHANTMENT_TAG = TAGS.registerEnchantmentTag("discoverable");
    public static final TagKey<Enchantment> UNOBTAINABLE_ENCHANTMENT_TAG = TAGS.registerEnchantmentTag("unobtainable");

    public static void touch() {

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
