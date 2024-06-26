package fuzs.enchantmentcontrol.mixin;

import fuzs.enchantmentcontrol.impl.EnchantmentControlMod;
import fuzs.enchantmentcontrol.impl.config.CommonConfig;
import fuzs.enchantmentcontrol.impl.util.ModEnchantmentHelper;
import fuzs.enchantmentcontrol.impl.world.item.enchantment.EnchantmentFeature;
import fuzs.enchantmentcontrol.impl.world.item.enchantment.EnchantmentHolder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Map;

@Mixin(LootTable.class)
abstract class LootTableMixin {

    @ModifyVariable(
            method = "getRandomItems(Lnet/minecraft/world/level/storage/loot/LootContext;)Lit/unimi/dsi/fastutil/objects/ObjectArrayList;",
            at = @At(value = "TAIL", shift = At.Shift.BEFORE)
    )
    private ObjectArrayList<ItemStack> getRandomItems(ObjectArrayList<ItemStack> items, LootContext context) {
        if (EnchantmentControlMod.CONFIG.get(CommonConfig.class).removeUnobtainableFromLoot) {
            // remove disabled enchantments from generated loot, turn enchanted books into normal books if no enchantments are left
            ObjectListIterator<ItemStack> iterator = items.iterator();
            while (iterator.hasNext()) {
                ItemStack itemStack = iterator.next();
                boolean isBook = itemStack.getItem() instanceof EnchantedBookItem;
                if (isBook || itemStack.isEnchanted()) {
                    Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemStack);
                    boolean result = enchantments.keySet().removeIf(enchantment -> {
                        if (!((EnchantmentFeature) enchantment).enchantmentcontrol$isEnabled()) {
                            return true;
                        }
                        if (isBook) {
                            EnchantmentHolder holder = ((EnchantmentFeature) enchantment).enchantmentcontrol$getHolder();
                            if (holder != null && !holder.isAllowedOnBooks()) {
                                return true;
                            }
                        }

                        return false;
                    });
                    if (result) {
                        // custom implementation that turns enchanted books into normal books when there are no enchantments left
                        iterator.set(ModEnchantmentHelper.setEnchantments(enchantments, itemStack));
                    }
                }
            }
        }

        return items;
    }
}
