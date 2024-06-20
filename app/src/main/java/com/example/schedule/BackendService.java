package com.example.schedule;

import com.example.schedule.dto.FlowResponse;
import com.example.schedule.dto.HomeworkResponse;
import com.example.schedule.dto.ScheduleResponse;
import com.example.schedule.dto.TempScheduleResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BackendService {
    @GET("flow/all")
    Call<List<FlowResponse>> allFlows();

    @GET("flow/flow")
    Call<FlowResponse> flow(
            @Query("flow_lvl") int flowLvl, @Query("course") int course, @Query("flow") int flow,
            @Query("subgroup") int subgroup
    );

    @GET("schedule/flow")
    Call<List<ScheduleResponse>> schedules(
            @Query("flow_lvl") int flowLvl, @Query("course") int course, @Query("flow") int flow,
            @Query("subgroup") int subgroup
    );

    @GET("homework/flow")
    Call<List<HomeworkResponse>> homeworks(
            @Query("flow_lvl") int flowLvl, @Query("course") int course, @Query("flow") int flow,
            @Query("subgroup") int subgroup
    );

    @GET("temp/flow")
    Call<List<TempScheduleResponse>> tempSchedules(
            @Query("flow_lvl") int flowLvl, @Query("course") int course, @Query("flow") int flow,
            @Query("subgroup") int subgroup
    );
}
