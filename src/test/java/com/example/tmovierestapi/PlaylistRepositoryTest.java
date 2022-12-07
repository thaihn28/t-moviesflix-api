//package com.example.tmovierestapi;
//
//import com.example.tmovierestapi.model.Playlist;
//import com.example.tmovierestapi.payload.dto.PlaylistDTO;
//import com.example.tmovierestapi.payload.response.PagedResponse;
//import com.example.tmovierestapi.repository.PlaylistRepository;
//import com.example.tmovierestapi.service.IPlaylistService;
//import com.example.tmovierestapi.service.impl.PlaylistServiceImpl;
//import com.example.tmovierestapi.utils.AppConstants;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.annotation.Rollback;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@Rollback(value = false)
//public class PlaylistRepositoryTest {
//    @Autowired
//    private PlaylistRepository playlistRepository;
//
//    @Autowired
//    private IPlaylistService iPlaylistService;
//
//    @Autowired
//    private ModelMapper modelMapper;
//
//    @Test
//    public void testAddPlaylist(){
////        PlaylistDTO playlistDTO = new PlaylistDTO();
////        playlistDTO.setThumbURL("asdgsfgsfdg");
////        playlistDTO.setName("Là Em");
////        playlistDTO.setOriginName("Bad Romeo");
////        playlistDTO.setSlug("la-em");
////        playlistDTO.setYear(2022);
////
////        PlaylistDTO playlistDTO2 = new PlaylistDTO();
////        playlistDTO2.setName("Là Em 2");
////        playlistDTO2.setOriginName("Bad Romeo 2");
////        playlistDTO2.setSlug("la-em 2");
////        playlistDTO2.setYear(2023);
//
//
////        playlistService.addPlaylist(playlistDTO);
////        PlaylistDTO playlistDTO4 = iPlaylistService.addPlaylist(playlistDTO);
////
////        PagedResponse<Playlist> playlistPagedResponse = iPlaylistService.getAllPlaylists(AppConstants.DEFAULT_PAGE_NUMBER, 30, AppConstants.SORT_DIRECTION, AppConstants.SORT_BY);
////
////        for(Playlist playlist : playlistPagedResponse.getContent()){
////            System.out.println(playlist.getSlug() + "adsfags");
////        }
//
////        Playlist savePlaylist = playlistRepository.save(playlist);
////
////        Assertions.assertNotNull(playlistDTO4);
////        Assertions.assertEquals( 1, savePlaylist.getId());
//    }
//}
