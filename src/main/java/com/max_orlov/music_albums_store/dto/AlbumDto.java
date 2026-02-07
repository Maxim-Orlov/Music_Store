package com.max_orlov.music_albums_store.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlbumDto {

    private static final String ALBUM_ERROR_MESSAGE = "Album name must be not empty";
    private static final String YEAR_ERROR_MESSAGE = "Year must be not empty";
    private static final String BAND_ERROR_MESSAGE = "Band name must be not empty";

    private Long albumId;
    @NotBlank (message = ALBUM_ERROR_MESSAGE)
    private String albumName;
    @NotBlank(message = YEAR_ERROR_MESSAGE)
    private String releaseYear;
    @NotBlank(message = BAND_ERROR_MESSAGE)
    private String bandName;
    private int rating;
    private String pathToPicture;
    private List<String> tagList;

}
