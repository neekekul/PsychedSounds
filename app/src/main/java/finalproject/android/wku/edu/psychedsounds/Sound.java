package finalproject.android.wku.edu.psychedsounds;
//LUKE KEEN
//5/8/2018
//This class specifies a sound object for my project.
public class Sound {

    //Each sound will have a String name, and a Integer id.
    private String name;

    private Integer id;

    //Each sound will be built using these two parameters.
    public Sound(String name, Integer id){
        this.name = name;
        this.id = id;
    }

    //getter method to pull the String name
    public String getName(){
        return name;
    }

    //getter method to pull the Integer id
    public Integer getId(){
        return id;
    }
}
