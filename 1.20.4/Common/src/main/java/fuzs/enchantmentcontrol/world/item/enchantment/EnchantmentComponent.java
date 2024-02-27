package fuzs.enchantmentcontrol.world.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public interface EnchantmentComponent {

    Enchantment.Rarity rarity();

    EquipmentSlot[] equipmentSlots();

    int minLevel();

    int maxLevel();

    EnchantmentCategory getEnchantmentCategory();

    boolean canEnchant(ItemStack itemStack);

    boolean isCompatibleWith(Enchantment other);

    boolean isTreasureOnly();

    boolean isCurse();

    boolean isTradeable();

    boolean isDiscoverable();

    boolean isUnobtainable();
}
