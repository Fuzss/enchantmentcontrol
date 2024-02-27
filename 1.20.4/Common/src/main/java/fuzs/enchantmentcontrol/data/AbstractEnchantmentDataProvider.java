package fuzs.enchantmentcontrol.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import fuzs.enchantmentcontrol.world.item.enchantment.DataBasedEnchantmentComponent;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractEnchantmentDataProvider implements DataProvider {
    private final Map<ResourceLocation, DataBasedEnchantmentComponent> providers = Maps.newHashMap();
    private final PackOutput.PathProvider pathProvider;

    public AbstractEnchantmentDataProvider(DataProviderContext context) {
        this(context.getPackOutput());
    }

    public AbstractEnchantmentDataProvider(PackOutput packOutput) {
        this.pathProvider = packOutput.createPathProvider(PackOutput.Target.DATA_PACK, "enchantments");
    }

    @Override
    public final CompletableFuture<?> run(CachedOutput output) {
        this.addEnchantmentData();
        List<CompletableFuture<?>> futures = Lists.newArrayList();
        for (Map.Entry<ResourceLocation, DataBasedEnchantmentComponent> entry : this.providers.entrySet()) {
            JsonElement jsonElement = entry.getValue().toJson();
            Path path = this.pathProvider.json(entry.getKey());
            futures.add(DataProvider.saveStable(output, jsonElement, path));
        }
        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    public abstract void addEnchantmentData();

    public void add(Enchantment enchantment, DataBasedEnchantmentComponent dataBasedEnchantmentComponent) {
        this.add(BuiltInRegistries.ENCHANTMENT.getKey(enchantment), dataBasedEnchantmentComponent);
    }

    public void add(ResourceLocation resourceLocation, DataBasedEnchantmentComponent dataBasedEnchantmentComponent) {
        this.providers.put(resourceLocation, dataBasedEnchantmentComponent);
    }

    @Override
    public String getName() {
        return "Enchantment Data Provider";
    }
}
