package fuzs.enchantmentcontrol.impl.network;

import fuzs.enchantmentcontrol.api.v1.data.EnchantmentDataHelper;
import fuzs.enchantmentcontrol.impl.world.item.enchantment.EnchantmentDataImpl;
import fuzs.enchantmentcontrol.impl.world.item.enchantment.EnchantmentHolder;
import fuzs.puzzleslib.api.network.v3.ClientMessageListener;
import fuzs.puzzleslib.api.network.v3.ClientboundMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public record ClientboundEnchantmentDataMessage(Map<ResourceLocation, EnchantmentDataImpl> enchantmentData) implements ClientboundMessage<ClientboundEnchantmentDataMessage> {
    @Override
    public ClientMessageListener<ClientboundEnchantmentDataMessage> getHandler() {
        return new ClientMessageListener<>() {
            @Override
            public void handle(ClientboundEnchantmentDataMessage message, Minecraft client, ClientPacketListener handler, LocalPlayer player, ClientLevel level) {
                EnchantmentDataHelper.clearAll();
                for (Map.Entry<ResourceLocation, EnchantmentDataImpl> entry : message.enchantmentData.entrySet()) {
                    ResourceLocation resourceLocation = entry.getKey();
                    EnchantmentHolder holder = EnchantmentHolder.getHolder(resourceLocation);
                    if (holder != null) holder.setEnchantmentData(entry.getValue());
                }
            }
        };
    }
}
