package fuzs.enchantmentcontrol.neoforge;

import fuzs.enchantmentcontrol.EnchantmentControl;
import fuzs.enchantmentcontrol.EnchantmentControlMod;
import fuzs.enchantmentcontrol.config.CommonConfig;
import fuzs.enchantmentcontrol.data.ModEnchantmentTagProvider;
import fuzs.enchantmentcontrol.util.ModEnchantmentHelper;
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
        DataProviderHelper.registerDataProviders(EnchantmentControl.MOD_ID, ModEnchantmentTagProvider::new);
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
