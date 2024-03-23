package me.fengming.concrete.items.songs;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class AbstractSongItem extends Item {
    public AbstractSongItem() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return true;
    }

    @Override
    public Component getName(ItemStack pStack) {
        return Component.translatable("item.concrete.abstract_song_" + pStack.getOrCreateTag().getString("Song"));
    }
}
