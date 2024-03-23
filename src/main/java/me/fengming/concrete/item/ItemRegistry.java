package me.fengming.concrete.items;

import me.fengming.concrete.Concrete;
import me.fengming.concrete.items.songs.AbstractSongItem;
import me.fengming.concrete.items.songs.ConcreteGuanJu;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Concrete.MODID);

    public static final RegistryObject<Item> BOOK_OF_SONGS = ITEMS.register("book_of_songs", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> ABSTRACT_SONG = ITEMS.register("abstract_song", AbstractSongItem::new);
    public static final RegistryObject<Item> CONCRETE_GUAN_JU = ITEMS.register("concrete_guan_ju", ConcreteGuanJu::new);


    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
