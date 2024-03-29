package ium.pg.warehouseclient.activity.deletebyid;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import ium.pg.warehouseclient.R;
import ium.pg.warehouseclient.persistence.DaoController;

public class DeleteByIdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_by_id);

        setUpButton();
    }

    private void setUpButton() {
        Button button = this.findViewById(R.id.delete_by_id_button);
        button.setOnClickListener(view -> {
            EditText inputText = this.findViewById(R.id.delete_by_id_input);
            String text = String.valueOf(inputText.getText());
            long id = Long.valueOf(text);

            DaoController daoController = new DaoController(this);
            daoController.delete(id);
        });
    }
}
