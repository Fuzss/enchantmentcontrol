package fuzs.enchantmentcontrol.network;

import com.google.common.base.Preconditions;
import fuzs.enchantmentcontrol.world.item.enchantment.EnchantmentData;
import fuzs.enchantmentcontrol.world.item.enchantment.EnchantmentHolder;
import fuzs.puzzleslib.api.network.v3.ClientMessageListener;
import fuzs.puzzleslib.api.network.v3.ClientboundMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public record ClientboundEnchantmentDataMessage(Map<ResourceLocation, EnchantmentData> enchantmentData) implements ClientboundMessage<ClientboundEnchantmentDataMessage> {
    @Override
    public ClientMessageListener<ClientboundEnchantmentDataMessage> getHandler() {
        return new ClientMessageListener<>() {
            @Override
            public void handle(ClientboundEnchantmentDataMessage message, Minecraft client, ClientPacketListener handler, LocalPlayer player, ClientLevel level) {
                EnchantmentHolder.clearAll();
                for (Map.Entry<ResourceLocation, EnchantmentData> entry : message.enchantmentData.entrySet()) {
                    ResourceLocation resourceLocation = entry.getKey();
                    EnchantmentHolder holder = EnchantmentHolder.getHolder(resourceLocation);
                    if (holder != null) holder.setEnchantmentData(entry.getValue());
                }
            }
        };
    }
}
