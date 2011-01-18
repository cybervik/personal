package searchhelper;

import javax.xml.rpc.JAXRPCException;
import javax.xml.namespace.QName;
import javax.microedition.xml.rpc.Operation;
import javax.microedition.xml.rpc.Type;
import javax.microedition.xml.rpc.ComplexType;
import javax.microedition.xml.rpc.Element;

public class SearchHelper_Stub implements SearchHelper, javax.xml.rpc.Stub {

    private String[] _propertyNames;
    private Object[] _propertyValues;

    public SearchHelper_Stub() {
        _propertyNames = new String[] { ENDPOINT_ADDRESS_PROPERTY };
        _propertyValues = new Object[] { "http://www.whereyoudey.com/WhereYouDeyWebServices/SearchHelper.asmx" };
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

    public void createReview(String name, String comment, long locationID, int starCount) throws java.rmi.RemoteException {
        Object inputObject[] = new Object[] {
            name,
            comment,
            new Long(locationID),
            new Integer(starCount)
        };

        Operation op = Operation.newInstance( _qname_operation_createReview, _type_createReview, _type_createReviewResponse );
        _prepOperation( op );
        op.setProperty( Operation.SOAPACTION_URI_PROPERTY, "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx/createReview" );
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

    }

    public String[] getSearchSuggestions(String prefixText, int count) throws java.rmi.RemoteException {
        Object inputObject[] = new Object[] {
            prefixText,
            new Integer(count)
        };

        Operation op = Operation.newInstance( _qname_operation_GetSearchSuggestions, _type_GetSearchSuggestions, _type_GetSearchSuggestionsResponse );
        _prepOperation( op );
        op.setProperty( Operation.SOAPACTION_URI_PROPERTY, "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx/GetSearchSuggestions" );
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

        String[] _res = new String[((Object [])((Object []) resultObj)[0]).length];
        System.arraycopy(((Object []) resultObj)[0], 0, _res, 0, _res.length);
        return _res;
    }

    public String getMainBanners() throws java.rmi.RemoteException {
        Object inputObject[] = new Object[] {
        };

        Operation op = Operation.newInstance( _qname_operation_GetMainBanners, _type_GetMainBanners, _type_GetMainBannersResponse );
        _prepOperation( op );
        op.setProperty( Operation.SOAPACTION_URI_PROPERTY, "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx/GetMainBanners" );
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

    public String getBusinessDetail(String locationID) throws java.rmi.RemoteException {
        Object inputObject[] = new Object[] {
            locationID
        };

        Operation op = Operation.newInstance( _qname_operation_GetBusinessDetail, _type_GetBusinessDetail, _type_GetBusinessDetailResponse );
        _prepOperation( op );
        op.setProperty( Operation.SOAPACTION_URI_PROPERTY, "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx/GetBusinessDetail" );
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

    public long getReviewCount(long locationID) throws java.rmi.RemoteException {
        Object inputObject[] = new Object[] {
            new Long(locationID)
        };

        Operation op = Operation.newInstance( _qname_operation_getReviewCount, _type_getReviewCount, _type_getReviewCountResponse );
        _prepOperation( op );
        op.setProperty( Operation.SOAPACTION_URI_PROPERTY, "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx/getReviewCount" );
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

        return ((Long )((Object[])resultObj)[0]).longValue();
    }

    public String[] getLocationSuggestions(String prefixText, int count) throws java.rmi.RemoteException {
        Object inputObject[] = new Object[] {
            prefixText,
            new Integer(count)
        };

        Operation op = Operation.newInstance( _qname_operation_GetLocationSuggestions, _type_GetLocationSuggestions, _type_GetLocationSuggestionsResponse );
        _prepOperation( op );
        op.setProperty( Operation.SOAPACTION_URI_PROPERTY, "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx/GetLocationSuggestions" );
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

        String[] _res = new String[((Object [])((Object []) resultObj)[0]).length];
        System.arraycopy(((Object []) resultObj)[0], 0, _res, 0, _res.length);
        return _res;
    }

    public long getStarCount(long locationID) throws java.rmi.RemoteException {
        Object inputObject[] = new Object[] {
            new Long(locationID)
        };

        Operation op = Operation.newInstance( _qname_operation_getStarCount, _type_getStarCount, _type_getStarCountResponse );
        _prepOperation( op );
        op.setProperty( Operation.SOAPACTION_URI_PROPERTY, "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx/getStarCount" );
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

        return ((Long )((Object[])resultObj)[0]).longValue();
    }

    protected static final QName _qname_operation_getStarCount = new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx", "getStarCount" );
    protected static final QName _qname_operation_GetLocationSuggestions = new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx", "GetLocationSuggestions" );
    protected static final QName _qname_operation_GetBusinessDetail = new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx", "GetBusinessDetail" );
    protected static final QName _qname_operation_GetMainBanners = new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx", "GetMainBanners" );
    protected static final QName _qname_operation_getReviewCount = new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx", "getReviewCount" );
    protected static final QName _qname_operation_GetSearchSuggestions = new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx", "GetSearchSuggestions" );
    protected static final QName _qname_operation_createReview = new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx", "createReview" );
    protected static final QName _qname_getStarCount = new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx", "getStarCount" );
    protected static final QName _qname_getReviewCountResponse = new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx", "getReviewCountResponse" );
    protected static final QName _qname_GetBusinessDetailResponse = new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx", "GetBusinessDetailResponse" );
    protected static final QName _qname_GetSearchSuggestionsResponse = new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx", "GetSearchSuggestionsResponse" );
    protected static final QName _qname_GetSearchSuggestions = new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx", "GetSearchSuggestions" );
    protected static final QName _qname_getStarCountResponse = new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx", "getStarCountResponse" );
    protected static final QName _qname_GetLocationSuggestions = new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx", "GetLocationSuggestions" );
    protected static final QName _qname_GetBusinessDetail = new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx", "GetBusinessDetail" );
    protected static final QName _qname_GetMainBanners = new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx", "GetMainBanners" );
    protected static final QName _qname_createReviewResponse = new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx", "createReviewResponse" );
    protected static final QName _qname_getReviewCount = new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx", "getReviewCount" );
    protected static final QName _qname_GetMainBannersResponse = new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx", "GetMainBannersResponse" );
    protected static final QName _qname_createReview = new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx", "createReview" );
    protected static final QName _qname_GetLocationSuggestionsResponse = new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx", "GetLocationSuggestionsResponse" );
    protected static final Element _type_GetLocationSuggestionsResponse;
    protected static final Element _type_GetBusinessDetail;
    protected static final Element _type_getReviewCount;
    protected static final Element _type_createReview;
    protected static final Element _type_createReviewResponse;
    protected static final Element _type_GetMainBannersResponse;
    protected static final Element _type_GetSearchSuggestionsResponse;
    protected static final Element _type_GetLocationSuggestions;
    protected static final Element _type_getStarCountResponse;
    protected static final Element _type_GetSearchSuggestions;
    protected static final Element _type_getStarCount;
    protected static final Element _type_getReviewCountResponse;
    protected static final Element _type_GetBusinessDetailResponse;
    protected static final Element _type_GetMainBanners;

    static {
        _type_getStarCount = new Element( _qname_getStarCount, _complexType( new Element[] {
            new Element( new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx", "locationID" ), Type.LONG )}), 1, 1, false );
        _type_getReviewCountResponse = new Element( _qname_getReviewCountResponse, _complexType( new Element[] {
            new Element( new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx", "getReviewCountResult" ), Type.LONG )}), 1, 1, false );
        _type_GetBusinessDetailResponse = new Element( _qname_GetBusinessDetailResponse, _complexType( new Element[] {
            new Element( new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx", "GetBusinessDetailResult" ), Type.STRING, 0, 1, false )}), 1, 1, false );
        _type_GetSearchSuggestionsResponse = new Element( _qname_GetSearchSuggestionsResponse, _complexType( new Element[] {
            new Element( new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx", "GetSearchSuggestionsResult" ), _complexType( new Element[] {
                new Element( new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx", "string" ), Type.STRING, 0, Element.UNBOUNDED, true )}))}), 1, 1, false );
        _type_GetSearchSuggestions = new Element( _qname_GetSearchSuggestions, _complexType( new Element[] {
            new Element( new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx", "prefixText" ), Type.STRING, 0, 1, false ),
            new Element( new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx", "count" ), Type.INT )}), 1, 1, false );
        _type_getStarCountResponse = new Element( _qname_getStarCountResponse, _complexType( new Element[] {
            new Element( new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx", "getStarCountResult" ), Type.LONG )}), 1, 1, false );
        _type_GetLocationSuggestions = new Element( _qname_GetLocationSuggestions, _complexType( new Element[] {
            new Element( new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx", "prefixText" ), Type.STRING, 0, 1, false ),
            new Element( new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx", "count" ), Type.INT )}), 1, 1, false );
        _type_GetBusinessDetail = new Element( _qname_GetBusinessDetail, _complexType( new Element[] {
            new Element( new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx", "locationID" ), Type.STRING, 0, 1, false )}), 1, 1, false );
        _type_GetMainBanners = new Element( _qname_GetMainBanners, _complexType( new Element[] {
        }), 1, 1, false );
        _type_createReviewResponse = new Element( _qname_createReviewResponse, _complexType( new Element[] {
        }), 1, 1, false );
        _type_getReviewCount = new Element( _qname_getReviewCount, _complexType( new Element[] {
            new Element( new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx", "locationID" ), Type.LONG )}), 1, 1, false );
        _type_GetMainBannersResponse = new Element( _qname_GetMainBannersResponse, _complexType( new Element[] {
            new Element( new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx", "GetMainBannersResult" ), Type.STRING, 0, 1, false )}), 1, 1, false );
        _type_createReview = new Element( _qname_createReview, _complexType( new Element[] {
            new Element( new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx", "name" ), Type.STRING, 0, 1, false ),
            new Element( new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx", "comment" ), Type.STRING, 0, 1, false ),
            new Element( new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx", "locationID" ), Type.LONG ),
            new Element( new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx", "starCount" ), Type.INT )}), 1, 1, false );
        _type_GetLocationSuggestionsResponse = new Element( _qname_GetLocationSuggestionsResponse, _complexType( new Element[] {
            new Element( new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx", "GetLocationSuggestionsResult" ), _complexType( new Element[] {
                new Element( new QName( "http://www.WhereYouDey.com/WhereYouDeyWebServices/SearchHelper.asmx", "string" ), Type.STRING, 0, Element.UNBOUNDED, true )}))}), 1, 1, false );
    }

    private static ComplexType _complexType( Element[] elements ) {
        ComplexType result = new ComplexType();
        result.elements = elements;
        return result;
    }
}
