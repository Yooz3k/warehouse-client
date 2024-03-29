package ium.pg.warehouseclient.activity.findbyid;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import ium.pg.warehouseclient.R;
import ium.pg.warehouseclient.persistence.DaoController;

public class FindByIdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_by_id);

        setUpButton();
    }

    private void setUpButton() {
        Button button = this.findViewById(R.id.find_by_id_button);
        button.setOnClickListener(view -> {
            EditText inputText = this.findViewById(R.id.find_by_id_input);
            String text = String.valueOf(inputText.getText());
            long id = Long.valueOf(text);

            DaoController daoController = new DaoController(this);
            daoController.findById(id);
        });
    }
}
