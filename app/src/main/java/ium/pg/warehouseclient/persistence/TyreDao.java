package ium.pg.warehouseclient.persistence;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import ium.pg.warehouseclient.domain.Tyre;

@Dao
interface TyreDao {

    @Query("SELECT * FROM tyre")
    Tyre[] getAll();

    @Query("SELECT * FROM tyre WHERE id = :id")
    Tyre findById(long id);

    @Insert
    long insert(Tyre tyre);

    @Update
    void update(Tyre... tyres);

    @Query("DELETE FROM tyre")
    void deleteAll();
}
