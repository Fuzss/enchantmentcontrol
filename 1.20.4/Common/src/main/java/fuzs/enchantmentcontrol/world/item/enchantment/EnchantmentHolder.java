package fuzs.enchantmentcontrol.world.item.enchantment;

import com.google.common.collect.Maps;
import fuzs.enchantmentcontrol.EnchantmentControl;
import fuzs.enchantmentcontrol.init.ModRegistry;
import fuzs.extensibleenums.api.v2.CommonAbstractions;
import fuzs.puzzleslib.api.init.v3.registry.RegistryHelper;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;

public final class EnchantmentHolder {
    private static final Map<ResourceLocation, EnchantmentHolder> BY_ID = Maps.newHashMap();
    private static final Map<Enchantment, EnchantmentHolder> BY_ENCHANTMENT = Maps.newIdentityHashMap();

    private final Holder.Reference<Enchantment> holder;
    private final EnchantmentCategory originalEnchantmentCategory;
    private final Enchantment.Rarity originalRarity;
    private final EquipmentSlot[] originalSlots;
    private final EnchantmentCategory perEnchantmentCategory;
    private final TagKey<Item> enchantingTableItemTag;
    private final TagKey<Item> anvilItemTag;
    private final TagKey<Enchantment> incompatibleEnchantmentTag;
    private DataBasedEnchantmentComponent dataBasedEnchantmentComponent;

    public EnchantmentHolder(Holder.Reference<Enchantment> holder) {
        Enchantment enchantment = holder.value();
        EnchantmentFeature.testHolderIsNull(enchantment);
        this.holder = holder;
        this.originalEnchantmentCategory = enchantment.category;
        this.originalRarity = enchantment.rarity;
        this.originalSlots = enchantment.slots;
        ResourceLocation resourceLocation = holder.key().location();
        this.perEnchantmentCategory = CommonAbstractions.createEnchantmentCategory(EnchantmentControl.id(resourceLocation.getNamespace() + "/" + resourceLocation.getPath()), (Item item) -> {
            return !this.isUnobtainable() && RegistryHelper.is(this.getEnchantingTableItemTag(), item);
        });
        this.enchantingTableItemTag = ModRegistry.createEnchantingTableItemTag(enchantment);
        this.anvilItemTag = ModRegistry.createAnvilItemTag(enchantment);
        this.incompatibleEnchantmentTag = ModRegistry.createIncompatibleEnchantmentTag(enchantment);
        BY_ID.put(resourceLocation, this);
        BY_ENCHANTMENT.put(enchantment, this);
    }

    public static Collection<EnchantmentHolder> values() {
        return BY_ID.values();
    }

    @Nullable
    public static EnchantmentHolder getHolder(ResourceLocation resourceLocation) {
        return BY_ID.get(resourceLocation);
    }

    @Nullable
    public static EnchantmentHolder getHolder(Enchantment enchantment) {
        return BY_ENCHANTMENT.get(enchantment);
    }

    public ResourceLocation getResourceLocation() {
        return this.holder.key().location();
    }

    public Enchantment getEnchantment() {
        return this.holder.value();
    }

    public EnchantmentCategory getPerEnchantmentCategory() {
        return this.perEnchantmentCategory;
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

    public DataBasedEnchantmentComponent getEnchantmentData() {
        return this.dataBasedEnchantmentComponent;
    }

    public boolean isCompatibleWith(EnchantmentHolder holder) {
        if (this.is(holder)) {
            return false;
        } else if (holder.is(this.getIncompatibleEnchantmentTag())) {
            return false;
        } else if (this.is(holder.getIncompatibleEnchantmentTag())) {
            return false;
        } else {
            return true;
        }
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

    public static void restoreAllOriginalValues() {
        values().forEach(EnchantmentHolder::restoreOriginalValues);
    }

    public void restoreOriginalValues() {
        this.getEnchantment().rarity = this.originalRarity;
        this.getEnchantment().slots = this.originalSlots;
        this.getEnchantment().category = this.originalEnchantmentCategory;
        ((EnchantmentFeature) this.getEnchantment()).setHolder(null);
    }

    public void initNewValues(DataBasedEnchantmentComponent dataBasedEnchantmentComponent) {
        this.dataBasedEnchantmentComponent = dataBasedEnchantmentComponent;
        this.getEnchantment().rarity = dataBasedEnchantmentComponent.rarity();
        this.getEnchantment().slots = dataBasedEnchantmentComponent.equipmentSlots();
        this.getEnchantment().category = this.getPerEnchantmentCategory();
        ((EnchantmentFeature) this.getEnchantment()).setHolder(this);
    }
}
