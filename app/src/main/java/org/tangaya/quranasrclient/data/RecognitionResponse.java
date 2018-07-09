package org.tangaya.quranasrclient.data;

import org.json.JSONException;
import org.json.JSONObject;

public class RecognitionResponse {

    String raw;
    JSONObject json;

    public RecognitionResponse(String text) {

        raw = text;

        try {
            json = new JSONObject(text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getStatus() throws JSONException {
        return json.getInt("status");
    }

    public boolean isTranscriptionFinal() {
        try {
            return json.getJSONObject("result").getBoolean("final");
        } catch (JSONException e) {
            //e.printStackTrace();
        }
        return false;
    }
}
