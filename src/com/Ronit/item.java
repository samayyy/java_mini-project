package com.Ronit;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

public class item {
    private static AtomicInteger ID_GENERATOR = new AtomicInteger(1);
    static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/uuuu");
    static LocalDate localDate = LocalDate.now();
    int id;
    String name;
    int quantity;
    int price;
    String date;

    public item( String name, int quantity, int price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;

    }

    public int getId() {
        return id;
    }

    public void setId() {
        id = ID_GENERATOR.getAndIncrement();

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate() {
        date = dtf.format(localDate);
    }
}
