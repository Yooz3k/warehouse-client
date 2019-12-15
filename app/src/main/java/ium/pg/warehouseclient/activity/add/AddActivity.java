package ium.pg.warehouseclient.activity.add;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import ium.pg.warehouseclient.R;
import ium.pg.warehouseclient.domain.Tyre;
import ium.pg.warehouseclient.persistence.DaoController;

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);

        setUpButton();
    }

    private void setUpButton() {
        Button button = this.findViewById(R.id.add_add_new_button);
        button.setOnClickListener(view -> {
            Tyre tyre = getTyre();
            if (tyre != null) {
                DaoController daoController = new DaoController(this);
                daoController.add(tyre);
            } else {
                Toast.makeText(this, "All fields have to be filled", Toast.LENGTH_LONG).show();
            }
        });
    }

    private Tyre getTyre() {
        EditText inputProducer = this.findViewById(R.id.add_producer_input);
        String producer = String.valueOf(inputProducer.getText());

        EditText inputModel = this.findViewById(R.id.add_model_input);
        String model = String.valueOf(inputModel.getText());

        EditText inputRimSize = this.findViewById(R.id.add_rim_size_input);
        int rimSize = Integer.valueOf(String.valueOf(inputRimSize.getText()));

        EditText inputPrice = this.findViewById(R.id.add_price_input);
        double price = Double.valueOf(String.valueOf(inputPrice.getText()));

        return Tyre.builder()
                .producer(producer)
                .name(model)
                .rimSize(rimSize)
                .price(price)
                .build();
    }
}
