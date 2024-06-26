package fuzs.enchantmentcontrol.impl.util;

import fuzs.enchantmentcontrol.impl.world.item.enchantment.EnchantmentFeature;
import fuzs.enchantmentcontrol.impl.world.item.enchantment.EnchantmentHolder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BookItem;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.Map;

public class ModEnchantmentHelper {

    public static ItemStack addEnchantments(Map<Enchantment, Integer> enchantments, ItemStack itemStack, boolean override) {
        Map<Enchantment, Integer> oldEnchantments = EnchantmentHelper.getEnchantments(itemStack);
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            if (override) {
                oldEnchantments.put(entry.getKey(), entry.getValue());
            } else {
                oldEnchantments.merge(entry.getKey(), entry.getValue(), Math::max);
            }
        }

        return setEnchantments(enchantments, itemStack);
    }

    public static ItemStack setEnchantments(Map<Enchantment, Integer> enchantments, ItemStack itemStack) {

        ListTag listTag = new ListTag();

        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            if (entry.getKey() != null && entry.getValue() > 0) {
                ResourceLocation resourceLocation = BuiltInRegistries.ENCHANTMENT.getKey(entry.getKey());
                listTag.add(EnchantmentHelper.storeEnchantment(resourceLocation, entry.getValue()));
            }
        }

        itemStack.removeTagKey(ItemStack.TAG_ENCH);
        itemStack.removeTagKey(EnchantedBookItem.TAG_STORED_ENCHANTMENTS);
        // this will turn enchanted books into normal books if there are no enchantments left
        itemStack = getEnchantedStack(itemStack, !listTag.isEmpty());

        if (!listTag.isEmpty()) {
            String tagKey = itemStack.getItem() instanceof EnchantedBookItem ? EnchantedBookItem.TAG_STORED_ENCHANTMENTS : ItemStack.TAG_ENCH;
            itemStack.addTagElement(tagKey, listTag);
        }

        return itemStack;
    }

    public static ItemStack getEnchantedStack(ItemStack itemStack, boolean isEnchanted) {
        ItemStack newStack;
        if (itemStack.getItem() instanceof EnchantedBookItem && !isEnchanted) {
            newStack = new ItemStack(Items.BOOK, itemStack.getCount());
        } else if (itemStack.getItem() instanceof BookItem && isEnchanted) {
            newStack = new ItemStack(Items.ENCHANTED_BOOK, itemStack.getCount());
        } else {
            return itemStack;
        }
        CompoundTag tag = itemStack.getTag();
        if (tag != null) {
            newStack.setTag(tag.copy());
        }

        return newStack;
    }

    public static boolean isBookWithDisabledEnchantments(ItemStack itemStack) {
        if (itemStack.getItem() instanceof EnchantedBookItem) {
            for (Enchantment enchantment : EnchantmentHelper.getEnchantments(itemStack).keySet()) {
                if (!((EnchantmentFeature) enchantment).enchantmentcontrol$isEnabled()) {
                    return true;
                }
                EnchantmentHolder holder = ((EnchantmentFeature) enchantment).enchantmentcontrol$getHolder();
                if (holder != null && !holder.isAllowedOnBooks()) {
                    return true;
                }
            }
        }

        return false;
    }
}
