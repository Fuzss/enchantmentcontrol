package fuzs.enchantmentcontrol.api.v1.data;

import fuzs.enchantmentcontrol.api.v1.tags.EnchantmentTags;
import fuzs.enchantmentcontrol.impl.world.item.enchantment.EnchantmentFeature;
import fuzs.enchantmentcontrol.impl.world.item.enchantment.EnchantmentHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Basic data holder for enchantments for properties not covered by tags in {@link EnchantmentTags}.
 * <p>
 * All implementations of this interface are immutable (implemented via record).
 */
@ApiStatus.NonExtendable
public interface EnchantmentData {

    /**
     * @return the enchantment rarity
     */
    Enchantment.Rarity rarity();

    /**
     * @return valid slots for the enchantment to be effective
     */
    EquipmentSlot[] equipmentSlots();

    /**
     * @return minimum enchantment level, always at <code>1</code>> in vanilla
     */
    int minLevel();

    /**
     * @return maximum enchantment level
     */
    int maxLevel();

    /**
     * @return minimum enchanting table cost formula
     */
    @Nullable EnchantmentCost minCost();

    /**
     * @return maximum enchanting table cost formula
     */
    @Nullable EnchantmentCost maxCost();

    /**
     * @return other enchantments that will match when checking for this enchantment
     */
    List<Enchantment> aliases();

    /**
     * Get enchantment data from an enchantment if available, will return <code>null</code> when not in a world and data
     * is unloaded.
     *
     * @param enchantment the enchantment
     * @return the enchantment data
     */
    @Nullable
    static EnchantmentData getEnchantmentData(Enchantment enchantment) {
        EnchantmentHolder holder = ((EnchantmentFeature) enchantment).enchantmentcontrol$getHolder();
        return holder != null ? holder.getEnchantmentData() : null;
    }
}
