package com.cochrane.clinton.bikecompanion;

/**
 * Created by Clint on 16/03/2017.
 */
public class Contact
	{
	private int id;
	private String name;
	private String phoneNumber;


	public Contact()
		{
			this(-1, "", "");
		}


	public Contact( int id, String name, String phoneNumber )
		{
			this.id = id;
			this.name = name;
			this.phoneNumber = phoneNumber;
		}


	public int getID()
		{
			return id;
		}


	public void setID( int id )
		{
			this.id = id;
		}


	public String getName()
		{
			return name;
		}


	public void setName( String name )
		{
			this.name = name;
		}


	public String getPhoneNumber()
		{
			return phoneNumber;
		}


	public void setPhoneNumber( String phoneNumber )
		{
			this.phoneNumber = phoneNumber;
		}
	}
