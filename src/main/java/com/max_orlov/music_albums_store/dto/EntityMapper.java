package com.max_orlov.music_albums_store.dto;

import com.max_orlov.music_albums_store.model.Album;
import com.max_orlov.music_albums_store.model.Tag;
import com.max_orlov.music_albums_store.model.User;
import jakarta.annotation.Nullable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class EntityMapper {

  @Mapping(source = "totalRating", target = "rating")
  public abstract AlbumDto albumToAlbumDto(Album album);

  public abstract Album albumDtoToAlbum(AlbumDto albumDto);

  String map(Tag tag) {
    return tag.getTagName();
  }

  Tag map(@Nullable String tagName) {
    return new Tag();
  }

  public abstract UserDto userToUserDto(User user);

}
