package org.govonca.oo.osprov.vmware;


import org.govonca.oo.osprov.vmware.VIActions;


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

public class CustomFieldActions {

    @Action(name = "Set Custom Field",
            description = "Set Custom Field.",
            outputs = {
                    @Output("result"),
                    @Output("viSessionToken"),
                    @Output("result_message")
            },
            responses = {
                    @Response(text = "Success", field = "result", value = "0", matchType = MatchType.COMPARE_GREATER_OR_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = "Failure", field = "result", value = "0", matchType = MatchType.COMPARE_LESS, responseType = ResponseType.ERROR)
            })
    public Map<String, String> setCustomField(
            @Param(value="host",
                    required = true,
                    encrypted = false,
                    description = "Target VI Host") String host,
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
                    description = "Datacenter to get datastores from.") String datacenter,
            @Param(value="vmInventoryPath",
                    required = true,
                    encrypted = false,
                    description = "VM Inventory Path.") String vmInventoryPath,
            @Param(value="vmCustomFieldKey",
                    required = true,
                    encrypted = false,
                    description = "VM Custom Field Key") String vmCustomFieldKey,
            @Param(value="vmCustomFieldValue",
                    required = true,
                    encrypted = false,
                    description = "VM Custom Field Key") String vmCustomFieldValue){

        Map<String, String> resultMap = new HashMap<String, String>();

        ServiceInstance si;
        SearchIndex searchIndex;
        ManagedEntity me;
        VirtualMachine vm;
        CustomFieldsManager cfm;
        CustomFieldDef[] defs;

        int VM_CUSTOM_FIELD_KEY=-1;

        try{
            si = VIConnectionManager.getServiceInstance(host,user,password,protocol,port,viSessionToken);
            searchIndex = si.getSearchIndex();
            try{

                me=searchIndex.findByInventoryPath(vmInventoryPath);

                vm = (VirtualMachine)me;

                try{
                    cfm=si.getCustomFieldsManager();
                    defs=vm.getAvailableField();
                    for(int i=0; defs!=null && i<defs.length; i++)
                    {
                        if(vmCustomFieldKey.equals(defs[i].getName()))
                        {
                            VM_CUSTOM_FIELD_KEY = defs[i].getKey();
                        }
                    }
                    if(VM_CUSTOM_FIELD_KEY == -1)
                    {
                        VM_CUSTOM_FIELD_KEY = cfm.addCustomFieldDef(vmCustomFieldKey, "VirtualMachine",
                                null, null).getKey();
                    }
                    vm.setCustomValue(vmCustomFieldKey,vmCustomFieldValue);
                    resultMap.put("result","0");
                    resultMap.put("result_message","Successfully set Custom Field");
                    resultMap.put("viSessionToken",VIConnectionManager.endVIServiceSession(si,closeSession));
                }catch(Exception ee){
                    resultMap.put("result","-1");
                    resultMap.put("result_message","Error setting Custom Field");
                    resultMap.put("viSessionToken",VIConnectionManager.endVIServiceSession(si,closeSession));
                    return resultMap;
                }
            }catch(Exception vmError){
                resultMap.put("result","-1");
                resultMap.put("result_message","Error getting VM");
                resultMap.put("viSessionToken",VIConnectionManager.endVIServiceSession(si,closeSession));
                return resultMap;

            }
        }catch(Exception e){
            resultMap.put("result","-1");
            resultMap.put("result_message","Error during connection to VI. ");
            return resultMap;
        }


        return resultMap;


    }









}
