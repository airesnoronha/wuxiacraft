package com.lazydragonstudios.wuxiacraft.combat;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import com.lazydragonstudios.wuxiacraft.cultivation.Element;

import javax.annotation.Nonnull;
import java.math.BigDecimal;

public class WuxiaDamageSource extends DamageSource {

	private final Element element;
	private final Entity trueSource;
	private final BigDecimal damage;

	public WuxiaDamageSource(String damageTypeIn, Element element, BigDecimal damage) {
		super(damageTypeIn);
		this.element = element;
		this.damage = damage;
		this.trueSource = null;
	}

	public WuxiaDamageSource(String damageTypeIn, Element element, Entity trueSource, BigDecimal damage) {
		super(damageTypeIn);
		this.element = element;
		this.trueSource = trueSource;
		this.damage = damage;
	}

	public Element getElement() {
		return this.element;
	}

	@Nullable
	@Override
	public Entity getEntity() {
		return this.trueSource;
	}

	public BigDecimal getDamage() {
		return this.damage;
	}

	@Nonnull
	@Override
	public Component getLocalizedDeathMessage(@Nonnull LivingEntity target) {
		if (this.trueSource == null) return super.getLocalizedDeathMessage(target);
		Component trueSourceName = this.trueSource.getDisplayName();
		ItemStack stack = this.trueSource instanceof LivingEntity source ? source.getItemInHand(InteractionHand.MAIN_HAND) : ItemStack.EMPTY;
		String localizedMessageId = "death.attack." + this.msgId;
		String aboveButWithItem = localizedMessageId + ".item";
		return !stack.isEmpty() ?
						new TranslatableComponent(aboveButWithItem, target.getDisplayName(), trueSourceName, stack.getDisplayName()) :
						new TranslatableComponent(localizedMessageId, target.getDisplayName(), trueSourceName);
	}
}
