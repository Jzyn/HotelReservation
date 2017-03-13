package controllers;

import controllers.security.AuthAdmin;
import controllers.security.Secured;
import play.data.Form;
import play.data.FormFactory;
import play.db.ebean.Transactional;
import play.mvc.*;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import views.html.admin.*;
import models.*;
import models.users.User;

// Require Login
@Security.Authenticated(Secured.class)
// Authorise user (check if admin)
@With(AuthAdmin.class)
public class AdminController extends Controller {

    // Declare a private FormFactory instance
    private FormFactory formFactory;

    //  Inject an instance of FormFactory it into the controller via its constructor
    @Inject
    public AdminController(FormFactory f) {
        this.formFactory = f;
    }

    // Method returns the logged in user (or null)
    private User getUserFromSession() {
        return User.getUserById(session().get("email"));
    }


    public Result rooms(Long hot) {

        // Get list of all genres in ascending order
        List<Hotel> hotelsList = Hotel.findAll();
        List<Room> roomsList = new ArrayList<Room>();

        if (hot == 0) {
            // Get list of all genres in ascending order
            roomsList = Room.findAll();
        }
        else {
            // Get movies for selected genre
            // Find genre first then extract movies for that cat.
            roomsList = Hotel.find.ref(hot).getRooms();
        }

        return ok(rooms.render(roomsList, hotelList, getUserFromSession()));
    }

    // Render and return  the add new movie page
    // The page will load and display an empty add movie form

    public Result addRoom() {

        // Create a form by wrapping the Movie class
        // in a FormFactory form instance
        Form<Room> addRoomForm = formFactory.form(Room.class);

        // Render the Add Movie View, passing the form object
        return ok(addRoom.render(addRoomForm, getUserFromSession()));
    }

    @Transactional
    public Result addRoomSubmit() {

        // Create a movie form object (to hold submitted data)
        // 'Bind' the object to the submitted form (this copies the filled form)
        Form<Room> newRoomForm = formFactory.form(Room.class).bindFromRequest();

        // Check for errors (based on Movie class annotations)
        if(newRoomForm.hasErrors()) {
            // Display the form again
            return badRequest(addRoom.render(newRoomForm, getUserFromSession()));
        }

        // Extract the movie from the form object
        Room r = newRoomForm.get();

        if (r.getId() == null) {
            // Save to the database via Ebean (remember Movie extends Model)
            r.save();
        }
        // Movie already exists so update
        else if (r.getId() != null) {
            r.update();
        }

        // Set a success message in temporary flash
        // for display in return view
        flash("success", "Room " + m.getId() + " has been created/ updated");

        // Redirect to the admin home
        return redirect(routes.AdminController.rooms(0));
    }

    // Update a pmovie by ID
    // called when edit button is pressed
    @Transactional
    public Result updateRoom(Long id) {

        Room r;
        Form<Room> roomForm;

        try {
            // Find the movie by id
            r = Room.find.byId(id);

            // Create a form based on the Movie class and fill using m
            roomForm = formFactory.form(Room.class).fill(r);

            } catch (Exception ex) {
                // Display an error message or page
                return badRequest("error");
        }
        // Render the updateMovie view - pass form as parameter
        return ok(addRoom.render(roomForm, getUserFromSession()));
    }

    // Delete Movie by id
    @Transactional
    public Result deleteRoom(Long id) {

        // find movie by id and call delete method
        Room.find.ref(id).delete();
        // Add message to flash session
        flash("success", "Room has been deleted");

        // Redirect to movies page
        return redirect(routes.AdminController.rooms(0));
    }

}
