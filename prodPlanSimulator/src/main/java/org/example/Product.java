package org.example;

class Product {

    //Only to be used in the next Sprint
    private final String code;
    private final String name;
    private final String description;

    public Product(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return code + " - " + name + ": " + description;
    }
}
