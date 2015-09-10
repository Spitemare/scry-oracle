package io.github.scry.sort;

import java.util.Arrays;
import java.util.Collection;
import io.github.scry.model.MtgCard;

/**
 * MCI treats Kobolds from Legends as artifacts, not red cards.
 */
public class LegendsCardComparator extends OldSetCardComparator {

    private static final Collection<String> KOBOLDS = Arrays.asList("Crimson Kobolds",
            "Crookshank Kobolds", "Kobolds of Kher Keep");

    public LegendsCardComparator() {
        super("BUGRWALX");
    }

    @Override
    protected char findColor(MtgCard card) {
        if (KOBOLDS.contains(card.getName())) return 'A';
        return super.findColor(card);
    }

}
