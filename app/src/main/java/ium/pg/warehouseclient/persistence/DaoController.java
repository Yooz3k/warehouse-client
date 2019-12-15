package ium.pg.warehouseclient.persistence;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import androidx.appcompat.app.AlertDialog;
import ium.pg.warehouseclient.activity.show.ShowActivity;
import ium.pg.warehouseclient.domain.Tyre;
import ium.pg.warehouseclient.rest.RequestController;
import lombok.NonNull;

public class DaoController {

    private final Activity activity;
    private TyreDao dao;

    public DaoController(Activity activity) {
        this.activity = activity;
        this.dao = AppDatabase.getDb(activity).tyreDao();
    }

    public void synchronize() {
        List<Tyre> allTyres = Arrays.asList(dao.getAll());
        // Sending only those tyres which where modified
        // (quantityChange field doesn't affect lastModified field)
        List<Tyre> modifiedTyres = allTyres.stream()
                .filter(tyre -> tyre.getLastModified() != null || tyre.getQuantityChange() != 0)
                .collect(Collectors.toList());

        RequestController requestController = new RequestController();
        requestController.synchronize(modifiedTyres, activity);
    }

    public void overrideWithSynchronized(List<Tyre> synchronizedTyres) {
        deleteAll();
        synchronizedTyres.forEach(tyre -> dao.insert(tyre));
    }

    public void findById(long id) {
        Tyre tyre = dao.findById(id);
        if (tyre != null && !tyre.getDeleted()) {
            showResult(tyre);
        } else {
            tyreNotFound(id);
        }
    }

    public void findAll() {
        List<Tyre> existingTyres = Arrays.stream(dao.getAll())
                .filter(tyre -> !tyre.getDeleted())
                .collect(Collectors.toList());

        List<Tyre> deletedTyres = Arrays.stream(dao.getAll())
                .filter(Tyre::getDeleted)
                .collect(Collectors.toList());

        StringBuilder tyresInfo = new StringBuilder();
        if (!existingTyres.isEmpty()) {
            tyresInfo.append(existingTyres.stream()
                    .map(Tyre::toString)
                    .collect(Collectors.joining("\n")));
        }
        if (!deletedTyres.isEmpty()) {
            tyresInfo.append("\nDELETED:\n");
            tyresInfo.append(deletedTyres.stream()
                    .map(Tyre::toString)
                    .collect(Collectors.joining("\n")));
        }
        if (tyresInfo.toString().isEmpty()) {
            tyresInfo.append("No tyres found!");
        }

        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle("Tyres");
        alertDialog.setMessage(tyresInfo.toString());
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }

    public void add(@NonNull Tyre tyre) {
        tyre.setToBeAdded(true);
        tyre.setLastModified(LocalDateTime.now());

        long addedId = dao.insert(tyre);
        showResult(dao.findById(addedId));
    }

    public void modify(@NonNull Tyre modifiedTyre) {
        Tyre savedTyre = dao.findById(modifiedTyre.getId());
        if (savedTyre != null) {
            modifiedTyre.setLastModified(LocalDateTime.now());

            int quantityChange = savedTyre.getQuantityChange() + (modifiedTyre.getQuantity() - savedTyre.getQuantity());
            modifiedTyre.setQuantityChange(quantityChange);

            dao.update(modifiedTyre);
            showResult(dao.findById(modifiedTyre.getId()));
        } else {
            tyreNotFound(modifiedTyre.getId());
        }
    }

    public void changeQuantity(long id, int change) {
        Tyre tyre = dao.findById(id);
        if (tyre == null) {
            tyreNotFound(id);
            return;
        }

        int newQuantity = tyre.getQuantity() + change;
        if (newQuantity < 0) {
            Toast.makeText(activity.getApplicationContext(),
                    "Quantity cannot be negative!", Toast.LENGTH_LONG).show();
        } else {
            tyre.setQuantity(newQuantity);
            modify(tyre);
        }
    }

    public void delete(long id) {
        Tyre tyre = dao.findById(id);
        if (tyre == null) {
            tyreNotFound(id);
            return;
        }

        tyre.setDeleted(true);
        tyre.setLastModified(LocalDateTime.now());

        dao.update(tyre);
        Toast.makeText(activity.getApplicationContext(),
                "Tyre deleted successfully", Toast.LENGTH_LONG).show();
    }

    private void deleteAll() {
        dao.deleteAll();
    }

    private void showResult(@NonNull Tyre tyre) {
        Intent intent = new Intent(activity, ShowActivity.class);
        intent.putExtra("result", tyre);
        activity.startActivity(intent);
    }

    private void tyreNotFound(long tyreId) {
        Toast.makeText(activity.getApplicationContext(),
                "Tyre with ID " + tyreId + " was not found!", Toast.LENGTH_LONG).show();
    }
}
