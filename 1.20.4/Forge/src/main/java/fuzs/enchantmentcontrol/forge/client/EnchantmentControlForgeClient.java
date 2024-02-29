package fuzs.enchantmentcontrol.forge.client;

import fuzs.enchantmentcontrol.EnchantmentControl;
import fuzs.enchantmentcontrol.client.EnchantmentControlClient;
import fuzs.enchantmentcontrol.client.commands.EnchantmentDataCommand;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod.EventBusSubscriber(modid = EnchantmentControl.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EnchantmentControlForgeClient {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ClientModConstructor.construct(EnchantmentControl.MOD_ID, EnchantmentControlClient::new);
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        MinecraftForge.EVENT_BUS.addListener((final RegisterClientCommandsEvent evt) -> {
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
