package ar.com.libraapps.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import ar.com.libraapps.entities.Period;
import ar.com.libraapps.entities.Task;


/**
 * Created by a211589 on 14/07/2017.
 */

public class DataBaseHelper extends OrmLiteSqliteOpenHelper {


    private static final String NOMBRE_DB= "TaskTimeTrakerDB";
    private static final int VERSION = 1;

    public DataBaseHelper(Context context) {
        super(context, NOMBRE_DB,null, VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try{
            //creo tablas de entidades
            TableUtils.createTable(connectionSource, Task.class);
            TableUtils.createTable(connectionSource, Period.class);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }
}
