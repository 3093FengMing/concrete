package me.fengming.concrete.item;

import me.fengming.concrete.Concrete;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ConcreteTags {
    public static TagKey<Item> CONCRETE_SONGS = ItemTags.create(new ResourceLocation(Concrete.MODID, "concrete_songs"));
}
