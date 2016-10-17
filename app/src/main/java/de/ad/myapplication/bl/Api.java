package de.ad.myapplication.bl;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Api {
  @GET("sessions/") Call<List<Session>> loadSessions();

  @GET("sessions/{id}") Call<Session> loadSession(@Path("id") String id);
}
