package ium.pg.warehouseclient.activity.changequantity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import ium.pg.warehouseclient.R;
import ium.pg.warehouseclient.persistence.DaoController;

public class ChangeQuantityActivity extends AppCompatActivity {

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

            DaoController daoController = new DaoController(this);
            daoController.changeQuantity(id, change);
        });
    }
}
