package io.github.scry.sort;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.github.scry.ScryOracleApplication.OracleProperties;
import io.github.scry.model.MtgCard;
import io.github.scry.model.MtgSet;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SetCardSorter {

    Map<String, Comparator<MtgCard>> setComparators;

    @Autowired
    public SetCardSorter(OracleProperties oracleProperties) {
        setComparators = oracleProperties.getSetOrders().stream().map(s -> s.split("\\|"))
                .collect(Collectors.toMap(a -> a[0], a -> new OldSetCardComparator(a[1])));
    }

    public List<MtgCard> sortCardsIn(MtgSet set) {
        List<MtgCard> cards = set.getCards();
        if (!setComparators.containsKey(set.getName())) return cards;

        Collections.sort(cards, setComparators.get(set.getName()));
        IntStream.range(0, cards.size())
                .forEach(i -> cards.get(i).setNumber(String.valueOf(i + 1)));
        return cards;
    }

}
