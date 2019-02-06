package mypackage;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;

import java.sql.*;

public class CarRentalList extends HttpServlet {

  int cont = 0;

  public void doGet(HttpServletRequest req, HttpServletResponse res)
                    throws ServletException, IOException {
    res.setContentType("text/html");
    PrintWriter out = res.getWriter();
    String nombre = req.getParameter("userid");
    cont ++;
    
	String password = req.getParameter("password");
	
	/* //using a mysql database (
	 
	 Class.forName(com.mysql.jdbc.Driver);
      Connection connection = DriverManager.getConnection ("jdbc:mysql://localhost:3306/rentals", "root", "admin");
      Statement s = connection.createStatement();
      String query1 = "SELECT username, password FROM users WHERE username = admin AND password = admin";
      ResultSet rs = s.executeQuery(query1);
      if(rs.next()) {
      
		out.println("<html><body><h4>List of all rentals</h4></body></html>");
		Statement s2 = connection.createStatement();
		String query2 = "SELECT * FROM rentals";
		ResultSet rs2 = s2.executeQuery(query2);
		while(rs.next()) {
			
			String model_vehicle = rs.getString(2);
			String sub_model_vehicle = rs.getString(3);
			int dies_lloguer = rs.getInt(4);
			int num_vehicles = rs.getInt(5);
			double descompte = rs.getDouble(6);
		    out.println("<html><body><br><h6>model_vehicle: " + model_vehicle + "</h6><h6>sub_model_vehicle: " + sub_model_vehicle + "</h6><h6>dies_lloguer: " + dies_lloguer + "</h6><h6>num_vehicles: " + num_vehicles + "</h6><h6>descompte: " + descompte + "</h6><br></body></html>");
		 }
		 
		 out.println("<html><body><br><a href='carrental_home.html'>Home</a><br></body></html>");

       }
	 
	 */
	
	if(nombre.equals("admin") && password.equals("admin")) {
		
		JSONParser parser = new JSONParser();
		
		File path = new File("/home/adri/Downloads/pti/lab1/apache-tomcat-9.0.5/webapps/my_webapp/db");
		File [] files = path.listFiles();
		
		out.println("<html><body><h4>List of all rentals</h4></body></html>");
		
		try {
			for(int i = 0; i < files.length; i++) {
				if(files[i].isFile()) {
					
					Object obj = parser.parse(new FileReader(files[i]));
					JSONObject jsonObject = (JSONObject) obj;
					String model_vehicle = (String) jsonObject.get("model_vehicle");
					String sub_model_vehicle = (String) jsonObject.get("sub_model_vehicle");
					long dies_lloguer = (Long) jsonObject.get("dies_lloguer");
					long num_vehicles = (Long) jsonObject.get("num_vehicles");
					double descompte = (Double) jsonObject.get("descompte");
					
					out.println("<html><body><br><h6>model_vehicle: " + model_vehicle + "</h6><h6>sub_model_vehicle: " + sub_model_vehicle + "</h6><h6>dies_lloguer: " + dies_lloguer + "</h6><h6>num_vehicles: " + num_vehicles + "</h6><h6>descompte: " + descompte + "</h6><br></body></html>");
					}
    
			}

		out.println("<html><body><br><a href='carrental_home.html'>Home</a><br></body></html>");
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
	}

	else {
		out.println("<html><body><h5>The userid/password introduced are not correct. Please try again!</h5><br><a href='carrental_form_list.html'>Login</a><br></body></html>");
		}
}

  public void doPost(HttpServletRequest req, HttpServletResponse res)
                    throws ServletException, IOException {
    doGet(req, res);
  }
}
