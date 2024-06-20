package com.example.schedule;

import com.example.schedule.entity.Flow;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BackendService {
    @GET("flow/all")
    Call<List<Flow>> allFlows();
}
