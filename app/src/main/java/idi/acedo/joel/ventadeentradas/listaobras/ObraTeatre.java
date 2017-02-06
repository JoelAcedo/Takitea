package idi.acedo.joel.ventadeentradas.listaobras;

import android.graphics.Bitmap;

public class ObraTeatre {
    private String id;
    private String title;
    private String description;
    private int time_minutes;
    private double price;
    private String dates;
    private String img_path;
    private Bitmap image_bm;

    public ObraTeatre() {}

    public ObraTeatre(String id, String title, String description, int time_minutes, String dates) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.time_minutes = time_minutes;
        this.dates = dates;
    }

    public double getPrice() {
        return price;
    }

    public String getPriceString() {
        return String.valueOf(price) + " €";
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTime_minutes() {
        return time_minutes;
    }

    public String getTime() {
        return new String(String.valueOf(time_minutes)).concat(" minuts");
    }

    public void setTime_minutes(int time_minutes) {
        this.time_minutes = time_minutes;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public Bitmap getImage_bm() {
        return image_bm;
    }

    public void setImage_bm(Bitmap image_bm) {
        this.image_bm = image_bm;
    }
}
