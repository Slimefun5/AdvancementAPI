AdvancementAPI is a library for creating custom advancements with Bukkit's own API. It is not a plugin: an addon
such as SlimefunAdvancements shades and relocates it.

## :floppy_disk: Installation

| AdvancementAPI | Java | Minecraft |
|---|---|---|
| 1.0.0 | 8+ | 1.12.x to 26.x |

Add it with github-gradle and relocate it into your own package:

```kotlin
dependencies {
    githubImplementation("Slimefun5:AdvancementAPI:v1.0.0")
}

tasks.shadowJar {
    relocate("net.roxeez.advancement", "your.addon.libs.advancementapi")
}
```

## :scroll: Example

A root advancement unlocked by collecting three cobblestone:

```java
public class CobblestoneAdvancement implements AdvancementCreator {

    public static final String ID = "some_cobblestone";

    @Override
    public @NotNull Advancement create(@NotNull Context context) {
        Advancement advancement = new Advancement(context.getPlugin(), ID);

        advancement.setDisplay(x -> {
            x.setTitle("Some Cobblestone");
            x.setDescription("Get 3 cobblestone");
            x.setBackground(BackgroundType.STONE);
            x.setIcon(Material.COBBLESTONE);
        });

        advancement.addCriteria("cobblestone", TriggerType.INVENTORY_CHANGED, trigger -> {
            trigger.hasItemMatching(item -> {
                item.setType(Material.COBBLESTONE);
                item.setCount(3);
            });
        });

        return advancement;
    }
}
```

Registering it:

```java
public class MyPlugin extends JavaPlugin {

    private final AdvancementManager manager = new AdvancementManager(this);

    @Override
    public void onEnable() {
        manager.register(new CobblestoneAdvancement());
        manager.createAll();
    }
}
```

## :beetle: Bug Reports

Please report bugs via the [GitHub Issues](https://github.com/Slimefun5/AdvancementAPI/issues) page.

## :open_book: Contributing

Contributions are welcome! Please read the [Slimefun5 Contributing Guidelines](https://github.com/Slimefun5/Slimefun5/blob/master/CONTRIBUTING.md) before submitting a pull request.
