# Mindful Darkness

A Minecraft mod. Downloads can be found on [CurseForge](https://www.curseforge.com/members/fuzs_/projects) and [Modrinth](https://modrinth.com/user/Fuzs).

![](https://raw.githubusercontent.com/Fuzss/modresources/main/pages/data/mindfuldarkness/banner.png)

## Configuration
Mindful Darkness does not automatically darken every gui texture in the game, instead you have to manually specify in the config which textures are affected. Resources can be specified using `'*'` as wildcard char, can affect all or just a single namespace, and you can use `'!'` to exclude resources.

By default, configuration looks like this: `"textures/gui/*, !minecraft:textures/gui/icons.png"`. This will include all textures in the textures/gui directory from all namespaces (since this is where mods usually put their gui textures). Specifically for the minecraft namespace `icons.png` is excluded though (indicated by adding `'!'` at the beginning). NOTE: The order of defining resources matters for exclusions! Excluded resources need to come after any entry that would otherwise include them.