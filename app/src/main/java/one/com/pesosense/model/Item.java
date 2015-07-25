package one.com.pesosense.model;

/**
 * Created by Marc Lim on 7/8/15.
 */
public class Item {


    int id, ratings;
    String province, name, price, product_image, description, brand, inventory;


    public Item(int id, String province, String name, String price, String product_image, String description, int ratings, String brand, String inventory) {
        this.id = id;
        this.province = province;
        this.name = name;
        this.price = price;
        this.product_image = product_image;
        this.description = description;
        this.ratings = ratings;
        this.brand = brand;
        this.inventory = inventory;

    }

    public int getId() {
        return id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public int getRatings() {
        return ratings;
    }


    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }


    public String getInventory() {
        return inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }


}
