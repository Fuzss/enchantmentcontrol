package fuzs.enchantmentcontrol.neoforge.impl;

import fuzs.enchantmentcontrol.impl.EnchantmentControl;
import fuzs.enchantmentcontrol.impl.EnchantmentControlMod;
import fuzs.enchantmentcontrol.impl.config.CommonConfig;
import fuzs.enchantmentcontrol.impl.data.ModEnchantmentTagProvider;
import fuzs.enchantmentcontrol.impl.data.ModItemTagProvider;
import fuzs.enchantmentcontrol.impl.util.ModEnchantmentHelper;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

import java.util.Iterator;
import java.util.Map;

@Mod(EnchantmentControl.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EnchantmentControlNeoForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ModConstructor.construct(EnchantmentControl.MOD_ID, EnchantmentControlMod::new);
        DataProviderHelper.registerDataProviders(EnchantmentControl.MOD_ID,
                ModEnchantmentTagProvider::new,
                ModItemTagProvider::new
        );
    }

    @SubscribeEvent
    public static void onBuildCreativeModeTabContents(final BuildCreativeModeTabContentsEvent evt) {
        if (EnchantmentControlMod.CONFIG.get(CommonConfig.class).removeUnobtainableFromCreative) {
            Iterator<Map.Entry<ItemStack, CreativeModeTab.TabVisibility>> iterator = evt.getEntries().iterator();
            while (iterator.hasNext()) {
                ItemStack itemStack = iterator.next().getKey();
                if (ModEnchantmentHelper.isBookWithDisabledEnchantments(itemStack)) {
                    iterator.remove();
                }
            }
        }
    }
}
