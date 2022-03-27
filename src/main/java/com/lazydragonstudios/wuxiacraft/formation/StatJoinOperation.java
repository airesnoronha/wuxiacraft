package com.lazydragonstudios.wuxiacraft.formation;

import java.math.BigDecimal;

interface StatJoinOperation {
	BigDecimal join(BigDecimal initial, BigDecimal modifier);

	StatJoinOperation maxOp = BigDecimal::max;

	StatJoinOperation addOp = BigDecimal::add;

	//will add to two times the greater value, order matters in this but will leave that as is, it's going to be fun this way
	StatJoinOperation addTil2 = (i, m) -> i.add(m).min(i.max(m.multiply(BigDecimal.valueOf(2f))));
}
