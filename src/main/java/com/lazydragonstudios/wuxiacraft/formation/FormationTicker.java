package com.lazydragonstudios.wuxiacraft.formation;

import com.lazydragonstudios.wuxiacraft.blocks.entity.FormationCore;
import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.lazydragonstudios.wuxiacraft.init.WuxiaParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;
import java.math.BigDecimal;
import java.util.HashMap;

@ParametersAreNonnullByDefault
public class FormationTicker implements BlockEntityTicker<FormationCore> {

	public static HashMap<System, ParticleType<SimpleParticleType>> PARTICLE_BY_SYSTEM = new HashMap<>();

	static {
		PARTICLE_BY_SYSTEM.put(System.BODY, WuxiaParticleTypes.BODY_QI_FOG.get());
		PARTICLE_BY_SYSTEM.put(System.DIVINE, WuxiaParticleTypes.DIVINE_QI_FOG.get());
		PARTICLE_BY_SYSTEM.put(System.ESSENCE, WuxiaParticleTypes.ESSENCE_QI_FOG.get());
	}

	@Override
	public void tick(Level level, BlockPos pos, BlockState blockState, FormationCore core) {
		if (!core.isActive()) return;
		var owner = core.getOwner();
		if (owner == null) return;
		var cultivation = Cultivation.get(owner);
		if (cultivation.getFormation() != pos) {
			core.deactivate();
			return;
		}
		var barrierAmount = core.getStat(FormationStat.BARRIER_AMOUNT);
		var barrierMaxAmount = core.getStat(FormationStat.BARRIER_MAX_AMOUNT);
		var barrierRegen = core.getStat(FormationStat.BARRIER_REGEN);
		if (barrierAmount.compareTo(barrierMaxAmount) < 0) {
			core.setStat(FormationStat.BARRIER_AMOUNT, barrierAmount.add(barrierRegen).min(barrierMaxAmount));
		}
		if (!level.isClientSide) return;
		for (var system : System.values()) {
			var systemEnergyRegen = core.getStat(system, FormationSystemStat.ENERGY_REGEN);
			if (systemEnergyRegen.compareTo(BigDecimal.ZERO) <= 0) continue;
			var range = core.getStat(system, FormationSystemStat.ENERGY_REGEN_RANGE).doubleValue();
			int particles = (int) (systemEnergyRegen.doubleValue() / 0.005f);
			particles = Math.min(particles, 40);
			for (int i = 0; i < particles; i++) {
				level.addParticle((ParticleOptions) PARTICLE_BY_SYSTEM.get(system),
						pos.getX() + 0.5d + Math.random() * 2 * range - range,
						pos.getY() + 0.5d + Math.random() * 2 * range - range,
						pos.getZ() + 0.5d + Math.random() * 2 * range - range,
						0d, 0.1d, 0d);
			}
		}
	}
}
