package fuzs.enchantmentcontrol.fabric.mixin;

import fuzs.enchantmentcontrol.impl.handler.EnchantmentClassesCache;
import fuzs.enchantmentcontrol.mixin.AbstractMixinConfigPlugin;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.function.Consumer;

public class MixinConfigPluginFabricImpl extends AbstractMixinConfigPlugin {

    static {
        register("EnchantmentFabricMixin", EnchantmentClassesCache::load);
    }

    @Override
    protected Consumer<URL> getClassLoaderURLConsumer(ClassLoader classLoader, String packageName) {
        Method method = findAddUrlFwdMethod(classLoader);
        try {
            method.setAccessible(true);
            MethodHandle handle = MethodHandles.lookup().unreflect(method);
            return (URL url) -> {
                try {
                    handle.invoke(classLoader, url);
                } catch (Throwable throwable) {
                    throw new RuntimeException("Unexpected error adding URL", throwable);
                }
            };
        } catch (IllegalAccessException exception) {
            throw new RuntimeException("Couldn't get handle for " + method, exception);
        }
    }

    /**
     * We want to find {@link net.fabricmc.loader.impl.launch.knot.KnotClassDelegate.ClassLoaderAccess#addUrlFwd(URL)}.
     */
    protected static Method findAddUrlFwdMethod(ClassLoader classLoader) {
        Class<? extends ClassLoader> clazz = classLoader.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getReturnType() == void.class) {
                if (method.getParameterCount() == 1 && method.getParameterTypes()[0] == URL.class) {
                    return method;
                }
            }
        }

        throw new IllegalStateException("Couldn't find method in " + classLoader);
    }
}
