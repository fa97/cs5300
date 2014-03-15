package edu.cornell.cs5300.fa97.proj1.core;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class CustomSession implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1764947627517752214L;
	private String sessionID;
	private String sessionState;
	private String sessionVersion;
	private Date creationTime;
	private Date lastAccessedTime;
	private Date expirationTimeStamp;
	private CustomCookie cookie;
	private static int DEFAULT_COOKIE_AGE = 180;
	
	public CustomSession()
	{
		
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
     
		creationTime = now;
		lastAccessedTime = now;
		cal.add(Calendar.SECOND, DEFAULT_COOKIE_AGE);
		expirationTimeStamp = cal.getTime();
		//Set session_id, keep only numeric value in string
		sessionID = UUID.randomUUID().toString().replaceAll("[^\\d.]", "");;
		sessionState = "Hello, User!";
		
		sessionVersion = "1";
		
		LocationData[] location = {};
		//create cookie
		createCookie(sessionID, sessionVersion, location);
	}
	
	public void invalidate()
	{
		cookie.setMaxAge(0);
	}
	
	/**
	 * Refresh method call. Sets new expiration time stamp, sets cookie max age, incrementes version number
	 */
	public void refresh() 
	{
		Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, DEFAULT_COOKIE_AGE);
		expirationTimeStamp = cal.getTime();
		sessionVersion = getNewVersionNumber();
		cookie.setVersion(cookie.getVersion()+1);
		cookie.setMaxAge(DEFAULT_COOKIE_AGE);
	}
	
	//Sets version number of session
	private String getNewVersionNumber() 
	{
		Integer versionNum = Integer.parseInt(sessionVersion);
		versionNum++;
		String newSessionVersionNumber = versionNum.toString();
		return newSessionVersionNumber;
	}

	//Calls create cookie constructor
	public void createCookie(String sessionID, String sessionVersion, LocationData[] location)
	{
		cookie = new CustomCookie(sessionID, sessionVersion, 1, location,DEFAULT_COOKIE_AGE,"CS5300PROJ1");
		
	}
	
	public CustomCookie getCookie()
	{
		return cookie;
	}
	
	//Updates the session state
	public void updateSessionState(String newSessionState) 
	{
		this.sessionState = newSessionState;
	}
	
	
	public String getSessionID() {
		return sessionID;
	}

	public String getSessionState() {
		return sessionState;
	}

	public String getSessionVersion() {
		return sessionVersion;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public Date getLastAccessedTime() {
		return lastAccessedTime;
	}

	public Date getExpirationTimeStamp() {
		return expirationTimeStamp;
	}

}
