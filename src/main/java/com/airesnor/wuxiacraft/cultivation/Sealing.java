package com.airesnor.wuxiacraft.cultivation;

public class Sealing implements ISealing {

    private final ICultivation sealedCultivation;

    private boolean bodySealed;

    private boolean divineSealed;

    private boolean essenceSealed;

    public Sealing() {
        sealedCultivation = new Cultivation();
        bodySealed = false;
        divineSealed = false;
        essenceSealed = false;
    }

    @Override
    public boolean isBodySealed() {
        return bodySealed;
    }

    @Override
    public boolean isDivineSealed() {
        return divineSealed;
    }

    @Override
    public boolean isEssenceSealed() {
        return essenceSealed;
    }

    @Override
    public void sealBody(ICultivation cultivation, BaseSystemLevel toLevel, int toRank) throws IllegalArgumentException {
        if (BaseSystemLevel.BODY_LEVELS.contains(toLevel)) {
            if (!toLevel.greaterThan(cultivation.getBodyLevel(), BaseSystemLevel.BODY_LEVELS)) { //like how can you seal something that isn't there
                if ((toLevel.equals(cultivation.getBodyLevel()) && toRank < cultivation.getBodySubLevel()) || //checks sub rank if same level
                        cultivation.getBodyLevel().greaterThan(toLevel, BaseSystemLevel.BODY_LEVELS)) { // or if toLevel is smaller than cultivation level
                    this.sealedCultivation.setBodyLevel(cultivation.getBodyLevel());
                    this.sealedCultivation.setBodySubLevel(cultivation.getBodySubLevel());
                    cultivation.setBodyLevel(toLevel);
                    cultivation.setBodySubLevel(toRank);
                    this.bodySealed = true;
                }
            }
        } else {
            throw new IllegalArgumentException("toLevel is not a body level");
        }
    }

    @Override
    public void sealDivine(ICultivation cultivation, BaseSystemLevel toLevel, int toRank) throws IllegalArgumentException {
        if (BaseSystemLevel.DIVINE_LEVELS.contains(toLevel)) {
            if (!toLevel.greaterThan(cultivation.getDivineLevel(), BaseSystemLevel.DIVINE_LEVELS)) { //like how can you seal something that isn't there
                if ((toLevel.equals(cultivation.getEssenceLevel()) && toRank < cultivation.getEssenceSubLevel()) || //checks sub rank if same level
                        cultivation.getEssenceLevel().greaterThan(toLevel, BaseSystemLevel.ESSENCE_LEVELS)) { // or if toLevel is smaller than cultivation level
                    this.sealedCultivation.setDivineLevel(cultivation.getDivineLevel());
                    this.sealedCultivation.setDivineSubLevel(cultivation.getDivineSubLevel());
                    cultivation.setDivineLevel(toLevel);
                    cultivation.setDivineSubLevel(toRank);
                    this.bodySealed = true;
                }
            }
        } else {
            throw new IllegalArgumentException("toLevel is not a divine level");
        }
    }

    @Override
    public void sealEssence(ICultivation cultivation, BaseSystemLevel toLevel, int toRank) throws IllegalArgumentException {
        if (BaseSystemLevel.ESSENCE_LEVELS.contains(toLevel)) {
            if (!toLevel.greaterThan(cultivation.getEssenceLevel(), BaseSystemLevel.ESSENCE_LEVELS)) { //like how can you seal something that isn't there
                if ((toLevel.equals(cultivation.getEssenceLevel()) && toRank < cultivation.getEssenceSubLevel()) || //checks sub rank if same level
                        cultivation.getEssenceLevel().greaterThan(toLevel, BaseSystemLevel.ESSENCE_LEVELS)) { // or if toLevel is smaller than cultivation level
                    this.sealedCultivation.setEssenceLevel(cultivation.getEssenceLevel());
                    this.sealedCultivation.setEssenceSubLevel(cultivation.getEssenceSubLevel());
                    cultivation.setEssenceLevel(toLevel);
                    cultivation.setEssenceSubLevel(toRank);
                    this.bodySealed = true;
                }
            }
        } else {
            throw new IllegalArgumentException("toLevel is not an essence level");
        }
    }

    @Override
    public void releaseBody(ICultivation cultivation) {
        cultivation.setDivineLevel(this.sealedCultivation.getDivineLevel());
        cultivation.setDivineSubLevel(this.sealedCultivation.getDivineSubLevel());
        this.sealedCultivation.setBodyLevel(BaseSystemLevel.DEFAULT_BODY_LEVEL);
        this.sealedCultivation.setBodySubLevel(0);
        this.bodySealed = false;
    }

    @Override
    public void releaseDivine(ICultivation cultivation) {
        cultivation.setDivineLevel(this.sealedCultivation.getDivineLevel());
        cultivation.setDivineSubLevel(this.sealedCultivation.getDivineSubLevel());
        this.sealedCultivation.setDivineLevel(BaseSystemLevel.DEFAULT_DIVINE_LEVEL);
        this.sealedCultivation.setDivineSubLevel(0);
        this.divineSealed = false;
    }

    @Override
    public void releaseEssence(ICultivation cultivation) {
        cultivation.setDivineLevel(this.sealedCultivation.getDivineLevel());
        cultivation.setDivineSubLevel(this.sealedCultivation.getDivineSubLevel());
        this.sealedCultivation.setEssenceLevel(BaseSystemLevel.DEFAULT_ESSENCE_LEVEL);
        this.sealedCultivation.setEssenceSubLevel(0);
        this.essenceSealed = false;
    }

    @Override
    public void changeBodySeal(ICultivation cultivation, BaseSystemLevel toLevel, int toRank) throws IllegalArgumentException, IllegalStateException {
        if (this.bodySealed) {
            if (BaseSystemLevel.BODY_LEVELS.contains(toLevel)) {
                if (!toLevel.greaterThan(sealedCultivation.getBodyLevel(), BaseSystemLevel.BODY_LEVELS)) { //like how can you seal something that isn't there
                    if ((toLevel.equals(sealedCultivation.getBodyLevel()) && toRank < sealedCultivation.getBodySubLevel()) || //checks sub rank if same level
                            sealedCultivation.getBodyLevel().greaterThan(toLevel, BaseSystemLevel.BODY_LEVELS)) { // or if toLevel is smaller than cultivation level
                        cultivation.setBodyLevel(toLevel);
                        cultivation.setBodySubLevel(toRank);
                    }
                }
            } else {
                throw new IllegalArgumentException("toLevel is not a body level");
            }
        } else {
            throw new IllegalStateException("Body isn't sealed");
        }
    }

    @Override
    public void changeDivineSeal(ICultivation cultivation, BaseSystemLevel toLevel, int toRank) throws IllegalArgumentException, IllegalStateException  {
        if (this.divineSealed) {
            if (BaseSystemLevel.BODY_LEVELS.contains(toLevel)) {
                if (!toLevel.greaterThan(sealedCultivation.getDivineLevel(), BaseSystemLevel.DIVINE_LEVELS)) { //like how can you seal something that isn't there
                    if ((toLevel.equals(sealedCultivation.getDivineLevel()) && toRank < sealedCultivation.getDivineSubLevel()) || //checks sub rank if same level
                            sealedCultivation.getDivineLevel().greaterThan(toLevel, BaseSystemLevel.DIVINE_LEVELS)) { // or if toLevel is smaller than cultivation level
                        cultivation.setDivineLevel(toLevel);
                        cultivation.setDivineSubLevel(toRank);
                    }
                }
            } else {
                throw new IllegalArgumentException("toLevel is not a divine level");
            }
        } else {
            throw new IllegalStateException("Divinity isn't sealed");
        }

    }

    @Override
    public void changeEssenceSeal(ICultivation cultivation, BaseSystemLevel toLevel, int toRank) throws IllegalArgumentException, IllegalStateException  {
        if (this.essenceSealed) {
            if (BaseSystemLevel.ESSENCE_LEVELS.contains(toLevel)) {
                if (!toLevel.greaterThan(sealedCultivation.getEssenceLevel(), BaseSystemLevel.ESSENCE_LEVELS)) { //like how can you seal something that isn't there
                    if ((toLevel.equals(sealedCultivation.getEssenceLevel()) && toRank < sealedCultivation.getEssenceSubLevel()) || //checks sub rank if same level
                            sealedCultivation.getEssenceLevel().greaterThan(toLevel, BaseSystemLevel.ESSENCE_LEVELS)) { // or if toLevel is smaller than cultivation level
                        cultivation.setEssenceLevel(toLevel);
                        cultivation.setEssenceSubLevel(toRank);
                    }
                }
            } else {
                throw new IllegalArgumentException("toLevel is not an essence level");
            }
        } else {
            throw new IllegalStateException("Essence isn't sealed");
        }

    }

    @Override
    public void setBodySealed(boolean sealed) {
        this.bodySealed = sealed;
    }

    @Override
    public void setDivineSealed(boolean sealed) {
        this.divineSealed = sealed;
    }

    @Override
    public void setEssenceSealed(boolean sealed) {
        this.essenceSealed = sealed;
    }

    @Override
    public ICultivation getSealedCultivation() {
        return this.sealedCultivation;
    }

    @Override
    public void setSealedCultivation(ICultivation cultivation) {
        this.sealedCultivation.copyFrom(cultivation);
    }

    @Override
    public void copyFrom(ISealing sealing) {
        this.sealedCultivation.copyFrom(sealing.getSealedCultivation());
        this.bodySealed = sealing.isBodySealed();
        this.divineSealed = sealing.isDivineSealed();
        this.essenceSealed = sealing.isEssenceSealed();
    }
}
