package com.max_orlov.music_albums_store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum QueryOrderParameters {
    YEAR("a.releaseYear"),
    BAND_NAME("a.band.bandName"),
    ALBUM_NAME("a.albumName"),
    RATING("a.currentRating");

    private final String name;

    public static boolean contains(String order) {
        return Arrays.stream(QueryOrderParameters.values())
                .anyMatch(orderParam -> orderParam.getName().equals(order));
    }

}
