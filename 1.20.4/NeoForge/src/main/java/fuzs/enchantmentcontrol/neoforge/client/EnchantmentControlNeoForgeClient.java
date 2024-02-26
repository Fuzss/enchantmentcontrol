package fuzs.enchantmentcontrol.neoforge.client;

import fuzs.enchantmentcontrol.EnchantmentControl;
import fuzs.enchantmentcontrol.client.EnchantmentControlClient;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;

@Mod.EventBusSubscriber(modid = EnchantmentControl.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EnchantmentControlNeoForgeClient {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ClientModConstructor.construct(EnchantmentControl.MOD_ID, EnchantmentControlClient::new);
    }
}
