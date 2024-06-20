package com.example.schedule;

import com.example.schedule.dto.FlowResponse;
import com.example.schedule.dto.LessonResponse;
import com.example.schedule.dto.ScheduleResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {
    private static final BackendService backendService;

    static {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(
                        FlowResponse.class,
                        new FlowResponseJsonDeserializer()
                )
                .registerTypeAdapter(
                        LessonResponse.class,
                        new LessonResponseJsonDeserializer()
                )
                .registerTypeAdapter(
                        ScheduleResponse.class,
                        new ScheduleResponseJsonDeserializer()
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

    private static class FlowResponseJsonDeserializer implements JsonDeserializer<FlowResponse> {
        @Override
        public FlowResponse deserialize(
                JsonElement json, Type typeOfT, JsonDeserializationContext context
        ) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            JsonPrimitive flowLvl = jsonObject.getAsJsonPrimitive("flow_lvl");
            JsonPrimitive course = jsonObject.getAsJsonPrimitive("course");
            JsonPrimitive flow = jsonObject.getAsJsonPrimitive("flow");
            JsonPrimitive subgroup = jsonObject.getAsJsonPrimitive("subgroup");
            JsonPrimitive lastEdit = jsonObject.getAsJsonPrimitive("last_edit");
            JsonPrimitive lessonsStartDate = jsonObject
                    .getAsJsonPrimitive("lessons_start_date");
            JsonPrimitive sessionStartDate = jsonObject
                    .getAsJsonPrimitive("session_start_date");
            JsonPrimitive sessionEndDate = jsonObject
                    .getAsJsonPrimitive("session_end_date");
            JsonPrimitive active = jsonObject.getAsJsonPrimitive("active");

            FlowResponse result = new FlowResponse();
            result.setFlowLvl(flowLvl.getAsInt());
            result.setCourse(course.getAsInt());
            result.setFlow(flow.getAsInt());
            result.setSubgroup(subgroup.getAsInt());
            result.setLastEdit(LocalDateTime.parse(lastEdit.getAsString()));
            result.setLessonsStartDate(LocalDate.parse(lessonsStartDate.getAsString()));
            result.setSessionStartDate(LocalDate.parse(sessionStartDate.getAsString()));
            result.setSessionEndDate(LocalDate.parse(sessionEndDate.getAsString()));
            result.setActive(active.getAsBoolean());

            return result;
        }
    }

    private static class LessonResponseJsonDeserializer
            implements JsonDeserializer<LessonResponse> {
        @Override
        public LessonResponse deserialize(
                JsonElement json, Type typeOfT, JsonDeserializationContext context
        ) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            JsonPrimitive name = jsonObject.getAsJsonPrimitive("name");
            JsonPrimitive teacher = jsonObject.getAsJsonPrimitive("teacher");
            JsonPrimitive cabinet = jsonObject.getAsJsonPrimitive("cabinet");

            LessonResponse result = new LessonResponse();
            result.setName(name.getAsString());
            result.setTeacher(teacher.getAsString());
            result.setCabinet(cabinet.getAsString());

            return result;
        }
    }

    private static class ScheduleResponseJsonDeserializer
            implements JsonDeserializer<ScheduleResponse> {
        @Override
        public ScheduleResponse deserialize(
                JsonElement json, Type typeOfT, JsonDeserializationContext context
        ) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            JsonObject flowResponse = jsonObject.getAsJsonObject("flow");
            JsonObject lessonResponse = jsonObject.getAsJsonObject("lesson");
            JsonPrimitive dayOfWeek = jsonObject.getAsJsonPrimitive("day_of_week");
            JsonPrimitive lessonNum = jsonObject.getAsJsonPrimitive("lesson_num");
            JsonPrimitive numerator = jsonObject.getAsJsonPrimitive("numerator");

            ScheduleResponse result = new ScheduleResponse();
            result.setFlow(
                    new FlowResponseJsonDeserializer()
                            .deserialize(flowResponse, null, context)
            );
            result.setLesson(
                    new LessonResponseJsonDeserializer()
                            .deserialize(lessonResponse, null, context)
            );
            result.setDayOfWeek(dayOfWeek.getAsInt());
            result.setLessonNum(lessonNum.getAsInt());
            result.setNumerator(numerator.getAsBoolean());

            return result;
        }
    }
}
