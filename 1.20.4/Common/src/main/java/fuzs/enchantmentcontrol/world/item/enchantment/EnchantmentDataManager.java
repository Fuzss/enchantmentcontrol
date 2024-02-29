package fuzs.enchantmentcontrol.world.item.enchantment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import fuzs.enchantmentcontrol.handler.EnchantmentClassesCache;
import fuzs.enchantmentcontrol.init.ModRegistry;
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
        if (!EnchantmentClassesCache.isFailedLoad()) {
            EnchantmentHolder.clearAll();
            for (EnchantmentHolder holder : EnchantmentHolder.values()) {
                // checking the tag here only seems to work on Forge-like, Fabric is handled when tags update
                if (!holder.is(ModRegistry.UNTOUCHED_ENCHANTMENT_TAG)) {
                    JsonElement jsonElement = object.get(holder.getResourceLocation());
                    // every holder / enchantment must have a valid data entry since data is dynamically generated based on the enchantment registry
                    Objects.requireNonNull(jsonElement, "enchantment data for " + holder.getResourceLocation() + " is null");
                    // this does not just set the loaded data to the enchantment holders, but also sets the holders to the enchantments
                    // do not apply any changes to enchantments in this tag, every behavior should be vanilla this way
                    holder.setEnchantmentData(EnchantmentData.fromJson(holder, jsonElement));
                }
            }
        }
    }
}
