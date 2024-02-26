package fuzs.enchantmentcontrol.client;

import fuzs.enchantmentcontrol.world.item.enchantment.EnchantmentHolder;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.event.v1.entity.player.ClientPlayerNetworkEvents;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.Connection;

public class EnchantmentControlClient implements ClientModConstructor {

    @Override
    public void onConstructMod() {
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        ClientPlayerNetworkEvents.LOGGED_OUT.register((LocalPlayer player, MultiPlayerGameMode multiPlayerGameMode, Connection connection) -> {
            EnchantmentHolder.restoreAllOriginalValues();
        });
    }
}
