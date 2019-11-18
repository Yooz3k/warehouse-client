package ium.pg.warehouseclient.activity.modify;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import ium.pg.warehouseclient.R;
import ium.pg.warehouseclient.domain.Tyre;
import ium.pg.warehouseclient.rest.RequestController;

public class ModifyActivity extends AppCompatActivity {

    private final RequestController requestController = new RequestController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify);

        Button button = this.findViewById(R.id.modify_modify_tyre_button);
        button.setOnClickListener(view -> {
            Tyre tyre = getTyre();
            if (tyre != null) {
                requestController.modify(getTyre(), this);
            } else {
                Toast.makeText(this, "All fields have to be filled", Toast.LENGTH_LONG).show();
            }
        });
    }

    private Tyre getTyre() {
        try {
            EditText inputId = this.findViewById(R.id.modify_id_input);
            long id = Long.valueOf(String.valueOf(inputId.getText()));

            EditText inputProducer = this.findViewById(R.id.modify_producer_input);
            String producer = String.valueOf(inputProducer.getText());

            EditText inputModel = this.findViewById(R.id.modify_model_input);
            String model = String.valueOf(inputModel.getText());

            EditText inputRimSize = this.findViewById(R.id.modify_rim_size_input);
            int rimSize = Integer.valueOf(String.valueOf(inputRimSize.getText()));

            EditText inputPrice = this.findViewById(R.id.modify_price_input);
            double price = Double.valueOf(String.valueOf(inputPrice.getText()));

            EditText inputQuantity = this.findViewById(R.id.modify_quantity_input);
            int quantity = Integer.valueOf(String.valueOf(inputQuantity.getText()));

            return Tyre.builder()
                    .id(id)
                    .producer(producer)
                    .name(model)
                    .rimSize(rimSize)
                    .price(price)
                    .quantity(quantity)
                    .build();
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
