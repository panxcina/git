/*******************************************************************************
 *  Copyright 2009 Amazon Services.
 *  Licensed under the Apache License, Version 2.0 (the "License"); 
 *  
 *  You may not use this file except in compliance with the License. 
 *  You may obtain a copy of the License at: http://aws.amazon.com/apache2.0
 *  This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR 
 *  CONDITIONS OF ANY KIND, either express or implied. See the License for the 
 *  specific language governing permissions and limitations under the License.
 * ***************************************************************************** 
 *
 *  Marketplace Web Service Java Library
 *  API Version: 2009-01-01
 *  Generated: Wed Feb 18 13:28:48 PST 2009 
 * 
 */



package com.gudao.amazon;

import com.amazonaws.mws.*;
import com.amazonaws.mws.model.*;
import com.gudao.amazon.amazon;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.service.DispatchContext;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ServiceUtil;

/**
 *
 * Request Report  Samples
 *
 *
 */
public class Report {
    private static final String module = Report.class.getName();
    
    public static Map<String, Object> requestReport (DispatchContext dctx, Map context) {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Map<String, Object> result = ServiceUtil.returnSuccess();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String productStoreId = (String) context.get("accountName");
        productStoreId = productStoreId.trim();
        
        try {   //main Try == START
            Map apiKeyMap = FastMap.newInstance();
            Map apiKeyMapService = dispatcher.runSync("getAmazonApiInfo", UtilMisc.toMap("accountName", productStoreId, "userLogin", userLogin));
            if (ServiceUtil.isSuccess(apiKeyMapService)) {    //if apiKeyMapService success -- START
                apiKeyMap = (Map) apiKeyMapService.get("apiKeyMap");
            }   //if apiKeyMapService success -- END
            
            String merchantId = (String) apiKeyMap.get("merchantId");
            String sellerDevAuthToken = (String) apiKeyMap.get("sellerDevAuthToken");
            String marketplaceId = (String) apiKeyMap.get("marketplaceId");
            MarketplaceWebService service = (MarketplaceWebService) apiKeyMap.get("marketplaceWebService");
            
            final IdList marketplaces = new IdList(Arrays.asList(marketplaceId));
            
            RequestReportRequest request = new RequestReportRequest()
            .withMerchant(merchantId)
            .withMarketplaceIdList(marketplaces)
            .withReportType("_GET_MERCHANT_LISTINGS_DATA_")
            .withReportOptions("ShowSalesChannel=true");
            if (UtilValidate.isNotEmpty(sellerDevAuthToken)) {
                request = request.withMWSAuthToken(sellerDevAuthToken);
            }
            
            DatatypeFactory df = DatatypeFactory.newInstance();
            XMLGregorianCalendar startDate = df
            .newXMLGregorianCalendar(new GregorianCalendar(2011, 1, 1));
            request.setStartDate(startDate);
            
            //RESPONSE == START
            RequestReportResponse response = service.requestReport(request);
            
            if (response.isSetRequestReportResult()) {  //if response.isSetRequestReportResult() TRUE == START
                RequestReportResult requestReportResult = response.getRequestReportResult();
                if (requestReportResult.isSetReportRequestInfo()) { //requestReportResult.isSetReportRequestInfo() == START
                    ReportRequestInfo reportRequestInfo = requestReportResult.getReportRequestInfo();
                    GenericValue amazonRequestReport = delegator.findOne("AmazonRequestReport", UtilMisc.toMap("productStoreId", productStoreId, "reportRequestId", reportRequestInfo.getReportRequestId()), false);
                    if (UtilValidate.isEmpty(amazonRequestReport)) {
                        amazonRequestReport = delegator.makeValue("AmazonRequestReport", UtilMisc.toMap("productStoreId", productStoreId, "reportRequestId", reportRequestInfo.getReportRequestId()));
                    }
                    if (reportRequestInfo.isSetReportType()) {
                        amazonRequestReport.set("reportType", reportRequestInfo.getReportType());
                        //Debug.logError("ReportType: " + reportRequestInfo.getReportType(), module);
                    }
                    if (reportRequestInfo.isSetStartDate()) {
                        amazonRequestReport.set("startDate", reportRequestInfo.getStartDate());
                        //Debug.logError("StartDate: " + reportRequestInfo.getStartDate(), module);
                    }
                    if (reportRequestInfo.isSetEndDate()) {
                        amazonRequestReport.set("endDate", reportRequestInfo.getEndDate());
                        //Debug.logError("EndDate: " + reportRequestInfo.getEndDate(), module);
                    }
                    if (reportRequestInfo.isSetSubmittedDate()) {
                        amazonRequestReport.set("submittedDate", reportRequestInfo.getSubmittedDate());
                        //Debug.logError("SubmittedDate: " + reportRequestInfo.getSubmittedDate(), module);
                    }
                    if (reportRequestInfo.isSetReportProcessingStatus()) {
                        amazonRequestReport.set("reportProcessingStatus", reportRequestInfo.getReportProcessingStatus());
                        //Debug.logError("ReportProcessingStatus: " + reportRequestInfo.getReportProcessingStatus(), module);
                    }
                    if (response.isSetResponseMetadata()) { //response.isSetResponseMetadata() == START
                        ResponseMetadata  responseMetadata = response.getResponseMetadata();
                        if (responseMetadata.isSetRequestId()) {
                            amazonRequestReport.set("responseMetadataRequestId", responseMetadata.getRequestId());
                            //Debug.logError("ResponseMetadataRequestId: " + responseMetadata.getRequestId(), module);
                        }
                    }   //response.isSetResponseMetadata() == END
                    delegator.createOrStore(amazonRequestReport);
                }   //requestReportResult.isSetReportRequestInfo() == END
            }   //if response.isSetRequestReportResult() TRUE == END
            //RESPONSE == END
            
        }   //main Try == END
        catch (GenericEntityException e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
        }
        catch (GenericServiceException e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
        }
        catch (DatatypeConfigurationException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        catch (MarketplaceWebServiceException ex) { //catch MarketplaceWebServiceException == START
            try {
                GenericValue amazonRequestReportError = delegator.findOne("AmazonRequestReportError", UtilMisc.toMap("productStoreId", productStoreId, "requestId", ex.getRequestId()), false);
                if (UtilValidate.isEmpty(amazonRequestReportError)) {
                    amazonRequestReportError = delegator.makeValue("AmazonRequestReportError", UtilMisc.toMap("productStoreId", productStoreId, "requestId", ex.getRequestId()));
                }
                amazonRequestReportError.set("serviceName", "GetReportRequestList");
                amazonRequestReportError.set("caughtException", ex.getMessage());
                amazonRequestReportError.set("responseStatusCode", ex.getStatusCode());
                amazonRequestReportError.set("errorCode", ex.getErrorCode());
                amazonRequestReportError.set("errorType", ex.getErrorType());
                amazonRequestReportError.set("xml", ex.getXML());
                amazonRequestReportError.set("responseHeaderMetadata", ex.getResponseHeaderMetadata());
                delegator.createOrStore(amazonRequestReportError);
                /*Debug.logError("Caught Exception: " + ex.getMessage(), module);
                 Debug.logError("Response Status Code: " + ex.getStatusCode(), module);
                 Debug.logError("Error Code: " + ex.getErrorCode(), module);
                 Debug.logError("Error Type: " + ex.getErrorType(), module);
                 Debug.logError("Request ID: " + ex.getRequestId(), module);
                 Debug.logError("XML: " + ex.getXML(), module);
                 Debug.logError("ResponseHeaderMetadata: " + ex.getResponseHeaderMetadata(), module);*/
            }
            catch (GenericEntityException e) {
                e.printStackTrace();
                //Debug.logError(e.getMessage(), module);
            }
        }   //catch MarketplaceWebServiceException == END
        return result;
    }   //amazonRequestReport
    
    public static Map<String, Object> getReportRequestList (DispatchContext dctx, Map context) {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Map<String, Object> result = ServiceUtil.returnSuccess();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String productStoreId = (String) context.get("accountName");
        productStoreId = productStoreId.trim();
        
        try {   //main try == START
            Map apiKeyMap = FastMap.newInstance();
            Map apiKeyMapService = dispatcher.runSync("getAmazonApiInfo", UtilMisc.toMap("accountName", productStoreId, "userLogin", userLogin));
            if (ServiceUtil.isSuccess(apiKeyMapService)) {    //if apiKeyMapService success -- START
                apiKeyMap = (Map) apiKeyMapService.get("apiKeyMap");
            }   //if apiKeyMapService success -- END
            
            String merchantId = (String) apiKeyMap.get("merchantId");
            String sellerDevAuthToken = (String) apiKeyMap.get("sellerDevAuthToken");
            MarketplaceWebService service = (MarketplaceWebService) apiKeyMap.get("marketplaceWebService");
            
            GetReportRequestListRequest request = new GetReportRequestListRequest();
            request.setMerchant( merchantId );
            if (UtilValidate.isNotEmpty(sellerDevAuthToken)) {
                request.setMWSAuthToken(sellerDevAuthToken);
            }
            
            //RESPONSE == START
            GetReportRequestListResponse response = service.getReportRequestList(request);
            if (response.isSetGetReportRequestListResult()) {
                Debug.logError("GetReportRequestListResult", module);
                GetReportRequestListResult  getReportRequestListResult = response.getGetReportRequestListResult();
                if (getReportRequestListResult.isSetNextToken()) {
                    Debug.logError("NextToken: " + getReportRequestListResult.getNextToken(), module);
                }
                if (getReportRequestListResult.isSetHasNext()) {
                    Debug.logError("HasNext: " + getReportRequestListResult.isHasNext(), module);
                }
                java.util.List<ReportRequestInfo> reportRequestInfoList = getReportRequestListResult.getReportRequestInfoList();
                for (ReportRequestInfo reportRequestInfo : reportRequestInfoList) {
                    Debug.logError("ReportRequestInfo", module);
                    if (reportRequestInfo.isSetReportRequestId()) {
                        Debug.logError("ReportRequestId: " + reportRequestInfo.getReportRequestId(), module);
                    }
                    if (reportRequestInfo.isSetGeneratedReportId()) {
                        Debug.logError("GeneratedReportId: " + reportRequestInfo.getGeneratedReportId(), module);
                    }
                    if (reportRequestInfo.isSetReportType()) {
                        Debug.logError("ReportType: " + reportRequestInfo.getReportType(), module);
                    }
                    if (reportRequestInfo.isSetStartDate()) {
                        Debug.logError("StartDate: " + reportRequestInfo.getStartDate(), module);
                    }
                    if (reportRequestInfo.isSetEndDate()) {
                        Debug.logError("EndDate: " + reportRequestInfo.getEndDate(), module);
                    }
                    if (reportRequestInfo.isSetSubmittedDate()) {
                        Debug.logError("SubmittedDate: " + reportRequestInfo.getSubmittedDate(), module);
                    }
                    if (reportRequestInfo.isSetCompletedDate()) {
                        Debug.logError("CompletedDate: " + reportRequestInfo.getCompletedDate(), module);
                    }
                    if (reportRequestInfo.isSetReportProcessingStatus()) {
                        Debug.logError("ReportProcessingStatus: " + reportRequestInfo.getReportProcessingStatus(), module);
                    }
                }
            }
            if (response.isSetResponseMetadata()) {
                Debug.logError("ResponseMetadata", module);
                ResponseMetadata  responseMetadata = response.getResponseMetadata();
                if (responseMetadata.isSetRequestId()) {
                    Debug.logError("RequestId: " + responseMetadata.getRequestId(), module);
                }
            }
            //RESPONSE == END
        }   //main try == END
        catch (GenericServiceException e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
        }
        catch (MarketplaceWebServiceException ex) { //catch MarketplaceWebServiceException == START
            try {
                GenericValue amazonRequestReportError = delegator.findOne("AmazonRequestReportError", UtilMisc.toMap("productStoreId", productStoreId, "requestId", ex.getRequestId()), false);
                if (UtilValidate.isEmpty(amazonRequestReportError)) {
                    amazonRequestReportError = delegator.makeValue("AmazonRequestReportError", UtilMisc.toMap("productStoreId", productStoreId, "requestId", ex.getRequestId()));
                }
                amazonRequestReportError.set("serviceName", "GetReportRequestList");
                amazonRequestReportError.set("caughtException", ex.getMessage());
                amazonRequestReportError.set("responseStatusCode", ex.getStatusCode());
                amazonRequestReportError.set("errorCode", ex.getErrorCode());
                amazonRequestReportError.set("errorType", ex.getErrorType());
                amazonRequestReportError.set("xml", ex.getXML());
                amazonRequestReportError.set("responseHeaderMetadata", ex.getResponseHeaderMetadata());
                delegator.createOrStore(amazonRequestReportError);
                /*Debug.logError("Caught Exception: " + ex.getMessage(), module);
                 Debug.logError("Response Status Code: " + ex.getStatusCode(), module);
                 Debug.logError("Error Code: " + ex.getErrorCode(), module);
                 Debug.logError("Error Type: " + ex.getErrorType(), module);
                 Debug.logError("Request ID: " + ex.getRequestId(), module);
                 Debug.logError("XML: " + ex.getXML(), module);
                 Debug.logError("ResponseHeaderMetadata: " + ex.getResponseHeaderMetadata(), module);*/
            }
            catch (GenericEntityException e) {
                e.printStackTrace();
                //Debug.logError(e.getMessage(), module);
            }
        }   //catch MarketplaceWebServiceException == END
        
        return result;
    }   //amazonGetReportRequestList
    
    public static Map<String, Object> getReport (DispatchContext dctx, Map context) {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Map<String, Object> result = ServiceUtil.returnSuccess();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String productStoreId = (String) context.get("accountName");
        String reportId = (String) context.get("reportId");
        productStoreId = productStoreId.trim();
        reportId = reportId.trim();
        
        try {   //main try == START
            File reportDirectory = new File("hot-deploy/gudao/webapp/gudao/amazon/report/" + productStoreId + "/");
            if (!reportDirectory.exists()) {
                boolean created = reportDirectory.mkdirs();
                if (!created) {
                    String errMsg = "Not create target directory";
                    Debug.logFatal(errMsg, module);
                    return ServiceUtil.returnError(errMsg);
                }
            }
            
            Map apiKeyMap = FastMap.newInstance();
            Map apiKeyMapService = dispatcher.runSync("getAmazonApiInfo", UtilMisc.toMap("accountName", productStoreId, "userLogin", userLogin));
            if (ServiceUtil.isSuccess(apiKeyMapService)) {    //if apiKeyMapService success -- START
                apiKeyMap = (Map) apiKeyMapService.get("apiKeyMap");
            }   //if apiKeyMapService success -- END
            
            String merchantId = (String) apiKeyMap.get("merchantId");
            String sellerDevAuthToken = (String) apiKeyMap.get("sellerDevAuthToken");
            MarketplaceWebService service = (MarketplaceWebService) apiKeyMap.get("marketplaceWebService");
            
            GetReportRequest request = new GetReportRequest();
            request.setMerchant(merchantId);
            request.setReportId(reportId);
            if (UtilValidate.isNotEmpty(sellerDevAuthToken)) {
                request.setMWSAuthToken(sellerDevAuthToken);
            }
            
            OutputStream report = new FileOutputStream("hot-deploy/gudao/webapp/gudao/amazon/report/" + productStoreId + "/" + reportId + ".xml");
            request.setReportOutputStream(report);
            
            //RESPONSE == START
            GetReportResponse response = service.getReport(request);
            Debug.logError("MD5Checksum: " + response.getGetReportResult().getMD5Checksum(), module);
            
            if (response.isSetResponseMetadata()) {
                Debug.logError("        ResponseMetadata", module);
                ResponseMetadata  responseMetadata = response.getResponseMetadata();
                if (responseMetadata.isSetRequestId()) {
                    Debug.logError("            RequestId", module);
                    Debug.logError("                " + responseMetadata.getRequestId(), module);
                }
            }
            
            Debug.logError("Report", module);
            Debug.logError("================================================================", module);
            Debug.logError( response.toXML() , module);
            
            //Debug.logError(response.getResponseHeaderMetadata(), module);
            //RESPONSE == END
        }   //main try == END
        catch (GenericServiceException e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
        }
        catch (MarketplaceWebServiceException ex) { //catch MarketplaceWebServiceException == START
            try {
                GenericValue amazonRequestReportError = delegator.findOne("AmazonRequestReportError", UtilMisc.toMap("productStoreId", productStoreId, "requestId", ex.getRequestId()), false);
                if (UtilValidate.isEmpty(amazonRequestReportError)) {
                    amazonRequestReportError = delegator.makeValue("AmazonRequestReportError", UtilMisc.toMap("productStoreId", productStoreId, "requestId", ex.getRequestId()));
                }
                amazonRequestReportError.set("serviceName", "GetReport");
                amazonRequestReportError.set("caughtException", ex.getMessage());
                amazonRequestReportError.set("responseStatusCode", ex.getStatusCode());
                amazonRequestReportError.set("errorCode", ex.getErrorCode());
                amazonRequestReportError.set("errorType", ex.getErrorType());
                amazonRequestReportError.set("xml", ex.getXML());
                amazonRequestReportError.set("responseHeaderMetadata", ex.getResponseHeaderMetadata());
                delegator.createOrStore(amazonRequestReportError);
                /*Debug.logError("Caught Exception: " + ex.getMessage(), module);
                 Debug.logError("Response Status Code: " + ex.getStatusCode(), module);
                 Debug.logError("Error Code: " + ex.getErrorCode(), module);
                 Debug.logError("Error Type: " + ex.getErrorType(), module);
                 Debug.logError("Request ID: " + ex.getRequestId(), module);
                 Debug.logError("XML: " + ex.getXML(), module);
                 Debug.logError("ResponseHeaderMetadata: " + ex.getResponseHeaderMetadata(), module);*/
            }
            catch (GenericEntityException e) {
                e.printStackTrace();
                //Debug.logError(e.getMessage(), module);
            }
        }   //catch MarketplaceWebServiceException == END
        
        return result;
    }   //getReport
    
}
