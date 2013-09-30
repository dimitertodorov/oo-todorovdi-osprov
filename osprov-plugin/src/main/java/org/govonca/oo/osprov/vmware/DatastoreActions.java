package org.govonca.oo.osprov.vmware;


import org.govonca.oo.osprov.vmware.VIActions;
import org.govonca.oo.osprov.vmware.ior.Inputs;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;

import com.vmware.vim25.*;
import com.vmware.vim25.mo.*;

import com.google.gson.*;

import java.net.URL;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class DatastoreActions {


    @Action(name = "Get All Datastores - JSON",
            description = "Get VI Datastore information and return it in JSON format.",
            outputs = {
                    @Output("result"),
                    @Output("datastoresJSON"),
                    @Output("viSessionToken"),
                    @Output("result_message")
            },
            responses = {
                    @Response(text = "Success", field = "result", value = "0", matchType = MatchType.COMPARE_GREATER_OR_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = "Failure", field = "result", value = "0", matchType = MatchType.COMPARE_LESS, responseType = ResponseType.ERROR)
            })
    public Map<String, String> getDatastoresJSON(
            @Param(value=Inputs.HOST_VALUE,
                    required = true,
                    encrypted = false,
                    description = Inputs.HOST_DESCRIPTION) String host,
            @Param(value="user",
                    required = true,
                    encrypted = false,
                    description = "VI Username") String user,
            @Param(value="password",
                    required = true,
                    encrypted = true,
                    description = "VI Password") String password,
            @Param(value="port",
                    required = true,
                    encrypted = false,
                    description = "Target VI Port") String port,
            @Param(value="protocol",
                    required = true,
                    encrypted = false,
                    description = "Target VI Protocol") String protocol,
            @Param(value="closeSession",
                    required = false,
                    encrypted = false,
                    description = "Close Session or Keep Open?") String closeSession,
            @Param(value="viSessionToken",
                    required = false,
                    encrypted = true,
                    description = "Session token to use.") String viSessionToken,
            @Param(value="datacenter",
                    required = true,
                    encrypted = false,
                    description = "Datacenter to get datastores from.") String datacenter

    ){
        Map<String, String> resultMap = new HashMap<String, String>();

        ServiceInstance si;

        ManagedEntity[] datastores;

        String datastoreJSON;

        List<DatastoreSummary> datastoreList = new ArrayList<DatastoreSummary>();

        Gson gson = new Gson();

        try{
            si = VIConnectionManager.getServiceInstance(host,user,password,protocol,port,viSessionToken);

        }catch(Exception e){
            resultMap.put("result","-1");
            resultMap.put("result_message","Error during connection to VI. ");
            return resultMap;
        }

        try{
            datastores=this.getAllDatastores(si,datacenter);
        }catch (Exception e){
            resultMap.put("result","-1");
            resultMap.put("result_message","Error retrieving datastores ");
            resultMap.put("viSessionToken",VIConnectionManager.endVIServiceSession(si,closeSession));
            return resultMap;
        }

        //Check that datastores were returned.
        if((datastores!=null)&& datastores.length!=0){
            Map<String, Map<String, String>> datastoreMap = new HashMap<String, Map<String, String>>();


            for(int i=0;i<datastores.length;i++){
                Datastore ds = (Datastore)datastores[i];
                DatastoreSummary dss = ds.getSummary();
                datastoreList.add(dss);
            }

            //Serialize Summary listing to JSON. This
            datastoreJSON = gson.toJson(datastoreList);
        }else{
            resultMap.put("result","0");
            resultMap.put("result_message","Executed Successfully, but no datastores found in datacenter "+datacenter);
            resultMap.put("viSessionToken",VIConnectionManager.endVIServiceSession(si,closeSession));
            return resultMap;
        }




        resultMap.put("result","0");
        resultMap.put("result_message","Executed Successfully");
        resultMap.put("datastoresJSON",datastoreJSON);
        resultMap.put("viSessionToken",VIConnectionManager.endVIServiceSession(si,closeSession));

        return resultMap;
    }

    public ManagedEntity[] getAllDatastores(ServiceInstance si, String datacenter)  throws InvalidProperty, RuntimeFault, RemoteException, NullPointerException {
        Folder rootFolder = si.getRootFolder();
        Datacenter dc = (Datacenter) new InventoryNavigator(rootFolder).searchManagedEntity("Datacenter", datacenter);
        ManagedEntity[] datastores = new InventoryNavigator(dc).searchManagedEntities("Datastore");
        return datastores;
    }
}
