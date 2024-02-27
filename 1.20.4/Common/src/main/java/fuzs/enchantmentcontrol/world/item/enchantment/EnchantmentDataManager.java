package fuzs.enchantmentcontrol.world.item.enchantment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;
import java.util.Objects;

public final class EnchantmentDataManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().create();

    public EnchantmentDataManager() {
        super(GSON, "enchantments");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        EnchantmentHolder.restoreAllOriginalValues();
        for (EnchantmentHolder holder : EnchantmentHolder.values()) {
            Objects.requireNonNull(holder, "holder is null");
            JsonElement jsonElement = object.get(holder.getResourceLocation());
            Objects.requireNonNull(jsonElement, "enchantment data for " + holder.getResourceLocation() + " is null");
            holder.initNewValues(DataBasedEnchantmentComponent.fromJson(holder.getEnchantment(), jsonElement));
        }
    }
}
