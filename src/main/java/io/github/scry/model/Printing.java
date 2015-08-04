package io.github.scry.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Printing {

    String artist;

    Border border;

    String flavor;

    Integer multiverseid;

    String number;

    String rarity;

    String mci;

    String setcode;

    String setname;

}
