package net.roxeez.advancement.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializer;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.*;
import net.md_5.bungee.chat.*;
import net.roxeez.advancement.data.DimensionType;
import net.roxeez.advancement.data.EffectType;
import net.roxeez.advancement.data.PotionType;
import net.roxeez.advancement.display.FrameType;
import net.roxeez.advancement.utility.KeyValue;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.StructureType;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;

import java.util.List;
import java.util.Map;

public class ObjectSerializer {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .excludeFieldsWithoutExposeAnnotation()
            .registerTypeHierarchyAdapter(Material.class, new MaterialAdapter())
            .registerTypeHierarchyAdapter(EntityType.class, new EntityTypeAdapter())
            .registerTypeHierarchyAdapter(PotionType.class, new PotionTypeAdapter())
            .registerTypeHierarchyAdapter(DimensionType.class, new DimensionTypeAdapter())
            .registerTypeHierarchyAdapter(NamespacedKey.class, new NamespacedKeyAdapter())
            .registerTypeHierarchyAdapter(List.class, new ListAdapter())
            .registerTypeHierarchyAdapter(FrameType.class, new FrameTypeAdapter())
            .registerTypeHierarchyAdapter(Map.class, new MapAdapter())
            .registerTypeHierarchyAdapter(EffectType.class, new EffectTypeAdapter())
            .registerTypeHierarchyAdapter(KeyValue.class, new KeyValueAdapter())
            .registerTypeHierarchyAdapter(Biome.class, new BiomeAdapter())
            .registerTypeHierarchyAdapter(StructureType.class, new StructureTypeAdapter())
            .registerTypeHierarchyAdapter(GameMode.class, new GameModeAdapter())
            // Serialize Bungee chat components through the stable, public ComponentSerializer.toString(...)
            // rather than Bungee's internal per-type serializers: their public no-arg constructors were
            // removed in modern bungeecord-chat (1.20.6+/26.x), so `new TextComponentSerializer()` and
            // friends threw NoSuchMethodError at runtime.
            .registerTypeHierarchyAdapter(BaseComponent.class, (JsonSerializer<BaseComponent>) (src, type, context) -> parseComponentJson(src))
            .create();

    public String serialize(Object object) {
        return GSON.toJson(object);
    }

    @SuppressWarnings("deprecation") // compile target (spigot 1.16.5) ships gson 2.8.0, before JsonParser.parseString
    private static JsonElement parseComponentJson(BaseComponent component) {
        return new JsonParser().parse(ComponentSerializer.toString(component));
    }

}
