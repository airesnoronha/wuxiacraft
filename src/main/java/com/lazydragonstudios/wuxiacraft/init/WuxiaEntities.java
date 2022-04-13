package com.lazydragonstudios.wuxiacraft.init;

import com.lazydragonstudios.wuxiacraft.WuxiaCraft;
import com.lazydragonstudios.wuxiacraft.entity.ThrowSkill;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused")
public class WuxiaEntities {

	public static final DeferredRegister<EntityType<?>> ENTITY_TYPE_REGISTER = DeferredRegister.create(ForgeRegistries.ENTITIES, WuxiaCraft.MOD_ID);

	//placeholders to custom renderers

	public static final RegistryObject<EntityType<AbstractClientPlayer>> ANIMATED_PLAYER_ENTITY = ENTITY_TYPE_REGISTER.register("animated_player_entity",
			() -> EntityType.Builder.<AbstractClientPlayer>createNothing(MobCategory.MISC).build("animated_player_entity")
	);

	public static final RegistryObject<EntityType<AbstractClientPlayer>> GHOST_ENTITY = ENTITY_TYPE_REGISTER.register("ghost_entity",
			() -> EntityType.Builder.<AbstractClientPlayer>createNothing(MobCategory.MISC).build("ghost_entity")
	);

	public static final RegistryObject<EntityType<AbstractClientPlayer>> AURA_ENTITY = ENTITY_TYPE_REGISTER.register("aura_entity",
			() -> EntityType.Builder.<AbstractClientPlayer>createNothing(MobCategory.MISC).build("aura_entity")
	);

	//actual entities

	public static final RegistryObject<EntityType<ThrowSkill>> THROW_SKILL_TYPE = ENTITY_TYPE_REGISTER.register("throw_skill",
			() -> EntityType.Builder.<ThrowSkill>of(ThrowSkill::new, MobCategory.MISC).sized(0.6f, 0.6f).build("throw_skill")
	);

}
