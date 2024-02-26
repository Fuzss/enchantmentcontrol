package fuzs.enchantmentcontrol.data;

import fuzs.enchantmentcontrol.world.item.enchantment.EnchantmentData;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.world.item.enchantment.Enchantments;

public class ModEnchantmentDataProvider extends AbstractEnchantmentDataProvider {

    public ModEnchantmentDataProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addEnchantmentData() {
        this.add(Enchantments.SILK_TOUCH, EnchantmentData.fromEnchantment(Enchantments.SILK_TOUCH).withMaxLevel(8));
    }
}
