package ium.pg.warehouseclient.persistence;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import ium.pg.warehouseclient.domain.Tyre;
import ium.pg.warehouseclient.persistence.converter.LocalDateTimeConverter;

@Database(entities = {Tyre.class}, version = 1, exportSchema = false)
@TypeConverters({LocalDateTimeConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract TyreDao tyreDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDb(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "app_database")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
