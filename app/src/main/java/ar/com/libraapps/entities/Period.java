package ar.com.libraapps.entities;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Calendar;
import java.util.Date;

@DatabaseTable(tableName = "periodos")
public class Period {

    @DatabaseField(generatedId = true)    // For Autoincrement)
    private long id;
    @DatabaseField
    private Date start;
    @DatabaseField
    private Date end;
    @DatabaseField
    private long taskID;

    public Period() {
    }

    public Period(Calendar start, Calendar end, long taskID) {
        if (start!=null) {
            this.start = start.getTime();
        }else{
            this.start=null;
        }
        if (end != null){
            this.end = end.getTime();
        }else{
            this.end=null;
        }

        this.taskID = taskID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Calendar getStart() {

        Calendar aux = Calendar.getInstance();
        if (this.start != null) {
            aux.setTime(this.start);
        }else{
            aux= null;
        }
        return aux;
    }

    public void setStart(Calendar start) {
        this.start = start.getTime();
    }

    public Calendar getEnd() {
        Calendar aux = Calendar.getInstance();
        if (this.end != null) {
            aux.setTime(this.end);
        }else {
            aux = null;
        }
        return aux;
    }

    public void setEnd(Calendar end) {
        this.end = end.getTime();
    }

    public long getTaskID() {
        return taskID;
    }

    public void setTaskID(long taskID) {
        this.taskID = taskID;
    }

    public long getExpendedTime(){
        if (end!=null && start !=null) {
            return end.getTime() - start.getTime();
        }else{
            return 0l;
        }
    }


}
