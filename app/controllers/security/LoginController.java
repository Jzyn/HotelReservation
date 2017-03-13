package controllers.security;

import play.api.Environment;
import play.mvc.*;
import play.data.*;

import javax.inject.Inject;

import views.html.*;

// Import models
import models.users.*;
import models.*;


public class LoginController extends Controller {

    private FormFactory formFactory;

    private Environment env;

    @Inject
    public LoginController(Environment e, FormFactory f) {
        this.env = e;
        this.formFactory = f;
    }

    public Result login() {
        Form<Login> loginForm = formFactory.form(Login.class);

        return ok(login.render(loginForm, User.getUserById(session().get("email"))));
    }

    public Result loginSubmit() {
        Form<Login> loginForm = formFactory.form(Login.class).bindFromRequest();

        if(loginForm.hasErrors()){
            return badRequest(login.render(loginForm, User.getUserById(session().get("email"))));
        }else{
            session().clear();
            session("email", loginForm.get().getEmail());
        }

	User u = User.getUserById(session().get("email"));
	if (u.getRole().equals("admin")) {
		return redirect(controllers.routes.AdminController.movies(0));
	}
	else {
        return redirect(controllers.routes.HomeController.index());
	}
    }


    public Result logout() {
        session().clear();
        flash("success", "You've  been logged out");
        return redirect(routes.LoginController.login());
    }

}
