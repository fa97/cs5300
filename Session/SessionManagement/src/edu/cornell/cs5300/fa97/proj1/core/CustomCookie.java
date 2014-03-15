package edu.cornell.cs5300.fa97.proj1.core;

import javax.servlet.http.Cookie;

public class CustomCookie extends Cookie
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6172020080503161970L;
	private String sessionID;
	private String versionNumber;
	private LocationData[] wqAddresses;
	
	public CustomCookie(String sessionID, String versionNumber, int version, LocationData[] wqAddresses, Integer maxAge, String name)
	{
		super(name, sessionID);
		this.sessionID = sessionID;
		this.versionNumber = versionNumber;
		this.setVersion(version);
		this.wqAddresses = wqAddresses;
		this.setMaxAge(maxAge);
	}
	
	public String getSessionID()
	{
		return sessionID;
	}
	public String getVersionNumber() 
	{
		return versionNumber;
	}
	public LocationData[] getWqAddresses() 
	{
		return wqAddresses;
	}

}
