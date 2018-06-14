package ar.com.libraapps.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

import ar.com.libraapps.entities.Period;
import ar.com.libraapps.entities.Task;
import ar.com.libraapps.repository.RepoPeriod;
import ar.com.libraapps.tasktimetraker.R;

public class TaskAdapter extends BaseAdapter{

    private List<Task> taskList;

    public TaskAdapter(List<Task> taskList) {
        this.taskList = taskList;
    }

    @Override
    public int getCount() {
        return taskList.size();
    }

    @Override
    public Object getItem(int i) {
        return taskList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return taskList.get(i).getId();
    }

    @Override
    public View getView(int position, final View convertView, final ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_task, parent, false);
        final Task task = (Task) getItem(position);
        
        //Cargo todos los elementos de la lista.
        cargarVista(task, parent, view);
        
        Button btnStartStop = (Button) view.findViewById(R.id.btnStartStop);
        Button btnEndRestart = (Button) view.findViewById(R.id.btnEndRestart);

        btnStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaskItemButtons.btnStartStop(task,parent.getContext());
                notifyDataSetChanged();
            }
        });

        btnEndRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaskItemButtons.btnEndRestart(task, parent.getContext());
                notifyDataSetChanged();

            }
        });

        return view;

    }
    
    private void cargarVista(Task task, ViewGroup parent, View view){

        TextView tvTask = (TextView) view.findViewById(R.id.tvTask);
        TextView tvTaskStartDate = (TextView) view.findViewById(R.id.tvTaskStartDate);
        TextView tvTaskStartHour = (TextView) view.findViewById(R.id.tvTaskStartHour);
        TextView tvTaskEndDate = (TextView) view.findViewById(R.id.tvTaskEndDate);
        TextView tvTaskEndHour = (TextView) view.findViewById(R.id.tvTaskEndHour);
        TextView tvTaskState = (TextView) view.findViewById(R.id.tvTaskState);
        TextView tvExpendedTime = (TextView) view.findViewById(R.id.tvExpendedTime);


        //defino el formato con el que se mostraran las fechas y las horas.
        SimpleDateFormat dia = new SimpleDateFormat(parent.getContext().getResources().getString(R.string.formatdate));
        SimpleDateFormat hora = new SimpleDateFormat(parent.getContext().getResources().getString(R.string.formathour));
        //Cargo el nombre de la tarea
        tvTask.setText(task.getName());

        //cargo el primeri dia y horario de la tarea.
        Period first = RepoPeriod.getInstance(parent.getContext()).getFirstPeriodByTaskID(task.getId());
        if (first!=null){
            tvTaskStartDate.setText(dia.format(first.getStart().getTime()));
            tvTaskStartHour.setText(hora.format(first.getStart().getTime()));
        }else {
            tvTaskStartDate.setText("-");
            tvTaskStartHour.setText("-");
        }

        long timeExpended= 0l;
        try {
            timeExpended= CalendarUtils.getTotalExpendedTime(task,parent.getContext());
        } catch (SQLException e) {
            timeExpended= 0l;
            e.printStackTrace();
        }
        tvExpendedTime.setText(CalendarUtils.getExpandedTime(timeExpended));

        //cargo el ultimo dia y horario de la tarea.
        Period last = RepoPeriod.getInstance(parent.getContext()).getLastPeriodByTaskID(task.getId());
        if (last!=null && task.getState()=='T'){
            tvTaskEndDate.setText(dia.format(last.getEnd().getTime()));
            tvTaskEndHour.setText(hora.format(last.getEnd().getTime()));
        }else {
            tvTaskEndDate.setText("-");
            tvTaskEndHour.setText("-");
        }

        //Cargo el Estado.
        switch (task.getState()){
            case 'D': tvTaskState.setText(parent.getContext().getResources().getString(R.string.stoped));
                break;
            case 'I': tvTaskState.setText(parent.getContext().getResources().getString(R.string.started));
                break;
            case 'T': tvTaskState.setText(parent.getContext().getResources().getString(R.string.ended));
                break;
            default: tvTaskState.setText("-");
                break;
        }

        Button btnStartStop = (Button) view.findViewById(R.id.btnStartStop);
        Button btnEndRestart = (Button) view.findViewById(R.id.btnEndRestart);

        //Cargo el titulo de accion del boton en base al estado de la tarea.
        switch (task.getState()){
            case 'D':
                btnStartStop.setVisibility(View.VISIBLE);
                btnStartStop.setText(parent.getContext().getResources().getString(R.string.start));
                btnEndRestart.setText(parent.getContext().getResources().getString(R.string.end));
                break;
            case 'I':
                btnStartStop.setVisibility(View.VISIBLE);
                btnStartStop.setText(parent.getContext().getResources().getString(R.string.stop));
                btnEndRestart.setText(parent.getContext().getResources().getString(R.string.end));
                break;
            case 'T':
                btnStartStop.setVisibility(View.INVISIBLE);
                btnEndRestart.setText(parent.getContext().getResources().getString(R.string.restart));
                break;
            default:
                btnStartStop.setVisibility(View.VISIBLE);
                btnStartStop.setText("-");
                btnEndRestart.setText("-");
                break;
        }


    }
}
