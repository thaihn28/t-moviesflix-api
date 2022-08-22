package com.example.tmovierestapi;

import com.example.tmovierestapi.model.Playlist;
import com.example.tmovierestapi.repository.PlaylistRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class PlaylistRepositoryTest {
    @Autowired
    private PlaylistRepository playlistRepository;

    @Test
    public void testAddPlaylist(){
        Playlist playlist = new Playlist();
        playlist.setName("Là Em");
        playlist.setOriginName("Bad Romeo");
        playlist.setSlug("la-em");
        playlist.setYear(2022);

        Playlist savePlaylist = playlistRepository.save(playlist);

        Assertions.assertNotNull(savePlaylist);
        Assertions.assertEquals( 1, savePlaylist.getId());
    }
}
