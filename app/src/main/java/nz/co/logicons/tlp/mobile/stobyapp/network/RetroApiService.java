package nz.co.logicons.tlp.mobile.stobyapp.network;

import java.util.List;

import nz.co.logicons.tlp.mobile.stobyapp.domain.model.Manifest;
import nz.co.logicons.tlp.mobile.stobyapp.domain.model.User;
import nz.co.logicons.tlp.mobile.stobyapp.network.model.ManifestDto;
import nz.co.logicons.tlp.mobile.stobyapp.network.model.ManifestItemDto;
import nz.co.logicons.tlp.mobile.stobyapp.network.model.UserDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/*
 * @author by Allen
 */
public interface RetroApiService {
    // GET retrofit endpoints here
//    @POST(Constants.LOGOUT_API_ACTION)
//    Call<List<List<RecyclerModel>>> logout( @Query("username")String username, @Query("password")String password);
    @POST("/transport/actions/stoby/login")
    Call<UserDto> getUser(@Body User user);

    //    @POST("/transport/actions/stoby/login")
//    UserDto checkUser(@Body User user);
    @POST("/transport/actions/stoby/allocated-manifest")
    Call<List<ManifestDto>> getAllocatedManifest(@Query("username") String username,
            @Query("password") String password);

    @POST("/transport/actions/stoby/allocated-manifest-items")
    Call<List<ManifestItemDto>> getAllocatedManifestItems(@Body Manifest manifest,
            @Query("username") String username,
            @Query("password") String password);
}
