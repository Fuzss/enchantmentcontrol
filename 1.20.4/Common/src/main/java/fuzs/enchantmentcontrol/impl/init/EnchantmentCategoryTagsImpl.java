package fuzs.enchantmentcontrol.impl.init;

import fuzs.enchantmentcontrol.api.v1.EnchantmentCategories;
import fuzs.enchantmentcontrol.impl.EnchantmentControlMod;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.Map;

@ApiStatus.Internal
public final class EnchantmentCategoryTagsImpl {
    /**
     * Do not use enum map since the enum can change at any time.
     */
    private static final Map<EnchantmentCategory, TagKey<Item>> ENCHANTMENT_CATEGORY_TAGS = new IdentityHashMap<>();

    static {
        register(EnchantmentCategories.ARMOR, "armor");
        register(EnchantmentCategories.FOOT_ARMOR, "foot_armor");
        register(EnchantmentCategories.LEG_ARMOR, "leg_armor");
        register(EnchantmentCategories.CHEST_ARMOR, "chest_armor");
        register(EnchantmentCategories.HEAD_ARMOR, "head_armor");
        register(EnchantmentCategories.WEAPON, "weapon");
        register(EnchantmentCategories.SWORD, "sword");
        register(EnchantmentCategories.MINING, "mining");
        register(EnchantmentCategories.MINING_LOOT, "mining_loot");
        register(EnchantmentCategories.FISHING, "fishing");
        register(EnchantmentCategories.TRIDENT, "trident");
        register(EnchantmentCategories.DURABILITY, "durability");
        register(EnchantmentCategories.BOW, "bow");
        register(EnchantmentCategories.EQUIPPABLE, "equippable");
        register(EnchantmentCategories.CROSSBOW, "crossbow");
        register(EnchantmentCategories.SHEARS, EnchantmentControlMod.id("shears"));
        register(EnchantmentCategories.AXE, EnchantmentControlMod.id("axe"));
        register(EnchantmentCategories.HORSE_ARMOR, EnchantmentControlMod.id("horse_armor"));
        register(EnchantmentCategories.SHIELD, EnchantmentControlMod.id("shield"));
    }

    private EnchantmentCategoryTagsImpl() {
        // NO-OP
    }

    @Nullable
    public static TagKey<Item> getTagKey(EnchantmentCategory enchantmentCategory) {
        return ENCHANTMENT_CATEGORY_TAGS.get(enchantmentCategory);
    }

    public static TagKey<Item> makeTagKey(EnchantmentCategory enchantmentCategory) {
        String s = Util.sanitizeName(enchantmentCategory.name(), ResourceLocation::validPathChar);
        return makeTagKey(s);
    }

    public static TagKey<Item> makeTagKey(String s) {
        return makeTagKey(new ResourceLocation(s));
    }

    public static TagKey<Item> makeTagKey(ResourceLocation resourceLocation) {
        return TagKey.create(Registries.ITEM, resourceLocation.withPrefix("enchantable/"));
    }

    private static void register(EnchantmentCategory enchantmentCategory, String s) {
        register(enchantmentCategory, new ResourceLocation(s));
    }

    private static void register(EnchantmentCategory enchantmentCategory, ResourceLocation resourceLocation) {
        ENCHANTMENT_CATEGORY_TAGS.put(enchantmentCategory, makeTagKey(resourceLocation));
    }
}
