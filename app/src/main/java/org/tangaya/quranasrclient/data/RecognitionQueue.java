package org.tangaya.quranasrclient.data;

import java.util.LinkedList;

public class RecognitionQueue {

    LinkedList<RecognitionTask> recognitionTasks = new LinkedList<>();

    public void enqueue(RecognitionTask recognitionTask) {
        recognitionTasks.add(recognitionTask);
    }

    public void dequeue() {
        RecognitionTask recognitionTask = recognitionTasks.poll();
        recognitionTask.execute();
    }

    public boolean remove(Object o) {
        recognitionTasks.remove(o);
        return false;
    }

    public int size() {
        return recognitionTasks.size();
    }

}
