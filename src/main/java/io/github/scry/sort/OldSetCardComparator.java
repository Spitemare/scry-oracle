package io.github.scry.sort;

import java.util.Comparator;
import java.util.List;
import io.github.scry.model.MtgCard;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class OldSetCardComparator implements Comparator<MtgCard> {

    String colorOrder;

    @Override
    public int compare(MtgCard card1, MtgCard card2) {
        char color1 = findColor(card1);
        char color2 = findColor(card2);
        int i = colorOrder.indexOf(color1) - colorOrder.indexOf(color2);
        return i == 0 ? card1.getName().compareTo(card2.getName()) : i;
    }

    protected char findColor(MtgCard card) {
        if (card.getType().contains("Land")) return 'L';

        List<String> colors = card.getColors();
        if (colors.isEmpty()) return 'A';
        if (colors.size() > 1) return 'X';

        String color = colors.get(0);
        if ("Blue".equals(color)) return 'U';
        return color.charAt(0);
    }

    @Override
    public String toString() {
        return colorOrder;
    }

}
