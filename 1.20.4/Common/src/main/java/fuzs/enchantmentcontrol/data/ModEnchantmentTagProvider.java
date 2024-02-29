package fuzs.enchantmentcontrol.data;

import fuzs.enchantmentcontrol.init.ModRegistry;
import fuzs.puzzleslib.api.data.v2.AbstractTagProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.Enchantment;

public class ModEnchantmentTagProvider extends AbstractTagProvider.Intrinsic<Enchantment> {

    public ModEnchantmentTagProvider(DataProviderContext context) {
        super(Registries.ENCHANTMENT, context);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        this.tag(ModRegistry.TREASURE_ENCHANTMENT_TAG);
        this.tag(ModRegistry.CURSES_ENCHANTMENT_TAG);
        this.tag(ModRegistry.TRADEABLE_ENCHANTMENT_TAG);
        this.tag(ModRegistry.DISCOVERABLE_ENCHANTMENT_TAG);
        this.tag(ModRegistry.UNOBTAINABLE_ENCHANTMENT_TAG);
        this.tag(ModRegistry.UNTOUCHED_ENCHANTMENT_TAG);
    }
}
