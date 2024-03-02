package fuzs.enchantmentcontrol.impl.data;

import fuzs.enchantmentcontrol.api.v1.tags.EnchantmentTags;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.api.data.v2.tags.AbstractTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.Enchantment;

public class ModEnchantmentTagProvider extends AbstractTagProvider<Enchantment> {

    public ModEnchantmentTagProvider(DataProviderContext context) {
        super(Registries.ENCHANTMENT, context);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        // generate all those empty, so they are present in the jar for people to find the locations
        // actual tags populated with values are generated dynamically at runtime
        // do not use holders, they are created on load complete which does not run during data generation
        this.add(EnchantmentTags.TREASURE);
        this.add(EnchantmentTags.CURSES);
        this.add(EnchantmentTags.TRADEABLE);
        this.add(EnchantmentTags.DISCOVERABLE);
        this.add(EnchantmentTags.UNOBTAINABLE);
        this.add(EnchantmentTags.UNTOUCHED);
        for (Enchantment enchantment : BuiltInRegistries.ENCHANTMENT) {
            this.add(EnchantmentTags.getIncompatibleTag(enchantment));
        }
    }
}
