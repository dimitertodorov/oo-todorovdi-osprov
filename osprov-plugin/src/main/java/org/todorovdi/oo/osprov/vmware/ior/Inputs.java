package org.todorovdi.oo.osprov.vmware.ior;

import java.util.List;
import java.util.ArrayList;

public class Inputs {

    public class Values {
        public static final String HOST = "host";
        public static final String USER = "user";
        public static final String PASSWORD = "password";
        public static final String PORT = "port";
        public static final String PROTOCOL = "protocol";
        public static final String CLOSE_SESSION = "closeSession";
        public static final String VM_DATACENTER = "vmDatacenter";
        public static final String VI_SESSION_TOKEN = "viSessionToken";
        public static final String INVENTORY_PATH = "inventoryPath";
        public static final String VM_CUSTOM_FIELD_KEY = "vmCustomFieldKey";
        public static final String VM_CUSTOM_FIELD_VALUE = "vmCustomFieldValue";

    }
    public class Descriptions {
        public static final String HOST ="Name or IP of VCenter or ESX Host.";
        public static final String USER = "user";
        public static final String PASSWORD = "password";
        public static final String PORT = "port";
        public static final String PROTOCOL = "protocol";
        public static final String CLOSE_SESSION = "closeSession";
        public static final String VM_DATACENTER = "vmDatacenter";
        public static final String VI_SESSION_TOKEN = "viSessionToken";
        public static final String INVENTORY_PATH = "inventoryPath";
        public static final String VM_CUSTOM_FIELD_KEY = "vmCustomFieldKey";
        public static final String VM_CUSTOM_FIELD_VALUE = "vmCustomFieldValue";
    }

}


//<pre>Create a virtual machine.
//
//        Inputs:
//        host - VMWare host hostname or IP.
//        user - VMWare username.
//        password - VMWare user's password.
//        port - Port to connect on.
//        protocol - Connection protocol (http or https).
//        closeSession - Close the internally kept VMWare Infrastructure API session at completion of operation (true, false).
//        async - Asynchronously perform the task (true, false).
//        taskTimeOut - Time to wait before the operation is considered to have failed (seconds).
//        vmName - Virtual machine name being created (new).
//        vmCpuCount - Number of virtual machine CPUs to setup.
//        hostSystem - Target virtual machine host system.
//        clusterName - Name of the VMWare HA or DRS cluster.
//        description - Description / annotation.
//        dataStore - Datastore or datastore cluster (eg. host:dsname, mydatastore).
//        guestOSID - Guest OS ID (eg. win95Guest,winNetEnterprise64Guest,etc.) or OS description (Microsoft Windows 3.1,Microsoft Windows Vista (64-bit),Red Hat Enterprise Linux 3 (64-bit)). A list of valid entries can be retrieved via the GetOSDescriptors operation.
//        vmDatacenter - Virtual machine's datacenter.
//        vmMemorySize - Virtual machine memory size (megabytes).
//        vmResourcePool - Virtual machine's resource pool.
//        vmFolder - Virtual machine's folder by inventory path, / delimited not including datacenter (eg. ManagedVMs/DRS/Location1). For root folder, use /
//        vmDiskSize - Size of the virtual disk to create (MB).
//
//        Responses:
//        success - The operation completed successfully.
//        failure - Something went wrong.
//
//        Returns:
//        returnResult - Task result or operation result</pre>