//package br.gov.mt.seplag.backend.mapper;
//
//import br.gov.mt.seplag.backend.dto.response.AlbumResponseDTO;
//import br.gov.mt.seplag.backend.entity.Album;
//import br.gov.mt.seplag.backend.entity.Artist;
//import br.gov.mt.seplag.backend.service.MinioStorageService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//import java.util.stream.Collectors;
//
//@Component
//@RequiredArgsConstructor
//public class AlbumMapperBKP {
//
//    private final MinioStorageService storageService;
//
//    public AlbumResponseDTO toResponseDTO(Album album) {
//        if (album == null) {
//            return null;
//        }
//
//        AlbumResponseDTO dto = new AlbumResponseDTO();
//        dto.setId(album.getId());
//        dto.setTitle(album.getTitle());
//
//        if (album.getArtists() != null) {
//            dto.setArtists(
//                    album.getArtists()
//                            .stream()
//                            .map(Artist::getId)
//                            .collect(Collectors.toSet())
//            );
//
//            dto.setArtistNames(
//                    album.getArtists()
//                            .stream()
//                            .map(Artist::getName)
//                            .collect(Collectors.toSet())
//            );
//        }
//
//        if (album.getCoverImages() != null) {
//            dto.setCoverImageUrls(
//                    album.getCoverImages()
//                            .stream()
//                            .map(image ->
//                                    storageService.generatePresignedUrl(
//                                            image.getObjectKey(),
//                                            30
//                                    )
//                            )
//                            .toList()
//            );
//        }
//
//        return dto;
//    }
//}
