package fuzs.enchantmentcontrol.impl.handler;

import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureElement;
import sun.misc.Unsafe;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class UnsafeHandler {
    private static final Unsafe UNSAFE;

    static {
        try {
            Constructor<?> constructor = Unsafe.class.getDeclaredConstructors()[0];
            constructor.setAccessible(true);
            UNSAFE = (Unsafe) constructor.newInstance();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    public static void addEnchantmentsToFilteredRegistries() {
        // add enchantments registry to filtered registries map, not sure if there is another way to modify this interface field containing an immutable map
        // this will e.g. hide disabled enchantments from the /enchant command
        Field filteredRegistriesField = Iterators.forArray(FeatureElement.class.getFields()).next();
        long filteredRegistriesOffset = UNSAFE.staticFieldOffset(filteredRegistriesField);
        Object filteredRegistriesBase = UNSAFE.staticFieldBase(filteredRegistriesField);
        UNSAFE.putObject(filteredRegistriesBase,
                filteredRegistriesOffset,
                Sets.newHashSet(FeatureElement.FILTERED_REGISTRIES)
        );
        FeatureElement.FILTERED_REGISTRIES.add((ResourceKey<? extends Registry<? extends FeatureElement>>) (ResourceKey<?>) Registries.ENCHANTMENT);
    }

    public static Unsafe getUnsafe() {
        // we need this on Forge & NeoForge to access class loader internals
        return UNSAFE;
    }
}
