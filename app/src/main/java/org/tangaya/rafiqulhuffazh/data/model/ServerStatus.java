package org.tangaya.rafiqulhuffazh.data.model;

public class ServerStatus {

    private int num_workers_available;

    private int num_requests_processed;

    public int getNumWorkerAvailable() {
        return num_workers_available;
    }

    public int getRequestsProcessed() {
        return num_requests_processed;
    }
}
