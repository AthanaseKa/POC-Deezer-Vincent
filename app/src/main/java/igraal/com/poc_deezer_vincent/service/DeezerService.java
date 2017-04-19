package igraal.com.poc_deezer_vincent.service;


import igraal.com.poc_deezer_vincent.object.jsonobject.PlaylistListServiceResponse;
import igraal.com.poc_deezer_vincent.object.jsonobject.PlaylistServiceResponse;
import igraal.com.poc_deezer_vincent.object.jsonobject.UserJson;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by vincent on 03/04/2017.
 */

public interface DeezerService {
    @GET("user/{userId}")
    Observable<UserJson> getUser(@Path("userId") int user);

    @GET("user/{userid}/playlists")
    Observable <PlaylistListServiceResponse> getUserPlaylist(@Path("userid") int userId);

    @GET("playlist/{playlistId}")
    Observable <PlaylistServiceResponse> getPlaylist(@Path("playlistId") int playlistId);
}
