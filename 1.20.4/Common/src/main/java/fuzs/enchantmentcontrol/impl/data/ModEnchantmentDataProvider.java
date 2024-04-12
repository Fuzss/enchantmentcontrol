package fuzs.enchantmentcontrol.impl.data;

import fuzs.enchantmentcontrol.api.v1.data.AbstractEnchantmentDataProvider;
import fuzs.enchantmentcontrol.api.v1.data.EnchantmentCost;
import fuzs.enchantmentcontrol.api.v1.data.EnchantmentDataBuilder;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.world.item.enchantment.Enchantments;

public class ModEnchantmentDataProvider extends AbstractEnchantmentDataProvider {

    public ModEnchantmentDataProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addEnchantmentData() {
        // only manually enabled for testing
        this.add(Enchantments.BLOCK_FORTUNE, EnchantmentDataBuilder.fromEnchantment(Enchantments.BLOCK_FORTUNE)
                // values copied from efficiency enchantment in Minecraft 1.20.5
                .withMinCost(EnchantmentCost.dynamicCost(1, 10))
                .withMaxCost(EnchantmentCost.dynamicCost(51, 10))
                .withAliases(Enchantments.MENDING));
        this.add(Enchantments.MENDING,
                EnchantmentDataBuilder.fromEnchantment(Enchantments.MENDING).withAliases(Enchantments.BLOCK_FORTUNE)
        );
    }
}
