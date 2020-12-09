package com.airesnor.wuxiacraft.combat;

import com.airesnor.wuxiacraft.cultivation.elements.Element;
import com.airesnor.wuxiacraft.utils.MathUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * The whole idea of this class is to override vanilla way of making stuff
 * Because vanilla is too boring, weak and annoying
 * Pros: much control over stats
 * Cons: probably incompatibility with other mods and perhaps sponge (for now)
 */
public class PlayerStats implements IPlayerStats {

    private double HP;
    private double maxHP;
    private double armor;
    private double strength;
    private double moveSpeed;
    private double attackSpeed;

    private final List<Pair<Element, Double>> Resistances;

    public PlayerStats() {
        this.HP = 20;
        this.maxHP = 20;
        this.armor = 0;
        this.strength = 1;
        this.moveSpeed = 0.47d;
        this.attackSpeed = 1; // might change this later
        this.Resistances = new ArrayList<>();
    }

    @Override
    public double getHP() {
        return HP;
    }

    @Override
    public void setHP(double HP) {
        this.HP = MathUtils.clamp(HP, 0, this.maxHP);
    }

    @Override
    public double getMaxHP() {
        return maxHP;
    }

    @Override
    public void setMaxHP(double maxHP) {
        this.maxHP = maxHP;
    }

    @Override
    public double getArmor() {
        return armor;
    }

    @Override
    public void setArmor(double armor) {
        this.armor = armor;
    }

    @Override
    public double getStrength() {
        return strength;
    }

    @Override
    public void setStrength(double strength) {
        this.strength = strength;
    }

    @Override
    public double getMoveSpeed() {
        return moveSpeed;
    }

    @Override
    public void setMoveSpeed(double moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    @Override
    public double getAttackSpeed() {
        return attackSpeed;
    }

    @Override
    public void setAttackSpeed(double attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    @Override
    public List<Pair<Element, Double>> getResistances() {
        return Resistances;
    }

    @Override
    public double getResistanceByElement(Element element) {
        double resistance = 0;
        for(Pair<Element, Double> pair : this.Resistances) {
            if(pair.getLeft() == element) {
                resistance = pair.getRight();
            }
        }
        return resistance;
    }

    @Override
    public void addElementResistance(Element element, double resistance) {
        this.Resistances.add(Pair.of(element, resistance));
    }

}
