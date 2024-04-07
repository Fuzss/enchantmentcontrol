package fuzs.enchantmentcontrol.impl.world.item.enchantment;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fuzs.enchantmentcontrol.api.v1.data.EnchantmentCost;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import net.minecraft.Util;
import net.minecraft.util.ExtraCodecs;
import org.jetbrains.annotations.Nullable;

public record EnchantmentCostImpl(int baseCost, int perLevelCost) implements EnchantmentCost {
    private static final Codec<EnchantmentCost> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(ExtraCodecs.NON_NEGATIVE_INT.fieldOf("base_cost")
                        .forGetter(enchantmentCost -> ((EnchantmentCostImpl) enchantmentCost).baseCost()),
                ExtraCodecs.strictOptionalField(ExtraCodecs.NON_NEGATIVE_INT, "per_level_cost", 0)
                        .forGetter(enchantmentCost -> ((EnchantmentCostImpl) enchantmentCost).perLevelCost())
        ).apply(instance, EnchantmentCostImpl::new);
    });

    public EnchantmentCostImpl {
        Preconditions.checkArgument(baseCost >= 0, "base cost is negative");
        Preconditions.checkArgument(perLevelCost >= 0, "per level cost is negative");
    }

    @Override
    public int calculate(int enchantmentLevel) {
        return this.baseCost + this.perLevelCost * (enchantmentLevel - 1);
    }

    public JsonElement toJson() {
        return Util.getOrThrow(CODEC.encodeStart(JsonOps.INSTANCE, this),
                string -> new EncoderException("Failed to encode: " + string + " " + this)
        );
    }

    @Nullable
    public static EnchantmentCost fromJson(JsonObject jsonObject, String memberName) {
        if (jsonObject.has(memberName)) {
            JsonElement jsonElement = jsonObject.get(memberName);
            return Util.getOrThrow(CODEC.parse(JsonOps.INSTANCE, jsonElement),
                    string -> new DecoderException("Failed to decode: " + string + " " + jsonElement)
            );
        } else {
            return null;
        }
    }
}
