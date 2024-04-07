package fuzs.enchantmentcontrol.api.v1.tags;

import fuzs.enchantmentcontrol.impl.init.ModRegistry;
import fuzs.enchantmentcontrol.impl.world.item.enchantment.EnchantmentFeature;
import fuzs.enchantmentcontrol.impl.world.item.enchantment.EnchantmentHolder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * All available tags for controlling enchantment properties. Some tags are universal, while other exist for each
 * enchantment.
 */
public final class EnchantmentTags {
    /**
     * Enchantments that will be offered by librarian villagers on enchanted books.
     * <p>
     * Equivalent to {@link Enchantment#isTradeable()}.
     */
    public static final TagKey<Enchantment> IS_TRADEABLE = ModRegistry.IS_TRADEABLE_ENCHANTMENT_TAG;
    /**
     * Enchantments that cannot be obtained at an enchanting table. They are usually found in chest loot, or obtained
     * from trading. Librarian villagers will charge double for these.
     * <p>
     * Equivalent to {@link Enchantment#isTreasureOnly()}.
     */
    public static final TagKey<Enchantment> IS_TREASURE = ModRegistry.IS_TREASURE_ENCHANTMENT_TAG;
    /**
     * Enchantments that have negative effects, show their name colored red on tooltips and cannot be removed from an
     * item via repairing and in a grindstone.
     * <p>
     * Equivalent to {@link Enchantment#isCurse()}.
     */
    public static final TagKey<Enchantment> IS_CURSE = ModRegistry.IS_CURSE_ENCHANTMENT_TAG;
    /**
     * Enchantments that can be selected for randomly enchanted loot.
     * <p>
     * Equivalent to {@link Enchantment#isDiscoverable()}.
     */
    public static final TagKey<Enchantment> IS_DISCOVERABLE = ModRegistry.IS_DISCOVERABLE_ENCHANTMENT_TAG;
    /**
     * Enchantments that can be put on enchanted books when enchanting. Does not affect anvil mechanics nor enchanted
     * books generated as part of loot.
     */
    public static final TagKey<Enchantment> IS_ALLOWED_ON_BOOKS = ModRegistry.IS_ALLOWED_ON_BOOKS_ENCHANTMENT_TAG;
    /**
     * Enchantments that are disabled in-game. They will not be accessible in commands, will not be added to any loot,
     * will not show up in trades, in the creative inventory, and when still somehow obtained will not have any effect.
     */
    public static final TagKey<Enchantment> IS_UNOBTAINABLE = ModRegistry.IS_UNOBTAINABLE_ENCHANTMENT_TAG;
    /**
     * Enchantments that will not have any changes at all applied by this mod.
     */
    public static final TagKey<Enchantment> IS_UNTOUCHED = ModRegistry.IS_UNTOUCHED_ENCHANTMENT_TAG;

    private EnchantmentTags() {
        // NO-OP
    }

    /**
     * Items can randomly receive the enchantment in an enchanting table.
     *
     * @param enchantment the enchantment
     * @return the tag key
     */
    public static TagKey<Item> getEnchantingTableTag(Enchantment enchantment) {
        EnchantmentHolder holder = ((EnchantmentFeature) enchantment).enchantmentcontrol$getHolder();
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
    public static TagKey<Item> getAnvilTag(Enchantment enchantment) {
        EnchantmentHolder holder = ((EnchantmentFeature) enchantment).enchantmentcontrol$getHolder();
        return holder != null ? holder.getAnvilItemTag() : ModRegistry.createAnvilItemTag(enchantment);
    }

    /**
     * Enchantments that are not allowed to be put on an item when the given enchantment is present.
     *
     * @param enchantment the enchantment
     * @return the tag key
     */
    public static TagKey<Enchantment> getIncompatibleTag(Enchantment enchantment) {
        EnchantmentHolder holder = ((EnchantmentFeature) enchantment).enchantmentcontrol$getHolder();
        return holder != null ?
                holder.getIncompatibleEnchantmentTag() :
                ModRegistry.createIncompatibleEnchantmentTag(enchantment);
    }
}
