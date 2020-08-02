package com.airesnor.wuxiacraft.items;

import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class WuxiaHerbs {

    public static final List<Item> HERBS = new ArrayList<>();

    // Alchemy Herbs
    public static final Item HERB_1_TEST = new ItemHerb("11111", 1).setTemperatures(400, 600).setHerbElements(1, 1, 1, 1, 1);
    public static final Item HERB_2_TEST = new ItemHerb("54321", 1).setTemperatures(600, 800).setHerbElements(5, 4, 3, 2, 1);
    public static final Item HERB_3_TEST = new ItemHerb("-4-3-2-10", 1).setTemperatures(600, 800).setHerbElements(-4, -3, -2, -1, 0);
    public static final Item HERB_4_TEST = new ItemHerb("01234", 1).setTemperatures(600, 800).setHerbElements(0, 1, 2, 3, 4);
}
