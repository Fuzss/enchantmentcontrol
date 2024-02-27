package fuzs.enchantmentcontrol.data;

import fuzs.enchantmentcontrol.world.item.enchantment.DataBasedEnchantmentComponent;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.Map;

public class DynamicEnchantmentDataProvider extends AbstractEnchantmentDataProvider {

    public DynamicEnchantmentDataProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addEnchantmentData() {
        for (Map.Entry<ResourceKey<Enchantment>, Enchantment> entry : BuiltInRegistries.ENCHANTMENT.entrySet()) {
            this.add(entry.getKey().location(), DataBasedEnchantmentComponent.fromEnchantment(entry.getValue()));
        }
    }
}
