package org.tangaya.quranasrclient.domain;

import java.util.Map;

public interface AuthRepository {

    interface AuthCallback {
        void onLoginSuccess(String email);
        void onLoginError(String message);
    }

    interface ResetPasswordCallback {
        void onResetRequestSent();
        void onResetFailed(String message);
    }

    void login(String email, String password, AuthCallback authCallback);
    void register(String email, String password, AuthCallback authCallback);
    void logout();
    Map<String, String> checkLoginStatus();
    void resetPassword(String email, ResetPasswordCallback passwordCallback);
}
