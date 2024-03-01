package fuzs.enchantmentcontrol.forge.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fuzs.enchantmentcontrol.world.item.enchantment.EnchantmentFeature;
import net.minecraft.Util;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Mixin(EnchantmentHelper.class)
abstract class EnchantmentHelperForgeMixin {

    @WrapOperation(
            method = "runIterationOnItem", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;getAllEnchantments()Ljava/util/Map;",
            remap = false
    )
    )
    private static Map<Enchantment, Integer> runIterationOnItem(ItemStack itemStack, Operation<Map<Enchantment, Integer>> operation) {
        return operation.call(itemStack).entrySet().stream().filter(entry -> {
            return ((EnchantmentFeature) entry.getKey()).isEnabled();
        }).collect(Util.toMap());
    }
}
