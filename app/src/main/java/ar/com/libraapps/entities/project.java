package ar.com.libraapps.entities;

import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "tareas")
public class project {

    private long id;
    private String name;

    public project() {}

    public project(String name) {
        this.name = name;
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
}
