package fuzs.enchantmentcontrol.mixin;

import fuzs.enchantmentcontrol.util.ModEnchantmentHelper;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.Set;

@Mixin(value = CreativeModeTab.class, priority = 1200)
abstract class CreativeModeTabMixin {
    @Shadow
    private Collection<ItemStack> displayItems;
    @Shadow
    private Set<ItemStack> displayItemsSearchTab;

    @Inject(
            method = "buildContents",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/CreativeModeTab;rebuildSearchTree()V")
    )
    public void buildContents(CreativeModeTab.ItemDisplayParameters parameters, CallbackInfo callback) {
        this.displayItems.removeIf(ModEnchantmentHelper::isBookWithDisabledEnchantments);
        this.displayItemsSearchTab.removeIf(ModEnchantmentHelper::isBookWithDisabledEnchantments);
    }
}
