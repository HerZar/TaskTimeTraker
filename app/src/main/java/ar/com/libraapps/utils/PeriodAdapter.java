package ar.com.libraapps.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import ar.com.libraapps.entities.Period;
import ar.com.libraapps.tasktimetraker.R;

public class PeriodAdapter extends BaseAdapter{

    private List<Period> periodList;

    public PeriodAdapter(List<Period> periodList) {
        this.periodList = periodList;
    }

    public List<Period> getPeriodList() {
        return periodList;
    }

    public void setPeriodList(List<Period> periodList) {
        this.periodList = periodList;
    }

    @Override
    public int getCount() {
        return periodList.size();
    }

    @Override
    public Object getItem(int i) {
        return periodList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return periodList.get(i).getId();
    }

    @Override
    public View getView(int position, final View convertView, final ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_period, parent, false);
        final Period period = (Period) getItem(position);
        
        //Cargo todos los elementos de la lista.
        cargarVista(period, parent, view);
        
        return view;

    }
    
    private void cargarVista(Period period, ViewGroup parent, View view){
        
        TextView tvPeriodStartDate = (TextView) view.findViewById(R.id.tvPeriodStartDate);
        TextView tvPeriodStartHour = (TextView) view.findViewById(R.id.tvPeriodStartHour);
        TextView tvPeriodEndDate = (TextView) view.findViewById(R.id.tvPeriodEndDate);
        TextView tvPeriodEndHour = (TextView) view.findViewById(R.id.tvPeriodEndHour);
        TextView tvExpendedTime = (TextView) view.findViewById(R.id.tvPeriodExpendedTime);


        //defino el formato con el que se mostraran las fechas y las horas.
        SimpleDateFormat dia = new SimpleDateFormat(parent.getContext().getResources().getString(R.string.formatdate));
        SimpleDateFormat hora = new SimpleDateFormat(parent.getContext().getResources().getString(R.string.formathour));
        //Cargo el nombre de la tarea

        //cargo el primeri dia y horario de la tarea.

        if (period.getStart()!=null){
            tvPeriodStartDate.setText(dia.format(period.getStart().getTime()));
            tvPeriodStartHour.setText(hora.format(period.getStart().getTime()));
        }else {
            tvPeriodStartDate.setText("-");
            tvPeriodStartHour.setText("-");
        }

        if (period.getEnd()!=null){
            tvPeriodEndDate.setText(dia.format(period.getEnd().getTime()));
            tvPeriodEndHour.setText(hora.format(period.getEnd().getTime()));
        }else {
            tvPeriodEndDate.setText("-");
            tvPeriodEndHour.setText("-");
        }

        tvExpendedTime.setText(CalendarUtils.getExpandedTime(period.getExpendedTime()));
    }


}
