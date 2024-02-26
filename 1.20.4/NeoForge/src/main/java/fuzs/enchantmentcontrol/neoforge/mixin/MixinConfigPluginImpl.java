package fuzs.enchantmentcontrol.neoforge.mixin;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import cpw.mods.cl.ModuleClassLoader;
import fuzs.puzzleslib.api.core.v1.ModLoaderEnvironment;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import sun.misc.Unsafe;

import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.nio.file.Path;
import java.security.Permission;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MixinConfigPluginImpl implements IMixinConfigPlugin {
    private static final Map<String, String> DYNAMIC_MIXINS = Maps.newLinkedHashMap();
    private static boolean applied;

    private final Map<String, byte[]> additionalUrls = Maps.newHashMap();
    private final MapBackedURLStreamHandler urlStreamHandler = new MapBackedURLStreamHandler();

    static {
//        DYNAMIC_MIXINS.put("DynamicEnchantmentMixin", ".enchantmentcontrolcache");
    }

    @Override
    public void onLoad(String mixinPackage) {

        if (!applied) return;
        applied = true;

        mixinPackage = mixinPackage.replace('.', '/');

        Iterator<Map.Entry<String, String>> iterator = DYNAMIC_MIXINS.entrySet().iterator();
        while (iterator.hasNext()) {

            Map.Entry<String, String> pair = iterator.next();

            String mixinClassName = pair.getKey();
            if (mixinClassName.startsWith(mixinPackage)) {
                mixinClassName = mixinClassName.substring(mixinPackage.length());
            }

            String[] targets;
            Path path = ModLoaderEnvironment.INSTANCE.getConfigDirectory().resolve(pair.getValue());
            try (FileReader fileReader = new FileReader(path.toFile())) {
                targets = new Gson().fromJson(fileReader, String[].class);
            } catch (Throwable throwable) {
                targets = null;
            }

            if (targets == null || targets.length == 0) {
                iterator.remove();
                continue;
            }

            InputStream inputStream = this.getClass().getResourceAsStream(mixinClassName + ".class");
            Objects.requireNonNull(inputStream, "input stream is null");

            try {

                String _mixinClassName = mixinClassName;
                String[] _targets = targets;
                byte[] byteArray = readAndTransformClass(inputStream, classNode -> {
                    modifyClassNode(_mixinClassName, _targets, classNode);
                });

                addUrl(this.getClass().getClassLoader(), '/' + mixinPackage + "/$/" + mixinClassName + ".class", byteArray);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static byte[] readAndTransformClass(InputStream inputStream, Consumer<ClassNode> transformer) throws IOException {
        ClassReader classReader = new ClassReader(inputStream);
        inputStream.close();
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);
        transformer.accept(classNode);
        ClassWriter classWriter = new ClassWriter(0);
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }

    private static void modifyClassNode(String mixinClassName, String[] targets, ClassNode classNode) {
        classNode.name = mixinClassName;
        classNode.invisibleAnnotations.removeIf(annotationNode -> annotationNode.desc.equals("Lorg/spongepowered/asm/mixin/Mixin;"));
        AnnotationVisitor mixinAnnotation = classNode.visitAnnotation("Lorg/spongepowered/asm/mixin/Mixin;", false);
        AnnotationVisitor targetAnnotation = mixinAnnotation.visitArray("targets");
        for (String target : targets) {
            targetAnnotation.visit(null, target);
        }
        targetAnnotation.visitEnd();
        mixinAnnotation.visitEnd();
    }

    private static String moveToSubpackage(String className, String subPackage) {
        char separator = className.contains(".") ? '.' : '/';
        return className.substring(0, className.lastIndexOf(separator) + 1) + subPackage + separator;
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

    }

    @Override
    public List<String> getMixins() {
        return DYNAMIC_MIXINS.keySet().stream().map(s -> ("$/" + s).replace('/', '.')).collect(Collectors.toList());
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    private static void addUrl(ClassLoader classLoader, String urlPath, byte[] byteArray) {
        if (!(classLoader instanceof ModuleClassLoader)) throw new IllegalStateException("Class loader not supported " + classLoader);
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            Unsafe unsafe = (Unsafe) theUnsafe.get(null);
            Field parentLoadersField = ModuleClassLoader.class.getDeclaredField("parentLoaders");
            long parentLoadersOffset = unsafe.objectFieldOffset(parentLoadersField);
            Map<String, ClassLoader> parentLoaders = (Map<String, ClassLoader>) unsafe.getObject(classLoader, parentLoadersOffset);
            String urlDomain = urlPath.replace('/', '.').substring(1, urlPath.lastIndexOf('/'));
            ClassLoader parentLoader = parentLoaders.remove(urlDomain);
            if (parentLoader != null) throw new RuntimeException("Domain for URL '" + urlPath + "' already registered");
//            URL url = new URL("", null, -1, "/", new MapBackedURLStreamHandler(urlPath, byteArray));
//            parentLoaders.put(urlDomain, new URLClassLoader(new URL[]{url}, EmptyClassLoader.INSTANCE));
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static String getUrlPackage(String urlPath) {
        int startIndex = urlPath.startsWith("/") ? 1 : 0;
        int endIndex = urlPath.lastIndexOf('/');
        return urlPath.replace('/', '.').substring(startIndex, endIndex);
    }

    private static class EmptyClassLoader extends ClassLoader {
        private static final ClassLoader INSTANCE = new EmptyClassLoader();
        private static final Enumeration<URL> EMPTY_ENUMERATION = new Enumeration<URL>() {
            @Override
            public boolean hasMoreElements() {
                return false;
            }

            @Override
            public URL nextElement() {
                return null;
            }
        };

        static {
            registerAsParallelCapable();
        }

        @Override
        protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
            throw new ClassNotFoundException(name);
        }

        @Override
        public URL getResource(String name) {
            return null;
        }

        @Override
        public Enumeration<URL> getResources(String var1) throws IOException {
            return EMPTY_ENUMERATION;
        }
    }

    private class MapBackedURLStreamHandler extends URLStreamHandler {

        @Override
        protected URLConnection openConnection(URL url) throws IOException {
            if (!MixinConfigPluginImpl.this.additionalUrls.containsKey(url.getPath())) return null;
            return new URLConnection(url) {

                @Override
                public void connect() throws IOException {
                    throw new UnsupportedOperationException();
                }

                @Override
                public InputStream getInputStream() throws IOException {
                    return new ByteArrayInputStream(MixinConfigPluginImpl.this.additionalUrls.get(this.getURL().getPath()));
                }

                @Override
                public Permission getPermission() throws IOException {
                    return null;
                }
            };
        }
    }
}
