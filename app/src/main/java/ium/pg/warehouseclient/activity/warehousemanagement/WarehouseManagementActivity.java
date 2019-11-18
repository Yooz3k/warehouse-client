package ium.pg.warehouseclient.activity.warehousemanagement;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import ium.pg.warehouseclient.R;
import ium.pg.warehouseclient.activity.main.ButtonInitializer;

public class WarehouseManagementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.warehouse_management);

        setUpButtons();
    }

    private void setUpButtons() {
        new ButtonInitializer(this).execute();
    }
}
