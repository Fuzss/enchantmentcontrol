package fuzs.enchantmentcontrol.impl.world.item.enchantment;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import fuzs.enchantmentcontrol.api.v1.data.EnchantmentData;
import fuzs.enchantmentcontrol.api.v1.tags.EnchantmentTags;
import fuzs.enchantmentcontrol.impl.EnchantmentControlMod;
import fuzs.enchantmentcontrol.impl.handler.EnchantmentClassesCache;
import fuzs.enchantmentcontrol.impl.init.ModRegistry;
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
import java.util.function.Consumer;
import java.util.function.Function;

public final class EnchantmentHolder {
    private static final Map<ResourceLocation, EnchantmentHolder> BY_ENCHANTMENT_LOCATION = Maps.newHashMap();

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
        isOriginalState(enchantment);
        this.holder = holder;
        this.enchantmentData = this.originalEnchantmentData = EnchantmentDataImpl.fromEnchantment(enchantment);
        this.tagBasedEnchantmentCategory = this.createTagBasedEnchantmentCategory(resourceLocation);
        this.enchantingTableItemTag = ModRegistry.createEnchantingTableItemTag(enchantment);
        this.anvilItemTag = ModRegistry.createAnvilItemTag(enchantment);
        this.incompatibleEnchantmentTag = ModRegistry.createIncompatibleEnchantmentTag(enchantment);
        if (BY_ENCHANTMENT_LOCATION.put(resourceLocation, this) != null) {
            throw new IllegalStateException("Duplicate holder for enchantment " + resourceLocation);
        }
    }

    private EnchantmentCategory createTagBasedEnchantmentCategory(ResourceLocation resourceLocation) {
        return CommonAbstractions.createEnchantmentCategory(EnchantmentControlMod.id(
                resourceLocation.getNamespace() + "/" + resourceLocation.getPath()), (Item item) -> {
            return !this.isUnobtainable() && RegistryHelper.is(this.getEnchantingTableItemTag(), item);
        });
    }

    public static Collection<EnchantmentHolder> values() {
        return BY_ENCHANTMENT_LOCATION.values();
    }

    public static void forEach(Consumer<EnchantmentHolder> consumer) {
        values().forEach(consumer);
    }

    public static void clearAll() {
        if (!EnchantmentClassesCache.isFailedLoad()) {
            forEach(holder -> holder.setEnchantmentData(null));
        }
    }

    public static void bindAll() {
        if (!EnchantmentClassesCache.isFailedLoad()) {
            forEach(holder -> holder.getEnchantment().enchantmentcontrol$setHolder(holder));
        }
    }

    public static void unbindAll() {
        if (!EnchantmentClassesCache.isFailedLoad()) {
            forEach(holder -> holder.getEnchantment().enchantmentcontrol$setHolder(null));
        }
    }

    @Nullable
    public static EnchantmentHolder getHolder(ResourceLocation resourceLocation) {
        return BY_ENCHANTMENT_LOCATION.get(resourceLocation);
    }

    public static void isOriginalState(Enchantment enchantment) {
        Preconditions.checkState(((EnchantmentFeature) enchantment).enchantmentcontrol$getHolder() == null,
                "holder is still set"
        );
    }

    public static <T> void ifPresent(EnchantmentFeature enchantmentFeature, Function<EnchantmentHolder, T> valueExtractor, Consumer<T> valueConsumer) {
        ifPresent(enchantmentFeature, valueExtractor, valueConsumer, null);
    }

    public static <T> void ifPresent(EnchantmentFeature enchantmentFeature, Function<EnchantmentHolder, T> valueExtractor, Consumer<T> valueConsumer, @Nullable T fallback) {
        EnchantmentHolder holder = enchantmentFeature.enchantmentcontrol$getHolder();
        if (holder != null && (enchantmentFeature.enchantmentcontrol$isEnabled() || fallback != null)) {
            valueConsumer.accept(enchantmentFeature.enchantmentcontrol$isEnabled() ?
                    valueExtractor.apply(holder) :
                    fallback);
        }
    }

    public ResourceLocation getResourceLocation() {
        return this.holder.key().location();
    }

    public <T extends Enchantment & EnchantmentFeature> T getEnchantment() {
        return (T) this.holder.value();
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
            this.getEnchantment().enchantmentcontrol$setHolder(null);
        } else {
            this.getEnchantment().enchantmentcontrol$setHolder(this);
        }

        ((EnchantmentDataImpl) enchantmentData).apply(this.getEnchantment());
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
        return this.is(EnchantmentTags.UNOBTAINABLE);
    }

    public boolean isTreasureOnly() {
        return this.is(EnchantmentTags.TREASURE);
    }

    public boolean isCurse() {
        return this.is(EnchantmentTags.CURSES);
    }

    public boolean isTradeable() {
        return this.is(EnchantmentTags.TRADEABLE);
    }

    public boolean isDiscoverable() {
        return this.is(EnchantmentTags.DISCOVERABLE);
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
