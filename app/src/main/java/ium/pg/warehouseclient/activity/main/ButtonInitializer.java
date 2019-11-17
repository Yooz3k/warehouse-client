package ium.pg.warehouseclient.activity.main;

import android.app.Activity;
import android.content.Intent;
import android.widget.Button;

import ium.pg.warehouseclient.R;
import ium.pg.warehouseclient.activity.add.AddActivity;
import ium.pg.warehouseclient.activity.changequantity.ChangeQuantityActivity;
import ium.pg.warehouseclient.activity.deletebyid.DeleteByIdActivity;
import ium.pg.warehouseclient.activity.findbyid.FindByIdActivity;
import ium.pg.warehouseclient.activity.modify.ModifyActivity;
import ium.pg.warehouseclient.rest.RequestController;

class ButtonInitializer {

    private final Activity activity;
    private final RequestController requestController = new RequestController();

    ButtonInitializer(Activity activity) {
        this.activity = activity;
    }

    void execute() {
        setUpButtonShowAll();
        setUpButtonShowSingle();
        setUpButtonAdd();
        setUpButtonModify();
        setUpButtonChangeQuantity();
        setUpButtonDelete();
    }

    private void setUpButtonShowAll() {
        //TODO: odczyt tablicy, nie pojedynczego elementu JSON
        Button button = activity.findViewById(R.id.button_show_all);
        button.setOnClickListener(view -> requestController.getAll(activity));
    }

    private void setUpButtonShowSingle() {
        Button button = activity.findViewById(R.id.button_show_by_id);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(activity, FindByIdActivity.class);
            activity.startActivity(intent);
        });
    }

    private void setUpButtonAdd() {
        Button button = activity.findViewById(R.id.button_add);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(activity, AddActivity.class);
            activity.startActivity(intent);
        });
    }

    private void setUpButtonModify() {
        Button button = activity.findViewById(R.id.button_modify);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(activity, ModifyActivity.class);
            activity.startActivity(intent);
        });
    }

    private void setUpButtonChangeQuantity() {
        Button button = activity.findViewById(R.id.button_change_quantity);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(activity, ChangeQuantityActivity.class);
            activity.startActivity(intent);
        });

    }

    private void setUpButtonDelete() {
        Button button = activity.findViewById(R.id.button_delete);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(activity, DeleteByIdActivity.class);
            activity.startActivity(intent);
        });
    }
}
