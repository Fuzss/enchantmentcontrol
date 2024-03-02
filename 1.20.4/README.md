# Enchantment Control

A Minecraft mod. Downloads can be found on [CurseForge](https://www.curseforge.com/members/fuzs_/projects)
and [Modrinth](https://modrinth.com/user/Fuzs).

![](https://raw.githubusercontent.com/Fuzss/modresources/main/pages/data/enchantmentcontrol/banner.png)

## Setting up configuration via a data pack

Most aspects of Enchantment Control are provided via tags. The following tags are made available.

### Global tags

These tags exist only once, and are used by all enchantments simultaneously.

- `enchantmentcontrol:tradeable`: Enchantments that will be offered by librarian villagers on enchanted books.

### Per enchantment tags

These tags all exist once for every enchantment.

### Further customization
For all remaining enchantment features, a new data format which is also part of data packs is introduced.

## Using Enchantment Control as a developer
Enchantment Control is specifically designed in a way that does not require other developers using any of its features to introduce a hard-dependency. All tags and other data pack files can easily be created and shipped without the mod being present. All runtime information can be obtained via vanilla methods.

You want to get the new max level Enchantment Control has set for an enchantment?</br>
Just call `Enchantment::getMaxLevel`, nothing different from vanilla here.

You want to know if an enchantment is a treasure enchantment with the data provided by Enchantment Control?</br>
Also same as before, just call `Enchantment::isTreasureOnly`.

You want to know if an enchantment is currently disabled?</br>
Vanilla's `FeatureElement` interface is implemented via Mixin on `Enchantment`, simply cast an instance to the interface (possibly wrapped in an `instanceof` check for optional compatibility) and call `FeatureElement::isEnabled`. The required `FeatureFlagSet` parameter is ignored and may therefore be `null`.

### Accessing custom enchantment values


## A quick dive into some technical details

Enchantment Control dynamically patches all `Enchantment` classes, injecting into the following methods:

- `Enchantment::getMinLevel`
- `Enchantment::getMaxLevel`
- `Enchantment::canEnchant`
- `Enchantment::isTreasureOnly`
- `Enchantment::isCurse`
- `Enchantment::isTradeable`
- `Enchantment::isDiscoverable`
- `Enchantment::canApplyAtEnchantingTable` (Forge & NeoForge only)

All injectors are optional, meaning they all set `require = 0`.

Targeting all enchantment classes is possible by dynamically expanding the `@Mixin#targets` field to include
all `Enchantment` classes. The underlying approach is heavily based upon [Fabric-ASM](https://github.com/Chocohead/Fabric-ASM). Targets are retrieved from the enchantment registry (`BuiltInRegistries#ENCHANTMENT`) and
stored in a cache file during a previous launch.

This approach is chosen in favor of patching the callsites (an approach the Apotheosis mod has taken), since there is no
way for preventing other mods from using the vanilla methods, and mods that want to support such a system must
introduce a hard-dependency to do so to use the new methods for retrieving up-to-date enchantment data values.
