package age.AgeClub.administrator.age_102;

import java.util.ArrayList;

/**
 * Created by Administrator on 9.06.2018.
 */

public class DiscountCategoriesSingleton {

    private static DiscountCategoriesSingleton instance;

    private ArrayList<Discount> categoryA; //Giyim
    private ArrayList <Discount> categoryB; //Eğlence
    private ArrayList <Discount> categoryC; //Sağlık
    private ArrayList <Discount> allCategories;

    private DiscountCategoriesSingleton(){}

    public static DiscountCategoriesSingleton getInstance() {
        if(instance == null){
            instance = new DiscountCategoriesSingleton();
        }
        return instance;
    }

    public void setInstance(){
        instance = this;
    }

    public ArrayList<Discount> getCategoryA() {
        return categoryA;
    }

    public void setCategoryA(ArrayList<Discount> categoryA) {
        this.categoryA = categoryA;
    }

    public ArrayList<Discount> getCategoryB() {
        return categoryB;
    }

    public void setCategoryB(ArrayList<Discount> categoryB) {
        this.categoryB = categoryB;
    }

    public ArrayList<Discount> getCategoryC() {
        return categoryC;
    }

    public void setCategoryC(ArrayList<Discount> categoryC) {
        this.categoryC = categoryC;
    }

    public ArrayList<Discount> getAllCategories() {
        return allCategories;
    }

    public void setAllCategories(ArrayList<Discount> allCategories) {
        this.allCategories = allCategories;
    }
}
