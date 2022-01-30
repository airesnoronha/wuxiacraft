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

public class WuxiaDamageSource extends DamageSource {

	private final Element element;
	private final Entity trueSource;

	public WuxiaDamageSource(String damageTypeIn, Element element) {
		super(damageTypeIn);
		this.element = element;
		this.trueSource = null;
	}

	public WuxiaDamageSource(String damageTypeIn, Element element, Entity trueSource) {
		super(damageTypeIn);
		this.element = element;
		this.trueSource = trueSource;
	}

	public Element getElement() {
		return this.element;
	}

	@Nullable
	@Override
	public Entity getEntity() {
		return this.trueSource;
	}

	@Nonnull
	@Override
	public Component getLocalizedDeathMessage(@Nonnull LivingEntity directSource) {
		if (this.trueSource == null) return super.getLocalizedDeathMessage(directSource);
		Component trueSourceName = this.trueSource.getDisplayName();
		ItemStack stack = this.trueSource instanceof LivingEntity source ? source.getItemInHand(InteractionHand.MAIN_HAND) : ItemStack.EMPTY;
		String localizedMessageId = "death.attack." + this.msgId;
		String aboveButWithItem = localizedMessageId + ".item";
		return !stack.isEmpty() ?
						new TranslatableComponent(aboveButWithItem, directSource.getDisplayName(), trueSourceName, stack.getDisplayName()) :
						new TranslatableComponent(localizedMessageId, directSource.getDisplayName(), trueSourceName);
	}
}
