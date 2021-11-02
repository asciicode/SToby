package nz.co.logicons.tlp.mobile.stobyapp.network;

import java.util.List;

import nz.co.logicons.tlp.mobile.stobyapp.domain.model.ActionManifestItem;
import nz.co.logicons.tlp.mobile.stobyapp.domain.model.Manifest;
import nz.co.logicons.tlp.mobile.stobyapp.domain.model.User;
import nz.co.logicons.tlp.mobile.stobyapp.network.model.ActionManifestItemDto;
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
    @POST("/transport/actions/stoby/login")
    Call<UserDto> getUser(@Body User user);

    @POST("/transport/actions/stoby/allocated-manifest")
    Call<List<ManifestDto>> getAllocatedManifest(@Body Manifest manifest,
            @Query("username") String username,
            @Query("password") String password);

    @POST("/transport/actions/stoby/allocated-manifest-items")
    Call<List<ManifestItemDto>> getAllocatedManifestItems(@Body Manifest manifest,
            @Query("username") String username,
            @Query("password") String password);

    @POST("/transport/actions/stoby/check-make-manifest-item")
    Call<ActionManifestItemDto> checkMakeManifestItem(@Body ActionManifestItem actionManifestItem,
            @Query("username") String username,
            @Query("password") String password);

    @POST("/transport/actions/stoby/remove-make-manifest-item")
    Call<ActionManifestItemDto> removeMakeManifestItem(@Body ActionManifestItem actionManifestItem,
            @Query("username") String username,
            @Query("password") String password);

    @POST("/transport/actions/stoby/load-complete-manifest")
    Call<ActionManifestItemDto> loadCompleteActionManifestItem(@Body ActionManifestItem actionManifestItem,
            @Query("username") String username,
            @Query("password") String password);

    @POST("/transport/actions/stoby/complete-inward-manifest")
    Call<ActionManifestItemDto> completeInwardManifest(@Body ActionManifestItem actionManifestItem,
            @Query("username") String username,
            @Query("password") String password);

    @POST("/transport/actions/stoby/save-fcm-token")
    Call<Void> saveFcmToken(@Query("fcmToken") String fcmToken,
            @Query("username") String username,
            @Query("password") String password);

    @POST("/transport/actions/stoby/available-manifest")
    Call<List<ManifestDto>> getAvailableManifest(@Body Manifest manifest,
            @Query("username") String username,
            @Query("password") String password);

    @POST("/transport/actions/stoby/assign-manifest")
    Call<List<ManifestDto>> assignPersonToManifest(@Body Manifest manifest,
            @Query("username") String username,
            @Query("password") String password);
}
