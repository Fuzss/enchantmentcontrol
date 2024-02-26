package fuzs.enchantmentcontrol.fabric.client;

import fuzs.enchantmentcontrol.EnchantmentControl;
import fuzs.enchantmentcontrol.client.EnchantmentControlClient;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.fabricmc.api.ClientModInitializer;

public class EnchantmentControlFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(EnchantmentControl.MOD_ID, EnchantmentControlClient::new);
    }
}
