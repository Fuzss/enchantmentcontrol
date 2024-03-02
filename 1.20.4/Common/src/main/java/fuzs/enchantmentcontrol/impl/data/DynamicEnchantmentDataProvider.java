package fuzs.enchantmentcontrol.impl.data;

import fuzs.enchantmentcontrol.api.v1.data.AbstractEnchantmentDataProvider;
import fuzs.enchantmentcontrol.impl.world.item.enchantment.EnchantmentDataImpl;
import fuzs.enchantmentcontrol.impl.world.item.enchantment.EnchantmentHolder;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;

public class DynamicEnchantmentDataProvider extends AbstractEnchantmentDataProvider {
    private final boolean skipHolderValidation;

    private DynamicEnchantmentDataProvider(DataProviderContext context, boolean skipHolderValidation) {
        super(context);
        this.skipHolderValidation = skipHolderValidation;
    }

    @Override
    public void addEnchantmentData() {
        for (EnchantmentHolder holder : EnchantmentHolder.values()) {
            this.add(holder.getResourceLocation(), EnchantmentDataImpl.fromEnchantment(holder.getEnchantment(), this.skipHolderValidation));
        }
    }

    public static DataProviderContext.Factory create(boolean skipHolderValidation) {
        return context -> new DynamicEnchantmentDataProvider(context, skipHolderValidation);
    }
}
