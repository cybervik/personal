package searchhelper;
import javax.xml.namespace.QName;

public interface SearchHelper extends java.rmi.Remote {

    /**
     *
     */
    public void createReview(String name, String comment, long locationID, int starCount) throws java.rmi.RemoteException;

    /**
     *
     */
    public String[] getSearchSuggestions(String prefixText, int count) throws java.rmi.RemoteException;

    /**
     *
     */
    public String getMainBanners() throws java.rmi.RemoteException;

    /**
     *
     */
    public String getBusinessDetail(String locationID) throws java.rmi.RemoteException;

    /**
     *
     */
    public long getReviewCount(long locationID) throws java.rmi.RemoteException;

    /**
     *
     */
    public String[] getLocationSuggestions(String prefixText, int count) throws java.rmi.RemoteException;

    /**
     *
     */
    public long getStarCount(long locationID) throws java.rmi.RemoteException;

}
