package wuxiacraft.item;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.ICultivation;
import wuxiacraft.cultivation.KnownTechnique;
import wuxiacraft.cultivation.technique.Technique;
import wuxiacraft.init.WuxiaTechniques;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TechniqueScrollItem extends Item {

	private final Technique technique;

	public TechniqueScrollItem(Properties properties, Technique technique) {
		super(properties);
		this.technique = technique;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ICultivation cultivation = Cultivation.get(playerIn);
		KnownTechnique kt = cultivation.getTechniqueBySystem(technique.getSystem());
		boolean success = false;
		if (kt == null) {
			cultivation.setKnownTechnique(WuxiaTechniques.BASIC_BODY_FORGING_MANUAL, 0);
			cultivation.setKnownTechnique(WuxiaTechniques.BASIC_QI_GATHERING_TECHNIQUE, 0);
			cultivation.setKnownTechnique(WuxiaTechniques.BASIC_MENTAL_ENERGY_MANIPULATION, 0);
			cultivation.setKnownTechnique(this.technique, 0);
			success = true;
		} else if (kt.getTechnique().getCompatibles().contains(this.technique)) {
			cultivation.setKnownTechnique(this.technique, kt.getProficiency());
			success = true;
		} else if (kt.getTechnique() == WuxiaTechniques.BASIC_BODY_FORGING_MANUAL
				&& this.technique != WuxiaTechniques.BASIC_BODY_FORGING_MANUAL) {
			cultivation.setKnownTechnique(this.technique, kt.getProficiency() / 2f);
			success = true;
		} else if (kt.getTechnique() == WuxiaTechniques.BASIC_QI_GATHERING_TECHNIQUE
				&& this.technique != WuxiaTechniques.BASIC_QI_GATHERING_TECHNIQUE) {
			cultivation.setKnownTechnique(this.technique, kt.getProficiency() / 2f);
			success = true;
		} else if (kt.getTechnique() == WuxiaTechniques.BASIC_MENTAL_ENERGY_MANIPULATION
				&& this.technique != WuxiaTechniques.BASIC_MENTAL_ENERGY_MANIPULATION) {
			cultivation.setKnownTechnique(this.technique, kt.getProficiency() / 2f);
			success = true;
		}
		if(success) playerIn.getHeldItem(handIn).shrink(1);
		return success ? ActionResult.resultConsume(playerIn.getHeldItem(handIn)) : ActionResult.resultPass(playerIn.getHeldItem(handIn));
	}
}
