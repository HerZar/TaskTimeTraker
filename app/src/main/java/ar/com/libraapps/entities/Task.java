package ar.com.libraapps.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "tareas")
public class Task {

    @DatabaseField(generatedId = true)    // For Autoincrement)
    private long id;
    @DatabaseField
    private String name;
    @DatabaseField
    private char state;

    public Task() {}

    public Task(String name, char state) {
        this.name = name;
        this.state = state;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public char getState() {
        return state;
    }

    /**
     * Metodo para cargar el estado de una tarea.
     * precondicion: el estado a cargar debe ser uno de los siguientes
     * (el sistema no los valida)
     * Detenida = D;
     * Iniciada = I;
     * Terminada = T;
     * */
    public void setState(char state) {
        this.state = state;
    }
}
