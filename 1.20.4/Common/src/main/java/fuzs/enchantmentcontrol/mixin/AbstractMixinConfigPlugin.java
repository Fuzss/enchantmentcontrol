package fuzs.enchantmentcontrol.mixin;

import com.google.common.collect.Maps;
import fuzs.enchantmentcontrol.EnchantmentControl;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.security.Permission;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A mixin plugin for dynamically applying our enchantment mixin (found in mod loader specific subprojects) to all
 * enchantment classes. This approach is chosen as:
 * <ul>
 * <li>Only applying to the base enchantment class is not sufficient, as most methods are designed to be
 * overridden</li>
 * <li>Also modifying the callsites (an approach the Apotheosis mod has taken) is not an option, since there is no way
 * for preventing other mods from using the vanilla methods</li>
 * </ul>
 * All class targets for our dynamic enchantment mixin are
 * determined at runtime from all registered enchantments and stored to a cache file that can be used for future
 * applications. Since enchantment registration happens long after mixins must be applied, the dynamic mixin creation
 * will not work on the first launch with this mod when the cache file is still absent. When that is the case a warning
 * screen will show in-game. This is no issue for mod packs (what this mod is primarily designed for), as they will
 * already ship the cache file as part of their default configuration.
 * <p>
 * Note that the newly generated mixin class must be placed in a different and also unused package from the original to
 * comply with module restrictions on Forge &amp; NeoForge. On Fabric this wouldn't be necessary.
 * <p>
 * The design of this mixin plugin class is mostly copied from the <a
 * href="https://github.com/Chocohead/Fabric-ASM">Manningham Mills</a> project with some necessary adjustments for
 * Forge &amp; NeoForge.
 */
public abstract class AbstractMixinConfigPlugin implements IMixinConfigPlugin {
    private static final Map<String, Supplier<List<String>>> DYNAMIC_MIXINS = Maps.newLinkedHashMap();
    private static final String DYNAMIC_MIXIN_CLASSES_SUB_PACKAGE = "dynamic";
    private static boolean applied;

    protected static void register(String mixinClassName, Supplier<List<String>> targetClasses) {
        DYNAMIC_MIXINS.put(mixinClassName.replace('.', '/'), targetClasses);
    }

    @Override
    public final void onLoad(String mixinPackage) {

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
            List<String> targets = entry.getValue().get();
            if (!targets.isEmpty()) {
                this.loadAndExpandMixinTargets(this.getClass(),
                        mixinPackage,
                        entry.getKey(),
                        targets,
                        classGenerators::put
                );
            } else {
                // remove this for IMixinConfigPlugin::getMixins
                iterator.remove();
            }
        }

        if (!classGenerators.isEmpty()) {
            Consumer<URL> addUrlFwd = this.getClassLoaderURLConsumer(this.getClass().getClassLoader(),
                    getDynamicMixinClassesSubPackage(mixinPackage)
            );
            addUrlFwd.accept(createMapBackedUrl(classGenerators));
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

    protected static String getDynamicMixinClassesSubPackage(String mixinPackage) {
        return mixinPackage + "/" + DYNAMIC_MIXIN_CLASSES_SUB_PACKAGE;
    }

    protected abstract Consumer<URL> getClassLoaderURLConsumer(ClassLoader classLoader, String packageName);

    protected void loadAndExpandMixinTargets(Class<?> clazz, String mixinPackage, String mixinClassName, List<String> targets, BiConsumer<String, byte[]> classGenerators) {

        try (InputStream inputStream = clazz.getResourceAsStream(mixinClassName + ".class")) {

            Objects.requireNonNull(inputStream, "input stream is null");

            ClassReader classReader = new ClassReader(inputStream);
            ClassNode classNode = new ClassNode();
            classReader.accept(classNode, 0);

            this.expandMixinClazzTargets(classNode, targets);

            ClassWriter classWriter = new ClassWriter(0);
            classNode.accept(classWriter);
            byte[] byteArray = classWriter.toByteArray();

            StringJoiner joiner = new StringJoiner("/", "/", "");
            joiner.add(getDynamicMixinClassesSubPackage(mixinPackage));
            joiner.add(mixinClassName + ".class");
            classGenerators.accept(joiner.toString(), byteArray);

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    protected void expandMixinClazzTargets(ClassNode classNode, List<String> targets) {
        classNode.invisibleAnnotations.removeIf((AnnotationNode annotationNode) -> {
            return annotationNode.desc.equals("Lorg/spongepowered/asm/mixin/Mixin;");
        });
        AnnotationVisitor mixinAnnotation = classNode.visitAnnotation("Lorg/spongepowered/asm/mixin/Mixin;", false);
        AnnotationVisitor targetsAnnotation = mixinAnnotation.visitArray("targets");
        for (String target : targets) {
            targetsAnnotation.visit(null, target);
        }
        targetsAnnotation.visitEnd();
        // higher priority to apply after other mixins
        // was intended for other mods that implement super methods via override (not mixin's overwrite, e.g. Enchantment::getMaxLevel),
        // but injecting in them seems to work regardless of the priority
        mixinAnnotation.visit("priority", 1500);
        mixinAnnotation.visitEnd();
    }

    protected static URL createMapBackedUrl(Map<String, byte[]> classGenerators) {
        try {
            return new URL(EnchantmentControl.MOD_ID, null, -1, "/", new MapBackedURLStreamHandler(classGenerators));
        } catch (MalformedURLException exception) {
            throw new RuntimeException(exception);
        }
    }

    protected static class MapBackedURLStreamHandler extends URLStreamHandler {
        private final Map<String, byte[]> classGenerators;

        public MapBackedURLStreamHandler(Map<String, byte[]> classGenerators) {
            this.classGenerators = classGenerators;
        }

        @Override
        protected URLConnection openConnection(URL url) {
            if (!this.classGenerators.containsKey(url.getPath())) {
                return null;
            } else {
                return new URLConnection(url) {

                    @Override
                    public void connect() {
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public InputStream getInputStream() {
                        return new ByteArrayInputStream(MapBackedURLStreamHandler.this.classGenerators.get(this.url.getPath()));
                    }

                    @Override
                    public Permission getPermission() {
                        return null;
                    }
                };
            }
        }
    }
}
