package com.example.tmovierestapi.service;

import com.example.tmovierestapi.model.Actor;
import com.example.tmovierestapi.payload.dto.ActorDTO;
import com.example.tmovierestapi.payload.response.PagedResponse;

public interface IActorService {
    PagedResponse<Actor> getAllActors(int pageNo, int pageSize, String sortDir, String sortBy);
    ActorDTO addActor(ActorDTO actorDTO);
}
