package id.ac.umn.zapplemobileapp.apihelper;

public class UtilsApi {
    public static final String BASE_URL_API = "https://zapple.lumiere.my.id/api/";

    // Mendeklarasikan Interface BaseApiService
    public static BaseApiService getAPIService(){
        return RetrofitClient.getClient(BASE_URL_API).create(BaseApiService.class);
    }
}
