package org.tangaya.quranasrclient.service;

import android.os.AsyncTask;
import android.util.Log;

import org.tangaya.quranasrclient.data.Recording;
import org.tangaya.quranasrclient.data.Transcription;

public class Transcriber {

    TranscriptionRequest transcriptionRequest;

    public Transcriber() {
    }

    public Transcription getTranscription(Recording recording) {
        Log.d("Transcriber","getTranscription");
        transcriptionRequest = new TranscriptionRequest();
        transcriptionRequest.execute(recording);

        Log.d("Transcriber","end of getTranscription");
        return null;
    }

    public static class TranscriptionRequest extends AsyncTask<Recording, String, Boolean> {

        interface OnRecognitionCompleted {
            void onRecognitionCompleted();
            void onRecognitionCancelled();
        }

        private boolean isRunning;

        @Override
        protected Boolean doInBackground(Recording... recordings) {
            isRunning = true;

            return null;
        }

        @Override
        protected void onCancelled() {
            isRunning = false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            isRunning = false;
        }

        @Override
        protected void onProgressUpdate(String... values) {

        }
    }

}
