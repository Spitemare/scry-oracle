package io.github.scry.sort;

import io.github.scry.model.MtgCard;

/**
 * MCI treats Badlands as a red card.
 */
public class BetaCardComparator extends OldSetCardComparator {

    public BetaCardComparator() {
        super("BUGRWAL");
    }

    @Override
    protected char findColor(MtgCard card) {
        if ("Badlands".equals(card.getName())) return 'R';
        return super.findColor(card);
    }

}
