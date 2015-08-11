package io.github.scry.sort;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;
import static java.util.Arrays.asList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;
import io.github.scry.model.MtgCard;

public class OldSetCardComparatorTest {

    @Test
    public void testAlphaOrdering() {
        MtgCard card1 = new MtgCard();
        card1.setName("A");
        card1.setColors(asList("White"));

        MtgCard card2 = new MtgCard();
        card2.setName("B");
        card2.setColors(asList("White"));

        MtgCard card3 = new MtgCard();
        card3.setName("C");
        card3.setColors(asList("Blue"));

        MtgCard card4 = new MtgCard();
        card4.setName("D");
        card4.setType("Legendary Land");

        MtgCard card5 = new MtgCard();
        card5.setName("E");

        MtgCard card6 = new MtgCard();
        card6.setName("F");
        card6.setColors(asList("Black"));

        MtgCard card7 = new MtgCard();
        card7.setName("G");
        card7.setColors(asList("Green"));

        MtgCard card8 = new MtgCard();
        card8.setName("H");
        card8.setColors(asList("Red"));

        MtgCard card9 = new MtgCard();
        card9.setName("I");
        card9.setColors(asList("White", "Blue"));

        List<MtgCard> cards = asList(card1, card2, card3, card4, card5, card6, card7, card8, card9);
        Collections.sort(cards, new OldSetCardComparator("BUGRWALX"));

        String s = cards.stream().map(MtgCard::getName).collect(Collectors.joining());
        assertThat(s, is("FCGHABEDI"));
    }

}
