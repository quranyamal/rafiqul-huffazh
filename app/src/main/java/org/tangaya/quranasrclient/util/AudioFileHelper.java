package org.tangaya.quranasrclient.util;

import android.os.Environment;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import timber.log.Timber;

/**
 * Created by Rahman Adianto on 30-Apr-17.
 */

public class AudioFileHelper {

    public static String TRANSCRIBER_DIR = Environment.getExternalStorageDirectory() +
            File.separator + "Transcriber";

    private FileOutputStream mOuput;
    private File mRawFile;
    private File mWavFile;

    private int mSampleRate;

    public AudioFileHelper(String file, int sampleRate) {

        mSampleRate = sampleRate;

        File folder = new File(TRANSCRIBER_DIR);

        if (!folder.exists()) {
            folder.mkdirs();
        }

        try {
            mRawFile = new File(folder.getPath() + File.separator + file  + ".raw");
            mWavFile = new File(folder.getPath() + File.separator + file  + ".wav");
            mOuput = new FileOutputStream(mRawFile.getPath());
        }
        catch (FileNotFoundException e) {
            Timber.e(e.getMessage());
        }
    }

    public void pushToFile(byte[] data) {
        try {
            mOuput.write(data);
        }
        catch (IOException e) {
            Timber.e(e.getMessage());
        }
    }

    public void close() {
        try {
            mOuput.close();
        }
        catch (IOException e) {
            Timber.e(e.getMessage());
        }
    }

    public void clearRaw() {

        if (mRawFile.exists()) {
            if (mRawFile.delete()) {
                Timber.d(String.format("File deleted : %s", mRawFile.getPath()));
            }
            else {
                Timber.e(String.format("Failed delete file : %s", mRawFile.getPath()));
            }
        }
    }

    public void rawToWave16Mono() throws IOException {

        Timber.d("Convert raw to wav");

        byte[] rawData = new byte[(int) mRawFile.length()];
        DataInputStream input = null;
        try {
            input = new DataInputStream(new FileInputStream(mRawFile));
            input.read(rawData);
        }
        finally {
            if (input != null) {
                input.close();
            }
        }

        DataOutputStream output = null;
        try {
            output = new DataOutputStream(new FileOutputStream(mWavFile));
            // WAVE header
            writeString(output, "RIFF"); // chunk id
            writeInt(output, 36 + rawData.length); // chunk size
            writeString(output, "WAVE"); // format
            writeString(output, "fmt "); // subchunk 1 id
            writeInt(output, 16); // subchunk 1 size
            writeShort(output, (short) 1); // audio format (1 = PCM)
            writeShort(output, (short) 1); // number of channels
            writeInt(output, mSampleRate); // sample rate
            writeInt(output, mSampleRate * 2); // byte rate
            writeShort(output, (short) 2); // block align
            writeShort(output, (short) 16); // bits per sample
            writeString(output, "data"); // subchunk 2 id
            writeInt(output, rawData.length); // subchunk 2 size
            // Wav Body
            output.write(fullyReadFileToBytes(mRawFile));
        }
        finally {
            if (output != null) {
                output.close();
            }
        }
    }

    private byte[] fullyReadFileToBytes(File f) throws IOException {
        int size = (int) f.length();
        byte bytes[] = new byte[size];
        byte tmpBuff[] = new byte[size];
        FileInputStream fis = new FileInputStream(f);
        try {

            int read = fis.read(bytes, 0, size);
            if (read < size) {
                int remain = size - read;
                while (remain > 0) {
                    read = fis.read(tmpBuff, 0, remain);
                    System.arraycopy(tmpBuff, 0, bytes, size - remain, read);
                    remain -= read;
                }
            }
        }
        finally {
            fis.close();
        }

        return bytes;
    }

    private void writeInt(final DataOutputStream output, final int value)
            throws IOException {

        output.write(value);
        output.write(value >> 8);
        output.write(value >> 16);
        output.write(value >> 24);
    }

    private void writeShort(final DataOutputStream output, final short value)
            throws IOException {

        output.write(value);
        output.write(value >> 8);
    }

    private void writeString(final DataOutputStream output, final String value)
            throws IOException {

        for (int i = 0; i < value.length(); i++) {
            output.write(value.charAt(i));
        }
    }
}