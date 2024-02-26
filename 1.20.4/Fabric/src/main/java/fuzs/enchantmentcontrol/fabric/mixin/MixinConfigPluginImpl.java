package fuzs.enchantmentcontrol.fabric.mixin;

import com.google.common.collect.Maps;
import fuzs.enchantmentcontrol.handler.EnchantmentClassesCache;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.security.Permission;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class MixinConfigPluginImpl implements IMixinConfigPlugin {
    private static final Map<String, Supplier<List<String>>> DYNAMIC_MIXINS = Maps.newLinkedHashMap();
    private static final String DYNAMIC_MIXIN_CLASSES_SUB_PACKAGE = "dynamic";
    private static boolean applied;

    static {
        register("EnchantmentFabricMixin", EnchantmentClassesCache::load);
    }

    private static void register(String mixinClassName, Supplier<List<String>> targetClasses) {
        DYNAMIC_MIXINS.put(mixinClassName.replace('.', '/'), targetClasses);
    }

    @Override
    public void onLoad(String mixinPackage) {

        if (applied) {
            return;
        } else {
            applied = true;
        }

        mixinPackage = mixinPackage.replace('.', '/');
        Map<String, byte[]> classGenerators = Maps.newHashMap();

        Iterator<Map.Entry<String, Supplier<List<String>>>> iterator = DYNAMIC_MIXINS.entrySet().iterator();
        while (iterator.hasNext()) {

            Map.Entry<String, Supplier<List<String>>> entry = iterator.next();

            String mixinClassName = entry.getKey();
            if (mixinClassName.startsWith(mixinPackage)) {
                mixinClassName = mixinClassName.substring(mixinPackage.length());
            }

            List<String> targets = entry.getValue().get();
            if (targets.isEmpty()) {
                iterator.remove();
                continue;
            }

            try (InputStream inputStream = this.getClass().getResourceAsStream(mixinClassName + ".class")) {

                Objects.requireNonNull(inputStream, "input stream is null");

                ClassReader classReader = new ClassReader(inputStream);
                ClassNode classNode = new ClassNode();
                classReader.accept(classNode, 0);

                classNode.invisibleAnnotations.removeIf((AnnotationNode annotationNode) -> {
                    return annotationNode.desc.equals("Lorg/spongepowered/asm/mixin/Mixin;");
                });
                AnnotationVisitor mixinAnnotation = classNode.visitAnnotation("Lorg/spongepowered/asm/mixin/Mixin;",
                        false
                );
                AnnotationVisitor targetAnnotation = mixinAnnotation.visitArray("targets");
                for (String target : targets) {
                    targetAnnotation.visit(null, target);
                }
                targetAnnotation.visitEnd();
                mixinAnnotation.visitEnd();

                ClassWriter classWriter = new ClassWriter(0);
                classNode.accept(classWriter);
                byte[] byteArray = classWriter.toByteArray();

                StringJoiner joiner = new StringJoiner("/", "/", "");
                joiner.add(mixinPackage);
                joiner.add(DYNAMIC_MIXIN_CLASSES_SUB_PACKAGE);
                joiner.add(mixinClassName + ".class");
                classGenerators.put(joiner.toString(), byteArray);

            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        }

        try {
            getURLConsumer(this.getClass().getClassLoader()).accept(new URL("magic-at",
                    null,
                    -1,
                    "/",
                    new URLStreamHandler() {

                        @Override
                        protected URLConnection openConnection(URL url) throws IOException {
                            if (!classGenerators.containsKey(url.getPath())) {
                                return null;
                            } else {
                                return new URLConnection(url) {

                                    @Override
                                    public void connect() throws IOException {
                                        throw new UnsupportedOperationException();
                                    }

                                    @Override
                                    public InputStream getInputStream() throws IOException {
                                        return new ByteArrayInputStream(classGenerators.get(this.url.getPath()));
                                    }

                                    @Override
                                    public Permission getPermission() throws IOException {
                                        return null;
                                    }
                                };
                            }
                        }
                    }
            ));
        } catch (MalformedURLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
        // NO-OP
    }

    @Override
    public List<String> getMixins() {
        return DYNAMIC_MIXINS.keySet().stream().map((String clazzName) -> {
            return DYNAMIC_MIXIN_CLASSES_SUB_PACKAGE + "." + clazzName.replace("/", ".");
        }).toList();
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        // NO-OP
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        // NO-OP
    }

    private static Consumer<URL> getURLConsumer(ClassLoader classLoader) {
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
