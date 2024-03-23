package me.fengming.concrete.items;

import me.fengming.concrete.Concrete;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;

public class ConcreteTiers {
    public static ForgeTier CONCRETE_SONG = new ForgeTier(6, -1, 8.0F, 3.0F, 20,
            BlockTags.create(new ResourceLocation(Concrete.MODID, "songs")), () -> Ingredient.of(ItemRegistry.ABSTRACT_SONG.get())
    );

}
