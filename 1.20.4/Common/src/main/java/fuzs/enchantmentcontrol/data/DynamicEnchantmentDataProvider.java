package fuzs.enchantmentcontrol.data;

import fuzs.enchantmentcontrol.world.item.enchantment.EnchantmentData;
import fuzs.enchantmentcontrol.world.item.enchantment.EnchantmentHolder;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;

public class DynamicEnchantmentDataProvider extends AbstractEnchantmentDataProvider {

    public DynamicEnchantmentDataProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addEnchantmentData() {
        for (EnchantmentHolder holder : EnchantmentHolder.values()) {
            this.add(holder.getResourceLocation(), EnchantmentData.fromEnchantment(holder.getEnchantment()));
        }
    }
}
