package io.github.scry.sort;

import io.github.scry.model.MtgCard;

/**
 * MCI swaps Orim's Prayer and Orim, Samite Healer.
 */
public class TempestCardComparator extends OldSetCardComparator {

    public TempestCardComparator() {
        super("BUGRWALX");
    }

    @Override
    public int compare(MtgCard card1, MtgCard card2) {
        String name1 = card1.getName();
        String name2 = card2.getName();
        if ("Orim's Prayer".equals(name1) && "Orim, Samite Healer".equals(name2)) return 1;
        if ("Orim's Prayer".equals(name2) && "Orim, Samite Healer".equals(name1)) return -1;
        return super.compare(card1, card2);
    }

}
