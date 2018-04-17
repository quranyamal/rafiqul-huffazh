package org.tangaya.quranasrclient;

import org.tangaya.quranasrclient.domain.AbstractInteractor;
import org.tangaya.quranasrclient.domain.DecoderWSRepository;
import org.tangaya.quranasrclient.domain.DisconnectWSInteractor;
import org.tangaya.quranasrclient.domain.Executor;
import org.tangaya.quranasrclient.domain.MainThread;

/**
 * Created by Rahman Adianto on 11-Apr-17.
 */

public class DisconnectWSInteractorImpl extends AbstractInteractor
        implements DisconnectWSInteractor {

    DecoderWSRepository mRepository;

    public DisconnectWSInteractorImpl(Executor threadExecutor,
                                      MainThread mainThread,
                                      DecoderWSRepository repository) {

        super(threadExecutor, mainThread);

        mRepository = repository;
    }

    @Override
    public void run() {
        mRepository.disconnect();
    }
}
