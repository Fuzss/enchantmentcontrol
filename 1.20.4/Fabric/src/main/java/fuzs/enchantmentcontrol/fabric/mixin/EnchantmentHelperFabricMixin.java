package fuzs.enchantmentcontrol.fabric.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fuzs.enchantmentcontrol.impl.world.item.enchantment.EnchantmentFeature;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(EnchantmentHelper.class)
abstract class EnchantmentHelperFabricMixin {

    @WrapOperation(
            method = "runIterationOnItem", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/Registry;getOptional(Lnet/minecraft/resources/ResourceLocation;)Ljava/util/Optional;"
    )
    )
    private static Optional<Enchantment> runIterationOnItem(Registry<Enchantment> registry, ResourceLocation resourceLocation, Operation<Optional<Enchantment>> operation) {
        return operation.call(registry, resourceLocation).filter(enchantment -> {
            return ((EnchantmentFeature) enchantment).enchantmentcontrol$isEnabled();
        });
    }
}
