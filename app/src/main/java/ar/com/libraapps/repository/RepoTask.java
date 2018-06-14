package ar.com.libraapps.repository;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

import ar.com.libraapps.entities.Task;

public class RepoTask {


    private static  RepoTask instance;
    private Context contexto;

    private Dao<Task,Long> dao;

    public RepoTask(Context contexto) {
        OrmLiteSqliteOpenHelper helper= OpenHelperManager.getHelper(contexto, DataBaseHelper.class);
        this.contexto = contexto;
        try{
            dao = helper.getDao(Task.class);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static RepoTask getInstance(Context contexto){
        if(instance ==null){
            instance = new RepoTask(contexto);
        }
        return instance;
    }

    public void save(Task c) throws Exception{
        dao.create(c);
    }
    public void update (Task c) throws Exception{
        dao.update(c);
    }

    public Task getTasksById(long id) throws Exception{
        return dao.queryForId(id);
    }

    public List<Task> getAllTaskss()throws Exception{
        return dao.queryForAll();

        //return null;
    }

    public void deleteTasksById(long id) throws Exception{
        RepoPeriod.getInstance(this.contexto).detelePeriodsByTaskId(id);
        dao.deleteById(id);
    }

    public List<Task> getTasksByState(String state) throws SQLException {
        QueryBuilder<Task,Long> qb = dao.queryBuilder();
        qb.where().eq("state", state);

        return qb.query();

    }

}
