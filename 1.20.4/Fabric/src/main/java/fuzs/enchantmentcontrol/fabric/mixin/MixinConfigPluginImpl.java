package fuzs.enchantmentcontrol.fabric.mixin;

import fuzs.enchantmentcontrol.handler.EnchantmentClassesCache;
import fuzs.enchantmentcontrol.mixin.AbstractMixinConfigPlugin;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.function.Consumer;

public class MixinConfigPluginImpl extends AbstractMixinConfigPlugin {

    static {
        register("EnchantmentFabricMixin", EnchantmentClassesCache::load);
    }

    @Override
    protected Consumer<URL> getClassLoaderURLConsumer(ClassLoader classLoader, String packageName) {
        // we want to find ClassLoaderAccess::addUrlFwd
        Method foundMethod = null;
        Class<? extends ClassLoader> clazz = classLoader.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getReturnType() == void.class) {
                if (method.getParameterCount() == 1 && method.getParameterTypes()[0] == URL.class) {
                    foundMethod = method;
                    break;
                }
            }
        }

        if (foundMethod == null) {
            throw new IllegalStateException("Couldn't find method in " + classLoader);
        } else {
            try {
                foundMethod.setAccessible(true);
                MethodHandle handle = MethodHandles.lookup().unreflect(foundMethod);
                return (URL url) -> {
                    try {
                        handle.invoke(classLoader, url);
                    } catch (Throwable throwable) {
                        throw new RuntimeException("Unexpected error adding URL", throwable);
                    }
                };
            } catch (IllegalAccessException exception) {
                throw new RuntimeException("Couldn't get handle for " + foundMethod, exception);
            }
        }
    }
}
