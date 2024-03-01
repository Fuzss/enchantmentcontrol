package fuzs.enchantmentcontrol.data;

import fuzs.enchantmentcontrol.init.ModRegistry;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.api.data.v2.tags.AbstractTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.Enchantment;

public class ModEnchantmentTagProvider extends AbstractTagProvider<Enchantment> {

    public ModEnchantmentTagProvider(DataProviderContext context) {
        super(Registries.ENCHANTMENT, context);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        this.add(ModRegistry.TREASURE_ENCHANTMENT_TAG);
        this.add(ModRegistry.CURSES_ENCHANTMENT_TAG);
        this.add(ModRegistry.TRADEABLE_ENCHANTMENT_TAG);
        this.add(ModRegistry.DISCOVERABLE_ENCHANTMENT_TAG);
        this.add(ModRegistry.UNOBTAINABLE_ENCHANTMENT_TAG);
        this.add(ModRegistry.UNTOUCHED_ENCHANTMENT_TAG);
    }
}
