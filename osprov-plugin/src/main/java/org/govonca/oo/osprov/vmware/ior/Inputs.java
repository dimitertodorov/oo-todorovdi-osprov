package org.govonca.oo.osprov.vmware.ior;

import java.util.List;
import java.util.ArrayList;

public class Inputs {

    public class Values {
        public static final String HOST ="host";
    }
    public class Descriptions {
        public static final String HOST ="Name or IP of VCenter or ESX Host.";
    }
    public class Required {
        public static final boolean HOST=true;
    }
    public class Encrypted {
        public static final boolean HOST=false;
    }
}
