package com.airesnor.wuxiacraft.combat;

import com.airesnor.wuxiacraft.cultivation.elements.Element;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface IPlayerStats {
    double getHP();

    void setHP(double HP);

    double getMaxHP();

    void setMaxHP(double maxHP);

    double getArmor();

    void setArmor(double armor);

    double getStrength();

    void setStrength(double strength);

    double getMoveSpeed();

    void setMoveSpeed(double moveSpeed);

    double getAttackSpeed();

    void setAttackSpeed(double attackSpeed);

    List<Pair<Element, Double>> getResistances();

    double getResistanceByElement(Element element);

    void addElementResistance(Element element, double resistance);
}
