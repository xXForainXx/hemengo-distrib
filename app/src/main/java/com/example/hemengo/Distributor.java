package com.example.hemengo;

import android.icu.text.SimpleDateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class Distributor {
    private final int id;
    private final double lat;
    private final double lng;
    private String lastScanDate;
    private JSONObject qrCodeData;

    Distributor(int id, double lat, double lng) {
        this.id = id;
        this.lat = lat;
        this.lng = lng;
    }

    public String getLastScanDate() {
        return this.lastScanDate;
    }

    public void scan() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        this.lastScanDate = formatter.format(date);
    }

    public void setQrCodeData() throws JSONException {
        this.qrCodeData = new JSONObject();
        this.qrCodeData.put("id", this.id);
        this.qrCodeData.put("lat", this.lat);
        this.qrCodeData.put("lng", this.lng);
    }

    public JSONObject getQrCodeData() {
        return this.qrCodeData;
    }
}
