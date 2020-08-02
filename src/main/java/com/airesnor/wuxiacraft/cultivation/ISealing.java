package com.airesnor.wuxiacraft.cultivation;

public interface ISealing {

    boolean isBodySealed();

    boolean isDivineSealed();

    boolean isEssenceSealed();

    void sealBody(ICultivation cultivation, BaseSystemLevel toLevel, int toRank);

    void sealDivine(ICultivation cultivation, BaseSystemLevel toLevel, int toRank);

    void sealEssence(ICultivation cultivation, BaseSystemLevel toLevel, int toRank);

    void releaseBody(ICultivation cultivation);

    void releaseDivine(ICultivation cultivation);

    void releaseEssence(ICultivation cultivation);

    void changeBodySeal(ICultivation cultivation, BaseSystemLevel toLevel, int toRank);

    void changeDivineSeal(ICultivation cultivation, BaseSystemLevel toLevel, int toRank);

    void changeEssenceSeal(ICultivation cultivation, BaseSystemLevel toLevel, int toRank);

    void setBodySealed(boolean sealed);

    void setDivineSealed(boolean sealed);

    void setEssenceSealed(boolean sealed);

    void setSealedCultivation(ICultivation cultivation);

    ICultivation getSealedCultivation();

    void copyFrom(ISealing sealing);
}
