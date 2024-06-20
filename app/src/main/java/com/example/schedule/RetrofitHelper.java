package com.example.schedule;

import com.example.schedule.entity.Flow;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.time.LocalDate;
import java.time.LocalDateTime;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {
    private static final BackendService backendService;

    static {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(
                        Flow.class,
                        (JsonDeserializer<Flow>) (json, typeOfT, context) -> {
                            JsonObject jsonObject = json.getAsJsonObject();
                            JsonPrimitive flowLvl = jsonObject
                                    .getAsJsonPrimitive("flow_lvl");
                            JsonPrimitive course = jsonObject
                                    .getAsJsonPrimitive("course");
                            JsonPrimitive flow = jsonObject
                                    .getAsJsonPrimitive("flow");
                            JsonPrimitive subgroup = jsonObject
                                    .getAsJsonPrimitive("subgroup");
                            JsonPrimitive lastEdit = jsonObject
                                    .getAsJsonPrimitive("last_edit");
                            JsonPrimitive lessonsStartDate = jsonObject
                                    .getAsJsonPrimitive("lessons_start_date");
                            JsonPrimitive sessionStartDate = jsonObject
                                    .getAsJsonPrimitive("session_start_date");
                            JsonPrimitive sessionEndDate = jsonObject
                                    .getAsJsonPrimitive("session_end_date");
                            JsonPrimitive active = jsonObject
                                    .getAsJsonPrimitive("active");

                            Flow result = new Flow();
                            result.setFlowLvl(flowLvl.getAsInt());
                            result.setCourse(course.getAsInt());
                            result.setFlow(flow.getAsInt());
                            result.setSubgroup(subgroup.getAsInt());
                            result.setLastEdit(LocalDateTime.parse(lastEdit.getAsString()));
                            result.setLessonsStartDate(LocalDate.parse(
                                    lessonsStartDate.getAsString()
                            ));
                            result.setSessionStartDate(LocalDate.parse(
                                    sessionStartDate.getAsString()
                            ));
                            result.setSessionEndDate(LocalDate.parse(sessionEndDate.getAsString()));
                            result.setActive(active.getAsBoolean());

                            return result;
                        }
                )
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SettingsStorage.backendBaseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        backendService = retrofit.create(BackendService.class);
    }

    public static BackendService getBackendService() {
        return backendService;
    }
}
