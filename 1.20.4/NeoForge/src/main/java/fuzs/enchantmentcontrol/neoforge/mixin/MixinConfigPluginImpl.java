package fuzs.enchantmentcontrol.neoforge.mixin;

import fuzs.enchantmentcontrol.handler.EnchantmentClassesCache;
import fuzs.enchantmentcontrol.handler.UnsafeHandler;
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
        register("EnchantmentNeoForgeMixin", EnchantmentClassesCache::load);
    }

    @Override
    protected Consumer<URL> getClassLoaderURLConsumer(ClassLoader classLoader, String packageName) {
        // we want to find SecureModuleClassLoader::packageToParentLoader (Forge 1.20.4) or ModuleClassLoader::parentLoaders (NeoForge 1.20.4)
        Field foundField = null;
        Class<?> clazz = classLoader.getClass();
        $1:
        while (clazz != ClassLoader.class && clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.getType() == Map.class) {
                    Type[] typeArguments = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
                    if (typeArguments.length == 2 && typeArguments[0] == String.class &&
                            typeArguments[1] == ClassLoader.class) {
                        foundField = field;
                        break $1;
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }

        if (foundField == null) {
            throw new IllegalStateException("Couldn't find field in " + classLoader);
        } else {
            try {
                // we cannot access anything inside this module, use unsafe to get access to the field
                Unsafe unsafe = UnsafeHandler.getUnsafe();
                long fieldOffset = unsafe.objectFieldOffset(foundField);
                Map<String, ClassLoader> parentLoaders = (Map<String, ClassLoader>) unsafe.getObject(classLoader,
                        fieldOffset
                );
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
                                    "Unsupported class loader " + moduleClassLoader + " found for module " +
                                            moduleName);
                        }
                    } catch (Throwable throwable) {
                        throw new RuntimeException("Unexpected error adding URL", throwable);
                    }
                };
            } catch (Throwable throwable) {
                throw new RuntimeException("Couldn't get value from " + foundField, throwable);
            }
        }
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
