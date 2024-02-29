package fuzs.enchantmentcontrol.neoforge.client;

import fuzs.enchantmentcontrol.EnchantmentControl;
import fuzs.enchantmentcontrol.client.EnchantmentControlClient;
import fuzs.enchantmentcontrol.client.commands.EnchantmentDataCommand;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod.EventBusSubscriber(modid = EnchantmentControl.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EnchantmentControlNeoForgeClient {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ClientModConstructor.construct(EnchantmentControl.MOD_ID, EnchantmentControlClient::new);
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        NeoForge.EVENT_BUS.addListener((final RegisterClientCommandsEvent evt) -> {
            evt.getDispatcher()
                    .register(Commands.literal(EnchantmentControl.MOD_ID)
                            .then(Commands.literal("export")
                                    .executes(commandContext -> EnchantmentDataCommand.triggerExport((Component component) -> {
                                        commandContext.getSource().sendSuccess(() -> component, true);
                                    }, (Component component) -> {
                                        commandContext.getSource().sendFailure(component);
                                    }))));
        });
    }
}
