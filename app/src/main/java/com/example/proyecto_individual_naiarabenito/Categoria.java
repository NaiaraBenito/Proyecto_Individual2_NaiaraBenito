package com.example.proyecto_individual_naiarabenito;

public class Categoria {
    public String name;
    public String color;
    public int img_id;



    public Categoria(String name, String color, int img_id) {
        this.name = name;
        this.color = color;
        this.img_id = img_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getImg_id() {
        return img_id;
    }

    public void setImg_id(int img_id) {
        this.img_id = img_id;
    }
}
