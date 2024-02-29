package fuzs.enchantmentcontrol.fabric.mixin;

import fuzs.enchantmentcontrol.world.item.enchantment.EnchantmentFeature;
import fuzs.enchantmentcontrol.world.item.enchantment.EnchantmentHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * This mixin is dynamically applied to the enchantment class and all subclasses. The mixin config plugin expands the
 * targets property of the {@link Mixin} annotation at runtime and hacks the newly created class into the classloader.
 * <p>
 * All injectors are set to require 0 successful injections as not all subclasses will necessarily override every method
 * we want to inject into.
 * <p>
 * Also, all possible mappings (order is: NeoForge &amp; Fabric &amp; Forge) must be provided right here, as the
 * dynamically generated mixin class is a new class which will not have an entry in the refmap. Since the refmap is not
 * used the remap property is set to <code>false</code>.
 * <p>
 * This class must be copied into every subproject since the mixin package is in the mod loader specific package
 * (<code>fuzs.enchantmentcontrol.fabric.mixin</code> instead of <code>fuzs.enchantmentcontrol.mixin</code>) and
 * {@link Class#getResourceAsStream(String)} seems to be unable to load resources from a package that is not a
 * subpackage.
 */
@Mixin(Enchantment.class)
abstract class EnchantmentFabricMixin implements EnchantmentFeature {

    @SuppressWarnings("target")
    @Inject(
            method = {"getMinLevel()I", "method_8187()I", "m_44702_()I"},
            at = @At("HEAD"),
            cancellable = true,
            require = 0,
            remap = false
    )
    public void getMinLevel(CallbackInfoReturnable<Integer> callback) {
        this.ifHolderPresent(holder -> holder.getEnchantmentData().minLevel(), callback::setReturnValue);
    }

    @SuppressWarnings("target")
    @Inject(
            method = {"getMaxLevel()I", "method_8183()I", "m_6586_()I"},
            at = @At("HEAD"),
            cancellable = true,
            require = 0,
            remap = false
    )
    public void getMaxLevel(CallbackInfoReturnable<Integer> callback) {
        this.ifHolderPresent(holder -> holder.getEnchantmentData().maxLevel(), callback::setReturnValue);
    }

    @SuppressWarnings("target")
    @Inject(
            method = {
                    "canEnchant(Lnet/minecraft/world/item/ItemStack;)Z",
                    "method_8192(Lnet/minecraft/class_1799;)Z",
                    "m_6081_(Lnet/minecraft/world/item/ItemStack;)Z"
            }, at = @At("HEAD"), cancellable = true, require = 0, remap = false
    )
    public void canEnchant(ItemStack itemStack, CallbackInfoReturnable<Boolean> callback) {
        this.ifHolderPresent(holder -> itemStack.is(holder.getAnvilItemTag()), callback::setReturnValue, false);
    }

    @SuppressWarnings("target")
    @Inject(
            method = {"isTreasureOnly()Z", "method_8193()Z", "m_6591_()Z"},
            at = @At("HEAD"),
            cancellable = true,
            require = 0,
            remap = false
    )
    public void isTreasureOnly(CallbackInfoReturnable<Boolean> callback) {
        this.ifHolderPresent(EnchantmentHolder::isTreasureOnly, callback::setReturnValue);
    }

    @SuppressWarnings("target")
    @Inject(
            method = {"isCurse()Z", "method_8195()Z", "m_6589_()Z"},
            at = @At("HEAD"),
            cancellable = true,
            require = 0,
            remap = false
    )
    public void isCurse(CallbackInfoReturnable<Boolean> callback) {
        this.ifHolderPresent(EnchantmentHolder::isCurse, callback::setReturnValue);
    }

    @SuppressWarnings("target")
    @Inject(
            method = {"isTradeable()Z", "method_25949()Z", "m_6594_()Z"},
            at = @At("HEAD"),
            cancellable = true,
            require = 0,
            remap = false
    )
    public void isTradeable(CallbackInfoReturnable<Boolean> callback) {
        this.ifHolderPresent(EnchantmentHolder::isTradeable, callback::setReturnValue, false);
    }

    @SuppressWarnings("target")
    @Inject(
            method = {"isDiscoverable()Z", "method_25950()Z", "m_6592_()Z"},
            at = @At("HEAD"),
            cancellable = true,
            require = 0,
            remap = false
    )
    public void isDiscoverable(CallbackInfoReturnable<Boolean> callback) {
        this.ifHolderPresent(EnchantmentHolder::isDiscoverable, callback::setReturnValue, false);
    }
}
