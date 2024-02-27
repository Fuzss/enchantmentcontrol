package fuzs.enchantmentcontrol;

import fuzs.enchantmentcontrol.data.DynamicEnchantmentDataProvider;
import fuzs.enchantmentcontrol.data.DynamicEnchantmentTagProvider;
import fuzs.enchantmentcontrol.data.DynamicItemTagProvider;
import fuzs.enchantmentcontrol.handler.EnchantmentClassesCache;
import fuzs.enchantmentcontrol.handler.UnsafeHandler;
import fuzs.enchantmentcontrol.init.ModRegistry;
import fuzs.enchantmentcontrol.world.item.enchantment.EnchantmentDataManager;
import fuzs.enchantmentcontrol.world.item.enchantment.EnchantmentHolder;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.core.v1.ModContainer;
import fuzs.puzzleslib.api.core.v1.ModLoaderEnvironment;
import fuzs.puzzleslib.api.core.v1.context.AddReloadListenersContext;
import fuzs.puzzleslib.api.core.v1.context.PackRepositorySourcesContext;
import fuzs.puzzleslib.api.event.v1.LoadCompleteCallback;
import fuzs.puzzleslib.api.resources.v1.DynamicPackResources;
import fuzs.puzzleslib.api.resources.v1.PackResourcesHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.repository.Pack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnchantmentControl implements ModConstructor {
    public static final String MOD_ID = "enchantmentcontrol";
    public static final String MOD_NAME = "Enchantment Control";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

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
    }

    @Override
    public void onAddDataPackFinders(PackRepositorySourcesContext context) {
        context.addRepositorySource(PackResourcesHelper.buildServerPack(id("enchantments"),
                () -> new DynamicPackResources(DynamicEnchantmentDataProvider::new,
                        DynamicItemTagProvider::new,
                        DynamicEnchantmentTagProvider::new
                ) {

                    @Override
                    protected void setup() {
                        EnchantmentHolder.restoreAllOriginalValues();
                        super.setup();
                    }
                },
                Component.literal("Generated Data Pack"),
                getPackDescription(MOD_ID),
                true,
                Pack.Position.BOTTOM,
                false,
                false
        ));
    }

    @Deprecated(forRemoval = true)
    private static Component getPackDescription(String modId) {
        return ModLoaderEnvironment.INSTANCE.getModContainer(modId).map(ModContainer::getDisplayName).map(name -> {
            return Component.literal(name + " Dynamic Resources");
        }).orElseGet(() -> Component.literal("Dynamic Resources (" + modId + ")"));
    }

    @Override
    public void onRegisterDataPackReloadListeners(AddReloadListenersContext context) {
        context.registerReloadListener("enchantment_data", new EnchantmentDataManager());
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
