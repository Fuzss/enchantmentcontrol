package fuzs.enchantmentcontrol.api.v1.tags;

import fuzs.enchantmentcontrol.api.v1.EnchantmentCategories;
import fuzs.enchantmentcontrol.impl.init.EnchantmentCategoryTagsImpl;
import net.minecraft.CharPredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

/**
 * All supported enchantment categories are automatically assigned a corresponding item tag which holds the same values,
 * so it can be used for configuring item compatibility for individual enchantments.
 * <p>
 * This class lists all supported enchantment categories.
 * <p>
 * The tag names are adapted from <code>enchantable</code> tags in Minecraft 1.20.5 where possible.
 */
public final class EnchantmentCategoryTags {
    /**
     * Tag key: <code>#minecraft:enchantable/armor</code>
     * <p>
     * Matching items: {@link net.minecraft.world.item.ArmorItem}
     * <p>
     * Item examples: <code>minecraft:golden_helmet</code>, <code>minecraft:diamond_chestplate</code>,
     * <code>minecraft:leather_leggings</code>, <code>minecraft:iron_boots</code>
     * <p>
     * Enchantment examples: <code>minecraft:protection</code>, <code>minecraft:blast_protection</code>
     */
    public static final TagKey<Item> ARMOR = getTagKey(EnchantmentCategories.ARMOR);
    /**
     * Tag key: <code>#minecraft:enchantable/foot_armor</code>
     * <p>
     * Matching items: {@link net.minecraft.world.item.ArmorItem} for
     * {@link net.minecraft.world.entity.EquipmentSlot#FEET}
     * <p>
     * Item examples: <code>minecraft:leather_boots</code>, <code>minecraft:iron_boots</code>
     * <p>
     * Enchantment examples: <code>minecraft:depth_strider</code>, <code>minecraft:feather_falling</code>
     */
    public static final TagKey<Item> FOOT_ARMOR = getTagKey(EnchantmentCategories.FOOT_ARMOR);
    /**
     * Tag key: <code>#minecraft:enchantable/leg_armor</code>
     * <p>
     * Matching items: {@link net.minecraft.world.item.ArmorItem} for
     * {@link net.minecraft.world.entity.EquipmentSlot#LEGS}
     * <p>
     * Item examples: <code>minecraft:chainmail_leggings</code>, <code>minecraft:golden_leggings</code>
     * <p>
     * Enchantment examples: <code>minecraft:swift_sneak</code>
     */
    public static final TagKey<Item> LEG_ARMOR = getTagKey(EnchantmentCategories.LEG_ARMOR);
    /**
     * Tag key: <code>#minecraft:enchantable/chest_armor</code>
     * <p>
     * Matching items: {@link net.minecraft.world.item.ArmorItem} for
     * {@link net.minecraft.world.entity.EquipmentSlot#CHEST}
     * <p>
     * Item examples: <code>minecraft:golden_chestplate</code>, <code>minecraft:iron_chestplate</code>
     * <p>
     * Enchantment examples: <code>minecraft:thorns</code>
     */
    public static final TagKey<Item> CHEST_ARMOR = getTagKey(EnchantmentCategories.CHEST_ARMOR);
    /**
     * Tag key: <code>#minecraft:enchantable/head_armor</code>
     * <p>
     * Matching items: {@link net.minecraft.world.item.ArmorItem} for
     * {@link net.minecraft.world.entity.EquipmentSlot#HEAD}
     * <p>
     * Item examples: <code>minecraft:diamond_helmet</code>, <code>minecraft:turtle_helmet</code>
     * <p>
     * Enchantment examples: <code>minecraft:aqua_affinity</code>, <code>minecraft:respiration</code>
     */
    public static final TagKey<Item> HEAD_ARMOR = getTagKey(EnchantmentCategories.HEAD_ARMOR);
    /**
     * Tag key: <code>#minecraft:enchantable/weapon</code>
     * <p>
     * Item examples: <code>minecraft:netherite_sword</code>, <code>minecraft:wooden_axe</code>
     * <p>
     * Matching items: {@link net.minecraft.world.item.SwordItem}, {@link net.minecraft.world.item.AxeItem}
     */
    public static final TagKey<Item> WEAPON = getTagKey(EnchantmentCategories.WEAPON);
    /**
     * Tag key: <code>#minecraft:enchantable/sword</code>
     * <p>
     * Matching items: {@link net.minecraft.world.item.SwordItem}
     * <p>
     * Item examples: <code>minecraft:stone_sword</code>
     * <p>
     * Enchantment examples: <code>minecraft:sharpness</code>, <code>minecraft:looting</code>
     */
    public static final TagKey<Item> SWORD = getTagKey(EnchantmentCategories.SWORD);
    /**
     * Tag key: <code>#minecraft:enchantable/mining</code>
     * <p>
     * Matching items: {@link net.minecraft.world.item.DiggerItem}, {@link net.minecraft.world.item.ShearsItem}
     * <p>
     * Item examples: <code>minecraft:iron_pickaxe</code>, <code>minecraft:golden_shovel</code>, ,
     * <code>minecraft:wooden_axe</code>, <code>minecraft:stone_hoe</code>, <code>minecraft:shears</code>
     */
    public static final TagKey<Item> MINING = getTagKey(EnchantmentCategories.MINING);
    /**
     * Tag key: <code>#minecraft:enchantable/mining_loot</code>
     * <p>
     * Matching items: {@link net.minecraft.world.item.DiggerItem}
     * <p>
     * Item examples: <code>minecraft:diamond_pickaxe</code>, <code>minecraft:iron_shovel</code>, ,
     * <code>minecraft:golden_axe</code>, <code>minecraft:wooden_hoe</code>
     * <p>
     * Enchantment examples: <code>minecraft:efficiency</code>, <code>minecraft:silk_touch</code>
     */
    public static final TagKey<Item> MINING_LOOT = getTagKey(EnchantmentCategories.MINING_LOOT);
    /**
     * Tag key: <code>#minecraft:enchantable/fishing</code>
     * <p>
     * Matching items: {@link net.minecraft.world.item.FishingRodItem}
     * <p>
     * Item examples: <code>minecraft:fishing_rod</code>
     * <p>
     * Enchantment examples: <code>minecraft:lure</code>, <code>minecraft:luck_of_the_sea</code>
     */
    public static final TagKey<Item> FISHING = getTagKey(EnchantmentCategories.FISHING);
    /**
     * Tag key: <code>#minecraft:enchantable/trident</code>
     * <p>
     * Matching items: {@link net.minecraft.world.item.TridentItem}
     * <p>
     * Item examples: <code>minecraft:trident</code>
     * <p>
     * Enchantment examples: <code>minecraft:impaling</code>, <code>minecraft:channeling</code>
     */
    public static final TagKey<Item> TRIDENT = getTagKey(EnchantmentCategories.TRIDENT);
    /**
     * Tag key: <code>#minecraft:enchantable/durability</code>
     * <p>
     * Matching items: {@link net.minecraft.world.item.Item} with {@link Item#maxDamage} <code>&gt; 0</code>
     * <p>
     * Item examples: <code>minecraft:bow</code>, <code>minecraft:elytra</code>, <code>minecraft:shield</code>,
     * <code>minecraft:trident</code>, <code>minecraft:diamond_helmet</code>, <code>minecraft:stone_pickaxe</code>
     * <p>
     * Enchantment examples: <code>minecraft:unbreaking</code>, <code>minecraft:mending</code>
     */
    public static final TagKey<Item> DURABILITY = getTagKey(EnchantmentCategories.DURABILITY);
    /**
     * Tag key: <code>#minecraft:enchantable/bow</code>
     * <p>
     * Matching items: {@link net.minecraft.world.item.BowItem}
     * <p>
     * Item examples: <code>minecraft:bow</code>
     * <p>
     * Enchantment examples: <code>minecraft:power</code>, <code>minecraft:punch</code>
     */
    public static final TagKey<Item> BOW = getTagKey(EnchantmentCategories.BOW);
    /**
     * Tag key: <code>#minecraft:enchantable/equippable</code>
     * <p>
     * Matching items: {@link net.minecraft.world.item.Equipable}
     * <p>
     * Item examples: <code>minecraft:diamond_boots</code>, <code>minecraft:iron_chestplate</code>,
     * <code>minecraft:elytra</code>
     * <p>
     * Enchantment examples: <code>minecraft:binding_curse</code>
     */
    public static final TagKey<Item> EQUIPPABLE = getTagKey(EnchantmentCategories.EQUIPPABLE);
    /**
     * Tag key: <code>#minecraft:enchantable/crossbow</code>
     * <p>
     * Matching items: {@link net.minecraft.world.item.CrossbowItem}
     * <p>
     * Item examples: <code>minecraft:crossbow</code>
     * <p>
     * Enchantment examples: <code>minecraft:quick_charge</code>, <code>minecraft:piercing</code>
     */
    public static final TagKey<Item> CROSSBOW = getTagKey(EnchantmentCategories.CROSSBOW);
    /**
     * Tag key: <code>#minecraft:enchantable/vanishing</code>
     * <p>
     * Matching items: {@link net.minecraft.world.item.Vanishable}
     * <p>
     * Item examples: <code>#minecraft:enchantable/durability</code>, <code>minecraft:compass</code>
     * <p>
     * Enchantment examples: <code>minecraft:vanishing_curse</code>
     */
    public static final TagKey<Item> VANISHING = getTagKey(EnchantmentCategories.VANISHING);
    /**
     * Tag key: <code>#enchantmentcontrol:enchantable/shears</code>
     * <p>
     * Matching items: {@link net.minecraft.world.item.ShearsItem}
     * <p>
     * Item examples: <code>minecraft:shears</code>
     */
    public static final TagKey<Item> SHEARS = getTagKey(EnchantmentCategories.SHEARS);
    /**
     * Tag key: <code>#enchantmentcontrol:enchantable/axe</code>
     * <p>
     * Matching items: {@link net.minecraft.world.item.AxeItem}
     * <p>
     * Item examples: <code>minecraft:golden_axe</code>, <code>minecraft:wooden_axe</code>
     */
    public static final TagKey<Item> AXE = getTagKey(EnchantmentCategories.AXE);
    /**
     * Tag key: <code>#enchantmentcontrol:enchantable/horse_armor</code>
     * <p>
     * Matching items: {@link net.minecraft.world.item.HorseArmorItem}
     * <p>
     * Item examples: <code>minecraft:diamond_horse_armor</code>, <code>minecraft:leather_horse_armor</code>
     */
    public static final TagKey<Item> HORSE_ARMOR = getTagKey(EnchantmentCategories.HORSE_ARMOR);
    /**
     * Tag key: <code>#enchantmentcontrol:enchantable/shield</code>
     * <p>
     * Matching items: {@link net.minecraft.world.item.ShieldItem}
     * <p>
     * Item examples: <code>minecraft:shield</code>
     */
    public static final TagKey<Item> SHIELD = getTagKey(EnchantmentCategories.SHIELD);

    private EnchantmentCategoryTags() {
        // NO-OP
    }

    /**
     * Get the item tag for an enchantment category containing the same item values.
     * <p>
     * For non-default enchantment categories the tag key will simply be the enum name sanitized via
     * {@link net.minecraft.Util#sanitizeName(String, CharPredicate)}.
     *
     * @param enchantmentCategory the enchantment category
     * @return the corresponding item tag
     */
    public static TagKey<Item> getTagKey(EnchantmentCategory enchantmentCategory) {
        TagKey<Item> tagKey = EnchantmentCategoryTagsImpl.getTagKey(enchantmentCategory);
        return tagKey != null ? tagKey : makeTagKey(enchantmentCategory);
    }

    /**
     * Creates an item tag using the <code>enchantable/</code> prefix.
     *
     * @param enchantmentCategory the enchantment category
     * @return the item tag
     */
    public static TagKey<Item> makeTagKey(EnchantmentCategory enchantmentCategory) {
        return EnchantmentCategoryTagsImpl.makeTagKey(enchantmentCategory);
    }

    /**
     * Creates an item tag using the <code>enchantable/</code> prefix.
     *
     * @param s the resource location path
     * @return the item tag
     */
    public static TagKey<Item> makeTagKey(String s) {
        return EnchantmentCategoryTagsImpl.makeTagKey(s);
    }

    /**
     * Creates an item tag using the <code>enchantable/</code> prefix.
     *
     * @param resourceLocation the resource location
     * @return the item tag
     */
    public static TagKey<Item> makeTagKey(ResourceLocation resourceLocation) {
        return EnchantmentCategoryTagsImpl.makeTagKey(resourceLocation);
    }
}
