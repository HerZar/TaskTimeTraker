package ar.com.libraapps.repository;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

import ar.com.libraapps.entities.Period;

public class RepoPeriod {


    private static RepoPeriod instance;

    private Dao<Period,Long> dao;

    public RepoPeriod(Context contexto) {
        OrmLiteSqliteOpenHelper helper= OpenHelperManager.getHelper(contexto, DataBaseHelper.class);
        try{
            dao = helper.getDao(Period.class);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static RepoPeriod getInstance(Context contexto){
        if(instance ==null){
            instance = new RepoPeriod(contexto);
        }
        return instance;
    }

    public void save(Period c) throws Exception{
        dao.create(c);
    }
    public void update (Period c) throws Exception{
        dao.update(c);
    }

    public Period getPeriodsById(long id) throws Exception{
        return dao.queryForId(id);
    }

    public List<Period> getAllPeriodss()throws Exception{
        return dao.queryForAll();

        //return null;
    }

    public void deletePeriodsById(long id) throws Exception{
        dao.deleteById(id);
    }

    public List<Period> getPeriodsBytaskID(long taskID) throws SQLException {
        QueryBuilder<Period,Long> qb = dao.queryBuilder();
        qb.orderBy("start",true);
        qb.where().eq("taskID", taskID);
        return qb.query();

    }

    public void detelePeriodsByTaskId(long taskID) throws SQLException {
        DeleteBuilder<Period,Long> db = dao.deleteBuilder();
        db.where().eq("taskID", taskID);
        db.delete();
    }

    public Period getFirstPeriodByTaskID(long taskID) {
        try {
            QueryBuilder<Period, Long> qb = dao.queryBuilder();
            qb.orderBy("start", true);
            qb.where().eq("taskID", taskID);
            List<Period> aux = qb.query();
            if (aux.size()>0) {
                return aux.get(0);
            }
            else{
                return null;
            }
        }catch (Exception e){
            return null;
        }

    }

    public Period getLastPeriodByTaskID(long taskID) {
        try {
            QueryBuilder<Period, Long> qb = dao.queryBuilder();
            qb.orderBy("start", true);
            qb.where().eq("taskID", taskID);
            List<Period> aux = qb.query();
            if (aux.size()>0) {
                return aux.get(aux.size()-1);
            }
            else{
                return null;
            }
        }catch (Exception e){
            return null;
        }

    }

}
