package com.max_orlov.music_albums_store.repository;

import com.max_orlov.music_albums_store.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;

public interface AlbumRepository extends JpaRepository<Album, Long> {

    @Modifying
    @NativeQuery(value = "update albums set current_rating = ?1, total_rating = ?2, rating_update_counter = ?3 where album_id = ?4")
    void updateAlbumRating(int currentRating, int totalRating, int ratingUpdateCounter, Long albumId);

    Album getAlbumByAlbumId(Long albumId);

}
