package fuzs.enchantmentcontrol.handler;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import fuzs.enchantmentcontrol.EnchantmentControl;
import fuzs.puzzleslib.api.core.v1.ModLoaderEnvironment;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.enchantment.Enchantment;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public final class EnchantmentClassesCache {
    public static final Path CACHE_FILE_PATH = ModLoaderEnvironment.INSTANCE.getConfigDirectory()
            .resolve("." + EnchantmentControl.MOD_ID + "cache");

    public static void save() {
        try {
            try (PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(CACHE_FILE_PATH.toFile()),
                    StandardCharsets.UTF_8
            ))) {
                getAllEnchantmentClassNames().forEach(printWriter::println);
            }
        } catch (Exception exception) {
            EnchantmentControl.LOGGER.error("Failed to save file at " + CACHE_FILE_PATH, exception);
        }
    }

    public static List<String> load() {
        try {
            if (CACHE_FILE_PATH.toFile().exists()) {
                try (BufferedReader bufferedReader = Files.newReader(CACHE_FILE_PATH.toFile(),
                        Charsets.UTF_8
                )) {
                    return bufferedReader.lines().toList();
                }
            }
        } catch (Throwable throwable) {
            EnchantmentControl.LOGGER.warn("Failed to load file at " + CACHE_FILE_PATH, throwable);
        }

        return Collections.emptyList();
    }

    public static Stream<String> getAllEnchantmentClassNames() {
        return getAllEnchantmentClasses().map(Class::getName).sorted((String s1, String s2) -> {
            String ss1 = s1.substring(0, s1.lastIndexOf("."));
            String ss2 = s2.substring(0, s2.lastIndexOf("."));
            if (ss1.equals(ss2)) {
                return s1.compareTo(s2);
            } else if (ss1.startsWith(ss2)) {
                return 1;
            } else if (ss2.startsWith(ss1)) {
                return -1;
            } else {
                return s1.compareTo(s2);
            }
        });
    }

    private static Stream<Class<?>> getAllEnchantmentClasses() {
        return Stream.concat(Stream.of(Enchantment.class),
                BuiltInRegistries.ENCHANTMENT.stream().mapMulti(EnchantmentClassesCache::addEnchantmentAndSuperClasses)
        ).distinct();
    }

    private static void addEnchantmentAndSuperClasses(Object o, Consumer<Class<?>> consumer) {
        Class<?> clazz = o.getClass();
        while (clazz != Enchantment.class) {
            consumer.accept(clazz);
            clazz = clazz.getSuperclass();
        }
    }
}
