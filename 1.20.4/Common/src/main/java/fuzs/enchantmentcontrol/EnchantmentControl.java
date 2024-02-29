package fuzs.enchantmentcontrol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Split this from mod main class to be usable in mixin config plugins without already loading any Minecraft classes.
 */
public class EnchantmentControl {
    public static final String MOD_ID = "enchantmentcontrol";
    public static final String MOD_NAME = "Enchantment Control";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);
}
