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

import org.ofbiz.base.util.*;
import org.ofbiz.entity.*;
import org.ofbiz.entity.util.*;
import org.ofbiz.entity.condition.*;

import java.io.File;

productId = parameters.productId
context.productId=productId
motherSku = productId.substring(0,7);

oldFormat = false;

fileServerPathCheck = "hot-deploy/gudao/webapp/gudao/images/" + motherSku;
File directoryCheck = new File(fileServerPathCheck);
if (UtilValidate.isNotEmpty(directoryCheck) && directoryCheck.isDirectory()) {
    File[] fileListCheck = directoryCheck.listFiles();
    for (File fileCheck : fileListCheck) {
        imageFilenameCheck = fileCheck.getPath().substring(29);
        if (imageFilenameCheck.endsWith(".jpg")) {
            oldFormat = true;
            fileServerPathEbayCheck = "hot-deploy/gudao/webapp/gudao/images/" + motherSku + "/EBAY/";
            File directoryEbayCheck = new File(fileServerPathEbayCheck);
            if (UtilValidate.isNotEmpty(directoryEbayCheck) && directoryEbayCheck.isDirectory()) {
                oldFormat = false;
            }
            fileServerPathAmaCheck = "hot-deploy/gudao/webapp/gudao/images/" + motherSku + "/AMAZON/";
            File directoryAmaCheck = new File(fileServerPathAmaCheck);
            if (UtilValidate.isNotEmpty(directoryAmaCheck) && directoryAmaCheck.isDirectory()) {
                oldFormat = false;
            }
        }
    }
}
context.oldFormat = oldFormat;

/* old format == START */
imageListOld = [];
fileServerPathOld = "hot-deploy/gudao/webapp/gudao/images/" + motherSku;
File directoryOld = new File(fileServerPathOld);
if (UtilValidate.isNotEmpty(directoryOld) && directoryOld.isDirectory()) {
    File[] fileListOld = directoryCheck.listFiles();
    for (File fileOld : fileListOld) {
        imageFilenameOld = fileOld.getPath().substring(29);
        if (imageFilenameOld.endsWith(".jpg")) {
            imageListOld.add(imageFilenameOld);
        }
    }
}

context.imageListOld = imageListOld;
/* old format == END */

    /* AMAZON == START */
    imageListAma = [];
    fileServerPathAma = "hot-deploy/gudao/webapp/gudao/images/" + motherSku + "/AMAZON/";
    File directoryAma = new File(fileServerPathAma);
    if (UtilValidate.isNotEmpty(directoryAma) && directoryAma.isDirectory()) {
        File[] fileListAma = directoryAma.listFiles();
        for (File fileAma : fileListAma) {
            imageFilenameAma = fileAma.getPath().substring(29);
            if (imageFilenameAma.endsWith(".jpg")) {
                imageListAma.add(imageFilenameAma);
            }
        }
    } else {
        context.errorMsg = "No AMAZON image directory found for " + motherSku;
    }
    context.imageListAma = imageListAma;
    /* AMAZON == END */

    /* EBAY == START */
    imageListEbay = [];
    fileServerPathEbay = "hot-deploy/gudao/webapp/gudao/images/" + motherSku + "/EBAY/";
    File directoryEbay = new File(fileServerPathEbay);
    if (UtilValidate.isNotEmpty(directoryEbay) && directoryEbay.isDirectory()) {
        File[] fileListEbay = directoryEbay.listFiles();
        for (File fileEbay : fileListEbay) {
            imageFilenameEbay = fileEbay.getPath().substring(29);
            if (imageFilenameEbay.endsWith(".jpg")) {
                imageListEbay.add(imageFilenameEbay);
            }
        }
    } else {
        context.errorMsg = "No EBAY image directory found for " + motherSku;
    }
    context.imageListEbay = imageListEbay;
    /* EBAY == END */

    /* SMT == START */
    imageListSmt = [];
    fileServerPathSmt = "hot-deploy/gudao/webapp/gudao/images/" + motherSku + "/SMT/";
    File directorySmt = new File(fileServerPathSmt);
    if (UtilValidate.isNotEmpty(directorySmt) && directorySmt.isDirectory()) {
        File[] fileListSmt = directorySmt.listFiles();
        for (File fileSmt : fileListSmt) {
            imageFilenameSmt = fileSmt.getPath().substring(29);
            if (imageFilenameSmt.endsWith(".jpg")) {
                imageListSmt.add(imageFilenameSmt);
            }
        }
    } else {
        context.errorMsg = "No SMT image directory found for " + motherSku;
    }
    context.imageListSmt = imageListSmt;
    /* SMT == END */

    /* WISH == START */
    imageListWish = [];
    fileServerPathWish = "hot-deploy/gudao/webapp/gudao/images/" + motherSku + "/WISH/";
    File directoryWish = new File(fileServerPathWish);
    if (UtilValidate.isNotEmpty(directoryWish) && directoryWish.isDirectory()) {
        File[] fileListWish = directoryWish.listFiles();
        for (File fileWish : fileListWish) {
            imageFilenameWish = fileWish.getPath().substring(29);
            if (imageFilenameWish.endsWith(".jpg")) {
                imageListWish.add(imageFilenameWish);
            }
        }
    } else {
        context.errorMsg = "No WISH image directory found for " + motherSku;
    }
    context.imageListWish = imageListWish;
    /* WISH == END */











