package fuzs.enchantmentcontrol.data;

import fuzs.enchantmentcontrol.init.ModRegistry;
import fuzs.puzzleslib.api.data.v2.AbstractTagProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

public class ModEnchantmentTagProvider extends AbstractTagProvider.Intrinsic<Enchantment> {

    public ModEnchantmentTagProvider(DataProviderContext context) {
        super(Registries.ENCHANTMENT, context);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        this.tag(ModRegistry.TREASURE_ENCHANTMENT_TAG).add(Enchantments.SHARPNESS, Enchantments.MOB_LOOTING, Enchantments.KNOCKBACK, Enchantments.UNBREAKING);
        this.tag(ModRegistry.CURSES_ENCHANTMENT_TAG).add(Enchantments.BLOCK_EFFICIENCY);
        this.tag(ModRegistry.TRADEABLE_ENCHANTMENT_TAG);
        this.tag(ModRegistry.DISCOVERABLE_ENCHANTMENT_TAG);
        this.tag(ModRegistry.UNOBTAINABLE_ENCHANTMENT_TAG).add(Enchantments.SWIFT_SNEAK, Enchantments.MENDING, Enchantments.FISHING_LUCK, Enchantments.FISHING_SPEED);
        this.tag(ModRegistry.UNTOUCHED_ENCHANTMENT_TAG).add(Enchantments.MENDING);
    }
}
