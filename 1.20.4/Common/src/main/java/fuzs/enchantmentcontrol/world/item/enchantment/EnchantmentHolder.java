package fuzs.enchantmentcontrol.world.item.enchantment;

import com.google.common.collect.Maps;
import fuzs.enchantmentcontrol.EnchantmentControlMod;
import fuzs.enchantmentcontrol.handler.EnchantmentClassesCache;
import fuzs.enchantmentcontrol.init.ModRegistry;
import fuzs.extensibleenums.api.v2.CommonAbstractions;
import fuzs.puzzleslib.api.init.v3.registry.RegistryHelper;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;

public final class EnchantmentHolder {
    private static final Map<ResourceLocation, EnchantmentHolder> BY_ENCHANTMENT_ID = Maps.newHashMap();

    private final Holder.Reference<Enchantment> holder;
    private final EnchantmentData originalEnchantmentData;
    private final EnchantmentCategory tagBasedEnchantmentCategory;
    private final TagKey<Item> enchantingTableItemTag;
    private final TagKey<Item> anvilItemTag;
    private final TagKey<Enchantment> incompatibleEnchantmentTag;
    private EnchantmentData enchantmentData;

    public EnchantmentHolder(Holder.Reference<Enchantment> holder) {
        Enchantment enchantment = holder.value();
        ResourceLocation resourceLocation = holder.key().location();
        EnchantmentFeature.testHolderIsNull(enchantment);
        this.holder = holder;
        this.enchantmentData = this.originalEnchantmentData = EnchantmentData.fromEnchantment(enchantment);
        this.tagBasedEnchantmentCategory = this.createTagBasedEnchantmentCategory(resourceLocation);
        this.enchantingTableItemTag = ModRegistry.createEnchantingTableItemTag(enchantment);
        this.anvilItemTag = ModRegistry.createAnvilItemTag(enchantment);
        this.incompatibleEnchantmentTag = ModRegistry.createIncompatibleEnchantmentTag(enchantment);
        BY_ENCHANTMENT_ID.put(resourceLocation, this);
    }

    private EnchantmentCategory createTagBasedEnchantmentCategory(ResourceLocation resourceLocation) {
        return CommonAbstractions.createEnchantmentCategory(EnchantmentControlMod.id(
                resourceLocation.getNamespace() + "/" + resourceLocation.getPath()), (Item item) -> {
            return !this.isUnobtainable() && RegistryHelper.is(this.getEnchantingTableItemTag(), item);
        });
    }

    public static Collection<EnchantmentHolder> values() {
        return BY_ENCHANTMENT_ID.values();
    }

    public static void clearAll() {
        if (!EnchantmentClassesCache.isFailedLoad()) {
            values().forEach(holder -> holder.setEnchantmentData(null));
        }
    }

    @Nullable
    public static EnchantmentHolder getHolder(ResourceLocation resourceLocation) {
        return BY_ENCHANTMENT_ID.get(resourceLocation);
    }

    public ResourceLocation getResourceLocation() {
        return this.holder.key().location();
    }

    public Enchantment getEnchantment() {
        return this.holder.value();
    }

    public EnchantmentCategory getTagBasedEnchantmentCategory() {
        return this.tagBasedEnchantmentCategory;
    }

    public TagKey<Item> getEnchantingTableItemTag() {
        return this.enchantingTableItemTag;
    }

    public TagKey<Item> getAnvilItemTag() {
        return this.anvilItemTag;
    }

    public TagKey<Enchantment> getIncompatibleEnchantmentTag() {
        return this.incompatibleEnchantmentTag;
    }

    public EnchantmentData getEnchantmentData() {
        return this.enchantmentData;
    }

    public void setEnchantmentData(@Nullable EnchantmentData enchantmentData) {
        if (enchantmentData == null) {
            enchantmentData = this.originalEnchantmentData;
            ((EnchantmentFeature) this.getEnchantment()).setHolder(null);
        } else {
            ((EnchantmentFeature) this.getEnchantment()).setHolder(this);
        }

        enchantmentData.apply(this.getEnchantment());
        this.enchantmentData = enchantmentData;
    }

    public boolean isOriginalEnchantmentData() {
        return this.originalEnchantmentData == this.enchantmentData;
    }

    public boolean isCompatibleWith(EnchantmentHolder holder) {
        return !this.is(holder) && !holder.is(this.getIncompatibleEnchantmentTag()) &&
                !this.is(holder.getIncompatibleEnchantmentTag());
    }

    public boolean isUnobtainable() {
        return this.is(ModRegistry.UNOBTAINABLE_ENCHANTMENT_TAG);
    }

    public boolean isTreasureOnly() {
        return this.is(ModRegistry.TREASURE_ENCHANTMENT_TAG);
    }

    public boolean isCurse() {
        return this.is(ModRegistry.CURSES_ENCHANTMENT_TAG);
    }

    public boolean isTradeable() {
        return this.is(ModRegistry.TRADEABLE_ENCHANTMENT_TAG);
    }

    public boolean isDiscoverable() {
        return this.is(ModRegistry.DISCOVERABLE_ENCHANTMENT_TAG);
    }

    public boolean is(EnchantmentHolder holder) {
        return this.getEnchantment() == holder.getEnchantment();
    }

    public boolean is(TagKey<Enchantment> tagKey) {
        return RegistryHelper.is(tagKey, this.getEnchantment());
    }

    @Override
    public String toString() {
        return this.holder.toString();
    }
}
