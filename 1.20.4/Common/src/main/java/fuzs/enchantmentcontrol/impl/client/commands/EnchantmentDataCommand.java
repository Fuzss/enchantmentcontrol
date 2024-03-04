package fuzs.enchantmentcontrol.impl.client.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import fuzs.enchantmentcontrol.impl.EnchantmentControl;
import fuzs.enchantmentcontrol.impl.data.DynamicEnchantmentDataProvider;
import fuzs.enchantmentcontrol.impl.data.DynamicEnchantmentTagProvider;
import fuzs.enchantmentcontrol.impl.data.DynamicItemTagProvider;
import fuzs.enchantmentcontrol.impl.handler.EnchantmentClassesCache;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.Consumer;

public class EnchantmentDataCommand {
    private static final String EXPORT_DIRECTORY = "export";
    private static final Path ENCHANTMENT_DATA_EXPORT_PATH = EnchantmentClassesCache.HIDDEN_MOD_ROOT_PATH.resolve(
            EXPORT_DIRECTORY);
    private static final DynamicCommandExceptionType ERROR_FAILED_EXPORT = new DynamicCommandExceptionType(object -> Component.literal(
            "Running enchantment data providers failed: " + object));

    public static DataProviderContext.Factory[] getEnchantmentDataFactories(boolean skipHolderValidation) {
        return new DataProviderContext.Factory[]{
                DynamicEnchantmentDataProvider.create(skipHolderValidation),
                DynamicItemTagProvider.create(skipHolderValidation),
                DynamicEnchantmentTagProvider.create(skipHolderValidation)
        };
    }

    ;

    public static int triggerExport(Consumer<Component> feedbackConsumer, Consumer<Component> errorConsumer) throws CommandSyntaxException {
        if (EnchantmentClassesCache.isFailedLoad()) {
            throw ERROR_FAILED_EXPORT.create("Invalid configuration present, please restart your game");
        } else {
            // currently only a client command since initially implemented this way, too lazy to change that right now
            Util.ioPool().execute(() -> {
                deleteExistingExportDirectory();
                DataProviderContext context = DataProviderContext.fromModId(EnchantmentControl.MOD_ID,
                        ENCHANTMENT_DATA_EXPORT_PATH
                );
                DataProviderContext.Factory[] factories = getEnchantmentDataFactories(true);
                for (DataProviderContext.Factory factory : factories) {
                    DataProvider dataProvider = factory.apply(context);
                    try {
                        dataProvider.run(CachedOutput.NO_CACHE).get();
                    } catch (Exception exception) {
                        errorConsumer.accept(Component.literal(ERROR_FAILED_EXPORT.create(exception.getMessage())
                                .getMessage()));
                        return;
                    }
                }

                feedbackConsumer.accept(Component.literal("Exported enchantment data at ")
                        .append(Component.literal(
                                        File.separator + EnchantmentClassesCache.HIDDEN_MOD_ROOT_DIRECTORY_NAME +
                                                File.separator + EXPORT_DIRECTORY)
                                .withStyle(ChatFormatting.UNDERLINE)
                                .withStyle(arg -> arg.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE,
                                        ENCHANTMENT_DATA_EXPORT_PATH.toFile().getAbsolutePath()
                                )))));
            });

            return 1;
        }
    }

    /**
     * Copied from {@link LevelStorageSource.LevelStorageAccess#deleteLevel()}.
     */
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
}
