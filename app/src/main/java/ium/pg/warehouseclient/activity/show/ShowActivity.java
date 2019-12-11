package ium.pg.warehouseclient.activity.show;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import ium.pg.warehouseclient.R;
import ium.pg.warehouseclient.activity.warehousemanagement.WarehouseManagementActivity;
import ium.pg.warehouseclient.domain.Tyre;

public class ShowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show);

        showTyreInfo();
    }

    private void showTyreInfo() {
        Tyre tyre = (Tyre) getIntent().getSerializableExtra("result");

        TextView id = findViewById(R.id.id_value);
        id.setText(String.valueOf(tyre.getId()));

        TextView producer = findViewById(R.id.producer_value);
        producer.setText(tyre.getProducer());

        TextView model = findViewById(R.id.model_value);
        model.setText(tyre.getName());

        TextView rimSize = findViewById(R.id.rim_size_value);
        rimSize.setText(String.valueOf(tyre.getRimSize()));

        TextView price = findViewById(R.id.price_value);
        price.setText(String.valueOf(tyre.getPrice()));

        TextView quantity = findViewById(R.id.quantity_value);
        quantity.setText(String.valueOf(tyre.getQuantity()));

        Button button = findViewById(R.id.back_button);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(this, WarehouseManagementActivity.class);
            startActivity(intent);
        });
    }
}
