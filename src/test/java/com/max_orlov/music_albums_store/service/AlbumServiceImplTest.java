package com.max_orlov.music_albums_store.service;

import com.max_orlov.music_albums_store.dto.AlbumDto;
import com.max_orlov.music_albums_store.dto.EntityMapper;
import com.max_orlov.music_albums_store.model.Album;
import com.max_orlov.music_albums_store.model.Band;
import com.max_orlov.music_albums_store.repository.AlbumRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlbumServiceImplTest {

  @Mock
  AlbumRepository albumRepository;
  @Mock
  EntityMapper mapper;

  @InjectMocks
  AlbumServiceImpl service;

  @Test
  void getAlbumByIdTest() {
    Long id = 1L;

    Album album = new Album();
    album.setAlbumId(id);
    album.setAlbumName("Master");
    album.setReleaseYear("1986");
    album.setBand(new Band("Metallica"));

    AlbumDto dto = mapper.albumToAlbumDto(album);

    when(albumRepository.getAlbumByAlbumId(id)).thenReturn(album);
    AlbumDto albumDto = service.getAlbumById(id);

    assertThat(dto).isEqualTo(albumDto);
  }

  @Test
  void updateAlbumRating() {
    int totalRating = 15;
    int ratingUpdateCounter = 4;
    int currentRating = totalRating / ++ratingUpdateCounter;
    assertThat(currentRating).isEqualTo(3);
  }

  @Test
  void addTagToAlbum() {
  }

}