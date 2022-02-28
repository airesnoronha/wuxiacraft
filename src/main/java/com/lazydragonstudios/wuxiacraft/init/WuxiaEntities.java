package com.lazydragonstudios.wuxiacraft.init;

import com.lazydragonstudios.wuxiacraft.WuxiaCraft;
import com.lazydragonstudios.wuxiacraft.entity.ThrowSkill;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused")
public class WuxiaEntities {

	public static final DeferredRegister<EntityType<?>> ENTITY_TYPE_REGISTER = DeferredRegister.create(ForgeRegistries.ENTITIES, WuxiaCraft.MOD_ID);

	public static final RegistryObject<EntityType<ThrowSkill>> THROW_SKILL_TYPE = ENTITY_TYPE_REGISTER.register("throw_skill_type", () -> EntityType.Builder.<ThrowSkill>createNothing(MobCategory.MISC).build("throw_skill_type"));

}
