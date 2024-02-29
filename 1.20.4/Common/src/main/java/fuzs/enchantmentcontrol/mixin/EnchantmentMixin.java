package fuzs.enchantmentcontrol.mixin;

import com.google.common.base.Preconditions;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import fuzs.enchantmentcontrol.world.item.enchantment.EnchantmentFeature;
import fuzs.enchantmentcontrol.world.item.enchantment.EnchantmentHolder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
abstract class EnchantmentMixin implements EnchantmentFeature {
    @Unique
    @Nullable
    private EnchantmentHolder enchantmentcontrol$enchantmentHolder;

    @Override
    public @Nullable EnchantmentHolder getHolder() {
        return this.enchantmentcontrol$enchantmentHolder;
    }

    @Override
    public void setHolder(@Nullable EnchantmentHolder enchantmentHolder) {
        Preconditions.checkState(enchantmentHolder == null || this.enchantmentcontrol$enchantmentHolder == null,
                "holder is not null"
        );
        this.enchantmentcontrol$enchantmentHolder = enchantmentHolder;
    }

    @Inject(method = "isCompatibleWith", at = @At("HEAD"), cancellable = true)
    public void isCompatibleWith(Enchantment other, CallbackInfoReturnable<Boolean> callback) {
        // hook compatibility check here instead of injecting into Enchantment::checkCompatibility for all enchantment classes to reduce the patch surface
        EnchantmentHolder holder = this.getHolder();
        EnchantmentHolder otherHolder = ((EnchantmentFeature) other).getHolder();
        if (holder != null && otherHolder != null) {
            callback.setReturnValue(holder.isCompatibleWith(otherHolder));
        }
    }

    @ModifyReturnValue(method = "getFullname", at = @At("TAIL"))
    public Component getFullname(Component component) {
        return !this.isEnabled() ? ((MutableComponent) component).withStyle(ChatFormatting.STRIKETHROUGH) : component;
    }
}
