package fuzs.enchantmentcontrol.fabric.client;

import com.mojang.brigadier.CommandDispatcher;
import fuzs.enchantmentcontrol.EnchantmentControl;
import fuzs.enchantmentcontrol.client.EnchantmentControlClient;
import fuzs.enchantmentcontrol.client.commands.EnchantmentDataCommand;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.commands.CommandBuildContext;

public class EnchantmentControlFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(EnchantmentControl.MOD_ID, EnchantmentControlClient::new);
        ClientCommandRegistrationCallback.EVENT.register((CommandDispatcher<FabricClientCommandSource> dispatcher, CommandBuildContext registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal(EnchantmentControl.MOD_ID)
                    .then(ClientCommandManager.literal("export")
                            .executes(commandContext -> EnchantmentDataCommand.triggerExport(commandContext.getSource()::sendFeedback,
                                    commandContext.getSource()::sendError
                            ))));
        });
    }
}
