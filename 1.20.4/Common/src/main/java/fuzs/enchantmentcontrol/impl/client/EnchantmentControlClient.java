package fuzs.enchantmentcontrol.impl.client;

import fuzs.enchantmentcontrol.impl.client.gui.screens.InvalidCacheOnboardingScreen;
import fuzs.enchantmentcontrol.impl.handler.EnchantmentClassesCache;
import fuzs.enchantmentcontrol.impl.world.item.enchantment.EnchantmentHolder;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.event.v1.entity.player.ClientPlayerNetworkEvents;
import fuzs.puzzleslib.api.client.event.v1.gui.ScreenOpeningCallback;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import net.minecraft.client.gui.screens.TitleScreen;
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
            EnchantmentHolder.clearAll();
        });
        ScreenOpeningCallback.EVENT.register((oldScreen, newScreen) -> {
            if (newScreen.get() instanceof TitleScreen && EnchantmentClassesCache.isFailedLoad() &&
                    !InvalidCacheOnboardingScreen.hasSeenOnboardingScreen()) {
                newScreen.accept(new InvalidCacheOnboardingScreen(newScreen.get()));
            }
            return EventResult.PASS;
        });
    }
}
