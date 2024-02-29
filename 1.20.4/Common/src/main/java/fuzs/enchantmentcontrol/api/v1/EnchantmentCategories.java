package fuzs.enchantmentcontrol.api.v1;

import fuzs.enchantmentcontrol.EnchantmentControlMod;
import fuzs.enchantmentcontrol.api.v1.tags.EnchantmentCategoryTags;
import fuzs.extensibleenums.api.v2.CommonAbstractions;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

/**
 * Lists all default enchantment categories made available to users via dynamically generated item tags for data pack
 * configuration.
 *
 * @see EnchantmentCategoryTags
 */
public final class EnchantmentCategories {
    /**
     * @see EnchantmentCategoryTags#FOOT_ARMOR_ITEM_TAG
     */
    public static final EnchantmentCategory FOOT_ARMOR = EnchantmentCategory.ARMOR_FEET;
    /**
     * @see EnchantmentCategoryTags#LEG_ARMOR_ITEM_TAG
     */
    public static final EnchantmentCategory LEG_ARMOR = EnchantmentCategory.ARMOR_LEGS;
    /**
     * @see EnchantmentCategoryTags#CHEST_ARMOR_ITEM_TAG
     */
    public static final EnchantmentCategory CHEST_ARMOR = EnchantmentCategory.ARMOR_CHEST;
    /**
     * @see EnchantmentCategoryTags#HEAD_ARMOR_ITEM_TAG
     */
    public static final EnchantmentCategory HEAD_ARMOR = EnchantmentCategory.ARMOR_HEAD;
    /**
     * @see EnchantmentCategoryTags#ARMOR_ITEM_TAG
     */
    public static final EnchantmentCategory ARMOR = EnchantmentCategory.ARMOR;
    /**
     * @see EnchantmentCategoryTags#WEAPON_ITEM_TAG
     */
    public static final EnchantmentCategory WEAPON = CommonAbstractions.createEnchantmentCategory(EnchantmentControlMod.id(
            "weapon"), item -> item instanceof SwordItem || item instanceof AxeItem);
    /**
     * @see EnchantmentCategoryTags#SWORD_ITEM_TAG
     */
    public static final EnchantmentCategory SWORD = EnchantmentCategory.WEAPON;
    /**
     * @see EnchantmentCategoryTags#MINING_ITEM_TAG
     */
    public static final EnchantmentCategory MINING = CommonAbstractions.createEnchantmentCategory(EnchantmentControlMod.id(
            "mining"), item -> item instanceof DiggerItem || item instanceof ShearsItem);
    /**
     * @see EnchantmentCategoryTags#MINING_LOOT_ITEM_TAG
     */
    public static final EnchantmentCategory MINING_LOOT = EnchantmentCategory.DIGGER;
    /**
     * @see EnchantmentCategoryTags#FISHING_ITEM_TAG
     */
    public static final EnchantmentCategory FISHING = EnchantmentCategory.FISHING_ROD;
    /**
     * @see EnchantmentCategoryTags#TRIDENT_ITEM_TAG
     */
    public static final EnchantmentCategory TRIDENT = EnchantmentCategory.TRIDENT;
    /**
     * @see EnchantmentCategoryTags#DURABILITY_ITEM_TAG
     */
    public static final EnchantmentCategory DURABILITY = EnchantmentCategory.BREAKABLE;
    /**
     * @see EnchantmentCategoryTags#BOW_ITEM_TAG
     */
    public static final EnchantmentCategory BOW = EnchantmentCategory.BOW;
    /**
     * @see EnchantmentCategoryTags#EQUIPPABLE_ITEM_TAG
     */
    public static final EnchantmentCategory EQUIPPABLE = EnchantmentCategory.WEARABLE;
    /**
     * @see EnchantmentCategoryTags#CROSSBOW_ITEM_TAG
     */
    public static final EnchantmentCategory CROSSBOW = EnchantmentCategory.CROSSBOW;
    /**
     * @see EnchantmentCategoryTags#VANISHING_ITEM_TAG
     */
    public static final EnchantmentCategory VANISHING = EnchantmentCategory.VANISHABLE;
    /**
     * @see EnchantmentCategoryTags#SHEARS_ITEM_TAG
     */
    public static final EnchantmentCategory SHEARS = CommonAbstractions.createEnchantmentCategory(EnchantmentControlMod.id(
            "shears"), item -> item instanceof ShearsItem);
    /**
     * @see EnchantmentCategoryTags#AXE_ITEM_TAG
     */
    public static final EnchantmentCategory AXE = CommonAbstractions.createEnchantmentCategory(EnchantmentControlMod.id(
            "axe"), item -> item instanceof AxeItem);
    /**
     * @see EnchantmentCategoryTags#HORSE_ARMOR_ITEM_TAG
     */
    public static final EnchantmentCategory HORSE_ARMOR = CommonAbstractions.createEnchantmentCategory(
            EnchantmentControlMod.id("horse_armor"),
            item -> item instanceof HorseArmorItem
    );
    /**
     * @see EnchantmentCategoryTags#SHIELD_ITEM_TAG
     */
    public static final EnchantmentCategory SHIELD = CommonAbstractions.createEnchantmentCategory(EnchantmentControlMod.id(
            "shield"), item -> item instanceof ShieldItem);

    private EnchantmentCategories() {
        // NO-OP
    }
}
