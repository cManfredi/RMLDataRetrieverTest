package com.manfredi.main;

import java.io.IOException;

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
 * Servlet implementation class UpdateData
 */
@WebServlet("/UpdateData")
public class UpdateData extends HttpServlet { 
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateData() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String dsType = request.getParameter("ds");
		// ID utente
		String id = "prova-" + dsType;
		//Test recupero informazioni di profilo e scrittura su file
		IDataSource ds;
		try {
			if(dsType.equals("fitbit")){
				ds = new FitbitDataSource();
			} else {
				ds = new NokiaHealthDataSource();
			}
			// Metodo checkAuth(String userId) per verificare se sono state salvate credenziali per questo utente
			if(ds.checkAuth(id)){
				// In caso di successo non è necessario richiedere una nuova autorizzazione
				String[] files = ds.updateAllData(id, System.currentTimeMillis() - 86400000);
				for(String file : files){
					response.getWriter().println("Info updated - written file " + file);					
				}
			} else {
				// In caso di fallimento il metodo buildAuthRequest costruisce l'URL per reindirizzare l'utente presso il provider
				response.sendRedirect(ds.buildAuthRequest(id, "http://localhost:8080/RMLDataRetrieverTest/AuthCallback?ds="+dsType));
			}
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
