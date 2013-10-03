package org.todorovdi.oo.osprov.vmware;

//LOCAL Imports
import org.todorovdi.oo.osprov.vmware.ior.*;

//HP OO Imports
import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;

//VMWare Imports
import com.vmware.vim25.*;
import com.vmware.vim25.mo.*;

//Google Imports
import com.google.gson.*;

//Java Base Imports
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class DatastoreActions {


    @Action(name = "Get All Datastores - JSON",
            description = "Get VI Datastore information and return it in JSON format.",
            outputs = {
                    @Output(Outputs.RESULT),
                    @Output(Outputs.DATASTORES_JSON),
                    @Output(Outputs.VI_SESSION_TOKEN),
                    @Output(Outputs.RESULT_MESSAGE)
            },
            responses = {
                    @Response(text = "Success", field = "result", value = "0", matchType = MatchType.COMPARE_GREATER_OR_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = "Failure", field = "result", value = "0", matchType = MatchType.COMPARE_LESS, responseType = ResponseType.ERROR)
            })
    public Map<String, String> getDatastoresJSON(
            @Param(value        = Inputs.Values.HOST,
                    required    = true,
                    encrypted   = false,
                    description = Inputs.Descriptions.HOST) String host,
            @Param(value=Inputs.Values.USER,
                    required = true,
                    encrypted = false,
                    description = Inputs.Descriptions.USER) String user,
            @Param(value=Inputs.Values.PASSWORD,
                    required = true,
                    encrypted = true,
                    description = Inputs.Descriptions.PASSWORD) String password,
            @Param(value=Inputs.Values.PORT,
                    required = true,
                    encrypted = false,
                    description = Inputs.Descriptions.PORT) String port,
            @Param(value=Inputs.Values.PROTOCOL,
                    required = true,
                    encrypted = false,
                    description = Inputs.Descriptions.PROTOCOL) String protocol,
            @Param(value=Inputs.Values.CLOSE_SESSION,
                    required = false,
                    encrypted = false,
                    description = Inputs.Descriptions.CLOSE_SESSION) String closeSession,
            @Param(value=Inputs.Values.VI_SESSION_TOKEN,
                    required = false,
                    encrypted = false,
                    description = Inputs.Descriptions.VI_SESSION_TOKEN) String viSessionToken,
            @Param(value=Inputs.Values.VM_DATACENTER,
                    required = true,
                    encrypted = false,
                    description = Inputs.Descriptions.VM_DATACENTER) String datacenter

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
