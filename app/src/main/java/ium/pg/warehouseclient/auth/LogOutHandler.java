package ium.pg.warehouseclient.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import ium.pg.warehouseclient.activity.main.MainActivity;
import ium.pg.warehouseclient.util.SharedPreferencesNames;
import lombok.RequiredArgsConstructor;

import static ium.pg.warehouseclient.OAuthTokens.WEB_CLIENT_TOKEN;

@RequiredArgsConstructor
public class LogOutHandler {

    private final Activity activity;

    public void handle() {
        deleteBearerToken(activity);
        signOutGoogleUser();
    }

    private void signOutGoogleUser() {
        if (GoogleSignIn.getLastSignedInAccount(activity) != null) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(WEB_CLIENT_TOKEN)
                    .requestEmail()
                    .build();
            GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
            mGoogleSignInClient.signOut().addOnCompleteListener(task -> startMainActivity());
        } else {
            startMainActivity();
        }
    }

    private void deleteBearerToken(final Activity activity) {
        SharedPreferences pref = activity.getSharedPreferences(SharedPreferencesNames.TOKENS_PREF_NAME, Context.MODE_PRIVATE);
        pref.edit()
                .remove(SharedPreferencesNames.BEARER_KEY)
                .commit();
    }

    private void startMainActivity() {
        Toast.makeText(activity.getApplicationContext(), "Logged out successfully!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }
}
