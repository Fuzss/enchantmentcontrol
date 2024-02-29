package fuzs.enchantmentcontrol.fabric;

import fuzs.enchantmentcontrol.EnchantmentControl;
import fuzs.enchantmentcontrol.EnchantmentControlMod;
import fuzs.enchantmentcontrol.util.ModEnchantmentHelper;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.item.CreativeModeTab;

public class EnchantmentControlFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConstructor.construct(EnchantmentControl.MOD_ID, EnchantmentControlMod::new);
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        ItemGroupEvents.MODIFY_ENTRIES_ALL.register((CreativeModeTab group, FabricItemGroupEntries entries) -> {
            entries.getDisplayStacks().removeIf(ModEnchantmentHelper::isBookWithDisabledEnchantments);
            entries.getSearchTabStacks().removeIf(ModEnchantmentHelper::isBookWithDisabledEnchantments);
        });
    }
}
