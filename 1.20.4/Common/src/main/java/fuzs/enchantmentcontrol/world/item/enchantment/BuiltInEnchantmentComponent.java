package fuzs.enchantmentcontrol.world.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public record BuiltInEnchantmentComponent(Enchantment enchantment) implements EnchantmentComponent {
    @Override
    public Enchantment.Rarity rarity() {
        return this.enchantment.rarity;
    }

    @Override
    public EquipmentSlot[] equipmentSlots() {
        return this.enchantment.slots;
    }

    @Override
    public int minLevel() {
        return this.enchantment.getMinLevel();
    }

    @Override
    public int maxLevel() {
        return this.enchantment.getMaxLevel();
    }

    @Override
    public EnchantmentCategory getEnchantmentCategory() {
        return this.enchantment.category;
    }

    @Override
    public boolean canEnchant(ItemStack itemStack) {
        return this.enchantment.canEnchant(itemStack);
    }

    @Override
    public boolean isCompatibleWith(Enchantment other) {
        return this.enchantment.isCompatibleWith(other);
    }

    @Override
    public boolean isTreasureOnly() {
        return this.enchantment.isTreasureOnly();
    }

    @Override
    public boolean isCurse() {
        return this.enchantment.isCurse();
    }

    @Override
    public boolean isTradeable() {
        return this.enchantment.isTradeable();
    }

    @Override
    public boolean isDiscoverable() {
        return this.enchantment.isDiscoverable();
    }

    @Override
    public boolean isUnobtainable() {
        return false;
    }
}
