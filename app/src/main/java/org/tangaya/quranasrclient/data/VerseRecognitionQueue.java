package org.tangaya.quranasrclient.data;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class VerseRecognitionQueue implements Queue<VerseRecognitionTask> {

    LinkedList<VerseRecognitionTask> recognitionTasksList = new LinkedList<>();

    @Override
    public int size() {
        return recognitionTasksList.size();
    }

    @Override
    public boolean isEmpty() {
        return recognitionTasksList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return recognitionTasksList.contains(o);
    }

    @NonNull
    @Override
    public Iterator<VerseRecognitionTask> iterator() {
        return recognitionTasksList.iterator();
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @NonNull
    @Override
    public <T> T[] toArray(@NonNull T[] a) {
        return null;
    }

    @Override
    public boolean add(VerseRecognitionTask verseRecognitionTask) {
        recognitionTasksList.add(verseRecognitionTask);
        return false;
    }

    @Override
    public boolean remove(Object o) {
        recognitionTasksList.remove(o);
        return false;
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends VerseRecognitionTask> c) {
        return false;
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean offer(VerseRecognitionTask verseRecognitionTask) {
        return false;
    }

    @Override
    public VerseRecognitionTask remove() {
        return null;
    }

    @Override
    public VerseRecognitionTask poll() {
        VerseRecognitionTask task = recognitionTasksList.poll();
        task.execute();
        return null;
    }

    @Override
    public VerseRecognitionTask element() {
        return null;
    }

    @Override
    public VerseRecognitionTask peek() {
        return null;
    }

    public static void main(String[] args) {
        VerseRecognitionQueue queue = new VerseRecognitionQueue();

    }
}
