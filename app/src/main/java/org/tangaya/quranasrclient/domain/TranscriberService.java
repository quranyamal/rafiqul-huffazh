package org.tangaya.quranasrclient.domain;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadService;

import org.tangaya.quranasrclient.AndroidApplication;
import org.tangaya.quranasrclient.domain.model.Transcription;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import timber.log.Timber;

/**
 * Created by Rahman Adianto on 22-Apr-17.
 */

public class TranscriberService implements DecoderRepository {

    private RESTService service;
    private String userId;
    private Context mContext;
    private String mEndpoint;

    public TranscriberService(Application application) {

        mContext = application.getApplicationContext();
        mEndpoint = "http://192.168.1.217:8888/client/dynamic/";

//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        try {
//            userId = mAuth.getCurrentUser().getUid();
//        }
//        catch (NullPointerException e) {
//            Timber.e("Undefined UID");
//            userId = "undefined";
//        }

        userId = "quranyamal";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mEndpoint)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(RESTService.class);
    }

    @Override
    public String requestTranscriptionId(Transcription data) {

        Log.d("TransServ", "inside requestTranscriptionId emthod");

        String transcriptionId = null;
        Call<Map<String, String>> call = service.saveTranscriptionInfo(userId, data);
        try {
            Map<String, String> response = call.execute().body();
            if (response.get("status").equals("OK")) {
                transcriptionId = response.get("transcription_id");
            }
            Timber.d("Got transcription id");
            Log.d("TS", "Got transcription id");
        }
        catch (IOException e) {
            Timber.e(e.getMessage());
        }


        return transcriptionId;
    }

    @Override
    public boolean saveFCMToken(String fcm) {
        boolean fcmSaved = false;

        Map<String, String> data = new HashMap<>();
        data.put("fcm", fcm);
        Call<Map<String, String>> call = service.saveFCMToken(userId, data);

        try {
            Map<String, String> response = call.execute().body();
            if (response.get("status").equals("OK")) {
                fcmSaved = true;
            }
            Timber.d("FCM Token saved on server");
        }
        catch (IOException e) {
            Timber.e(e.getMessage());
        }

        return fcmSaved;
    }

    public boolean uploadAudio(Transcription transcription) {

        Log.d("TransServ", "inside uploadAudio method");

        UploadService.NAMESPACE = "org.tangaya.quranasrclient";

        boolean isStart = false;
        try {
            MultipartUploadRequest service =
                    new MultipartUploadRequest(mContext, mEndpoint + "audio");

            UploadNotificationConfig config = new UploadNotificationConfig();
            config.setTitleForAllStatuses("Transcriber");

            transcription.local_uri = "/storage/emulated/0/DCIM/1-1.wav";

            service.addHeader("User-Id", userId);
            service.addHeader("name", transcription.name);
            service.addHeader("about", transcription.about);
            service.addHeader("date", transcription.date);
            service.addHeader("local_uri", transcription.local_uri);
            service.addHeader("num_speaker", String.valueOf(transcription.num_speaker));
            service.addHeader("diarization", String.valueOf(transcription.diarization));
            service.addHeader("speaker_list", transcription.speaker_list);

            Uri uri = Uri.fromFile(new File(transcription.local_uri.replace("\\ ", " ")));

            Timber.d("Local URI = " + uri.toString());
            Log.d("TransServ","Local URI = " + uri.toString());

            service.addFileToUpload(uri.getPath(), "audiofile");
            service.setUtf8Charset();
            service.setNotificationConfig(config);
            service.setMaxRetries(2);
            service.startUpload();
            Timber.d("Uploading audio file to server..");

            Log.d("TransServ","Uploading audio file to server..");
            isStart = true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.d("upload...", e.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("upload...", e.toString());
        }

        Log.d("TransServ","end of uploadAudio. isStart:" + isStart);

        return isStart;
    }

    private interface RESTService {

        @Headers({
                "Refs-Type: transcription_info",
                "Content-Type: application/json"
        })
        @POST("reference")
        Call<Map<String, String>> saveTranscriptionInfo(@Header("User-Id") String userId,
                                                        @Body Transcription data);

        @Headers({
                "Refs-Type: fcm_token",
                "Content-Type: application/json"
        })
        @POST("reference")
        Call<Map<String, String>> saveFCMToken(@Header("User-Id") String userId,
                                               @Body Map<String, String> fcm);
    }
}
