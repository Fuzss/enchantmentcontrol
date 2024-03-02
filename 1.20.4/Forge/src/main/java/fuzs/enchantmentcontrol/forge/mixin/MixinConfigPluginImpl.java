package fuzs.enchantmentcontrol.forge.mixin;

import fuzs.enchantmentcontrol.impl.handler.EnchantmentClassesCache;
import fuzs.enchantmentcontrol.impl.handler.UnsafeHandler;
import fuzs.enchantmentcontrol.mixin.AbstractMixinConfigPlugin;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.function.Consumer;

public class MixinConfigPluginImpl extends AbstractMixinConfigPlugin {

    static {
        register("EnchantmentForgeMixin", EnchantmentClassesCache::load);
    }

    @Override
    protected Consumer<URL> getClassLoaderURLConsumer(ClassLoader classLoader, String packageName) {
        Field field = findParentLoadersField(classLoader);
        try {
            Map<String, ClassLoader> parentLoaders = getParentLoaders(classLoader, field);
            return (URL url) -> {
                try {
                    // add the new url with a new url class loader in a new module path
                    // cannot use a path where a module already exists
                    String moduleName = packageName.replace('/', '.');
                    ClassLoader moduleClassLoader = parentLoaders.computeIfAbsent(moduleName,
                            $ -> new AccessibleURLClassLoader()
                    );
                    if (moduleClassLoader instanceof AccessibleURLClassLoader urlClassLoader) {
                        urlClassLoader.addURL(url);
                    } else {
                        throw new IllegalStateException(
                                "Unsupported class loader " + moduleClassLoader + " found for module " + moduleName);
                    }
                } catch (Throwable throwable) {
                    throw new RuntimeException("Unexpected error adding URL", throwable);
                }
            };
        } catch (Throwable throwable) {
            throw new RuntimeException("Couldn't get value from " + field, throwable);
        }
    }

    /**
     * We want to find {@link net.minecraftforge.securemodules.SecureModuleClassLoader#packageToParentLoader}.
     */
    protected static Field findParentLoadersField(ClassLoader classLoader) {
        Class<?> clazz = classLoader.getClass();
        while (clazz != ClassLoader.class && clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.getType() == Map.class) {
                    Type[] typeArguments = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
                    if (typeArguments.length == 2 && typeArguments[0] == String.class &&
                            typeArguments[1] == ClassLoader.class) {
                        return field;
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }

        throw new IllegalStateException("Couldn't find field in " + classLoader);
    }

    @SuppressWarnings("unchecked")
    protected static Map<String, ClassLoader> getParentLoaders(ClassLoader classLoader, Field field) {
        // we cannot access anything inside this module, use unsafe to get access to the field
        // do not try to set the field accessible here, unsafe does not need it
        Unsafe unsafe = UnsafeHandler.getUnsafe();
        long fieldOffset = unsafe.objectFieldOffset(field);
        return (Map<String, ClassLoader>) unsafe.getObject(classLoader, fieldOffset);
    }

    protected static class AccessibleURLClassLoader extends URLClassLoader {

        public AccessibleURLClassLoader() {
            // the Forge class loader also doesn't have a parent, so let's just assume this is ok
            // otherwise can just implement a basic empty class loader, Fabric loader has something like that *somewhere*
            super(new URL[0], null);
        }

        @Override
        public void addURL(URL url) {
            super.addURL(url);
        }
    }
}
