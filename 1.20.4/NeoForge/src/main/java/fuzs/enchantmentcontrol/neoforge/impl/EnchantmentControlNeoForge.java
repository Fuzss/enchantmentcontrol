package fuzs.enchantmentcontrol.neoforge.impl;

import fuzs.enchantmentcontrol.impl.EnchantmentControl;
import fuzs.enchantmentcontrol.impl.EnchantmentControlMod;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;

@Mod(EnchantmentControl.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EnchantmentControlNeoForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ModConstructor.construct(EnchantmentControl.MOD_ID, EnchantmentControlMod::new);
//        DataProviderHelper.registerDataProviders(EnchantmentControl.MOD_ID,
//                ModEnchantmentDataProvider::new,
//                ModEnchantmentTagProvider::new,
//                ModItemTagProvider::new
//        );
    }
}
