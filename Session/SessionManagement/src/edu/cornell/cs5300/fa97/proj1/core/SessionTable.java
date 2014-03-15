package edu.cornell.cs5300.fa97.proj1.core;

import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class SessionTable extends ConcurrentHashMap<String, CustomSession>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7277059863170333557L;

	/**
	 * Gets the session corresponding to sessionID
	 * @param sessionID
	 * @return CustomSession object
	 */
	private CustomSession getSession(String sessionID)
	{
		CustomSession customSession = new CustomSession();
		if(get(sessionID) !=null)
		{
			customSession = get(sessionID);
		}
		return customSession;
		
	}
	
	/**
	 * 
	 * Get the cookie corresponding to session
	 * @param sessionID
	 * @return CustomCookie
	 */
	public CustomCookie getSessionCookie(String sessionID)
	{
		CustomSession customSession = getSession(sessionID);
		return customSession.getCookie();
	}
	
	/**
	 * Creates new session
	 * @return CustomSession
	 */
	private CustomSession createSession()
	{
		
		CustomSession customSession = new CustomSession();
		put(customSession.getSessionID(),customSession);
		return customSession;
	}

	/**
	 * Create new session, public facing, return cookie instead of session
	 * @return CustomCookie
	 */
	public CustomCookie createNewSession()
	{
		
		CustomSession customSession = createSession();
		return customSession.getCookie();
	}
	
	/**
	 * Updates the session state value, refreshes the session
	 * @param sessionID
	 * @param newSessionState
	 * @return CustomCookie
	 */
	public CustomCookie updateSession(String sessionID, String newSessionState) 
	{
		CustomSession customSession = getSession(sessionID);
		customSession.updateSessionState(newSessionState);
		customSession.refresh();
		return customSession.getCookie();
	}

	/**
	 * Invalidates session- sets cookie age 0 and removes from session table
	 * @param sessionID
	 * @return CustomCookie
	 */
	public CustomCookie invalidate(String sessionID) 
	{
		
		CustomSession customSession = getSession(sessionID);
		customSession.invalidate();
		CustomCookie customCookie = customSession.getCookie();
		remove(sessionID);
		return customCookie;
		
	}

	/**
	 * Refreshes the session. Resets cookie age and increments version number
	 * @param sessionID
	 * @return
	 */
	public CustomCookie refreshSession(String sessionID) 
	{
		CustomSession customSession = getSession(sessionID);
		customSession.refresh();
		return customSession.getCookie();
		
	}

	/**
	 * Getter for session state
	 * @param sessionID
	 * @return sessionState
	 */
	public String getSessionState(String sessionID) 
	{
		CustomSession customSession = getSession(sessionID);
		return customSession.getSessionState();
	}
	
	/**
	 * Getter for expirationTimeStAMP
	 * @param sessionID
	 * @return ExpirationTimeStamp
	 */
	public Date getExpirationTimeStamp(String sessionID) 
	{
		CustomSession customSession = getSession(sessionID);
		return customSession.getExpirationTimeStamp();
	}
	
	/**
	 * Daemon call this to remove the expired sessions.
	 * Checks the table for session with past expiration time stamp
	 */
	public void cleanExpiredSessions()
	{
		Iterator<Entry<String, CustomSession>> it = this.entrySet().iterator();
		while(it.hasNext())
		{
			Entry<String, CustomSession> tableEntry = it.next();
			if(tableEntry.getValue().getExpirationTimeStamp().before(new Date()))
			{
				it.remove();
			}
		}
	}
}
