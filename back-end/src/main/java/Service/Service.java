package service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.*;
import dao.*;
import model.*;
import spark.Request;
import spark.Response;

public class Service{

    private UserDAO userDAO;

    public Service(){
        userDAO = new UserDAO();
    }

    public Object add(Request request, Response response) {
        String nome = request.queryParams("nome");
        String id = request.queryParams("id");
		User user = new User(id, nome);

		if(userDAO.createUser(user)){
            response.status(201);
        }else {
        	response.status(500);
        }
		return id;
	}

    public Object get(Request request, Response response) {
		String id = (request.params(":id"));
		
		User user = (User) userDAO.getUser(id);
		
		if (user != null) {
    	    response.header("Content-Type", "application/xml");
    	    response.header("Content-Encoding", "UTF-8");

            return "\n<user>\n" + 
            		"\t<id>" + user.getId() + "</id>\n" +
                    "\t<nome>" + user.getNome() + "</nome>\n" +
            		"</user>\n";
        } else {
            response.status(404); // 404 Not found
            return "User " + id + " no encontrado.";
        }

	}
} 