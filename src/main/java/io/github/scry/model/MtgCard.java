package io.github.scry.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MtgCard {

    // Card specific fields

    String layout;

    String name;

    List<String> names;

    String manaCost;

    String type = "";

    String text;

    String power;

    String toughness;

    Integer loyalty;

    List<Ruling> rulings;

    List<String> colors = new ArrayList<>();

    // Set specific fields

    String flavor;

    String artist;

    String number;

    Integer multiverseid;

    Border border;

    String rarity;

    // Fields we create

    LinkedList<Integer> multiverseids = new LinkedList<>();

    @JsonProperty("sets")
    LinkedList<Printing> printings = new LinkedList<>();

}
