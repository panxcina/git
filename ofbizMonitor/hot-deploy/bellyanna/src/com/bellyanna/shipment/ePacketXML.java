package com.bellyanna.shipment;

import java.util.Map;

import javolution.util.FastMap;

public class ePacketXML {

public static final String module= ePacketXML.class.getName();

public static String accountInfoRequestXML(Object APIDevUserId, Object APIPassword, Object Version, Object APISellerUserId)
  {
     StringBuffer request = new StringBuffer("        <APIDevUserID>" + APIDevUserId + "</APIDevUserID>\r\n");
     request.append("        <APIPassword>" + APIPassword + "</APIPassword>\r\n");
     request.append("        <APISellerUserID>" + APISellerUserId + "</APISellerUserID>\r\n");
     request.append("        <Version>" + Version + "</Version>\r\n");

     return request.toString();
  }

public static String addAPACShippingPackageRequestXML(Map mapContent)
  {
     //Creates an APAC shipping package and generate a tracking number for the package

     StringBuffer request = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
     request.append("<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\r\n");
     request.append("  <soap12:Body>\r\n");
     request.append("    <AddAPACShippingPackage xmlns=\"http://shippingapi.ebay.cn/\">\r\n");
     request.append("      <AddAPACShippingPackageRequest>\r\n");
     request.append(accountInfoRequestXML(mapContent.get("APIDevUserId"), mapContent.get("APIPassword"), mapContent.get("APIVersion"), mapContent.get("APISellerUserId")));
     request.append("        <OrderDetail>\r\n");
     request.append("          <PickUpAddress>\r\n");
     request.append("            <Email>" + mapContent.get("PickUpEmail") + "</Email>\r\n");
     request.append("            <Company>" + mapContent.get("PickUpCompany") + "</Company>\r\n");		//Optional
     request.append("            <Country>" + mapContent.get("PickUpCountry") + "</Country>\r\n");
     request.append("            <Province>" + mapContent.get("PickUpProvince") + "</Province>\r\n");
     request.append("            <City>" + mapContent.get("PickUpCity") + "</City>\r\n");
     request.append("            <District>" + mapContent.get("PickUpDistrict") + "</District>\r\n");
     request.append("            <Street>" + mapContent.get("PickUpStreet") + "</Street>\r\n");
     request.append("            <Postcode>" + mapContent.get("PickUpPostcode") + "</Postcode>\r\n");
     request.append("            <Contact>" + mapContent.get("PickUpContact") + "</Contact>\r\n");
     request.append("            <Mobile>" + mapContent.get("PickUpMobile") + "</Mobile>\r\n");
     request.append("            <Phone>" + mapContent.get("PickUpPhone") + "</Phone>\r\n");
     request.append("          </PickUpAddress>\r\n");
     request.append("          <ShipFromAddress>\r\n");
     request.append("            <Contact>" + mapContent.get("ShipFromContact") + "</Contact>\r\n");
     request.append("            <Company>" + mapContent.get("ShipFromCompany") + "</Company>\r\n");		//Optional
     request.append("            <Street>" + mapContent.get("ShipFromStreet") + "</Street>\r\n");
     request.append("            <District>" + mapContent.get("ShipFromDistrict") + "</District>\r\n");
     request.append("            <City>" + mapContent.get("ShipFromCity") + "</City>\r\n");
     request.append("            <Province>" + mapContent.get("ShipFromProvince") + "</Province>\r\n");
     request.append("            <Postcode>" + mapContent.get("ShipFromPostcode") + "</Postcode>\r\n");
     request.append("            <Country>" + mapContent.get("ShipFromCountry") + "</Country>\r\n");
     request.append("            <Email>" + mapContent.get("ShipFromEmail") + "</Email>\r\n");
     request.append("            <Mobile>" + mapContent.get("ShipFromMobile") + "</Mobile>\r\n");
     request.append("          </ShipFromAddress>\r\n");
     request.append("          <ShipToAddress>\r\n");
     request.append("            <Email>" + mapContent.get("ShipToEmail") + "</Email>\r\n");
     //request.append("            <Company>" + mapContent.get("ShipToCompany") + "</Company>\r\n");		//Optional
     request.append("            <Contact>" + mapContent.get("ShipToContact") + "</Contact>\r\n");
     if (mapContent.get("ShipToPhone") != null || !"".equals(mapContent.get("ShipToPhone"))) {
    	 request.append("            <Phone>" + mapContent.get("ShipToPhone") + "</Phone>\r\n");		//Optional - maybe Required?
     }
     request.append("            <Street>" + mapContent.get("ShipToStreet") + "</Street>\r\n");
     request.append("            <City>" + mapContent.get("ShipToCity") + "</City>\r\n");
     request.append("            <Province>" + mapContent.get("ShipToProvince") + "</Province>\r\n");
     request.append("            <Postcode>" + mapContent.get("ShipToPostcode") + "</Postcode>\r\n");
     request.append("            <Country>" + mapContent.get("ShipToCountry") + "</Country>\r\n");
     request.append("            <CountryCode>" + mapContent.get("ShipToCountryCode") + "</CountryCode>\r\n");
     request.append("          </ShipToAddress>\r\n");
     request.append("          <ReturnAddress>\r\n");
     request.append("            <Email>" + mapContent.get("PickUpEmail") + "</Email>\r\n");
     request.append("            <Company>" + mapContent.get("PickUpCompany") + "</Company>\r\n");		//Optional
     request.append("            <Country>" + mapContent.get("PickUpCountry") + "</Country>\r\n");
     request.append("            <Province>" + mapContent.get("ReturnProvince") + "</Province>\r\n");
     request.append("            <City>" + mapContent.get("ReturnCity") + "</City>\r\n");
     request.append("            <District>" + mapContent.get("PickUpDistrict") + "</District>\r\n");
     request.append("            <Street>" + mapContent.get("PickUpStreet") + "</Street>\r\n");
     request.append("            <Postcode>" + mapContent.get("PickUpPostcode") + "</Postcode>\r\n");
     request.append("            <Contact>" + mapContent.get("PickUpContact") + "</Contact>\r\n");
     request.append("            <Mobile>" + mapContent.get("PickUpMobile") + "</Mobile>\r\n");
     request.append("            <Phone>" + mapContent.get("PickUpPhone") + "</Phone>\r\n");
     request.append("          </ReturnAddress>\r\n");
     request.append("          <ItemList>\r\n");
     //request.append(AddAPACShippingPackageItemList());			//call ItemList generator function
     //request.append("          </ItemList>\r\n");
     //request.append("          <EMSPickUpType>" + mapContent.get("EMSPickUpType") + "</EMSPickUpType>\r\n");	//Required; 0 - Door pickup; 1 - Seller send to carrier
     //request.append("        </OrderDetail>\r\n");
     //request.append("      </AddAPACShippingPackageRequest>\r\n");
     //request.append("    </AddAPACShippingPackage>\r\n");
     //request.append("  </soap12:Body>\r\n");
     //request.append("</soap12:Envelope>\r\n");

     return request.toString();

/* Response
  <soap12:Body>
    <AddAPACShippingPackageResponse xmlns="http://shippingapi.ebay.cn/">
      <AddAPACShippingPackageResult>
        <TrackCode>String</TrackCode>
      </AddAPACShippingPackageResult>
    </AddAPACShippingPackageResponse>
  </soap12:Body>
*/
  }

public static String AddAPACShippingPackageItemList(Map mapItem)
  {

     StringBuffer request = new StringBuffer("            <Item>\r\n");
     request.append("	          <EBayItemID>" + mapItem.get("eBayItemID") + "</EBayItemID>\r\n");
     request.append("	          <EBayTransactionID>" + mapItem.get("eBayTransactionID") + "</EBayTransactionID>\r\n");
     request.append("	          <EBayBuyerID>" + mapItem.get("eBayBuyerID") + "</EBayBuyerID>\r\n");
     request.append("	          <PostedQTY>" + mapItem.get("PostedQTY") + "</PostedQTY>\r\n");
     /*request.append("	      <EBayItemTitle>" +  + "</EBayItemTitle>\r\n");		//Optional
     request.append("	      <EBayEmail>" +  + "</EBayEmail>\r\n");			//Optional
     request.append("	      <SoldQTY>" +  + "</SoldQTY>\r\n");				//Optional
     request.append("	      <SalesRecordNumber>" +  + "</SalesRecordNumber>\r\n");	//Optional
     request.append("	      <OrderSalesRecordNumber>" +  + "</OrderSalesRecordNumber>\r\n");		//Optional
     request.append("	      <OrderID>" +  + "</OrderID>\r\n");				//Optional
     request.append("	      <EBaySiteID>" +  + "</EBaySiteID>\r\n");			//Optional
     request.append("	      <ReceivedAmount>" +  + "</ReceivedAmount>\r\n");		//Optional
     request.append("	      <PaymentDate>" +  + "</PaymentDate>\r\n");			//Optional
     request.append("	      <SoldPrice>" +  + "</SoldPrice>\r\n");			//Optional
     request.append("	      <SoldDate>" +  + "</SoldDate>\r\n");				//Optional
     request.append("	      <CurrencyCode>" +  + "</CurrencyCode>\r\n");			//Optional
     request.append("	      <EBayMessage>" +  + "</EBayMessage>\r\n");			//Optional
     request.append("	      <PayPalEmail>" +  + "</PayPalEmail>\r\n");			//Optional
     request.append("	      <PayPalMessage>" +  + "</PayPalMessage>\r\n");		//Optional
     request.append("	      <Note>" +  + "</Note>\r\n");					//Optional	*/
     request.append("	          <SKU>\r\n");
     //request.append("	            <SKUID>" +  + "</SKUID>\r\n");				//Optional
     request.append("	            <DeclaredValue>" + mapItem.get("DeclaredValue") + "</DeclaredValue>\r\n");			//Declared as 5
     request.append("	            <Weight>" + mapItem.get("Weight") + "</Weight>\r\n");
     request.append("	            <CustomsTitleCN>" + mapItem.get("CustomsTitleCN") + "</CustomsTitleCN>\r\n");
     request.append("	            <CustomsTitleEN>" + mapItem.get("CustomsTitleEN") + "</CustomsTitleEN>\r\n");
     request.append("	            <OriginCountryCode>CN</OriginCountryCode>\r\n");		//Set China as OriginCountryCode
     request.append("	            <OriginCountryName>China</OriginCountryName>\r\n");		//Set China as OriginCountryCode
     request.append("	          </SKU>\r\n");
     request.append("	        </Item>\r\n");

     return request.toString();
  }

public static String getAPACShippingLabelRequestXML(Map mapContent, Object trackingCode)
  {
     //Retrieves an APAC shipping package label in PDF format. Set PageSize to 0 for A4 paper, 1 to 4 inch thermal printer paper
     StringBuffer request = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
     request.append("<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\r\n");
     request.append("  <soap12:Body>\r\n");
     request.append("    <GetAPACShippingLabel xmlns=\"http://shippingapi.ebay.cn/\">\r\n");
     request.append("      <GetAPACShippingLabelRequest>\r\n");
     request.append(accountInfoRequestXML(mapContent.get("APIDevUserId"), mapContent.get("APIPassword"), mapContent.get("APIVersion"), mapContent.get("APISellerUserId")));
     request.append("        <TrackCode>" + trackingCode + "</TrackCode>\r\n");
     request.append("        <PageSize>" + mapContent.get("LabelPageSize") + "</PageSize>\r\n");
     request.append("      </GetAPACShippingLabelRequest>\r\n");
     request.append("    </GetAPACShippingLabel>\r\n");
     request.append("  </soap12:Body>\r\n");
     request.append("</soap12:Envelope>");

     return request.toString();
/*
 Response
  <soap12:Body>
    <GetAPACShippingLabelResponse xmlns="http://shippingapi.ebay.cn/">
      <GetAPACShippingLabelResult>
        <Label>base64Binary</Label>
      </GetAPACShippingLabelResult>
    </GetAPACShippingLabelResponse>
  </soap12:Body>
*/
  }

public static String confirmAPACShippingPackageRequestXML(Map mapContent, Object trackingCode)
  {
     //Confirms a shipping package. The package information will be uploaded to China Post system. Afterward, the package cannot be canceled or revised.

     StringBuffer request = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
     request.append("<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\r\n");
     request.append("  <soap12:Body>\r\n");
     request.append("    <ConfirmAPACShippingPackage xmlns=\"http://shippingapi.ebay.cn/\">\r\n");
     request.append("      <ConfirmAPACShippingPackageRequest>\r\n");
     request.append(accountInfoRequestXML(mapContent.get("APIDevUserId"), mapContent.get("APIPassword"), mapContent.get("APIVersion"), mapContent.get("APISellerUserId")));
     request.append("        <TrackCode>" + trackingCode + "</TrackCode>\r\n");
     request.append("      </ConfirmAPACShippingPackageRequest>\r\n");
     request.append("    </ConfirmAPACShippingPackage>\r\n");
     request.append("  </soap12:Body>\r\n");
     request.append("</soap12:Envelope>");

     return request.toString();

/* Response
  <soap12:Body>
    <ConfirmAPACShippingPackageResponse xmlns="http://shippingapi.ebay.cn/">
      <ConfirmAPACShippingPackageResult />
    </ConfirmAPACShippingPackageResponse>
  </soap12:Body>
*/
  }

public static String cancelAPACShippingPackageRequestXML(Map mapContent, Object trackingCode)		//TODO
  {
     //Cancels an APAC shipping package. A package can only be canceled when its status is New Order. After cancellation, the record will be permanently removed from database.

     StringBuffer request = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
     request.append("<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\r\n");
     request.append("  <soap12:Body>\r\n");
     request.append("    <CancelAPACShippingPackage xmlns=\"http://shippingapi.ebay.cn/\">\r\n");
     request.append("      <CancelAPACShippingPackageRequest>\r\n");
     request.append(accountInfoRequestXML(mapContent.get("APIDevUserId"), mapContent.get("APIPassword"), mapContent.get("APIVersion"), mapContent.get("APISellerUserId")));
     request.append("        <TrackCode>" + trackingCode + "</TrackCode>\r\n");
     request.append("      </CancelAPACShippingPackageRequest>\r\n");
     request.append("    </CancelAPACShippingPackage>\r\n");
     request.append("  </soap12:Body>\r\n");
     request.append("</soap12:Envelope>");

     return request.toString();

/* Response
  <soap12:Body>
    <CancelAPACShippingPackageResponse xmlns="http://shippingapi.ebay.cn/">
      <CancelAPACShippingPackageResult />
    </CancelAPACShippingPackageResponse>
  </soap12:Body>
*/
  }

public static String getAPACShippingPackageRequestXML(Map mapContent, Object trackingCode)		//TODO
  {
     //Retrieves an APAC shipping package. Full detailed information as if in the addAPACShippingPackageRequestXML

     StringBuffer request = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
     request.append("<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\r\n");
     request.append("  <soap12:Body>\r\n");
     request.append("    <GetAPACShippingPackage xmlns=\"http://shippingapi.ebay.cn/\">\r\n");
     request.append("      <GetAPACShippingPackageRequest>\r\n");
     request.append(accountInfoRequestXML(mapContent.get("APIDevUserId"), mapContent.get("APIPassword"), mapContent.get("APIVersion"), mapContent.get("APISellerUserId")));
     request.append("        <TrackCode>" + trackingCode + "</TrackCode>\r\n");
     request.append("      </GetAPACShippingPackageRequest>\r\n");
     request.append("    </GetAPACShippingPackage>\r\n");
     request.append("  </soap12:Body>\r\n");
     request.append("</soap12:Envelope>");

     return request.toString();

/* Response
  <soap12:Body>
    <GetAPACShippingPackageResponse xmlns="http://shippingapi.ebay.cn/">
      <GetAPACShippingPackageResult>
        <OrderDetail>
          <PickUpAddress>
            <Email>String</Email>
            <Company>String</Company>
            <Country>String</Country>
            <Province>String</Province>
            <City>String</City>
            <District>String</District>
            <Street>String</Street>
            <Postcode>String</Postcode>
            <Contact>String</Contact>
            <Mobile>String</Mobile>
            <Phone>String</Phone>
          </PickUpAddress>
          <ShipFromAddress>
            <Contact>String</Contact>
            <Company>String</Company>
            <Street>String</Street>
            <District>String</District>
            <City>String</City>
            <Province>String</Province>
            <Postcode>String</Postcode>
            <Country>String</Country>
            <Email>String</Email>
            <Mobile>String</Mobile>
          </ShipFromAddress>
          <ShipToAddress>
            <Email>String</Email>
            <Company>String</Company>
            <Contact>String</Contact>
            <Phone>String</Phone>
            <Street>String</Street>
            <City>String</City>
            <Province>String</Province>
            <Postcode>String</Postcode>
            <Country>String</Country>
            <CountryCode>String</CountryCode>
          </ShipToAddress>
          <ItemList>
            <Item xsi:nil="true" />
            <Item xsi:nil="true" />
          </ItemList>
          <EMSPickUpType>int</EMSPickUpType>
        </OrderDetail>
      </GetAPACShippingPackageResult>
    </GetAPACShippingPackageResponse>
  </soap12:Body>
*/
  }

public static String verifyAPACShippingUserRequestXML(Map mapContent)		//TODO
  {
     //Verify whether user authorization is success or not.

     StringBuffer request = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
     request.append("<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\r\n");
     request.append("  <soap12:Body>\r\n");
     request.append("    <VerifyAPACShippingUser xmlns=\"http://shippingapi.ebay.cn/\">\r\n");
     request.append("      <VerifyAPACShippingUserRequest>\r\n");
     request.append(accountInfoRequestXML(mapContent.get("APIDevUserId"), mapContent.get("APIPassword"), mapContent.get("APIVersion"), mapContent.get("APISellerUserId")));
     request.append("      </VerifyAPACShippingUserRequest>\r\n");
     request.append("    </VerifyAPACShippingUser>\r\n");
     request.append("  </soap12:Body>\r\n");
     request.append("</soap12:Envelope>");

     return request.toString();

/* Response
  <soap12:Body>
    <VerifyAPACShippingUserResponse xmlns="http://shippingapi.ebay/cn/">
      <VerifyAPACShippingUserResult />
    </VerifyAPACShippingUserResponse>
  </soap12:Body>
*/
  }

public static String getAPACShippingPackageStatusRequestXML(Map mapContent, Object trackingCode)
  {
     //Retrieves delivery status of an APAC shipping package.

     StringBuffer request = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
     request.append("<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\r\n");
     request.append("  <soap12:Body>\r\n");
     request.append("    <GetAPACShippingPackageStatus xmlns=\"http://shippingapi.ebay.cn/\">\r\n");
     request.append("      <GetAPACShippingPackageStatusRequest>\r\n");
     request.append(accountInfoRequestXML(mapContent.get("APIDevUserId"), mapContent.get("APIPassword"), mapContent.get("APIVersion"), mapContent.get("APISellerUserId")));
     request.append("        <TrackCode>" + trackingCode + "</TrackCode>\r\n");
     request.append("      </GetAPACShippingPackageStatusRequest>\r\n");
     request.append("    </GetAPACShippingPackageStatus>\r\n");
     request.append("  </soap12:Body>\r\n");
     request.append("</soap12:Envelope>");

     return request.toString();

/* Response
  <soap12:Body>
    <GetAPACShippingPackageStatusResponse xmlns="http://shippingapi.ebay.cn/">
      <GetAPACShippingPackageStatusResult>
        <Status>int</Status>
        <Note>String</Note>
      </GetAPACShippingPackageStatusResult>
    </GetAPACShippingPackageStatusResponse>
  </soap12:Body>
*/
  }

public static String getAPACShippingRateRequestXML(Map mapContent)	//TODO
  {
     //Calculates the shipping cost of an APAC shipping package.

     StringBuffer request = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
     request.append("<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\r\n");
     request.append("  <soap12:Body>\r\n");
     request.append("    <GetAPACShippingRate xmlns=\"http://shippingapi.ebay.cn/\">\r\n");
     request.append("      <GetAPACShippingRateRequest>\r\n");
     request.append(accountInfoRequestXML(mapContent.get("APIDevUserId"), mapContent.get("APIPassword"), mapContent.get("APIVersion"), mapContent.get("APISellerUserId")));
     request.append("        <ShipCode>int</ShipCode>\r\n");
     request.append("        <CountryCode>String</CountryCode>\r\n");
     request.append("        <Weight>decimal</Weight>\r\n");
     request.append("        <InsuranceType>int</InsuranceType>\r\n");		//Optional
     request.append("        <InsuranceAmount>decimal</InsuranceAmount>\r\n");	//Optional
     request.append("        <MailType>int</MailType>\r\n");
     request.append("      </GetAPACShippingRateRequest>\r\n");
     request.append("    </GetAPACShippingRate>\r\n");
     request.append("  </soap12:Body>\r\n");
     request.append("</soap12:Envelope>");

     return request.toString();

/* Response
  <soap12:Body>
    <GetAPACShippingRateResponse xmlns="http://shippingapi.ebay.cn/">
      <GetAPACShippingRateResult>
        <DeliveryCharge>decimal</DeliveryCharge>
        <AdditionalCharge>decimal</AdditionalCharge>
        <InsuranceFee>decimal</InsuranceFee>
      </GetAPACShippingRateResult>
    </GetAPACShippingRateResponse>
  </soap12:Body>
*/
  }

public static String recreateAPACShippingPackageRequestXML(Map mapContent, Object trackingCode)
  {
     //Recreates an already posted shipping package. A shipping package can only be Recreated when its status is In-Transit,Deliver Success or Delivery Fail. For some cases a seller may need to recreate a shipping package. After recreation, the shipping package's status will be reset to New Order.

     StringBuffer request = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
     request.append("<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\r\n");
     request.append("  <soap12:Body>\r\n");
     request.append("    <RecreateAPACShippingPackage xmlns=\"http://shippingapi.ebay.cn/\">\r\n");
     request.append("      <RecreateAPACShippingPackageRequest>\r\n");
     request.append(accountInfoRequestXML(mapContent.get("APIDevUserId"), mapContent.get("APIPassword"), mapContent.get("APIVersion"), mapContent.get("APISellerUserId")));
     request.append("        <TrackCode>" + trackingCode + "</TrackCode>\r\n");
     request.append("      </RecreateAPACShippingPackageRequest>\r\n");
     request.append("    </RecreateAPACShippingPackage>\r\n");
     request.append("  </soap12:Body>\r\n");
     request.append("</soap12:Envelope>");

     return request.toString();

/* Response
  <soap12:Body>
    <RecreateAPACShippingPackageResponse xmlns="http://shippingapi.ebay.cn/">
      <RecreateAPACShippingPackageResult>
        <TrackCode>String</TrackCode>
      </RecreateAPACShippingPackageResult>
    </RecreateAPACShippingPackageResponse>
  </soap12:Body>
*/
  }

public static String getAPACShippingTrackCodeRequestXML(Map mapContent, Object ebayItemId, Object ebayTransactionId)
  {
     //Retrieves the trackcode of a shipping package that includes the item. If the item is not included in any package, it returns null.

     StringBuffer request = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
     request.append("<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\r\n");
     request.append("  <soap12:Body>\r\n");
     request.append("    <GetAPACShippingTrackCode xmlns=\"http://shippingapi.ebay.cn/\">\r\n");
     request.append("      <GetAPACShippingTrackCodeRequest>\r\n");
     request.append(accountInfoRequestXML(mapContent.get("APIDevUserId"), mapContent.get("APIPassword"), mapContent.get("APIVersion"), mapContent.get("APISellerUserId")));
     request.append("        <EBayItemID>" + ebayItemId + "</EBayItemID>\r\n");
     request.append("        <EBayTransactionID>" + ebayTransactionId + "</EBayTransactionID>\r\n");
     request.append("      </GetAPACShippingTrackCodeRequest>\r\n");
     request.append("    </GetAPACShippingTrackCode>\r\n");
     request.append("  </soap12:Body>\r\n");
     request.append("</soap12:Envelope>");

     return request.toString();

/* Response
  <soap12:Body>
    <GetAPACShippingTrackCodeResponse xmlns="http://shippingapi.ebay.cn/">
      <GetAPACShippingTrackCodeResult>
        <TrackCode>String</TrackCode>
      </GetAPACShippingTrackCodeResult>
    </GetAPACShippingTrackCodeResponse>
  </soap12:Body>
*/
  }

}
