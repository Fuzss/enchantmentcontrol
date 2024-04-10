package fuzs.enchantmentcontrol.impl.world.item.enchantment;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EquipmentSlot;

import java.util.*;
import java.util.stream.Stream;

/**
 * A helper implementation for populating {@link net.minecraft.world.item.enchantment.Enchantment#slots}.
 */
public enum EnchantmentType implements StringRepresentable {
    HAND(EquipmentSlot.Type.HAND),
    ARMOR(EquipmentSlot.Type.ARMOR),
    ALL(EquipmentSlot.Type.HAND, EquipmentSlot.Type.ARMOR),
    NONE;

    private static final EnchantmentType[] VALUES = EnchantmentType.values();

    private final Set<EquipmentSlot.Type> types;
    private final List<EquipmentSlot> slots;

    EnchantmentType(EquipmentSlot.Type... types) {
        this.types = types.length != 0 ? EnumSet.copyOf(Arrays.asList(types)) : EnumSet.noneOf(EquipmentSlot.Type.class);
        this.slots = Stream.of(EquipmentSlot.values()).filter(slot -> this.types.contains(slot.getType())).toList();
    }

    public EquipmentSlot[] getSlots() {
        return this.slots.toArray(EquipmentSlot[]::new);
    }

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    public static EnchantmentType fromSlots(EquipmentSlot[] slots) {

        Set<EquipmentSlot.Type> types = EnumSet.noneOf(EquipmentSlot.Type.class);
        for (EquipmentSlot slot : slots) {
            types.add(slot.getType());
        }

        for (EnchantmentType type : VALUES) {
            if (type.types.equals(types)) {
                return type;
            }
        }

        return NONE;
    }
}
