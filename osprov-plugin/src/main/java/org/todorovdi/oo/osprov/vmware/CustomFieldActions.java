package org.todorovdi.oo.osprov.vmware;


import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;

import com.vmware.vim25.*;
import com.vmware.vim25.mo.*;


import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.todorovdi.oo.osprov.vmware.ior.Inputs;
import org.todorovdi.oo.osprov.vmware.ior.Outputs;

public class CustomFieldActions {

    @Action(name = "Set Custom Field",
            description = "Set Custom Field.",
            outputs = {
                    @Output(Outputs.RESULT),
                    @Output(Outputs.VI_SESSION_TOKEN),
                    @Output(Outputs.RESULT_MESSAGE)
            },
            responses = {
                    @Response(text = "Success", field = "result", value = "0", matchType = MatchType.COMPARE_GREATER_OR_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = "Failure", field = "result", value = "0", matchType = MatchType.COMPARE_LESS, responseType = ResponseType.ERROR)
            })
    public Map<String, String> setCustomField(
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
                    required = true,
                    encrypted = false,
                    description = Inputs.Descriptions.CLOSE_SESSION) String closeSession,
            @Param(value=Inputs.Values.VI_SESSION_TOKEN,
                    required = false,
                    encrypted = false,
                    description = Inputs.Descriptions.VI_SESSION_TOKEN) String viSessionToken,
            @Param(value=Inputs.Values.INVENTORY_PATH,
                    required = false,
                    encrypted = false,
                    description = Inputs.Descriptions.INVENTORY_PATH) String vmInventoryPath,
            @Param(value=Inputs.Values.VM_CUSTOM_FIELD_KEY,
                    required = true,
                    encrypted = false,
                    description = Inputs.Descriptions.VM_CUSTOM_FIELD_KEY) String vmCustomFieldKey,
            @Param(value=Inputs.Values.VM_CUSTOM_FIELD_VALUE,
                    required = true,
                    encrypted = false,
                    description = Inputs.Descriptions.VM_CUSTOM_FIELD_VALUE) String vmCustomFieldValue){

        Map<String, String> resultMap = new HashMap<String, String>();

        ServiceInstance si;
        SearchIndex searchIndex;
        ManagedEntity me;
        VirtualMachine vm;
        CustomFieldsManager cfm;
        CustomFieldDef[] defs;

        int VM_CUSTOM_FIELD_KEY=-1;
        Logger.getLogger(getClass()).debug("CustomFieldActions.setCustomField");

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
                resultMap.put("result_message", "Error getting VM");
                resultMap.put("viSessionToken", VIConnectionManager.endVIServiceSession(si, closeSession));
                return resultMap;

            }
        }catch(Exception e){
            resultMap.put("result", "-1");
            resultMap.put("result_message", "Error during connection to VI. ");
            return resultMap;
        }
        Logger.getLogger(getClass()).debug("CustomFieldActions.setCustomField - SUCCESS - "+vmInventoryPath+" CF:"+vmCustomFieldKey+" VALUE:"+vmCustomFieldValue);
        return resultMap;


    }









}
