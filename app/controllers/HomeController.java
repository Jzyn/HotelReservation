package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class HomeController extends Controller {

    // Declare a private FormFactory instance
    private FormFactory formFactory;

    //  Inject an instance of FormFactory it into the controller via its constructor
    @Inject
    public HomeController(FormFactory f) {
        this.formFactory = f;
    }

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public Result rooms(Long hot) {
        List<Hotel> hotelsList = Hotel.findAll();
        List<Room> roomsList = new ArrayList<Room>();

        if (hot == 0) {
            roomsList = Room.findAll();
        }
        else {
            roomsList = Hotel.find.ref(hot).getRooms();
        }

        return ok(rooms.renderroomsList, hotelsList));
    }
}

