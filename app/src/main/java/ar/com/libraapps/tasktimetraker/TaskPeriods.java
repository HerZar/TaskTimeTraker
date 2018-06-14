package ar.com.libraapps.tasktimetraker;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

import ar.com.libraapps.entities.Period;
import ar.com.libraapps.entities.Task;
import ar.com.libraapps.repository.RepoPeriod;
import ar.com.libraapps.repository.RepoTask;
import ar.com.libraapps.utils.CalendarUtils;
import ar.com.libraapps.utils.PeriodAdapter;
import ar.com.libraapps.utils.TaskItemButtons;

public class TaskPeriods extends AppCompatActivity {

    private long taskID;
    private TextView tvTask;
    private TextView tvTaskStartDate;
    private TextView tvTaskStartHour;
    private TextView tvTaskEndDate;
    private TextView tvTaskEndHour;
    private TextView tvTaskState;
    private TextView tvExpendedTime;
    private ListView lvPeriodsList;
    private Button btnStartStop;
    private Button btnEndRestart;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_periods);

        taskID = getIntent().getExtras().getLong("taskID");

        Task task = null;
        try {
            task = RepoTask.getInstance(getApplicationContext()).getTasksById(taskID);
        } catch (Exception e) {
            e.printStackTrace();
            onBackPressed();
        }
        btnStartStop = (Button) findViewById(R.id.btnStartStop);
        btnEndRestart = (Button) findViewById(R.id.btnEndRestart);
        tvTask = (TextView) findViewById(R.id.tvTask);
        tvTaskStartDate = (TextView) findViewById(R.id.tvTaskStartDate);
        tvTaskStartHour = (TextView) findViewById(R.id.tvTaskStartHour);
        tvTaskEndDate = (TextView) findViewById(R.id.tvTaskEndDate);
        tvTaskEndHour = (TextView) findViewById(R.id.tvTaskEndHour);
        tvTaskState = (TextView) findViewById(R.id.tvTaskState);
        tvExpendedTime = (TextView) findViewById(R.id.tvExpendedTime);
        lvPeriodsList = (ListView) findViewById(R.id.lvPeriodsList);

        final Task finalTask = task;
        btnStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaskItemButtons.btnStartStop(finalTask,getApplicationContext());
                cargarTask(finalTask);
                updateList(finalTask, getApplicationContext());

            }
        });

        btnEndRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaskItemButtons.btnEndRestart(finalTask, getApplicationContext());
                cargarTask(finalTask);
                updateList(finalTask, getApplicationContext());

            }
        });

        cargarTask(task);
        cargeList(task, getApplicationContext());

    }

    private void cargarTask(Task task){
        
        //defino el formato con el que se mostraran las fechas y las horas.
        SimpleDateFormat dia = new SimpleDateFormat(getApplicationContext().getResources().getString(R.string.formatdate));
        SimpleDateFormat hora = new SimpleDateFormat(getApplicationContext().getResources().getString(R.string.formathour));
        //Cargo el nombre de la tarea
        tvTask.setText(task.getName());

        //cargo el primeri dia y horario de la tarea.
        Period first = RepoPeriod.getInstance(getApplicationContext()).getFirstPeriodByTaskID(task.getId());
        if (first!=null){
            tvTaskStartDate.setText(dia.format(first.getStart().getTime()));
            tvTaskStartHour.setText(hora.format(first.getStart().getTime()));
        }else {
            tvTaskStartDate.setText("-");
            tvTaskStartHour.setText("-");
        }

        //cargo el ultimo dia y horario de la tarea.
        Period last = RepoPeriod.getInstance(getApplicationContext()).getLastPeriodByTaskID(task.getId());
        if (last!=null && task.getState()=='T'){
            tvTaskEndDate.setText(dia.format(last.getEnd().getTime()));
            tvTaskEndHour.setText(hora.format(last.getEnd().getTime()));
        }else {
            tvTaskEndDate.setText("-");
            tvTaskEndHour.setText("-");
        }

        //Cargo el Estado.
        switch (task.getState()){
            case 'D': tvTaskState.setText(getApplicationContext().getResources().getString(R.string.stoped));
                break;
            case 'I': tvTaskState.setText(getApplicationContext().getResources().getString(R.string.started));
                break;
            case 'T': tvTaskState.setText(getApplicationContext().getResources().getString(R.string.ended));
                break;
            default: tvTaskState.setText("-");
                break;
        }

        
        //Cargo el titulo de accion del boton en base al estado de la tarea.
        switch (task.getState()){
            case 'D':
                btnStartStop.setVisibility(View.VISIBLE);
                btnStartStop.setText(getApplicationContext().getResources().getString(R.string.start));
                btnEndRestart.setText(getApplicationContext().getResources().getString(R.string.end));
                break;
            case 'I':
                btnStartStop.setVisibility(View.VISIBLE);
                btnStartStop.setText(getApplicationContext().getResources().getString(R.string.stop));
                btnEndRestart.setText(getApplicationContext().getResources().getString(R.string.end));
                break;
            case 'T':
                btnStartStop.setVisibility(View.INVISIBLE);
                btnEndRestart.setText(getApplicationContext().getResources().getString(R.string.restart));
                break;
            default:
                btnStartStop.setVisibility(View.VISIBLE);
                btnStartStop.setText("-");
                btnEndRestart.setText("-");
                break;
        }

        long timeExpended= 0l;
        try {
            timeExpended= CalendarUtils.getTotalExpendedTime(task,getApplicationContext());
        } catch (SQLException e) {
            timeExpended= 0l;
            e.printStackTrace();
        }
        tvExpendedTime.setText(CalendarUtils.getExpandedTime(timeExpended));


    }

    private void cargeList(Task task, Context contexto){
        List<Period> periodList = null;
        try {
            periodList = RepoPeriod.getInstance(contexto).getPeriodsBytaskID(task.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        final PeriodAdapter periodAdapter = new PeriodAdapter(periodList);
        lvPeriodsList.setAdapter(periodAdapter);
    }

    private void updateList(Task task, Context contexto){
        List<Period> periodList = null;
        try {
            periodList = RepoPeriod.getInstance(contexto).getPeriodsBytaskID(task.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ((PeriodAdapter)lvPeriodsList.getAdapter()).setPeriodList(periodList);
        ((PeriodAdapter)lvPeriodsList.getAdapter()).notifyDataSetChanged();

    }

}
