package fuzs.enchantmentcontrol.impl.world.item.enchantment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import fuzs.enchantmentcontrol.api.v1.data.EnchantmentDataHelper;
import fuzs.enchantmentcontrol.api.v1.tags.EnchantmentTags;
import fuzs.enchantmentcontrol.impl.EnchantmentControlMod;
import fuzs.enchantmentcontrol.impl.handler.EnchantmentClassesCache;
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
            EnchantmentDataHelper.clearAll();
            for (EnchantmentHolder holder : EnchantmentHolder.values()) {
                // checking the tag here only seems to work on Forge-like, Fabric is handled when tags update
                if (!holder.is(EnchantmentTags.UNTOUCHED)) {
                    JsonElement jsonElement = object.get(holder.getResourceLocation());
                    // every holder / enchantment must have a valid data entry since data is dynamically generated based on the enchantment registry
                    Objects.requireNonNull(jsonElement, "enchantment data for " + holder.getResourceLocation() + " is null");
                    // this does not just set the loaded data to the enchantment holders, but also sets the holders to the enchantments
                    // do not apply any changes to enchantments in this tag, every behavior should be vanilla this way
                    try {
                        holder.setEnchantmentData(EnchantmentDataImpl.fromJson(holder, jsonElement));
                    } catch (Exception exception) {
                        EnchantmentControlMod.LOGGER.error("Failed to parse enchantment data for {}", holder.getResourceLocation(), exception);
                    }
                }
            }
        }
    }
}
