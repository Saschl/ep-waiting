package com.example.sascha.myapplication.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainViewModel extends ViewModel {

    private MutableLiveData<List<WaitingTime>> waitTimes;
    public LiveData<List<WaitingTime>> getWaitTimes() {
            waitTimes = new MutableLiveData<>();
            fetchTimes();
        return waitTimes;
    }


    private void fetchTimes() {
        String secret = "ZhQCqoZp";
        Mac sha256_HMAC = null;

        try {
            sha256_HMAC = Mac.getInstance("HmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            Log.e("We won't get far here..",e.getMessage());
        }

        SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        try {
            sha256_HMAC.init(secret_key);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        String message = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            message = DateTimeFormatter.ofPattern("uuuuMMddHH").format(LocalDateTime.now(ZoneId.of("UTC")));
        } else {
            message = new SimpleDateFormat("yyyyMMddHH", Locale.UK).format(Calendar.getInstance().getTime());
        }

        String hash = bytesToHex(sha256_HMAC.doFinal(message.getBytes()));
        Log.i("HASH",hash);

        OkHttpClient yas = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("https://api.europapark.de/api-5.9/waitingtimes?token="+hash+"&mock=false")
                .build();

        yas.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    WaitingTime failure = new WaitingTime();
                    failure.setCode("0");
                    failure.setTime("0");
                    waitTimes.postValue(Collections.singletonList(failure));
                    throw new IOException("Unexpected code " + response);
                }

                //Log.i("APP", response.body().string());
                final String res = response.body().string();
                Gson gson = new Gson();
                final List<WaitingTime> times = gson.fromJson(res, new TypeToken<List<WaitingTime>>(){}.getType());
                waitTimes.postValue(times);

            }
        });
    }
    private String bytesToHex(byte[] bytes) {
        final  char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
