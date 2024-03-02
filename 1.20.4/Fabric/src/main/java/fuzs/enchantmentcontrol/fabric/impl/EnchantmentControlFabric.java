package fuzs.enchantmentcontrol.fabric.impl;

import fuzs.enchantmentcontrol.impl.EnchantmentControl;
import fuzs.enchantmentcontrol.impl.EnchantmentControlMod;
import fuzs.enchantmentcontrol.impl.config.CommonConfig;
import fuzs.enchantmentcontrol.impl.util.ModEnchantmentHelper;
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
            if (EnchantmentControlMod.CONFIG.get(CommonConfig.class).removeUnobtainableFromCreative) {
                entries.getDisplayStacks().removeIf(ModEnchantmentHelper::isBookWithDisabledEnchantments);
                entries.getSearchTabStacks().removeIf(ModEnchantmentHelper::isBookWithDisabledEnchantments);
            }
        });
    }
}
