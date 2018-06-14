package ar.com.libraapps.tasktimetraker;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import ar.com.libraapps.entities.Task;
import ar.com.libraapps.repository.RepoTask;
import ar.com.libraapps.utils.TaskAdapter;


public class MainActivity extends AppCompatActivity {

    private Dialog dialogoAgregar = null;
    private Dialog dialogoOpciones = null;
    private TextView tvEmptyMessage  = null;
    private ListView lvTaskList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Creo el dialogo para crear o editar una tarea.
        dialogoAgregar = new Dialog(MainActivity.this, R.style.Theme_AppCompat_Light_Dialog);
        //quito el titulo de la aplicacion al dialogo.
        dialogoAgregar.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //agrego un layout al dialogo
        dialogoAgregar.setContentView(R.layout.dialog_taskeditor);

        //Creo el dialogo para opciones de edicion o eliminacion.
        dialogoOpciones = new Dialog(MainActivity.this, R.style.Theme_AppCompat_Light_Dialog);
        //quito el titulo de la aplicacion al dialogo.
        dialogoOpciones.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //agrego un layout al dialogo
        dialogoOpciones.setContentView(R.layout.dialog_options);

        //Cargo los elementos del contenido de la actividad main.
        tvEmptyMessage= findViewById(R.id.tvEmptyMesage);
        lvTaskList=findViewById(R.id.lvtasklist);

        //Cargo la lista y

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                //Llamo al metodo createOrEditTask con id -1 (como no existe el id -1, crea una task nuevo).
                createOrEditTask(-1l);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createOrEditTask( long id){

        Task task=null;
        try {
            task = RepoTask.getInstance(getApplicationContext()).getTasksById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final EditText etTaskName = (EditText) dialogoAgregar.findViewById(R.id.etTaskName);
        Button btnCancelar = (Button) dialogoAgregar.findViewById(R.id.btncancelar);
        Button btnAceptar = (Button) dialogoAgregar.findViewById(R.id.btnaceptar);

        if (task != null){
            etTaskName.setText(task.getName());
        } else{
            etTaskName.setText("");
        }
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogoAgregar.dismiss();
            }
        });
        final Task finalTask = task;
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!etTaskName.getText().toString().isEmpty()) {
                        if (finalTask !=null){
                            finalTask.setName(etTaskName.getText().toString());
                            RepoTask.getInstance(MainActivity.this).update(finalTask);
                        }else{
                            RepoTask.getInstance(MainActivity.this).save(new Task(etTaskName.getText().toString(),'D'));
                        }
                        resumeList(MainActivity.this);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                dialogoAgregar.dismiss();
            }

        });

        dialogoAgregar.show();

    }


    @Override
    protected void onResume() {
        try {
            resumeList(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        lvTaskList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final long itemID = lvTaskList.getAdapter().getItemId(position);

                Button eliminar = (Button) dialogoOpciones.findViewById(R.id.btnDelete);
                eliminar.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    try {
                                                        RepoTask.getInstance(MainActivity.this).deleteTasksById(itemID);
                                                        //Toast.makeText(MainActivity.this, MainActivity.this.getResources().getString(R.string.la_reserva_ha_sido_eliminada), Toast.LENGTH_SHORT);
                                                        dialogoOpciones.dismiss();
                                                        resumeList(MainActivity.this);


                                                    } catch (Exception e) {
                                                        //Toast.makeText(MainActivity.this, MainActivity.this.getResources().getString(R.string.error_al_borrar_el_registro), Toast.LENGTH_LONG);
                                                    }
                                                }
                                            }
                );


                Button editar = (Button) dialogoOpciones.findViewById(R.id.btnEdit);
                editar.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {

                                                  try {
                                                      createOrEditTask(itemID);
                                                      dialogoOpciones.dismiss();
                                                      resumeList(MainActivity.this);
                                                  } catch (Exception e) {
                                                      e.printStackTrace();
                                                  }

                                              }
                                          }
                );
                dialogoOpciones.show();
                return true;
            }
        });
        lvTaskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final long taskId = lvTaskList.getAdapter().getItemId(position);
                Intent intent = new Intent(getApplicationContext(), TaskPeriods.class);
                intent.putExtra("taskID", taskId);
                startActivity(intent);
            }
        });
        super.onResume();
    }

    private void resumeList(Context contexto) throws Exception {
        List<Task> taskList = RepoTask.getInstance(contexto).getAllTaskss();
        final TaskAdapter taskAdapter = new TaskAdapter(taskList);
        lvTaskList.setAdapter(taskAdapter);
        if (lvTaskList.getAdapter().getCount() > 0) {
            tvEmptyMessage.setVisibility(View.GONE);
        }else {
            tvEmptyMessage.setVisibility(View.VISIBLE);
        }
    }

}
