package ar.com.libraapps.utils;

import android.content.Context;

import java.sql.SQLException;
import java.util.List;

import ar.com.libraapps.entities.Period;
import ar.com.libraapps.entities.Task;
import ar.com.libraapps.repository.RepoPeriod;

public class CalendarUtils {

    //private Context context;

    public static long getTotalExpendedTime(Task task, Context c) throws SQLException {
        long respuesta= 0;
        List<Period> periods = RepoPeriod.getInstance(c).getPeriodsBytaskID(task.getId());
        for (Period a: periods) {
            respuesta = respuesta + a.getExpendedTime();

        }
        return respuesta;
    }

    public static String getExpandedTime(long timeInMilis){


        long milis = timeInMilis%1000;
        long segundos = (timeInMilis/1000)%60;
        long minutos = ((timeInMilis/1000)/60)%60;
        long horas = (((timeInMilis/1000)/60)/60)%24;
        long dias = (((timeInMilis/1000)/60)/60)/24;
        String respuesta= dias+"d:"+horas+"h:"+minutos+"m:"+segundos+"s";
        return respuesta;

    }









}
