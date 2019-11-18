package ium.pg.warehouseclient.activity.main;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import ium.pg.warehouseclient.R;
import ium.pg.warehouseclient.rest.RequestController;

public class MainActivity extends AppCompatActivity {

    private final RequestController requestController = new RequestController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button logInButton = findViewById(R.id.log_in_button);
        logInButton.setOnClickListener(view -> {
            EditText inputLogin = this.findViewById(R.id.log_in_login_input);
            String login = String.valueOf(inputLogin.getText());

            EditText inputPassword = this.findViewById(R.id.log_in_password_input);
            String password = String.valueOf(inputPassword.getText());

            requestController.login(login, password, this);
        });
        Button logInGoogleButton = findViewById(R.id.log_in_google_button);
        //Intent i przejscie do innego activity w przypadku sukcesu
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
}
