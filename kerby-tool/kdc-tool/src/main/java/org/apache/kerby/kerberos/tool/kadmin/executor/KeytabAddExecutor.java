/**
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package org.apache.kerby.kerberos.tool.kadmin.executor;

import org.apache.kerby.config.Config;
import org.apache.kerby.kerberos.kerb.KrbException;
import org.apache.kerby.kerberos.kerb.admin.Kadmin;

import java.io.File;

public class KeytabAddExecutor implements KadminCommandExecutor{
    private static final String USAGE =
            "Usage: ktadd [-k[eytab] keytab] [-q] [-e keysaltlist] [-norandkey] [principal | -glob princ-exp] [...]";

    private static final String DEFAULT_KEYTAB_FILE_LOCATION = "/etc/krb5.keytab";

    private Config backendConfig;

    public KeytabAddExecutor(Config backendConfig) {
        this.backendConfig = backendConfig;
    }

    @Override
    public void execute(String input) {
        String[] commands = input.split(" ");

        String principal = null;
        String keytabFileLocation = null;

        //Since commands[0] is ktadd, the initial index is 1.
        int index = 1;
        while (index < commands.length) {
            String command = commands[index];
            if (command.equals("-k")) {
                index++;
                if (index >= commands.length) {
                    System.err.println(USAGE);
                    return;
                }
                keytabFileLocation = commands[index].trim();

            } else if (!command.startsWith("-")){
                principal = command;
            }
            index++;
        }

        if (keytabFileLocation == null) {
            keytabFileLocation = DEFAULT_KEYTAB_FILE_LOCATION;
        }
        File keytabFile = new File(keytabFileLocation);

        if (principal == null || !keytabFile.exists()) {
            System.err.println(USAGE);
            return;
        }

        Kadmin kadmin = new Kadmin(backendConfig);
        try {
        StringBuffer result = kadmin.addEntryToKeytab(keytabFile, principal);
            System.out.println(result.toString());
        } catch (KrbException e) {
            System.err.println("Principal \"" + principal + "\" fail to add entry to keytab." +
                e.getCause());
        }
    }
}
