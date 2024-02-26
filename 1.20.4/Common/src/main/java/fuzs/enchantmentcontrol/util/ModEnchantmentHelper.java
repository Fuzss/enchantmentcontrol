package fuzs.enchantmentcontrol.util;

import fuzs.enchantmentcontrol.world.item.enchantment.EnchantmentFeature;
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

    public static Map<Enchantment, Integer> getEnchantments(ItemStack itemStack) {
        return EnchantmentHelper.getEnchantments(itemStack);
    }

    public static ItemStack addEnchantments(Map<Enchantment, Integer> enchantments, ItemStack itemStack, boolean override) {
        Map<Enchantment, Integer> oldEnchantments = getEnchantments(itemStack);
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
        return itemStack.is(Items.ENCHANTED_BOOK) && getEnchantments(itemStack).keySet().removeIf(enchantment -> {
            // just a wacky compact syntax, we don't need to actually remove anything here
            return !((EnchantmentFeature) enchantment).isEnabled();
        });
    }
}
