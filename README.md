oo-todorovdi-osprov
===================

HP Operations Orchestration 10 - Custom OS Provisioning Plugin

Sample Plugin pack for OO. Leveraging @Action methods.

In order to import to studio, you will need to build it locally, then import the plugin-pack jar.
Check the Extension Developers Guide from the OO 10 or 10.01 media for some info on how to do that.
You will need:
JDK (I used 1.7)
Maven 3.0.x (I used 3.0.5)
JAVA_HOME is set to the JDK root.
mvn binary is in your path.

https://github.com/dimitertodorov/oo-todorovdi-osprov

Debug logging properties are stored in log4j.properties in the plugin resources folder. Set to FATAL or OFF before packaging for Studio.

The first action is "Set Custom Field"
It takes as inputs some VI connection information, Inventory Path of a VM, and Custom Field key and Value.
It creates the field if it does not exist, then sets it.
Example string to execute from command line:
mvn org.todorovdi:osprov-plugin:1.0.003:execute -Daction="Set Custom Field" -Duser=MYUSER -Dpassword=PASSWORD -Dport=443 -Dprotocol=https -Dhost=VCENTERIPORNAME -DvmCustomFieldKey="CI" -DvmCustomFieldValue="DDF" -DinventoryPath="DATACENTERNAME/vm/Tools/OSPROV/OSPROV49"
Output:
[INFO] --- osprov-plugin:1.0.003:execute (default-cli) @ osprov-plugin-project ---
2013-10-03 11:28:38 DEBUG CustomFieldActions:87 - CustomFieldActions.setCustomField
2013-10-03 11:28:41 DEBUG CustomFieldActions:135 - CustomFieldActions.setCustomField - SUCCESS - DATACENTERNAME/vm/Tools/OSPROV/OSPROV49 CF:CI VALUE:DDF


Second Action is "Get All Datastores - JSON"
Provide VI connection information and a Datacenter. Returns a JSON formatted String.
JSON is very handy since HPOO can easily play with it using Javascript scriptlets.

Sample exec:
mvn org.todorovdi:osprov-plugin:1.0.003:execute -Daction="Get All Datastores - JSON" -Duser=MYUSER -Dpassword=PASSWORD -Dport=443 -Dprotocol=https -Dhost=VCENTERIPORNAME -DvmDatacenter="DATACENTERNAME"

Sample Output: (Formatted the string a bit for better viewability)
[INFO] --- osprov-plugin:1.0.003:execute (default-cli) @ osprov-plugin-project ---
2013-10-03 11:36:28 DEBUG DatastoreActions:134 - DatastoreActions.getDatastoresJSON - SUCCESS -[
    {
        "datastore": {
            "val": "datastore-51",
            "type": "Datastore"
        },
        "name": "ONEDATASTORE",
        "url": "ds:///vmfs/volumes/51dd7ddf-9b42f8ac-c026-78acc0f755be/",
        "capacity": 1099243192320,
        "freeSpace": 757461942272,
        "uncommitted": 13618962432,
        "accessible": true,
        "multipleHostAccess": true,
        "type": "VMFS",
        "maintenanceMode": "normal"
    },
    {
        "datastore": {
            "val": "datastore-41",
            "type": "Datastore"
        },
        "name": "ANOTHERDATASTORE",
        "url": "ds:///vmfs/volumes/51dc2a84-14c17bc4-82db-78acc0f755be/",
        "capacity": 1648999006208,
        "freeSpace": 833462730752,
        "uncommitted": 0,
        "accessible": true,
        "multipleHostAccess": true,
        "type": "VMFS",
        "maintenanceMode": "normal"
    }]
