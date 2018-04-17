package org.tangaya.quranasrclient.domain.model;

/**
 * Created by Rahman Adianto on 02-Apr-17.
 */

public class Transcription {

    public String key;
    public String name;
    public String about;
    public String date;
    public int num_speaker;
    public String local_uri;
    public boolean diarization;
    public String speaker_list;

    public Transcription() {}

    public Transcription(String name, String about, String date, int num_speaker,
                         String local_uri, boolean diarization, String speaker_list) {

        this.name = name;
        this.about = about;
        this.date = date;
        this.num_speaker = num_speaker;

        this.local_uri = local_uri;
        this.diarization = diarization;
        this.speaker_list = speaker_list;
    }
}
