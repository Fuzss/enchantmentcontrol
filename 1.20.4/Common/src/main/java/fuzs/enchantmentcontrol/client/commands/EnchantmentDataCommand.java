package fuzs.enchantmentcontrol.client.commands;

import com.google.common.base.Suppliers;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import fuzs.enchantmentcontrol.EnchantmentControl;
import fuzs.enchantmentcontrol.data.DynamicEnchantmentDataProvider;
import fuzs.enchantmentcontrol.data.DynamicEnchantmentTagProvider;
import fuzs.enchantmentcontrol.data.DynamicItemTagProvider;
import fuzs.enchantmentcontrol.handler.EnchantmentClassesCache;
import fuzs.enchantmentcontrol.world.item.enchantment.EnchantmentData;
import fuzs.enchantmentcontrol.world.item.enchantment.EnchantmentHolder;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EnchantmentDataCommand {
    public static final DataProviderContext.Factory[] DYNAMIC_ENCHANTMENT_DATA_FACTORIES = {
            DynamicEnchantmentDataProvider::new, DynamicItemTagProvider::new, DynamicEnchantmentTagProvider::new
    };
    private static final String EXPORT_DIRECTORY = "export";
    private static final Path ENCHANTMENT_DATA_EXPORT_PATH = EnchantmentClassesCache.HIDDEN_MOD_ROOT_PATH.resolve(
            EXPORT_DIRECTORY);
    private static final DynamicCommandExceptionType ERROR_FAILED_EXPORT = new DynamicCommandExceptionType(object -> Component.literal(
            "Running enchantment data providers failed: " + object));

    public static int triggerExport(Consumer<Component> feedbackConsumer, Consumer<Component> errorConsumer) throws CommandSyntaxException {
        if (EnchantmentClassesCache.isFailedLoad()) {
            throw ERROR_FAILED_EXPORT.create("Invalid configuration present");
        } else {
            // this isn't such a great approach since we temporarily need to reset enchantment data for the export,
            // especially since this is performed on another thread there are potential concurrency issues
            // therefore the command is limited to clients, and will in a worst case scenario only cause desync issues
            // with the current server which can be fixed simply by re-logging
            // TODO probably should export current configuration instead of vanilla default, would avoid all this hackery
            Util.ioPool().execute(() -> {
                Map<EnchantmentHolder, EnchantmentData> enchantmentData = EnchantmentHolder.values()
                        .stream()
                        .collect(Collectors.toMap(Function.identity(), EnchantmentHolder::getEnchantmentData));
                EnchantmentHolder.clearAll();
                deleteExistingExportDirectory();
                DataProviderContext context = fromModId(EnchantmentControl.MOD_ID);
                for (DataProviderContext.Factory factory : DYNAMIC_ENCHANTMENT_DATA_FACTORIES) {
                    DataProvider dataProvider = factory.apply(context);
                    try {
                        dataProvider.run(CachedOutput.NO_CACHE).get();
                    } catch (Exception exception) {
                        errorConsumer.accept(Component.literal(ERROR_FAILED_EXPORT.create(exception.getMessage())
                                .getMessage()));
                        enchantmentData.forEach(EnchantmentHolder::setEnchantmentData);
                        return;
                    }
                }

                enchantmentData.forEach(EnchantmentHolder::setEnchantmentData);
                feedbackConsumer.accept(Component.literal("Exported enchantment data at ")
                        .append(Component.literal(
                                        EnchantmentClassesCache.HIDDEN_MOD_ROOT_DIRECTORY_NAME + File.separator + EXPORT_DIRECTORY)
                                .withStyle(ChatFormatting.UNDERLINE)
                                .withStyle(arg -> arg.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE,
                                        ENCHANTMENT_DATA_EXPORT_PATH.toFile().getAbsolutePath()
                                )))));
            });

            return 1;
        }
    }

    private static void deleteExistingExportDirectory() {
        if (Files.exists(ENCHANTMENT_DATA_EXPORT_PATH)) {
            try {
                Files.walkFileTree(ENCHANTMENT_DATA_EXPORT_PATH, new SimpleFileVisitor<>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
                        EnchantmentControl.LOGGER.debug("Deleting {}", file);
                        Files.delete(file);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path directory, @Nullable IOException exception) throws IOException {
                        if (exception != null) {
                            throw exception;
                        } else {
                            Files.delete(directory);
                            return FileVisitResult.CONTINUE;
                        }
                    }
                });
            } catch (IOException exception) {
                EnchantmentControl.LOGGER.warn("Failed to delete {}", ENCHANTMENT_DATA_EXPORT_PATH, exception);
            }
        }
    }

    public static DataProviderContext fromModId(String modId) {
        // TODO replace with Puzzles Lib method
        return new DataProviderContext(modId,
                new PackOutput(ENCHANTMENT_DATA_EXPORT_PATH),
                Suppliers.memoize(() -> CompletableFuture.supplyAsync(VanillaRegistries::createLookup,
                        Util.backgroundExecutor()
                ))
        );
    }
}
