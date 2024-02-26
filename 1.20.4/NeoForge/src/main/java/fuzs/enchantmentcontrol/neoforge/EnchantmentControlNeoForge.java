package fuzs.enchantmentcontrol.neoforge;

import fuzs.enchantmentcontrol.EnchantmentControl;
import fuzs.enchantmentcontrol.data.ModEnchantmentDataProvider;
import fuzs.enchantmentcontrol.data.ModEnchantmentTagProvider;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;

@Mod(EnchantmentControl.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EnchantmentControlNeoForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ModConstructor.construct(EnchantmentControl.MOD_ID, EnchantmentControl::new);
        DataProviderHelper.registerDataProviders(EnchantmentControl.MOD_ID,
                ModEnchantmentTagProvider::new,
                ModEnchantmentDataProvider::new
//                DynamicEnchantmentDataProvider::new,
//                DynamicEnchantmentTagProvider::new,
//                DynamicItemTagProvider::new
        );
    }
}
