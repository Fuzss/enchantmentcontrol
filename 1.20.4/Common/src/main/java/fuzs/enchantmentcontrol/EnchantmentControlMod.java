package fuzs.enchantmentcontrol;

import fuzs.enchantmentcontrol.client.commands.EnchantmentDataCommand;
import fuzs.enchantmentcontrol.config.CommonConfig;
import fuzs.enchantmentcontrol.handler.EnchantmentClassesCache;
import fuzs.enchantmentcontrol.handler.UnsafeHandler;
import fuzs.enchantmentcontrol.init.ModRegistry;
import fuzs.enchantmentcontrol.network.ClientboundEnchantmentDataMessage;
import fuzs.enchantmentcontrol.world.item.enchantment.EnchantmentDataImpl;
import fuzs.enchantmentcontrol.world.item.enchantment.EnchantmentDataManager;
import fuzs.enchantmentcontrol.world.item.enchantment.EnchantmentHolder;
import fuzs.puzzleslib.api.config.v3.ConfigHolder;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.core.v1.context.AddReloadListenersContext;
import fuzs.puzzleslib.api.core.v1.context.PackRepositorySourcesContext;
import fuzs.puzzleslib.api.event.v1.LoadCompleteCallback;
import fuzs.puzzleslib.api.event.v1.server.SyncDataPackContentsCallback;
import fuzs.puzzleslib.api.event.v1.server.TagsUpdatedCallback;
import fuzs.puzzleslib.api.network.v3.NetworkHandlerV3;
import fuzs.puzzleslib.api.network.v3.PlayerSet;
import fuzs.puzzleslib.api.resources.v1.DynamicPackResources;
import fuzs.puzzleslib.api.resources.v1.PackResourcesHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;

import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EnchantmentControlMod extends EnchantmentControl implements ModConstructor {
    public static final NetworkHandlerV3 NETWORK = NetworkHandlerV3.builder(MOD_ID)
            .registerClientbound(ClientboundEnchantmentDataMessage.class);
    public static final ConfigHolder CONFIG = ConfigHolder.builder(MOD_ID).common(CommonConfig.class);

    @Override
    public void onConstructMod() {
        ModRegistry.touch();
        UnsafeHandler.addEnchantmentsToFilteredRegistries();
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        LoadCompleteCallback.EVENT.register(EnchantmentClassesCache::save);
        LoadCompleteCallback.EVENT.register(() -> {
            BuiltInRegistries.ENCHANTMENT.holders().forEach(EnchantmentHolder::new);
        });
        SyncDataPackContentsCallback.EVENT.register((ServerPlayer player, boolean joined) -> {
            if (!EnchantmentClassesCache.isFailedLoad()) {
                NETWORK.sendMessage(PlayerSet.ofPlayer(player),
                        new ClientboundEnchantmentDataMessage(EnchantmentHolder.values()
                                .stream()
                                .filter(Predicate.not(EnchantmentHolder::isOriginalEnchantmentData))
                                .collect(Collectors.toMap(EnchantmentHolder::getResourceLocation,
                                        holder -> ((EnchantmentDataImpl) holder.getEnchantmentData())
                                )))
                );
            }
        });
        TagsUpdatedCallback.EVENT.register((registryAccess, client) -> {
            // checking the tag here only seems to work on Fabric, Forge-like is handled when data reloads
            // this happens before data pack contents are synced, so we don't need it on the client
            if (!client && !EnchantmentClassesCache.isFailedLoad()) {
                for (EnchantmentHolder holder : EnchantmentHolder.values()) {
                    if (holder.is(ModRegistry.UNTOUCHED_ENCHANTMENT_TAG)) {
                        holder.setEnchantmentData(null);
                    }
                }
            }
        });
    }

    @Override
    public void onAddDataPackFinders(PackRepositorySourcesContext context) {
        context.addRepositorySource(PackResourcesHelper.buildServerPack(id("enchantments"),
                () -> new DynamicPackResources(EnchantmentDataCommand.getEnchantmentDataFactories(false)) {

                    @Override
                    protected void setup() {
                        EnchantmentHolder.clearAll();
                    }
                },
                PackResourcesHelper.getPackTitle(PackType.SERVER_DATA),
                PackResourcesHelper.getPackDescription(MOD_ID),
                true,
                Pack.Position.BOTTOM,
                false,
                false
        ));
    }

    @Override
    public void onRegisterDataPackReloadListeners(AddReloadListenersContext context) {
        context.registerReloadListener("enchantment_data", new EnchantmentDataManager());
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
