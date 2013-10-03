package org.todorovdi.oo.osprov.vmware;

import com.vmware.vim25.*;
import com.vmware.vim25.mo.*;

import com.google.gson.*;

import java.net.URL;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;


public class VIConnectionManager {
    public static ServiceInstance getServiceInstance(String host, String user, String password, String protocol, String port, String sessionTicket) throws RemoteException, MalformedURLException
    {
        String viURL=protocol+"://"+host+":"+port+"/sdk";
        //Session ID Is provided. Attempt to re-use session.
        if(!((sessionTicket==null)||(sessionTicket.equals("")))){
            try{
                ServiceInstance si = new ServiceInstance(new URL(viURL),sessionTicket,true);
                String currentTime=si.currentTime().toString();
                return si;
            //If not authenticated or session is expired try to re-authenticate.
            }catch(NotAuthenticated e){
                try{
                    ServiceInstance si = new ServiceInstance(new URL(viURL),user,password,true);
                    String currentTime2=si.currentTime().toString();
                    return si;
                }catch(Exception ee){
                    throw new RemoteException("Error during VI Connection "+e);
                }
            }
        //No Session ID Found. Authenticate a new session.
        }else{
            try{
                ServiceInstance si = new ServiceInstance(new URL(viURL),user,password,true);
                String currentTime2=si.currentTime().toString();
                return si;
            }catch(Exception ee){
                throw new RemoteException("Error during VI Connection "+ee);
            }
        }

    }

    //Not fully implemented yet.
    public static String endVIServiceSession(ServiceInstance si, String closeSession)
    {
        String viSessionToken="";
        try{
            if(closeSession.equals("true")){
                si.getSessionManager().logout();
                viSessionToken="";
            }else{
                String sessionToken = si.getServerConnection().getSessionStr();
                viSessionToken=sessionToken;
            }
        }catch(Exception e){
            viSessionToken="";
            return viSessionToken;
        }
        return viSessionToken;
    }
}
