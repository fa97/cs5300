package edu.cornell.cs5300.fa97.proj1.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.cornell.cs5300.fa97.proj1.core.CustomCookie;
import edu.cornell.cs5300.fa97.proj1.core.CustomSession;
import edu.cornell.cs5300.fa97.proj1.core.SessionTable;

/**
 * Servlet implementation class SessionManagementServlet
 */
@WebServlet("")
public class SessionManagementServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    SessionTable sessionTable = new SessionTable();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SessionManagementServlet() {
        super();
        startSessionCleanupDaemon();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{

		Cookie[] cookies =  request.getCookies();
		String msg = "";
		CustomCookie customCookie;
		//Get the session id corresponding from the cookie
		String sessionID = getSessionID(cookies);
		
		//if session is not present create a new cookie
		if(sessionID == null)
		{
			customCookie = sessionTable.createNewSession();
			sessionID = customCookie.getSessionID();
		}
		//if session is present then get the cookie corresponding to session from session table,refresh session
		else
		{
			customCookie = sessionTable.getSessionCookie(sessionID);
			sessionTable.refreshSession(sessionID);
			
		}
		
		//get the session state for the session
		msg = sessionTable.getSessionState(sessionID);
		//add the cookie to request, add other values to be displayed
		response.addCookie(customCookie);
		request.setAttribute("message", msg);
		String cookie_value = customCookie.getVersion() +"_"+customCookie.getValue();
		request.setAttribute("cookie_value", cookie_value);
		String formattedDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(sessionTable.getExpirationTimeStamp(sessionID));
		request.setAttribute("expiration_timestamp", formattedDate);
		RequestDispatcher view = request.getRequestDispatcher("customsession.jsp");
		view.forward(request, response);

	}

	private String getSessionID(Cookie[] cookies)
	{
		
		String sessionID = null;
		if(cookies!=null)
		{
			for(int i =0 ; i<cookies.length;i++)
			{
				Cookie cookie = cookies[i];
				//Get the cookie corresponding to this name
				if(cookie.getName().equals("CS5300PROJ1"))
				{
					sessionID = cookie.getValue();
					break;
				}
			
			}
		}
		
		
		return sessionID;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
		CustomCookie customCookie;
		Cookie[] cookies =  request.getCookies();
		String sessionID = getSessionID(cookies);
		String msg = "";
		String path = request.getParameter("buttonValue");
		if(sessionID !=null)
		{
			customCookie = sessionTable.getSessionCookie(sessionID);
			//If Refresh is clicked, call a method that refreshes cookie
			if(path.equals("Refresh"))
			{
				sessionTable.refreshSession(sessionID);
			}
			//If Replace is clicked, call a method that updates session state
			else if(path.equals("Replace"))
			{
				String newSessionState = request.getParameter("message");
				customCookie = sessionTable.updateSession(sessionID, newSessionState);
				
			}
			//If logout is called, call a method that set max age to 0 and deletes session from session table
			else if(path.equals("Logout"))
			{
				customCookie = sessionTable.invalidate(sessionID);
				sessionID=null;
			}
		}
		else
		{
			customCookie = sessionTable.createNewSession();
			sessionID = customCookie.getSessionID();
		}
		//Set the dispaly values if session is not null(logout)
		if(sessionID!=null)
		{
			msg = sessionTable.getSessionState(sessionID);
			String cookie_value = customCookie.getVersion() +"_"+customCookie.getValue();
			request.setAttribute("cookie_value", cookie_value);
			String formattedDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(sessionTable.getExpirationTimeStamp(sessionID));
			request.setAttribute("expiration_timestamp", formattedDate);
		}
		request.setAttribute("message", msg);	
		response.addCookie(customCookie);
	
		RequestDispatcher view = request.getRequestDispatcher("customsession.jsp");
		view.forward(request, response);
	}
	
	//Daemon scheduled to run every 3minutes to remove the invalid session
	private void startSessionCleanupDaemon(){
		Thread sessionCleanupDaemon = new Thread(new Runnable() {
			@Override
			public void run(){
				while(true){
					sessionTable.cleanExpiredSessions();
					try {
						Thread.sleep(1000*60*3);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		sessionCleanupDaemon.setDaemon(true);
		sessionCleanupDaemon.start();
	}
}
