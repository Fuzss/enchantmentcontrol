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
     * Enchantments that cannot be obtained at an enchanting table. They are usually found in chest loot, or obtained
     * from trading. Librarian villagers will charge double for these.
     * <p>
     * Example: Mending
     * <p>
     * Equivalent to {@link Enchantment#isTreasureOnly()}.
     */
    public static final TagKey<Enchantment> IS_TREASURE = ModRegistry.IS_TREASURE_ENCHANTMENT_TAG;
    /**
     * Enchantments that have negative effects, show their name colored red on tooltips and cannot be removed from an
     * item via repairing or in a grindstone.
     * <p>
     * Example: Curse of Vanishing
     * <p>
     * Equivalent to {@link Enchantment#isCurse()}.
     */
    public static final TagKey<Enchantment> IS_CURSE = ModRegistry.IS_CURSE_ENCHANTMENT_TAG;
    /**
     * Enchantments that will be offered by librarian villagers on enchanted books.
     * <p>
     * Equivalent to {@link Enchantment#isTradeable()}.
     */
    public static final TagKey<Enchantment> IS_TRADEABLE = ModRegistry.IS_TRADEABLE_ENCHANTMENT_TAG;
    /**
     * Enchantments that can be selected at an enchanting table and for randomly enchanted loot.
     * <p>
     * Enchantments disabling this flag can still be added to loot via the <code>minecraft:enchant_randomly</code> loot
     * function.
     * <p>
     * Counterexample: Soul Speed
     * <p>
     * Equivalent to {@link Enchantment#isDiscoverable()}.
     */
    public static final TagKey<Enchantment> IS_DISCOVERABLE = ModRegistry.IS_DISCOVERABLE_ENCHANTMENT_TAG;
    /**
     * Enchantments that can be put on books at an enchanting table.
     * <p>
     * Enchantments disabling this flag have their enchanted books removed from the creative mode inventory and do not
     * show up on enchanted books generated as part of loot.
     */
    public static final TagKey<Enchantment> IS_ALLOWED_ON_BOOKS = ModRegistry.IS_ALLOWED_ON_BOOKS_ENCHANTMENT_TAG;
    /**
     * Enchantments that are disabled in-game. They will not be accessible in commands, will not be added to any loot,
     * will not show up in trades, in the creative inventory, and when still somehow obtained will not have any effect.
     */
    public static final TagKey<Enchantment> IS_UNOBTAINABLE = ModRegistry.IS_UNOBTAINABLE_ENCHANTMENT_TAG;
    /**
     * Enchantments that will not have any changes at all applied by this mod. Functions like a blacklist.
     */
    public static final TagKey<Enchantment> IS_UNTOUCHED = ModRegistry.IS_UNTOUCHED_ENCHANTMENT_TAG;

    private EnchantmentTags() {
        // NO-OP
    }

    /**
     * Items can randomly receive the enchantment in an enchanting table.
     * <p>
     * Format: <code>&lt;namespace&gt;:enchantable_at_enchanting_table/&lt;path&gt;</code>
     * <p>
     * Example: <code>minecraft:efficiency</code> -> <code>minecraft:enchantable_at_enchanting_table/efficiency</code>
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
     * books. Also controls behavior of <code>/enchant</code>.
     * <p>
     * Format: <code>&lt;namespace&gt;:enchantable_at_anvil/&lt;path&gt;</code>
     * <p>
     * Example: <code>minecraft:sharpness</code> -> <code>minecraft:enchantable_at_anvil/sharpness</code>
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
     * <p>
     * Format: <code>&lt;namespace&gt;:incompatible/&lt;path&gt;</code>
     * <p>
     * Example: <code>minecraft:silk_touch</code> -> <code>minecraft:incompatible/silk_touch</code>
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
