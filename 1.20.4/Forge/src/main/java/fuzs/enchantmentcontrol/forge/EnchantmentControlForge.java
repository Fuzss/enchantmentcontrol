package fuzs.enchantmentcontrol.forge;

import fuzs.enchantmentcontrol.EnchantmentControl;
import fuzs.enchantmentcontrol.EnchantmentControlMod;
import fuzs.enchantmentcontrol.config.CommonConfig;
import fuzs.enchantmentcontrol.util.ModEnchantmentHelper;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

import java.util.Iterator;
import java.util.Map;

@Mod(EnchantmentControl.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EnchantmentControlForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ModConstructor.construct(EnchantmentControl.MOD_ID, EnchantmentControlMod::new);
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
