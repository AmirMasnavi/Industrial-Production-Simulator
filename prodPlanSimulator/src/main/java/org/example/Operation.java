package org.example;

public class Operation {
    private int id;
    private String name;

    // Constructor
    public Operation(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // toString method for easier printing
    @Override
    public String toString() {
        return "Operation{id=" + id + ", name='" + name + "'}";
    }

    // Equals and hashCode methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operation operation = (Operation) o;
        return id == operation.id && name.equals(operation.name);
    }

    @Override
    public int hashCode() {
        return 31 * id + name.hashCode();
    }
}

