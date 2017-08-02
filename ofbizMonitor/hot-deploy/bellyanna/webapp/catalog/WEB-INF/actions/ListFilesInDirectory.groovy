/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.ofbiz.base.util.string.FlexibleStringExpander;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.entity.condition.EntityCondition;

import java.io.File;

fileServerPath = FlexibleStringExpander.expandString(UtilProperties.getPropertyValue("orderImport", "orderimport.server.path"), context);
listIt = [];
File file = new File(fileServerPath);
if (file.exists()) {
    files = file.listFiles();
    for (int i = 0; i < files.length; i++) {
        currentFile = files[i];
        allRecords = delegator.findCountByCondition("OrderImport", EntityCondition.makeCondition(["fileName" : currentFile.getPath()]), null, null);
        if (allRecords <= 0) {
            deleteFile(currentFile.getPath());
        } else {
            fileMap = [:];
            uploadDate = new Date(currentFile.lastModified());
            fileMap.put("uploadDate", uploadDate);
            fileMap.put("name", currentFile.getName());
            fileMap.put("path", currentFile.getPath());
            fileMap.put("allRecords", allRecords);
            unImportRecords = delegator.findCountByCondition("OrderImport", EntityCondition.makeCondition(["fileName" : currentFile.getPath(), "importedStatus" : "N"]), null, null);
            importedRecords = allRecords - unImportRecords;
            fileMap.put("unImportRecords", unImportRecords);
            fileMap.put("importedRecords", importedRecords);
            listIt.add(fileMap);
        }
    }
}

boolean deleteFile (path) {
    boolean result = false;
    File file = new File(path);
    if (file.exists()) {
        result = file.delete();
    }
    return result;
}
listIt.sort{it.uploadDate};
context.listIt = listIt.reverse();
