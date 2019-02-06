package mypackage;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

import java.sql.*;

public class CarRentalNew extends HttpServlet {

  int cont = 0;

  public void doGet(HttpServletRequest req, HttpServletResponse res)
                    throws ServletException, IOException {
    res.setContentType("text/html");
    PrintWriter out = res.getWriter();
    String nombre = req.getParameter("name");
    cont ++;
    
    String model_vehicle, sub_model_vehicle;
    int dies_lloguer, num_vehicles;
    dies_lloguer = num_vehicles = 0;
    double descompte = 0.0;
    
    model_vehicle = req.getParameter("model_vehicle");
    sub_model_vehicle = req.getParameter("sub_model_vehicle");
    dies_lloguer = Integer.parseInt(req.getParameter("dies_lloguer"));
    num_vehicles = Integer.parseInt(req.getParameter("num_vehicles"));
    descompte = Double.parseDouble(req.getParameter("descompte"));
    
    JSONObject obj = new JSONObject();
        obj.put("model_vehicle", model_vehicle);
        obj.put("sub_model_vehicle", sub_model_vehicle);
		obj.put("dies_lloguer", new Integer(dies_lloguer));
        obj.put("num_vehicles", new Integer(num_vehicles));
        obj.put("descompte", new Double(descompte));
    out.println("<html><body><h4>Your new rental has been added successfully!</h4><br><br><h6>model_vehicle: " + model_vehicle + "</h6><h6>sub_model_vehicle: " + sub_model_vehicle + "</h6><h6>dies_lloguer: " + dies_lloguer + "</h6><h6>num_vehicles: " + num_vehicles + "</h6><h6>descompte: " + descompte + "</h6><br><a href='carrental_form_new.html'>New rental</a><br><a href='carrental_home.html'>Home</a><br></body></html>");
    
    /*  //saving data in a mysql database  
      
      Class.forName(com.mysql.jdbc.Driver);
      Connection connection = DriverManager.getConnection ("jdbc:mysql://localhost:3306/rentals", "root", "admin");
      Statement s = connection.createStatement();
      * String query = "INSERT INTO rentals VALUES ('1','" + model_vehicle + "', '" + sub_model_vehicle + "', '" + dies_lloguer + "', '" + num_vehicles + "', '" + descompte + "');
      ResultSet rs = s.executeQuery(query);
      connection.close();
      
     */

		FileWriter file = new FileWriter("/home/adri/Downloads/pti/lab1/apache-tomcat-9.0.5/webapps/my_webapp/db/Rental" + java.time.LocalDateTime.now() + ".json");
        
        try {
			file.write(obj.toJSONString());
		}
		catch(IOException e){
			e.printStackTrace();
			}
		finally {
			file.flush();
			file.close();
		}
    

  }

  public void doPost(HttpServletRequest req, HttpServletResponse res)
                    throws ServletException, IOException {
    doGet(req, res);
  }
}
