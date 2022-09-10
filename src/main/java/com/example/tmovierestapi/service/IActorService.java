package com.example.tmovierestapi.service;

import com.example.tmovierestapi.model.Actor;
import com.example.tmovierestapi.payload.dto.ActorDTO;
import com.example.tmovierestapi.payload.response.ActorResponse;
import com.example.tmovierestapi.payload.response.PagedResponse;
import org.springframework.web.multipart.MultipartFile;

public interface IActorService {
    PagedResponse<ActorResponse> getAllActors(int pageNo, int pageSize, String sortDir, String sortBy);
    ActorDTO addActor(ActorDTO actorDTO, MultipartFile avatar);
    ActorDTO updateActor(Long id, ActorDTO actorDTO);
    void deleteActor(Long id);
    Actor getActorByID(Long id);
}
