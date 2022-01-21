package wuxiacraft.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import wuxiacraft.WuxiaCraft;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.CultivationRealm;
import wuxiacraft.cultivation.CultivationStage;

public class WuxiaRealms {

	public static DeferredRegister<CultivationRealm> REALM_REGISTER = DeferredRegister.create(CultivationRealm.class, WuxiaCraft.MOD_ID);
	public static DeferredRegister<CultivationStage> STAGE_REGISTER = DeferredRegister.create(CultivationStage.class, WuxiaCraft.MOD_ID);

	public static RegistryObject<CultivationRealm> BODY_MORTAL_REALM = REALM_REGISTER
					.register("body_mortal_realm",
									() -> new CultivationRealm("body_mortal_realm",
													Cultivation.System.BODY,
													new ResourceLocation(WuxiaCraft.MOD_ID, "body_mortal_stage"),
													new ResourceLocation(WuxiaCraft.MOD_ID)
									));

	public static RegistryObject<CultivationRealm> DIVINE_MORTAL_REALM = REALM_REGISTER
					.register("divine_mortal_realm",
									() -> new CultivationRealm("divine_mortal_realm",
													Cultivation.System.BODY,
													new ResourceLocation(WuxiaCraft.MOD_ID, "divine_mortal_stage"),
													new ResourceLocation(WuxiaCraft.MOD_ID)
									));

	public static RegistryObject<CultivationRealm> ESSENCE_MORTAL_REALM = REALM_REGISTER
					.register("essence_mortal_realm",
									() -> new CultivationRealm("essence_mortal_realm",
													Cultivation.System.BODY,
													new ResourceLocation(WuxiaCraft.MOD_ID, "essence_mortal_stage"),
													new ResourceLocation(WuxiaCraft.MOD_ID)
									));

	public static RegistryObject<CultivationRealm> ESSENCE_QI_ASSIMILATION_REALM = REALM_REGISTER
					.register("essence_qi_assimilation_realm",
									() -> new CultivationRealm("essence_qi_assimilation_realm",
													Cultivation.System.BODY,
													new ResourceLocation(WuxiaCraft.MOD_ID, "essence_mortal_stage"),
													new ResourceLocation(WuxiaCraft.MOD_ID)
									));

	//*********************************
	// body stages
	//*********************************

	public static RegistryObject<CultivationStage> BODY_MORTAL_STAGE = STAGE_REGISTER
					.register("body_mortal_stage",
									() -> new CultivationStage("body_mortal_stage",
													Cultivation.System.BODY,
													1000d,
													10d,
													0d,
													0.01d,
													0,
													0
									));

	//*********************************
	// divine stages
	//*********************************

	public static RegistryObject<CultivationStage> DIVINE_MORTAL_STAGE = STAGE_REGISTER
					.register("divine_mortal_stage",
									() -> new CultivationStage("divine_mortal_stage",
													Cultivation.System.BODY,
													1000d,
													10d,
													0d,
													0.01d,
													0,
													0
									));

	//*********************************
	// essence stages
	//*********************************

	public static RegistryObject<CultivationStage> ESSENCE_MORTAL_STAGE = STAGE_REGISTER
					.register("essence_mortal_stage",
									() -> new CultivationStage("essence_mortal_stage",
													Cultivation.System.BODY,
													1000d,
													10d,
													0d,
													0.01d,
													0,
													0
									));

	public static RegistryObject<CultivationStage> ESSENCE_QI_GATHERING_STAGE = STAGE_REGISTER
					.register("essence_qi_gathering_stage",
									() -> new CultivationStage("essence_qi_gathering_stage",
													Cultivation.System.BODY,
													1000d,
													10d,
													0d,
													0.01d,
													0,
													0
									));

	public static RegistryObject<CultivationStage> ESSENCE_QI_PATHWAYS_STAGE = STAGE_REGISTER
					.register("essence_qi_pathways_stage",
									() -> new CultivationStage("essence_qi_pathways_stage",
													Cultivation.System.BODY,
													1000d,
													10d,
													0d,
													0.01d,
													0,
													0
									));

	public static RegistryObject<CultivationStage> ESSENCE_QI_CONDENSATION_STAGE = STAGE_REGISTER
					.register("essence_qi_condensation_stage",
									() -> new CultivationStage("essence_qi_condensation_stage",
													Cultivation.System.BODY,
													1000d,
													10d,
													0d,
													0.01d,
													0,
													0
									));

	public static RegistryObject<CultivationStage> ESSENCE_QI_PHENOMENON_STAGE = STAGE_REGISTER
					.register("essence_qi_phenomenon_stage",
									() -> new CultivationStage("essence_qi_phenomenon_stage",
													Cultivation.System.BODY,
													1000d,
													10d,
													0d,
													0.01d,
													0,
													0
									));
}
