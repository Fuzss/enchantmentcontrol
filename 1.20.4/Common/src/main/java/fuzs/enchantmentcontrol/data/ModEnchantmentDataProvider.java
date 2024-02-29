package fuzs.enchantmentcontrol.data;

import fuzs.enchantmentcontrol.api.v1.data.AbstractEnchantmentDataProvider;
import fuzs.enchantmentcontrol.world.item.enchantment.EnchantmentDataImpl;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.world.item.enchantment.Enchantments;

public class ModEnchantmentDataProvider extends AbstractEnchantmentDataProvider {

    public ModEnchantmentDataProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addEnchantmentData() {
        this.add(Enchantments.SILK_TOUCH, EnchantmentDataImpl.fromEnchantment(Enchantments.SILK_TOUCH).withMaxLevel(8));
        this.add(Enchantments.BLOCK_FORTUNE, EnchantmentDataImpl.fromEnchantment(Enchantments.BLOCK_FORTUNE).withMaxLevel(8));
        this.add(Enchantments.BLOCK_EFFICIENCY, EnchantmentDataImpl.fromEnchantment(Enchantments.BLOCK_EFFICIENCY).withMaxLevel(8));
        this.add(Enchantments.MENDING, EnchantmentDataImpl.fromEnchantment(Enchantments.MENDING).withMaxLevel(8).withAliases(Enchantments.UNBREAKING));
    }
}
