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
import org.ofbiz.base.util.UtilProperties;

fileServerPath = FlexibleStringExpander.expandString(UtilProperties.getPropertyValue("orderImport", "orderexport.server.path"), context);
downloadPath = FlexibleStringExpander.expandString(UtilProperties.getPropertyValue("orderImport", "orderexport.download.path"), context);
listIt = [];
File file = new File(fileServerPath);
if (file.exists()) {
    files = file.listFiles();
    for (int i = 0; i < files.length; i++) {
        currentFile = files[i];
        fileMap = [:];
        createDate = new Date(currentFile.lastModified());
        fileMap.put("createDate", createDate);
        fileMap.put("fileName", currentFile.getName());
        fileMap.put("filePath", currentFile.getPath());
        fileMap.put("downloadPath", downloadPath + currentFile.getName())
        listIt.add(fileMap);
    }
}
listIt.sort{it.createDate};
context.listIt = listIt.reverse();

