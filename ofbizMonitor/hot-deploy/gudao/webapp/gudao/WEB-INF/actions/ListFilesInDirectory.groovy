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
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.util.EntityFindOptions;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.base.util.ObjectType;
import java.util.Arrays;
import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.File;
import java.text.SimpleDateFormat;

import javolution.util.FastList;
import javolution.util.FastMap;

userLogin = parameters.userLogin;
userLoginId = userLogin.getString("userLoginId");
userLoginPartyId = userLogin.getString("partyId");
partyGroupUserLoginList = delegator.findByAnd("UserLogin", UtilMisc.toMap("partyId", userLoginPartyId), null, false);
userLoginList = [];
for (userLoginIdGV in partyGroupUserLoginList) {
    userLoginList.add(userLoginIdGV.userLoginId);
}

fileServerPath = "hot-deploy/gudao/webapp/gudao/bulkModule/upload";

importFormId = parameters.importFormId;
updateFormId = parameters.updateFormId;
fileNameString = parameters.filename;

Calendar nowDate = Calendar.getInstance();
nowDate.add(Calendar.DATE, - 1);
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");


Timestamp todayTS = ObjectType.simpleTypeConvert((nowDate.getTime()), "Timestamp", null, null);
context.today = todayTS;

if (importFormId != null) {
    listIt = [];
    EntityCondition filenameConditions = EntityCondition.makeCondition(UtilMisc.toList(
                                            EntityCondition.makeCondition("fileName",EntityOperator.LIKE , "hot-deploy/gudao/webapp/gudao/bulkModule/upload/IMPORT%"),
                                            EntityCondition.makeCondition("createdStamp",EntityOperator.GREATER_THAN_EQUAL_TO , todayTS),
                                            EntityCondition.makeCondition("createdBy",EntityOperator.IN , userLoginList)
                                        ), EntityOperator.AND);

    fileNameList = delegator.findList("ProductImportGudao", filenameConditions,
                                        UtilMisc.toSet("fileName"),
                                        UtilMisc.toList("fileName DESC"),
                                        new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true),
                                        true);

    fileNameStringList = [];
    int countFilename = 1;
    for (fileName in fileNameList) {
        if (countFilename > 20) {
            break;
        }
        fileNameStringList.add(fileName.fileName);
        countFilename++;
    }

    for (fileNameString in fileNameStringList) {
        fileMap = [:];
        allRecords = delegator.findCountByCondition("ProductImportGudao", EntityCondition.makeCondition(["fileName" : fileNameString]), null, null);
if (UtilValidate.isNotEmpty(fileNameString)) {
uploadDate = EntityUtil.getFirst(delegator.findByAnd("ProductImportGudao", UtilMisc.toMap("fileName", fileNameString), UtilMisc.toList("createdStamp"), false)).createdStamp;
fileNameStringContext = fileNameString.replace("hot-deploy/gudao/webapp/gudao/bulkModule/upload/", "");
} else {
uploadDate = "";
fileNameStringContext = "";
}

        fileMap.put("uploadDate", uploadDate);
        fileMap.put("name", fileNameString.replace("hot-deploy/gudao/webapp/gudao/bulkModule/upload/", ""));
        fileMap.put("path", fileNameString);
        fileMap.put("allRecords", allRecords);
        unImportRecords = delegator.findCountByCondition("ProductImportGudao", EntityCondition.makeCondition(["fileName" : fileNameString, "importedStatus" : "N"]), null, null);
        importedRecords = allRecords - unImportRecords;
        fileMap.put("unImportRecords", unImportRecords);
        fileMap.put("importedRecords", importedRecords);
        listIt.add(fileMap);
    }
    context.listIt = listIt;

} else if (updateFormId != null) {
    listUpdateForm = [];
    EntityCondition filenameConditions = EntityCondition.makeCondition(UtilMisc.toList(
                                                            EntityCondition.makeCondition("fileName",EntityOperator.LIKE , "hot-deploy/gudao/webapp/gudao/bulkModule/upload/UPDATE%"),
                                                            EntityCondition.makeCondition("createdStamp",EntityOperator.GREATER_THAN_EQUAL_TO , todayTS)
                                            ), EntityOperator.AND);

    fileNameList = delegator.findList("ProductImportGudao", filenameConditions,
                                            UtilMisc.toSet("fileName"),
                                            UtilMisc.toList("fileName DESC"),
                                            new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true),
                                            true);

    fileNameStringList = [];
    int countFilename = 1;
    for (fileName in fileNameList) {
        if (countFilename > 20) {
            break;
        }
        fileNameStringList.add(fileName.fileName);
        countFilename++;
    }

    for (fileNameString in fileNameStringList) {
        fileMap = [:];
        allRecords = delegator.findCountByCondition("ProductImportGudao", EntityCondition.makeCondition(["fileName" : fileNameString]), null, null);
if (UtilValidate.isNotEmpty(fileNameString)) {
uploadDate = EntityUtil.getFirst(delegator.findByAnd("ProductImportGudao", UtilMisc.toMap("fileName", fileNameString), UtilMisc.toList("createdStamp"), false)).createdStamp;
fileNameStringContext = fileNameString.replace("hot-deploy/gudao/webapp/gudao/bulkModule/upload/", "")
} else {
uploadDate = "";
fileNameStringContext = "";
}

        fileMap.put("uploadDate", uploadDate);
        fileMap.put("name", fileNameStringContext);
        fileMap.put("path", fileNameString);
        fileMap.put("allRecords", allRecords);
        unImportRecords = delegator.findCountByCondition("ProductImportGudao", EntityCondition.makeCondition(["fileName" : fileNameString, "importedStatus" : "N"]), null, null);
        importedRecords = allRecords - unImportRecords;
        fileMap.put("unImportRecords", unImportRecords);
        fileMap.put("importedRecords", importedRecords);
        listUpdateForm.add(fileMap);
    }
    context.listUpdateForm = listUpdateForm
}


