package fuzs.enchantmentcontrol.api.v1.tags;

import fuzs.enchantmentcontrol.init.ModRegistry;
import fuzs.enchantmentcontrol.world.item.enchantment.EnchantmentFeature;
import fuzs.enchantmentcontrol.world.item.enchantment.EnchantmentHolder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * All available tags for controlling enchantment properties. Some tags are universal, while other exist for each
 * enchantment.
 */
public final class EnchantmentDataTags {
    /**
     * Enchantments that will be offered by librarian villagers on enchanted books.
     */
    public static final TagKey<Enchantment> TRADEABLE = ModRegistry.TRADEABLE_ENCHANTMENT_TAG;
    /**
     * Enchantments that cannot be obtained at an enchanting table.
     */
    public static final TagKey<Enchantment> TREASURE = ModRegistry.TREASURE_ENCHANTMENT_TAG;
    /**
     * Enchantments that have negative effects, show their name colored red on tooltips and cannot be removed from an
     * item via repairing and in a grindstone.
     */
    public static final TagKey<Enchantment> CURSES = ModRegistry.CURSES_ENCHANTMENT_TAG;
    /**
     * Enchantments that can be selected for randomly enchanted loot.
     */
    public static final TagKey<Enchantment> DISCOVERABLE = ModRegistry.DISCOVERABLE_ENCHANTMENT_TAG;
    /**
     * Enchantments that are disabled in-game. They will not be accessible in commands, will not be added to any loot,
     * will not show up in trades, in the creative inventory, and when still somehow obtained will not have any effect.
     */
    public static final TagKey<Enchantment> UNOBTAINABLE = ModRegistry.UNOBTAINABLE_ENCHANTMENT_TAG;
    /**
     * Enchantments that will not have any changes at all applied by this mod.
     */
    public static final TagKey<Enchantment> UNTOUCHED = ModRegistry.UNTOUCHED_ENCHANTMENT_TAG;

    private EnchantmentDataTags() {
        // NO-OP
    }

    /**
     * Items can randomly receive the enchantment in an enchanting table.
     *
     * @param enchantment the enchantment
     * @return the tag key
     */
    public static TagKey<Item> getEnchantingTableItemTag(Enchantment enchantment) {
        EnchantmentHolder holder = ((EnchantmentFeature) enchantment).getHolder();
        return holder != null ?
                holder.getEnchantingTableItemTag() :
                ModRegistry.createEnchantingTableItemTag(enchantment);
    }

    /**
     * Items can receive the enchantment in an anvil when combining with other items of the same type or enchanted
     * books.
     *
     * @param enchantment the enchantment
     * @return the tag key
     */
    public static TagKey<Item> getAnvilItemTag(Enchantment enchantment) {
        EnchantmentHolder holder = ((EnchantmentFeature) enchantment).getHolder();
        return holder != null ? holder.getAnvilItemTag() : ModRegistry.createAnvilItemTag(enchantment);
    }

    /**
     * Enchantments that are not allowed to be put on an item when the given enchantment is present.
     *
     * @param enchantment the enchantment
     * @return the tag key
     */
    public static TagKey<Enchantment> getIncompatibleEnchantmentTag(Enchantment enchantment) {
        EnchantmentHolder holder = ((EnchantmentFeature) enchantment).getHolder();
        return holder != null ?
                holder.getIncompatibleEnchantmentTag() :
                ModRegistry.createIncompatibleEnchantmentTag(enchantment);
    }
}
