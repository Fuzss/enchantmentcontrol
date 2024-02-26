package fuzs.enchantmentcontrol.fabric.mixin;

import fuzs.enchantmentcontrol.world.item.enchantment.EnchantmentFeature;
import fuzs.enchantmentcontrol.world.item.enchantment.EnchantmentHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
abstract class EnchantmentFabricMixin implements EnchantmentFeature {

    @Inject(method = {"getMinLevel()I", "method_8187()I", "m_44702_()I"}, at = @At("HEAD"), cancellable = true, require = 0, remap = false)
    public void getMinLevel(CallbackInfoReturnable<Integer> callback) {
        this.ifHolderPresent(holder -> holder.getEnchantmentData().minLevel(), callback::setReturnValue);
    }

    @Inject(method = {"getMaxLevel()I", "method_8183()I", "m_6586_()I"}, at = @At("HEAD"), cancellable = true, require = 0, remap = false)
    public void getMaxLevel(CallbackInfoReturnable<Integer> callback) {
        this.ifHolderPresent(holder -> holder.getEnchantmentData().maxLevel(), callback::setReturnValue);
    }

    @Inject(method = {"canEnchant(Lnet/minecraft/world/item/ItemStack;)Z", "method_8192(Lnet/minecraft/class_1799;)Z", "m_6081_(Lnet/minecraft/world/item/ItemStack;)Z"}, at = @At("HEAD"), cancellable = true, require = 0, remap = false)
    public void canEnchant(ItemStack itemStack, CallbackInfoReturnable<Boolean> callback) {
        this.ifHolderPresent(holder -> this.isEnabled() && itemStack.is(holder.getAnvilItemTag()), callback::setReturnValue);
    }

    @Inject(method = {"isTreasureOnly()Z", "method_8193()Z", "m_6591_()Z"}, at = @At("HEAD"), cancellable = true, require = 0, remap = false)
    public void isTreasureOnly(CallbackInfoReturnable<Boolean> callback) {
        this.ifHolderPresent(EnchantmentHolder::isTreasureOnly, callback::setReturnValue);
    }

    @Inject(method = {"isCurse()Z", "method_8195()Z", "m_6589_()Z"}, at = @At("HEAD"), cancellable = true, require = 0, remap = false)
    public void isCurse(CallbackInfoReturnable<Boolean> callback) {
        this.ifHolderPresent(EnchantmentHolder::isCurse, callback::setReturnValue);
    }

    @Inject(method = {"isTradeable()Z", "method_25949()Z", "m_6594_()Z"}, at = @At("HEAD"), cancellable = true, require = 0, remap = false)
    public void isTradeable(CallbackInfoReturnable<Boolean> callback) {
        this.ifHolderPresent(EnchantmentHolder::isTradeable, callback::setReturnValue);
    }

    @Inject(method = {"isDiscoverable()Z", "method_25950()Z", "m_6592_()Z"}, at = @At("HEAD"), cancellable = true, require = 0, remap = false)
    public void isDiscoverable(CallbackInfoReturnable<Boolean> callback) {
        this.ifHolderPresent(EnchantmentHolder::isDiscoverable, callback::setReturnValue);
    }
}
