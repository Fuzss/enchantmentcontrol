package fuzs.enchantmentcontrol.data;

import fuzs.enchantmentcontrol.world.item.enchantment.DataBasedEnchantmentComponent;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.world.item.enchantment.Enchantments;

public class ModEnchantmentDataProvider extends AbstractEnchantmentDataProvider {

    public ModEnchantmentDataProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addEnchantmentData() {
        this.add(Enchantments.SILK_TOUCH, DataBasedEnchantmentComponent.fromEnchantment(Enchantments.SILK_TOUCH).withMaxLevel(8));
        this.add(Enchantments.BLOCK_FORTUNE, DataBasedEnchantmentComponent.fromEnchantment(Enchantments.BLOCK_FORTUNE).withMaxLevel(8));
        this.add(Enchantments.BLOCK_EFFICIENCY, DataBasedEnchantmentComponent.fromEnchantment(Enchantments.BLOCK_EFFICIENCY).withMaxLevel(8));
        this.add(Enchantments.MENDING, DataBasedEnchantmentComponent.fromEnchantment(Enchantments.MENDING).withMaxLevel(8));
    }
}
