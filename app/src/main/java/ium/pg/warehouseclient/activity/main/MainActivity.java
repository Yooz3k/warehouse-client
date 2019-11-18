package ium.pg.warehouseclient.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import ium.pg.warehouseclient.R;
import ium.pg.warehouseclient.rest.RequestController;

import static ium.pg.warehouseclient.OAuthTokens.WEB_CLIENT_TOKEN;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RC_SIGN_IN = 1337;
    private final RequestController requestController = new RequestController();
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setUpButtons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.log_in_google_button) {
            signIn();
        }
    }

    private void setUpButtons() {
        setUpLogInButton();
        setUpGoogleLogInButton();
    }

    private void setUpLogInButton() {
        Button logInButton = findViewById(R.id.log_in_button);
        logInButton.setOnClickListener(view -> {
            EditText inputLogin = this.findViewById(R.id.log_in_login_input);
            String login = String.valueOf(inputLogin.getText());

            EditText inputPassword = this.findViewById(R.id.log_in_password_input);
            String password = String.valueOf(inputPassword.getText());

            requestController.login(login, password, this);
        });
    }

    private void setUpGoogleLogInButton() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(WEB_CLIENT_TOKEN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        /*GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (account != null) {
            Toast.makeText(getApplicationContext(),
                    "User already logged in with Google!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, WarehouseManagementActivity.class);
        startActivity(intent);
        }*/

        SignInButton logInGoogleButton = findViewById(R.id.log_in_google_button);
        logInGoogleButton.setOnClickListener(this);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();

            requestController.verifyGoogleToken(idToken, this);
        } catch (ApiException e) {
            Log.w("EXCEPTION", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(getApplicationContext(),
                    "Error while signing in with Google!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
