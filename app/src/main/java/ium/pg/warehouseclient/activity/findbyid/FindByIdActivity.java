package ium.pg.warehouseclient.activity.findbyid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import ium.pg.warehouseclient.R;
import ium.pg.warehouseclient.rest.RequestController;

public class FindByIdActivity extends AppCompatActivity {

    private final RequestController requestController = new RequestController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_by_id);

        Button button = this.findViewById(R.id.find_by_id_button);
        button.setOnClickListener(view -> {
            EditText inputText = this.findViewById(R.id.find_by_id_input);
            String text = String.valueOf(inputText.getText());
            long id = Long.valueOf(text);

            requestController.getSingle(id, this);
        });
    }
}
