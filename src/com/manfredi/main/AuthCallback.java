package com.manfredi.main;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration2.ex.ConfigurationException;

import com.manfredi.RMLDataRetriever.FitbitDataSource;
import com.manfredi.RMLDataRetriever.IDataSource;
import com.manfredi.RMLDataRetriever.NokiaHealthDataSource;

/**
 * Servlet implementation class AuthCallback
 */
@WebServlet("/AuthCallback")
public class AuthCallback extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AuthCallback() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String dsType = request.getParameter("ds");
		// ID utente
		String id = "prova-" + dsType;
		// E' necessario trasformare i parametri GET della richiesta in una HashMap 
		// da passare al metodo di salvataggio delle credenziali
		HashMap<String, String> params = new HashMap<>();
		Enumeration<String> paramNames = request.getParameterNames();
		while(paramNames.hasMoreElements()){
			String name = paramNames.nextElement();
			params.put(name, request.getParameter(name));
		}
		// Creazione DataSource
		IDataSource ds;
		try {
			switch(dsType){
				case "fitbit":
					ds = new FitbitDataSource();
					break;
				case "nokia":
					ds = new NokiaHealthDataSource();
					break;
				default:
					response.getWriter().println("Wrong data source type!");
					return;
			}
			ds.saveAuthResponse(id, params);
			response.sendRedirect("http://localhost:8080/RMLDataRetrieverTest/UpdateData?ds="+dsType);
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
