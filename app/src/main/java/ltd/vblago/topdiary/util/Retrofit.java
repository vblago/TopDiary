package ltd.vblago.topdiary.util;

import ltd.vblago.topdiary.model.NURE.Nure;
import ltd.vblago.topdiary.model.NURE.Teachers.NureTeachers;
import ltd.vblago.topdiary.model.NURE.TimeTable.TimeTable;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Query;


public class Retrofit {

    private static final String ENDPOINT = "http://cist.nure.ua/ias/app/tt";
    private static ApiInterface apiInterface;

    static {
        initialize();
    }

    interface ApiInterface {
        @GET("/P_API_GROUP_JSON")
        void getNure(Callback<Nure> callback);

        @GET("/P_API_PODR_JSON")
        void getTeachers(Callback<NureTeachers> callback);

        @GET("/P_API_EVENT_JSON")
        void getSchedule(@Query("timetable_id") String idGroup, @Query("type_id") String type, Callback<TimeTable> callback);

    }

    private static void initialize() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        apiInterface = restAdapter.create(ApiInterface.class);
    }

    public static void getNure(Callback<Nure> callback) {
        apiInterface.getNure(callback);
    }

    public static void getNureTeachers(Callback<NureTeachers> callback) {
        apiInterface.getTeachers(callback);
    }

    public static void getSchedule(int idGroup, int type, Callback<TimeTable> callback) {
        apiInterface.getSchedule(String.valueOf(idGroup), String.valueOf(type), callback);
    }

}


