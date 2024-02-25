package fuzs.enchantmentcontrol.world.item.enchantment;

import com.google.common.base.CharMatcher;
import com.google.gson.JsonArray;
import fuzs.enchantmentcontrol.EnchantmentControl;
import fuzs.puzzleslib.api.config.v3.json.JsonConfigFileUtil;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

import java.io.File;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

public final class MaxLevelManager {
    private static final Object2IntMap<Enchantment> MAX_LEVEL_OVERRIDES;

    static {
        Object2IntOpenHashMap<Enchantment> map = new Object2IntOpenHashMap<>();
        map.defaultReturnValue(-1);
        map.put(Enchantments.SHARPNESS, 10);
        map.put(Enchantments.BLOCK_EFFICIENCY, 10);
        map.put(Enchantments.POWER_ARROWS, 10);
        map.put(Enchantments.MOB_LOOTING, 5);
        map.put(Enchantments.FIRE_ASPECT, 5);
        map.put(Enchantments.MENDING, 2);
        MAX_LEVEL_OVERRIDES = Object2IntMaps.unmodifiable(map);
    }

    private MaxLevelManager() {

    }

    public static int getMaxLevel(Enchantment enchantment) {
        return MAX_LEVEL_OVERRIDES.getInt(enchantment);
    }

    public static void onLoadComplete() {
        JsonArray jsonArray = new JsonArray();
        Stream.concat(Stream.of(Enchantment.class), BuiltInRegistries.ENCHANTMENT.stream().map(Object::getClass))
                .distinct().map(Class::getName)
                .sorted(Comparator.<String>comparingInt(s -> CharMatcher.is('.').countIn(s)).thenComparing(Comparator.naturalOrder()))
                .forEach(jsonArray::add);
        File file = JsonConfigFileUtil.getConfigPath("." + EnchantmentControl.MOD_ID + "cache");
        JsonConfigFileUtil.saveToFile(file, jsonArray);
    }
}
