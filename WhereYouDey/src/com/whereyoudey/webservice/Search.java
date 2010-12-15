package com.whereyoudey.webservice;

public interface Search extends java.rmi.Remote {

    /**
     *
     */
    public String GetCityEvents(String city) throws java.rmi.RemoteException;

    /**
     *
     */
    public ArrayOfString SearchData_Ex(String locationID) throws java.rmi.RemoteException;

    /**
     *
     */
    public String IndexData() throws java.rmi.RemoteException;

    /**
     *
     */
    public String GetCityTheaters(String city) throws java.rmi.RemoteException;

    /**
     *
     */
    public ArrayOfString SearchData(String name, String location, ArrayOfString filter) throws java.rmi.RemoteException;

}
