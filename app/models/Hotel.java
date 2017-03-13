package models;

import java.util.*;
import javax.persistence.*;

import com.avaje.ebean.Model;
import play.data.validation.*;

// Product Entity managed by the ORM
@Entity
public class Hotel extends Model {

    // Properties
    // Annotate id as the primary key
    @Id
    private Long id;

    @Constraints.Required
    private String name;

    @Constraints.Required
    private String address;

    @OneToMany
    private List<Room> rooms;

    // Default constructor
    public Hotel() {

    }

    public Hotel(Long id, String name, String address, List<Room> rooms) {
        this.setId(id);
        this.setName(name);
        this.setAddress(address);
        this.setRooms(rooms);
    }

    //Generic query helper for entity Computer with id Long
    public static Finder<Long,Hotel> find = new Finder<Long,Hotel>(Hotel.class);

    //Find all Rooms in the database in ascending order by name
    public static List<Hotel> findAll() {
        return Hotel.find.where().orderBy("name asc").findList();
    }

    // Generate options for an HTML select control
    public static Map<String,String> options() {
        LinkedHashMap<String,String> options = new LinkedHashMap<>();

        // Get all genres from the DB and add to the options Hash map
        for(Hotel h: Hotel.findAll()) {
            options.put(h.getId().toString(), h.getName(), h.getAddress());
        }
        return options;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

   public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
}
