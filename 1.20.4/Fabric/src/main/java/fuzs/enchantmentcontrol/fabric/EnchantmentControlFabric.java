package fuzs.enchantmentcontrol.fabric;

import fuzs.enchantmentcontrol.EnchantmentControl;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.fabricmc.api.ModInitializer;

public class EnchantmentControlFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConstructor.construct(EnchantmentControl.MOD_ID, EnchantmentControl::new);
    }
}
