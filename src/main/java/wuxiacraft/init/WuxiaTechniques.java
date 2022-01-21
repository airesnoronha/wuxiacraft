package wuxiacraft.init;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import wuxiacraft.WuxiaCraft;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.Technique;

public class WuxiaTechniques {

	public static DeferredRegister<Technique> TECHNIQUES_REGISTER = DeferredRegister.create(Technique.class, WuxiaCraft.MOD_ID);

	//***********************
	// body ones
	//***********************

	//tier 0 - like no tier at all
	public static RegistryObject<Technique> BASIC_BODY_MANUAL = TECHNIQUES_REGISTER.register("basic_body_manual",
					() -> new Technique("basic_body_manual",
									Cultivation.System.BODY,
									0, // health
									0, // strength
									0, // agility
									0, // energy
									0, // energy regen
									1  //cultivation speed
					));

	//***********************
	// soul ones
	//***********************

	//tier 0 - like no tier at all
	public static RegistryObject<Technique> BASIC_SOUL_MANUAL = TECHNIQUES_REGISTER.register("basic_soul_manual",
					() -> new Technique("basic_soul_manual",
									Cultivation.System.DIVINE,
									0, // health
									0, // strength
									0, // agility
									0, // energy
									0, // energy regen
									1  //cultivation speed
					));

	//***********************
	// essence ones
	//***********************

	//tier 0 - like no tier at all
	public static RegistryObject<Technique> BASIC_QI_MANUAL = TECHNIQUES_REGISTER.register("basic_qi_manual",
					() -> new Technique("basic_qi_manual",
									Cultivation.System.ESSENCE,
									0, // health
									0, // strength
									0, // agility
									0, // energy
									0, // energy regen
									1 //cultivation speed
					));

}
