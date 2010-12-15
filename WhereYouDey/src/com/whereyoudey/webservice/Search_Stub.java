package com.whereyoudey.webservice;

import javax.xml.rpc.JAXRPCException;
import javax.xml.namespace.QName;
import javax.microedition.xml.rpc.Operation;
import javax.microedition.xml.rpc.Type;
import javax.microedition.xml.rpc.ComplexType;
import javax.microedition.xml.rpc.Element;

public class Search_Stub implements Search, javax.xml.rpc.Stub {

    private String[] _propertyNames;
    private Object[] _propertyValues;

    public Search_Stub() {
        _propertyNames = new String[] { ENDPOINT_ADDRESS_PROPERTY };
        _propertyValues = new Object[] { "http://www.whereyoudey.com/WhereYouDeyWebServices/Search.asmx" };
    }

    public void _setProperty( String name, Object value ) {
        int size = _propertyNames.length;
        for (int i = 0; i < size; ++i) {
            if( _propertyNames[i].equals( name )) {
                _propertyValues[i] = value;
                return;
            }
        }
        String[] newPropNames = new String[size + 1];
        System.arraycopy(_propertyNames, 0, newPropNames, 0, size);
        _propertyNames = newPropNames;
        Object[] newPropValues = new Object[size + 1];
        System.arraycopy(_propertyValues, 0, newPropValues, 0, size);
        _propertyValues = newPropValues;

        _propertyNames[size] = name;
        _propertyValues[size] = value;
    }

    public Object _getProperty(String name) {
        for (int i = 0; i < _propertyNames.length; ++i) {
            if (_propertyNames[i].equals(name)) {
                return _propertyValues[i];
            }
        }
        if (ENDPOINT_ADDRESS_PROPERTY.equals(name) || USERNAME_PROPERTY.equals(name) || PASSWORD_PROPERTY.equals(name)) {
            return null;
        }
        if (SESSION_MAINTAIN_PROPERTY.equals(name)) {
            return new Boolean(false);
        }
        throw new JAXRPCException("Stub does not recognize property: " + name);
    }

    protected void _prepOperation(Operation op) {
        for (int i = 0; i < _propertyNames.length; ++i) {
            op.setProperty(_propertyNames[i], _propertyValues[i].toString());
        }
    }

    public String GetCityEvents(String city) throws java.rmi.RemoteException {
        Object inputObject[] = new Object[] {
            city
        };

        Operation op = Operation.newInstance( _qname_operation_GetCityEvents, _type_GetCityEvents, _type_GetCityEventsResponse );
        _prepOperation( op );
        op.setProperty( Operation.SOAPACTION_URI_PROPERTY, "http://www.WhereYouDey.com/WhereYouDeyWebServices/Search.asmx/GetCityEvents" );
        Object resultObj;
        try {
            resultObj = op.invoke( inputObject );
        } catch( JAXRPCException e ) {
            Throwable cause = e.getLinkedCause();
            if( cause instanceof java.rmi.RemoteException ) {
                throw (java.rmi.RemoteException) cause;
            }
            throw e;
        }

        return (String )((Object[])resultObj)[0];
    }

    public ArrayOfString SearchData_Ex(String locationID) throws java.rmi.RemoteException {
        Object inputObject[] = new Object[] {
            locationID
        };

        Operation op = Operation.newInstance( _qname_operation_SearchData_Ex, _type_SearchData_Ex, _type_SearchData_ExResponse );
        _prepOperation( op );
        op.setProperty( Operation.SOAPACTION_URI_PROPERTY, "http://www.WhereYouDey.com/WhereYouDeyWebServices/Search.asmx/SearchData_Ex" );
        Object resultObj;
        try {
            resultObj = op.invoke( inputObject );
        } catch( JAXRPCException e ) {
            Throwable cause = e.getLinkedCause();
            if( cause instanceof java.rmi.RemoteException ) {
                throw (java.rmi.RemoteException) cause;
            }
            throw e;
        }

        return ArrayOfString_fromObject((Object[])((Object[]) resultObj)[0]);
    }

    public String IndexData() throws java.rmi.RemoteException {
        Object inputObject[] = new Object[] {
        };

        Operation op = Operation.newInstance( _qname_operation_IndexData, _type_IndexData, _type_IndexDataResponse );
        _prepOperation( op );
        op.setProperty( Operation.SOAPACTION_URI_PROPERTY, "http://www.WhereYouDey.com/WhereYouDeyWebServices/Search.asmx/IndexData" );
        Object resultObj;
        try {
            resultObj = op.invoke( inputObject );
        } catch( JAXRPCException e ) {
            Throwable cause = e.getLinkedCause();
            if( cause instanceof java.rmi.RemoteException ) {
                throw (java.rmi.RemoteException) cause;
            }
            throw e;
        }

        return (String )((Object[])resultObj)[0];
    }

    public String GetCityTheaters(String city) throws java.rmi.RemoteException {
        Object inputObject[] = new Object[] {
            city
        };

        Operation op = Operation.newInstance( _qname_operation_GetCityTheaters, _type_GetCityTheaters, _type_GetCityTheatersResponse );
        _prepOperation( op );
        op.setProperty( Operation.SOAPACTION_URI_PROPERTY, "http://www.WhereYouDey.com/WhereYouDeyWebServices/Search.asmx/GetCityTheaters" );
        Object resultObj;
        try {
            resultObj = op.invoke( inputObject );
        } catch( JAXRPCException e ) {
            Throwable cause = e.getLinkedCause();
            if( cause instanceof java.rmi.RemoteException ) {
                throw (java.rmi.RemoteException) cause;
            }
            throw e;
        }

        return (String )((Object[])resultObj)[0];
    }

    public ArrayOfString SearchData(String name, String location, ArrayOfString filter) throws java.rmi.RemoteException {
        Object inputObject[] = new Object[] {
            name,
            location,
            ArrayOfString_toObject( filter )
        };

        Operation op = Operation.newInstance( _qname_operation_SearchData, _type_SearchData, _type_SearchDataResponse );
        _prepOperation( op );
        op.setProperty( Operation.SOAPACTION_URI_PROPERTY, "http://www.WhereYouDey.com/WhereYouDeyWebServices/Search.asmx/SearchData" );
        Object resultObj;
        try {
            resultObj = op.invoke( inputObject );
        } catch( JAXRPCException e ) {
            Throwable cause = e.getLinkedCause();
            if( cause instanceof java.rmi.RemoteException ) {
                throw (java.rmi.RemoteException) cause;
            }
            throw e;
        }

        return ArrayOfString_fromObject((Object[])((Object[]) resultObj)[0]);
    }

    private static Object ArrayOfString_toObject( ArrayOfString obj ) {
        if(obj == null) return null;
        Object result[] = new Object[ 1 ];
        result[0] = obj.getString();
        return result;
    }

    private static ArrayOfString ArrayOfString_fromObject( Object obj[] ) {
        if(obj == null) return null;
        ArrayOfString result = new ArrayOfString();
        result.setString((String[]) obj[0]);
        return result;
    }

    protected static final QName _qname_operation_SearchData_Ex = new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/Search.asmx", "SearchData_Ex" );
    protected static final QName _qname_operation_GetCityEvents = new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/Search.asmx", "GetCityEvents" );
    protected static final QName _qname_operation_SearchData = new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/Search.asmx", "SearchData" );
    protected static final QName _qname_operation_GetCityTheaters = new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/Search.asmx", "GetCityTheaters" );
    protected static final QName _qname_operation_IndexData = new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/Search.asmx", "IndexData" );
    protected static final QName _qname_GetCityEvents = new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/Search.asmx", "GetCityEvents" );
    protected static final QName _qname_SearchData_Ex = new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/Search.asmx", "SearchData_Ex" );
    protected static final QName _qname_SearchData_ExResponse = new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/Search.asmx", "SearchData_ExResponse" );
    protected static final QName _qname_GetCityEventsResponse = new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/Search.asmx", "GetCityEventsResponse" );
    protected static final QName _qname_GetCityTheatersResponse = new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/Search.asmx", "GetCityTheatersResponse" );
    protected static final QName _qname_SearchDataResponse = new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/Search.asmx", "SearchDataResponse" );
    protected static final QName _qname_SearchData = new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/Search.asmx", "SearchData" );
    protected static final QName _qname_IndexDataResponse = new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/Search.asmx", "IndexDataResponse" );
    protected static final QName _qname_GetCityTheaters = new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/Search.asmx", "GetCityTheaters" );
    protected static final QName _qname_IndexData = new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/Search.asmx", "IndexData" );
    protected static final Element _type_SearchDataResponse;
    protected static final Element _type_IndexData;
    protected static final Element _type_IndexDataResponse;
    protected static final Element _type_SearchData_Ex;
    protected static final Element _type_GetCityTheaters;
    protected static final Element _type_GetCityTheatersResponse;
    protected static final Element _type_GetCityEventsResponse;
    protected static final Element _type_SearchData_ExResponse;
    protected static final Element _type_SearchData;
    protected static final Element _type_GetCityEvents;

    static {
        _type_SearchData_Ex = new Element( _qname_SearchData_Ex, _complexType( new Element[] {
            new Element( new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/Search.asmx", "locationID" ), Type.STRING, 0, 1, false )}), 1, 1, false );
        _type_GetCityEvents = new Element( _qname_GetCityEvents, _complexType( new Element[] {
            new Element( new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/Search.asmx", "city" ), Type.STRING, 0, 1, false )}), 1, 1, false );
        _type_SearchData_ExResponse = new Element( _qname_SearchData_ExResponse, _complexType( new Element[] {
            new Element( new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/Search.asmx", "SearchData_ExResult" ), _complexType( new Element[] {
                new Element( new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/Search.asmx", "string" ), Type.STRING, 0, Element.UNBOUNDED, true )}))}), 1, 1, false );
        _type_GetCityEventsResponse = new Element( _qname_GetCityEventsResponse, _complexType( new Element[] {
            new Element( new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/Search.asmx", "GetCityEventsResult" ), Type.STRING, 0, 1, false )}), 1, 1, false );
        _type_GetCityTheatersResponse = new Element( _qname_GetCityTheatersResponse, _complexType( new Element[] {
            new Element( new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/Search.asmx", "GetCityTheatersResult" ), Type.STRING, 0, 1, false )}), 1, 1, false );
        _type_SearchDataResponse = new Element( _qname_SearchDataResponse, _complexType( new Element[] {
            new Element( new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/Search.asmx", "SearchDataResult" ), _complexType( new Element[] {
                new Element( new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/Search.asmx", "string" ), Type.STRING, 0, Element.UNBOUNDED, true )}))}), 1, 1, false );
        _type_SearchData = new Element( _qname_SearchData, _complexType( new Element[] {
            new Element( new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/Search.asmx", "name" ), Type.STRING, 0, 1, false ),
            new Element( new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/Search.asmx", "location" ), Type.STRING, 0, 1, false ),
            new Element( new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/Search.asmx", "filter" ), _complexType( new Element[] {
                new Element( new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/Search.asmx", "string" ), Type.STRING, 0, Element.UNBOUNDED, true )}))}), 1, 1, false );
        _type_IndexDataResponse = new Element( _qname_IndexDataResponse, _complexType( new Element[] {
            new Element( new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/Search.asmx", "IndexDataResult" ), Type.STRING, 0, 1, false )}), 1, 1, false );
        _type_GetCityTheaters = new Element( _qname_GetCityTheaters, _complexType( new Element[] {
            new Element( new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/Search.asmx", "city" ), Type.STRING, 0, 1, false )}), 1, 1, false );
        _type_IndexData = new Element( _qname_IndexData, _complexType( new Element[] {
        }), 1, 1, false );
    }

    private static ComplexType _complexType( Element[] elements ) {
        ComplexType result = new ComplexType();
        result.elements = elements;
        return result;
    }
}
