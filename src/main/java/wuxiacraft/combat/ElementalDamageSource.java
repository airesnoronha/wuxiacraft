package wuxiacraft.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.jetbrains.annotations.Nullable;
import wuxiacraft.cultivation.Element;

import javax.annotation.Nonnull;

public class ElementalDamageSource extends DamageSource {

	private final Element element;
	private final Entity trueSource;

	public ElementalDamageSource(String damageTypeIn, Element element) {
		super(damageTypeIn);
		this.element = element;
		this.trueSource = null;
	}

	public ElementalDamageSource(String damageTypeIn, Element element, Entity trueSource) {
		super(damageTypeIn);
		this.element = element;
		this.trueSource = trueSource;
	}

	public Element getElement() {
		return element;
	}

	@Nullable
	@Override
	public net.minecraft.entity.Entity getTrueSource() {
		return this.trueSource;
	}

	@Nonnull
	@Override
	public ITextComponent getDeathMessage(@Nonnull LivingEntity entityLivingBaseIn) {
		if(this.trueSource == null) return super.getDeathMessage(entityLivingBaseIn);
			ITextComponent itextcomponent = this.trueSource.getDisplayName();
			ItemStack itemstack = this.trueSource instanceof LivingEntity ? ((LivingEntity)this.trueSource).getHeldItemMainhand() : ItemStack.EMPTY;
			String s = "death.attack." + this.damageType;
			String s1 = s + ".item";
			return !itemstack.isEmpty() && itemstack.hasDisplayName() ? new TranslationTextComponent(s1, entityLivingBaseIn.getDisplayName(), itextcomponent, itemstack.getTextComponent()) : new TranslationTextComponent(s, entityLivingBaseIn.getDisplayName(), itextcomponent);
	}
}
