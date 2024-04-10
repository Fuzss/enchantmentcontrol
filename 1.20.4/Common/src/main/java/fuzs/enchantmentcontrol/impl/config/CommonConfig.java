package fuzs.enchantmentcontrol.impl.config;

import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;

public class CommonConfig implements ConfigCore {
    @Config(
            description = {
                    "Automatically lift restrictions for enchantments such as frost walker to work on all armor pieces if allowed to be put on there (which must be enabled separately via a data pack).",
                    "Will have no effect in-game with the default vanilla configuration."
            }
    )
    public boolean abstractEquipmentSlots = true;
    @Config(description = "Prevent disabled enchantments from showing up on enchanted items in loot (e.g. mob drops or generated chests).")
    public boolean removeUnobtainableFromLoot = true;
    @Config(description = "Prevent enchanted books with disabled enchantments from being added to the creative mode inventory.")
    public boolean removeUnobtainableFromCreative = true;
}
