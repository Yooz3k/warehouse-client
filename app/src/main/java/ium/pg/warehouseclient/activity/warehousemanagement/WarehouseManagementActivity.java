package ium.pg.warehouseclient.activity.warehousemanagement;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import ium.pg.warehouseclient.R;
import ium.pg.warehouseclient.activity.main.ButtonInitializer;
import ium.pg.warehouseclient.auth.LogOutHandler;

public class WarehouseManagementActivity extends AppCompatActivity {

    private final LogOutHandler logOutHandler = new LogOutHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.warehouse_management);

        setUpButtons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_warehouse_management, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            logOutHandler.handle();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpButtons() {
        new ButtonInitializer(this).execute();
    }
}
