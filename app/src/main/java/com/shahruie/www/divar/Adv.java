package com.shahruie.www.divar;



/**
 * Created by samenta on 1/16/2017.
 */

public class Adv {

    private  String title;
    private  String description;
    private String date;
    private String id ;
    private String phone ;
    private String email;
    private  String category ;
    private String city;
    private  String adv_img;

    public Adv()
    {

    }

    public Adv(String title , String description , String date , String id , String phone , String email , String category, String  adv_img,String city)
    {
        this.title = title;
        this.description = description;
        this.date = date;
        this.id = id;
        this.phone = phone;
        this.email = email;
        this.category = category;
        this.adv_img = adv_img;
        this.city=city;
    }

    public  String getCity(){
        return city;
    }
    public void setCity(String city){
        this.city=city;
    }
    public String getTitle()
    {
        return title;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return  description;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }
    public String getDate()
    {
        return  date;
    }
    public void setDate(String date)
    {
        this.date = date;
    }
    public String getId()
    {
        return  id;
    }
    public void setId(String id)
    {
        this.id = id;
    }
    public String getPhone()
    {
        return  phone;
    }
    public void setPhone(String phone)
    {
        this.phone = phone;
    }
    public String getEmail()
    {
        return  email;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }
    public String getCategory()
    {
        return  category;
    }
    public void setCategory(String category)
    {
        this.category = category;
    }

    public String getAdv_img(){
        return adv_img;
    }
    public void setAdv_img(String adv_img)
    {
        this.adv_img = adv_img;

    }
}
