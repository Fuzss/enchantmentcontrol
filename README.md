# Enchantment Control

A Minecraft mod. Downloads can be found on [CurseForge](https://www.curseforge.com/members/fuzs_/projects)
and [Modrinth](https://modrinth.com/user/Fuzs).

![](https://raw.githubusercontent.com/Fuzss/modresources/main/pages/data/enchantmentcontrol/banner.png)

## Some use cases
Enchantment Control can do so much, here are a few examples, mainly geared towards mod pack developers:
- You have a number of mods that all add the same enchantment (like soulbound, for keeping an item in the player inventory on death). Now you can disable all these enchantments but one!
- You have some mods that absolutely clutter the enchantment pool, making it hard for players to obtain basic vanilla enchantments at an enchanting table. Now you can remove all those unwanted enchantments from the enchanting table pool!
- You want to bump up the max level on some basic enchantments beyond the vanilla limit such as sharpness and efficiency. Now you can do that by simply changing a value in the enchantment data.

## Important notice

Although Enchantment Control allows you to change almost all enchantment properties and behaviors, please keep in mind
that enchantments in the game code are mostly hardcoded (meaning there are a lot of special cases checking for some
specific enchantment & item combinations to be present).

This means just because you can now change a certain behavior does not necessarily mean it will work as you'd image
in-game. This is especially true when it comes to modded content.

So just because you are now able to e.g.

- Add Multishot to bows does not make it actually shoot more arrows, since the behavior is hardcoded to crossbows.
- Bump the max level for Frost Walker much higher than vanilla does not make a difference past level 14, since the
  behavior is capped at that.

This is especially true for enchantments with a max level of 1, which usually don't behave any differently with higher
levels.

## Implementation goals
While other projects offering some similar functionality in regard to making enchantments more configurable in-game exist, here is what Enchantment Control aims to do differently.

- A soft dependency: Any mod or data pack can use the features Enchantment Control provides without enforcing it to be present, in both development and production environments. 
- Fully data-driven via data packs: Most features are controlled via Minecraft's tags, the rest uses a simple custom `.json` format which is also part of data packs.
- Automatic compatibility with any mod: Instead of introducing custom hooks and patching callsites which other mods will miss out on, Enchantment Control modifies enchantment classes directly and is automatically compatible.

## Feature outline

### What Enchantment Control can do

Here is a quick outline of what you can change about enchantments:

- Disable enchantments from showing up anywhere in-game (at enchanting tables, on loot, in creative, in the `/enchant` command)
- Override `Enchantment::getMaxLevel` for controlling the maximum enchantment level obtainable at an enchanting table /
  anvil / via the `/enchant` command, e.g. change the max level for Sharpness from 5 to 10
- Adjust what items can receive an enchantment at an enchanting table
- Adjust what items can receive an enchantment at an anvil
- Override `Enchantment::getMinLevel`
- Override `Enchantment::getMinLevel` (mostly unused in vanilla, not sure what effects this actually has)

### What Enchantment Control currently cannot do

Here is a quick outline of what you cannot change about enchantments:

- Override `Enchantment::getMinCost` which controls for which player experience levels an enchantment shows up at the enchanting table
- Override `Enchantment::getMaxCost` which controls for which player experience levels an enchantment shows up at the enchanting table

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

Enchantment Control is specifically designed in a way that does not require other developers using any of its features
to introduce a hard-dependency. All tags and other data pack files can easily be created and shipped without the mod
being present. All runtime information can be obtained via vanilla methods.

You want to get the new max level Enchantment Control has set for an enchantment?</br>
Just call `Enchantment::getMaxLevel`, nothing different from vanilla here.

You want to know if an enchantment is a treasure enchantment with the data provided by Enchantment Control?</br>
Also same as before, just call `Enchantment::isTreasureOnly`.

You want to know if an enchantment is currently disabled?</br>
Vanilla's `FeatureElement` interface is implemented via Mixin on `Enchantment`, simply cast an instance to the
interface (possibly wrapped in an `instanceof` check for optional compatibility) and call `FeatureElement::isEnabled`.
The required `FeatureFlagSet` parameter is ignored and may therefore be `null`.

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
all `Enchantment` classes. The underlying approach is heavily based
upon [Fabric-ASM](https://github.com/Chocohead/Fabric-ASM). Targets are retrieved from the enchantment
registry (`BuiltInRegistries#ENCHANTMENT`) and
stored in a cache file during a previous launch.

This approach is chosen in favor of patching the callsites (an approach the Apotheosis mod has taken), since there is no
way for preventing other mods from using the vanilla methods, and mods that want to support such a system must
introduce a hard-dependency to do so to use the new methods for retrieving up-to-date enchantment data values.
