package fuzs.enchantmentcontrol;

import fuzs.enchantmentcontrol.world.item.enchantment.MaxLevelManager;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.event.v1.LoadCompleteCallback;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnchantmentControl implements ModConstructor {
    public static final String MOD_ID = "enchantmentcontrol";
    public static final String MOD_NAME = "Enchantment Control";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @Override
    public void onConstructMod() {
        LoadCompleteCallback.EVENT.register(MaxLevelManager::onLoadComplete);
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
