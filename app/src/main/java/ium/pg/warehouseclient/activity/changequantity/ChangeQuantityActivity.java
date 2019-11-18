package ium.pg.warehouseclient.activity.changequantity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import ium.pg.warehouseclient.R;
import ium.pg.warehouseclient.rest.RequestController;

public class ChangeQuantityActivity extends AppCompatActivity {

    private final RequestController requestController = new RequestController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_quantity);

        setUpButton();
    }

    private void setUpButton() {
        Button button = this.findViewById(R.id.change_quantity_button);
        button.setOnClickListener(view -> {
            EditText idInput = this.findViewById(R.id.change_quantity_id_input);
            String idText = String.valueOf(idInput.getText());
            long id = Long.valueOf(idText);

            EditText changeInput = this.findViewById(R.id.change_quantity_change_input);
            String changeText = String.valueOf(changeInput.getText());
            int change = Integer.valueOf(changeText);

            requestController.changeQuantity(id, change, this);
        });
    }
}
