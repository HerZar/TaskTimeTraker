package ar.com.libraapps.utils;

import android.content.Context;

import java.util.Calendar;

import ar.com.libraapps.entities.Period;
import ar.com.libraapps.entities.Task;
import ar.com.libraapps.repository.RepoPeriod;
import ar.com.libraapps.repository.RepoTask;

public class TaskItemButtons {
    
    
    /**Method btnStartStop
     * this method contains the functionality to be executed inside 
     * the onclick method of the button btnStartStop of the task Item view
     * */
    public static void btnStartStop(Task task, Context context){
        // defino su funcion dependiendo del estado de la tarea.
        switch (task.getState()){
            case 'D':
                try {
                    task.setState('I');
                    RepoTask.getInstance(context).update(task);
                    RepoPeriod.getInstance(context).save(new Period(Calendar.getInstance(),null,task.getId()));

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 'I':
                try {
                    task.setState('D');
                    RepoTask.getInstance(context).update(task);
                    Period auxperiod = RepoPeriod.getInstance(context).getLastPeriodByTaskID(task.getId());
                    auxperiod.setEnd(Calendar.getInstance());
                    RepoPeriod.getInstance(context).update(auxperiod);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 'T':
                break;
            default:
                break;
        }
    }

    /**Method btnStartStop
     * this method contains the functionality to be executed inside 
     * the onclick method of the button btnStartStop of the task Item view
     **/
    public static void btnEndRestart(Task task, Context context){
        // defino su funcion dependiendo del estado de la tarea.
        switch (task.getState()){
            case 'D':
                try {
                    task.setState('T');
                    RepoTask.getInstance(context).update(task);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 'I':
                try {
                    task.setState('T');
                    RepoTask.getInstance(context).update(task);
                    Period auxperiod = RepoPeriod.getInstance(context).getLastPeriodByTaskID(task.getId());
                    auxperiod.setEnd(Calendar.getInstance());
                    RepoPeriod.getInstance(context).update(auxperiod);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 'T':
                try {
                    task.setState('D');
                    RepoTask.getInstance(context).update(task);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }  
    }
}
