package fuzs.enchantmentcontrol.api.v1.data;

import fuzs.enchantmentcontrol.world.item.enchantment.EnchantmentFeature;
import fuzs.enchantmentcontrol.world.item.enchantment.EnchantmentHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@ApiStatus.NonExtendable
public interface EnchantmentData {

    Enchantment.Rarity rarity();

    EquipmentSlot[] equipmentSlots();

    int minLevel();

    int maxLevel();

    List<Enchantment> aliases();

    @Nullable
    static EnchantmentData getEnchantmentData(Enchantment enchantment) {
        EnchantmentHolder holder = ((EnchantmentFeature) enchantment).getHolder();
        return holder != null ? holder.getEnchantmentData() : null;
    }
}
