package com.bellyanna.ebay;
//http://developer.ebay.com/DevZone/XML/docs/WebHelp/wwhelp/wwhimpl/js/html/wwhelp.htm?context=eBay_XML_API&topic=StandardData
//Reference: http://developer.ebay.com/DevZone/XML/docs/Reference/ebay/GetItem.html

import java.util.Map;
import java.io.IOException;

import org.ofbiz.base.util.UtilXml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import com.bellyanna.common.bellyannaService;

public class requestXML {
	private static final String module = requestXML.class.getName();
	
/******Trading API XML - START *******/	

	public static String getItemRequestXML(Map mapContent)
	  {
          StringBuffer request = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
          request.append("<GetItemRequest xmlns=\"urn:ebay:apis:eBLBaseComponents\">\r\n");
          request.append("  <ItemID>" + mapContent.get("eBayItemId") + "</ItemID>\r\n");
          request.append("  <RequesterCredentials>\r\n");
          request.append("    <eBayAuthToken>" + mapContent.get("token") + "</eBayAuthToken>\r\n");
          request.append("  </RequesterCredentials>\r\n");
          request.append("  <WarningLevel>High</WarningLevel>\r\n");
          request.append("  <DetailLevel>ReturnAll</DetailLevel>\r\n");
          request.append("</GetItemRequest>");

          return request.toString();
	  }
	
	public static String completeSaleRequestXML(Map mapContent)
	  {
	     StringBuffer request = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
	     request.append("<CompleteSaleRequest xmlns=\"urn:ebay:apis:eBLBaseComponents\">\r\n");
	     request.append("  <RequesterCredentials>\r\n");
	     request.append("    <eBayAuthToken>" + mapContent.get("token") + "</eBayAuthToken>\r\n");
	     request.append("  </RequesterCredentials>\r\n");
	     request.append("  <WarningLevel>High</WarningLevel>\r\n");
	     request.append("  <ItemID>" + mapContent.get("eBayItemId") + "</ItemID>\r\n");
	     request.append("  <TransactionID>" + mapContent.get("eBayTransactionId") + "</TransactionID>\r\n");
	     if (mapContent.get("eBayOrderID") != null) {
		     request.append("  <OrderID>" + mapContent.get("eBayOrderId") + "</OrderID>\r\n");
	     }
	     //request.append("  <OrderLineItemID>" + mapContent.get("orderLineItemID") + "</OrderLineItemID>\r\n");
	     //request.append("  <Shipped>" + mapContent.get("shippedBoolean") + "</Shipped>\r\n");
	     request.append("  <Shipment>\r\n");
	     request.append("    <ShipmentTrackingDetails>\r\n");
	     request.append("      <ShipmentTrackingNumber>" + mapContent.get("trackingNumber") + "</ShipmentTrackingNumber>\r\n");
	     request.append("      <ShippingCarrierUsed>" + mapContent.get("shippingCarrier") + "</ShippingCarrierUsed>\r\n");
	     request.append("    </ShipmentTrackingDetails>\r\n");
	     request.append("  </Shipment>\r\n");
	     request.append("</CompleteSaleRequest>");

	     return request.toString();
	  }
	
	public static String getSellerListRequestXML(Map mapContent)
	{
	     StringBuffer request = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
	     request.append("<GetSellerListRequest xmlns=\"urn:ebay:apis:eBLBaseComponents\">\r\n");
	     request.append("  <RequesterCredentials>\r\n");
	     request.append("    <eBayAuthToken>" + mapContent.get("token") + "</eBayAuthToken>\r\n");
	     request.append("  </RequesterCredentials>\r\n");
	     //request.append("<!-- Call-specific Input Fields -->");
	     //request.append("  <AdminEndedItemsOnly> boolean </AdminEndedItemsOnly>");
	     //request.append("  <CategoryID> int </CategoryID>");
	     //request.append("  <EndTimeFrom> dateTime </EndTimeFrom>");
	     //request.append("  <EndTimeTo> dateTime </EndTimeTo>");
	     request.append("  <GranularityLevel>Fine</GranularityLevel>");
	     request.append("  <IncludeVariations>true</IncludeVariations>");
	     request.append("  <IncludeWatchCount>true</IncludeWatchCount>");
	     //request.append("  <MotorsDealerUsers> UserIDArrayType");
	     //request.append("    <UserID> UserIDType (string) </UserID>");
	     //request.append("    <!-- ... more UserID values allowed here ... -->");
	     //request.append("  </MotorsDealerUsers>");
	     request.append("  <Pagination>");
	     request.append("    <EntriesPerPage>100</EntriesPerPage>");
	     request.append("    <PageNumber>" + mapContent.get("pageNumber") + "</PageNumber>");
	     //request.append("	 <PageNumber>1</PageNumber>");
	     request.append("  </Pagination>");
	     //request.append("  <SKUArray>");
	     //request.append("    <SKU>TP-821A</SKU>");
	     //request.append("    <!-- ... more SKU values allowed here ... -->");
	     //request.append("  </SKUArray>");
	     request.append("  <Sort>2</Sort>");
	     request.append("  <EndTimeFrom>" + bellyannaService.ebayToday() + "</EndTimeFrom>");
	     request.append("  <EndTimeTo>" + bellyannaService.ebayToXDay(119) + "</EndTimeTo>");
	     request.append("  <UserID>" + mapContent.get("ebaySellerId") + "</UserID>");
	     //request.append("  <!-- Standard Input Fields -->");
	     //request.append("  <DetailLevel>ReturnAll</DetailLevel>");
	     //request.append("  <!-- ... more DetailLevel values allowed here ... -->");
	     //request.append("  <ErrorLanguage> string </ErrorLanguage>");
	     //request.append("  <MessageID> string </MessageID>");
        /*request.append("  <OutputSelector>ItemArray.Item.BuyItNowPrice</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.Country</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.CrossBorderTrade</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.Currency</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.DisableBuyerRequirements</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.eBayNowEligible</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.FreeAddedCategory</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.GetItFast</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.GiftIcon</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.HitCount</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.HitCounter</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.ItemID</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.ItemPolicyViolation</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.ListingCheckoutRedirectPreference</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.ListingDesigner</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.ListingDetails</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.ListingDuration</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.ListingEnhancement</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.ListingSubtype2</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.ListingType</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.Location</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.MotorsGermanySearchable</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.OutOfStockControl</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.PictureDetails</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.PrimaryCategory</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.PrivateListing</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.ProxyItem</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.Quantity</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.QuantityAvailableHint</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.QuantityThreshold</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.ReservePrice</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.ReturnPolicy</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.ReviseStatus</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.SecondaryCategory</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.SellerProfiles</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.SellingStatus</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.ShippingDetails</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.ShippingTermsInDescription</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.ShipToLocations</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.Site</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.SKU</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.SkypeContactOption</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.SkypeEnabled</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.SkypeID</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.StartPrice</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.Storefront</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.TimeLeft</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.Title</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.TotalQuestionCount</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.Variations</OutputSelector>");
        request.append("  <OutputSelector>ItemArray.Item.WatchCount</OutputSelector>");*/
	     //request.append("  <!-- ... more OutputSelector values allowed here ... -->");
	     //request.append("  <Version> string </Version>");
	     //request.append("  <WarningLevel> WarningLevelCodeType </WarningLevel>");
	     request.append("</GetSellerListRequest>");

	     return request.toString();
	}

	public static String getFeedbackRequestXML(Map mapContent)
	{
	     StringBuffer request = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
	     request.append("<GetFeedbackRequest  xmlns=\"urn:ebay:apis:eBLBaseComponents\">\r\n");
	     request.append("  <RequesterCredentials>\r\n");
	     request.append("    <eBayAuthToken>" + mapContent.get("token") + "</eBayAuthToken>\r\n");
	     request.append("  </RequesterCredentials>\r\n");
		 request.append("  <DetailLevel>ReturnAll</DetailLevel>");
		 request.append("  <FeedbackType>FeedbackReceivedAsSeller</FeedbackType>");
		 request.append("  <ItemID>" + mapContent.get("eBayItemId") + "</ItemID>");
		 request.append("  <TransactionID>" + mapContent.get("eBayTransactionId") + "</TransactionID>");
		 request.append("  <UserID>" + mapContent.get("eBayUserId") + "</UserID>");
		 request.append("</GetFeedbackRequest>");
		 
		 return request.toString();
	}
	
	public static String getItemsAwaitingFeedbackRequestXML(Map mapContent)
	{
	     StringBuffer request = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
	     request.append("<GetItemsAwaitingFeedback  xmlns=\"urn:ebay:apis:eBLBaseComponents\">\r\n");
	     request.append("  <RequesterCredentials>\r\n");
	     request.append("    <eBayAuthToken>" + mapContent.get("token") + "</eBayAuthToken>\r\n");
	     request.append("  </RequesterCredentials>\r\n");
		 request.append("  <Sort>FeedbackReceivedDescending</Sort>");
		 request.append("  <WarningLevel>High</WarningLevel>");
		 request.append("</GetItemsAwaitingFeedback>");
		 
		 return request.toString();
	}
    
    public static String addMemberMessageAAQToPartnerRequestRequestXML(Map mapContent)
	{
        StringBuffer request = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
        request.append("<AddMemberMessageAAQToPartnerRequest xmlns=\"urn:ebay:apis:eBLBaseComponents\">\r\n");
        request.append("  <RequesterCredentials>\r\n");
        request.append("    <eBayAuthToken>" + mapContent.get("token") + "</eBayAuthToken>\r\n");
        request.append("  </RequesterCredentials>\r\n");
        request.append("  <!-- Call-specific Input Fields -->");
        request.append("  <ItemID>" + mapContent.get("eBayItemId") + "</ItemID>");
        request.append("  <MemberMessage>");
        request.append("    <Body>" + mapContent.get("messageBody") + "</Body>");
        //request.append("    <EmailCopyToSender>true</EmailCopyToSender>");
        //request.append("    <HideSendersEmailAddress>true</HideSendersEmailAddress>");
        request.append("    <QuestionType>" + mapContent.get("questionType") + "</QuestionType>");
        request.append("    <RecipientID>" + mapContent.get("eBayUserId") + "</RecipientID>");
        request.append("    <!-- ... more RecipientID values allowed here ... -->");
        request.append("    <Subject>" + mapContent.get("messageSubject") + "</Subject>");
        request.append("  </MemberMessage>");
        request.append("  <!-- Standard Input Fields -->");
        //request.append("  <ErrorLanguage> string </ErrorLanguage>");
        //request.append("  <MessageID> string </MessageID>");
        //request.append("  <Version> string </Version>");
        //request.append("  <WarningLevel> WarningLevelCodeType </WarningLevel>");
        request.append("</AddMemberMessageAAQToPartnerRequest>");
        
        return request.toString();
	}
    
    public static String reviseFixedPriceItemRequestXML(Map mapContent)
	{
        StringBuffer request = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
        request.append("<ReviseFixedPriceItemRequest xmlns=\"urn:ebay:apis:eBLBaseComponents\">\r\n");
        request.append("  <RequesterCredentials>\r\n");
        request.append("    <eBayAuthToken>" + mapContent.get("token") + "</eBayAuthToken>\r\n");
        request.append("  </RequesterCredentials>\r\n");
        request.append("  <!-- Call-specific Input Fields -->");
        //<DeletedField> string </DeletedField>
        //<!-- ... more DeletedField values allowed here ... -->
        
        request.append("  <Item>");
        /*request.append("    /*<ApplicationData> string </ApplicationData>");
         request.append("    <AutoPay> boolean </AutoPay>");
         request.append("    <CategoryBasedAttributesPrefill> boolean </CategoryBasedAttributesPrefill>");
         request.append("    <CategoryMappingAllowed> boolean </CategoryMappingAllowed>");
         request.append("    <CeilingPrice> AmountType (double) </CeilingPrice>");
         request.append("    <ConditionDescription> string </ConditionDescription>");
         request.append("    <ConditionID> int </ConditionID>");
         request.append("    <Country> CountryCodeType </Country>");
         request.append("    <CrossBorderTrade> string </CrossBorderTrade>");
         request.append("    <!-- ... more CrossBorderTrade values allowed here ... -->");
         request.append("    <Description> string </Description>");
         request.append("    <DescriptionReviseMode> DescriptionReviseModeCodeType </DescriptionReviseMode>");
         request.append("    <DisableBuyerRequirements> boolean </DisableBuyerRequirements>");
         request.append("    <DiscountPriceInfo> DiscountPriceInfoType");
         request.append("      <MadeForOutletComparisonPrice> AmountType (double) </MadeForOutletComparisonPrice>");
         request.append("      <MinimumAdvertisedPrice> AmountType (double) </MinimumAdvertisedPrice>");
         request.append("      <MinimumAdvertisedPriceExposure> MinimumAdvertisedPriceExposureCodeType </MinimumAdvertisedPriceExposure>");
         request.append("      <OriginalRetailPrice> AmountType (double) </OriginalRetailPrice>");
         request.append("      <SoldOffeBay> boolean </SoldOffeBay>");
         request.append("      <SoldOneBay> boolean </SoldOneBay>");
         request.append("    </DiscountPriceInfo>");
         request.append("    <DispatchTimeMax> int </DispatchTimeMax>");
         request.append("    <FloorPrice> AmountType (double) </FloorPrice>");
         request.append("    <GetItFast> boolean </GetItFast>");
         request.append("    <GiftIcon> int </GiftIcon>");
         request.append("    <GiftServices> GiftServicesCodeType </GiftServices>");
         request.append("    <!-- ... more GiftServices values allowed here ... -->");
         request.append("    <HitCounter> HitCounterCodeType </HitCounter>");*/
        //request.append("    <InventoryTrackingMethod>SKU</InventoryTrackingMethod>");
        /*request.append("    /*<ItemCompatibilityList> ItemCompatibilityListType");
         request.append("      <Compatibility> ItemCompatibilityType");
         request.append("        <CompatibilityNotes> string </CompatibilityNotes>");
         request.append("        <Delete> boolean </Delete>");
         request.append("        <NameValueList> NameValueListType");
         request.append("          <Name> string </Name>");
         request.append("          <Value> string </Value>");
         request.append("          <!-- ... more Value values allowed here ... -->");
         request.append("        </NameValueList>");
         request.append("        <!-- ... more NameValueList nodes allowed here ... -->");
         request.append("      </Compatibility>");
         request.append("      <!-- ... more Compatibility nodes allowed here ... -->");
         request.append("      //<ReplaceAll> boolean </ReplaceAll>");
         request.append("    </ItemCompatibilityList>");*/
        request.append("    <ItemID>360507643256</ItemID>");
        /*request.append("    /*<ItemSpecifics> NameValueListArrayType");
         request.append("      <NameValueList> NameValueListType");
         request.append("        <Name> string </Name>");
         request.append("        <Value> string </Value>");
         request.append("        <!-- ... more Value values allowed here ... -->");
         request.append("      </NameValueList>");
         request.append("      <!-- ... more NameValueList nodes allowed here ... -->");
         request.append("    </ItemSpecifics>");
         request.append("    <ListingCheckoutRedirectPreference> ListingCheckoutRedirectPreferenceType");
         request.append("      <ProStoresStoreName> string </ProStoresStoreName>");
         request.append("      <SellerThirdPartyUsername> string </SellerThirdPartyUsername>");
         request.append("    </ListingCheckoutRedirectPreference>");
         request.append("    <ListingDesigner> ListingDesignerType");
         request.append("      <LayoutID> int </LayoutID>");
         request.append("      <OptimalPictureSize> boolean </OptimalPictureSize>");
         request.append("      <ThemeID> int </ThemeID>");
         request.append("    </ListingDesigner>");
         request.append("    <ListingDetails> ListingDetailsType");
         request.append("      <PayPerLeadEnabled> boolean </PayPerLeadEnabled>");
         request.append("    </ListingDetails>");
         request.append("    <ListingDuration> token </ListingDuration>");
         request.append("    <ListingEnhancement> ListingEnhancementsCodeType </ListingEnhancement>");
         request.append("    <!-- ... more ListingEnhancement values allowed here ... -->");
         request.append("    <Location> string </Location>");
         request.append("    <PaymentMethods> BuyerPaymentMethodCodeType </PaymentMethods>");
         request.append("    <!-- ... more PaymentMethods values allowed here ... -->");
         request.append("    <PayPalEmailAddress> string </PayPalEmailAddress>");*/
        /*request.append("    <PictureDetails> PictureDetailsType");
        request.append("      <ExternalPictureURL> anyURI </ExternalPictureURL>");
        request.append("      <GalleryDuration> token </GalleryDuration>");
        request.append("      <GalleryType> GalleryTypeCodeType </GalleryType>");
        request.append("      <GalleryURL> anyURI </GalleryURL>");
        request.append("      <PhotoDisplay> PhotoDisplayCodeType </PhotoDisplay>");
        request.append("      <PictureSource> PictureSourceCodeType </PictureSource>");
        request.append("      <PictureURL> anyURI </PictureURL>");
        request.append("      <!-- ... more PictureURL values allowed here ... -->");
        request.append("    </PictureDetails>");*/
        /*request.append("    /*<PostalCode> string </PostalCode>");
         request.append("    <PostCheckoutExperienceEnabled> boolean </PostCheckoutExperienceEnabled>");*/
        /*request.append("    <PrimaryCategory> CategoryType");
        request.append("      <CategoryID> string </CategoryID>");
        request.append("    </PrimaryCategory>");*/
        /*request.append("    /*<PrivateListing> boolean </PrivateListing>");
         request.append("    <PrivateNotes> string </PrivateNotes>");
         request.append("    <ProductListingDetails> ProductListingDetailsType");
         request.append("      <BrandMPN> BrandMPNType");
         request.append("        <Brand> string </Brand>");
         request.append("        <MPN> string </MPN>");
         request.append("      </BrandMPN>");
         request.append("      <EAN> string </EAN>");
         request.append("      <GTIN> string </GTIN>");
         request.append("      <IncludePrefilledItemInformation> boolean </IncludePrefilledItemInformation>");
         request.append("      <IncludeStockPhotoURL> boolean </IncludeStockPhotoURL>");
         request.append("      <ISBN> string </ISBN>");
         request.append("      <ListIfNoProduct> boolean </ListIfNoProduct>");
         request.append("      <ProductID> string </ProductID>");
         request.append("      <ProductReferenceID> string </ProductReferenceID>");
         request.append("      <ReturnSearchResultOnDuplicates> boolean </ReturnSearchResultOnDuplicates>");
         request.append("      <TicketListingDetails> TicketListingDetailsType");
         request.append("        <EventTitle> string </EventTitle>");
         request.append("        <PrintedDate> string </PrintedDate>");
         request.append("        <PrintedTime> string </PrintedTime>");
         request.append("        <Venue> string </Venue>");
         request.append("      </TicketListingDetails>");
         request.append("      <UPC> string </UPC>");
         request.append("      <UseFirstProduct> boolean </UseFirstProduct>");
         request.append("      <UseStockPhotoURLAsGallery> boolean </UseStockPhotoURLAsGallery>");
         request.append("    </ProductListingDetails>");
         request.append("    <Quantity> int </Quantity>");
         request.append("    <QuantityInfo> QuantityInfoType");
         request.append("      <MinimumRemnantSet> int </MinimumRemnantSet>");
         request.append("    </QuantityInfo>");
         request.append("    <QuantityRestrictionPerBuyer> QuantityRestrictionPerBuyerInfoType");
         request.append("      <MaximumQuantity> int </MaximumQuantity>");
         request.append("    </QuantityRestrictionPerBuyer>");
         request.append("    <ReturnPolicy> ReturnPolicyType");
         request.append("      <Description> string </Description>");
         request.append("      <EAN> string </EAN>");
         request.append("      <RefundOption> token </RefundOption>");
         request.append("      <RestockingFeeValueOption> token </RestockingFeeValueOption>");
         request.append("      <ReturnsAcceptedOption> token </ReturnsAcceptedOption>");
         request.append("      <ReturnsWithinOption> token </ReturnsWithinOption>");
         request.append("      <ShippingCostPaidByOption> token </ShippingCostPaidByOption>");
         request.append("      <WarrantyDurationOption> token </WarrantyDurationOption>");
         request.append("      <WarrantyOfferedOption> token </WarrantyOfferedOption>");
         request.append("      <WarrantyTypeOption> token </WarrantyTypeOption>");
         request.append("    </ReturnPolicy>");
         request.append("    <ScheduleTime> dateTime </ScheduleTime>");
         request.append("    <SecondaryCategory> CategoryType");
         request.append("      <CategoryID> string </CategoryID>");
         request.append("    </SecondaryCategory>");
         request.append("    <SellerProfiles> SellerProfilesType");
         request.append("      <SellerPaymentProfile> SellerPaymentProfileType");
         request.append("        <PaymentProfileID> long </PaymentProfileID>");
         request.append("        <PaymentProfileName> string </PaymentProfileName>");
         request.append("      </SellerPaymentProfile>");
         request.append("      <SellerReturnProfile> SellerReturnProfileType");
         request.append("        <ReturnProfileID> long </ReturnProfileID>");
         request.append("        <ReturnProfileName> string </ReturnProfileName>");
         request.append("      </SellerReturnProfile>");
         request.append("      <SellerShippingProfile> SellerShippingProfileType");
         request.append("        <ShippingProfileID> long </ShippingProfileID>");
         request.append("        <ShippingProfileName> string </ShippingProfileName>");
         request.append("      </SellerShippingProfile>");
         request.append("    </SellerProfiles>");
         request.append("    <SellerProvidedTitle> string </SellerProvidedTitle>");
         request.append("    <ShippingDetails> ShippingDetailsType");
         request.append("      <CalculatedShippingRate> CalculatedShippingRateType");
         request.append("        <MeasurementUnit> MeasurementSystemCodeType </MeasurementUnit>");
         request.append("        <OriginatingPostalCode> string </OriginatingPostalCode>");
         request.append("        <PackageDepth> MeasureType (decimal) </PackageDepth>");
         request.append("        <PackageLength> MeasureType (decimal) </PackageLength>");
         request.append("        <PackageWidth> MeasureType (decimal) </PackageWidth>");
         request.append("        <PackagingHandlingCosts> AmountType (double) </PackagingHandlingCosts>");
         request.append("        <ShippingIrregular> boolean </ShippingIrregular>");
         request.append("        <ShippingPackage> ShippingPackageCodeType </ShippingPackage>");
         request.append("        <WeightMajor> MeasureType (decimal) </WeightMajor>");
         request.append("        <WeightMinor> MeasureType (decimal) </WeightMinor>");
         request.append("      </CalculatedShippingRate>");
         request.append("      <CODCost> AmountType (double) </CODCost>");
         request.append("      <ExcludeShipToLocation> string </ExcludeShipToLocation>");
         request.append("      <!-- ... more ExcludeShipToLocation values allowed here ... -->");
         request.append("      <GlobalShipping> boolean </GlobalShipping>");
         request.append("      <InsuranceDetails> InsuranceDetailsType");
         request.append("        <InsuranceFee> AmountType (double) </InsuranceFee>");
         request.append("        <InsuranceOption> InsuranceOptionCodeType </InsuranceOption>");
         request.append("      </InsuranceDetails>");
         request.append("      <InsuranceFee> AmountType (double) </InsuranceFee>");
         request.append("      <InsuranceOption> InsuranceOptionCodeType </InsuranceOption>");
         request.append("      <InternationalInsuranceDetails> InsuranceDetailsType");
         request.append("        <InsuranceFee> AmountType (double) </InsuranceFee>");
         request.append("        <InsuranceOption> InsuranceOptionCodeType </InsuranceOption>");
         request.append("      </InternationalInsuranceDetails>");
         request.append("      <InternationalPromotionalShippingDiscount> boolean </InternationalPromotionalShippingDiscount>");
         request.append("      <InternationalShippingDiscountProfileID> string </InternationalShippingDiscountProfileID>");
         request.append("      <InternationalShippingServiceOption> InternationalShippingServiceOptionsType");
         request.append("        <ShippingService> token </ShippingService>");
         request.append("        <ShippingServiceAdditionalCost> AmountType (double) </ShippingServiceAdditionalCost>");
         request.append("        <ShippingServiceCost> AmountType (double) </ShippingServiceCost>");
         request.append("        <ShippingServicePriority> int </ShippingServicePriority>");
         request.append("        <ShipToLocation> string </ShipToLocation>");
         request.append("        <!-- ... more ShipToLocation values allowed here ... -->");
         request.append("      </InternationalShippingServiceOption>");
         request.append("      <!-- ... more InternationalShippingServiceOption nodes allowed here ... -->");
         request.append("      <PaymentInstructions> string </PaymentInstructions>");
         request.append("      <PromotionalShippingDiscount> boolean </PromotionalShippingDiscount>");
         request.append("      <RateTableDetails> RateTableDetailsType");
         request.append("        <DomesticRateTable> string </DomesticRateTable>");
         request.append("        <InternationalRateTable> string </InternationalRateTable>");
         request.append("      </RateTableDetails>");
         request.append("      <SalesTax> SalesTaxType");
         request.append("        <SalesTaxPercent> float </SalesTaxPercent>");
         request.append("        <SalesTaxState> string </SalesTaxState>");
         request.append("        <ShippingIncludedInTax> boolean </ShippingIncludedInTax>");
         request.append("      </SalesTax>");
         request.append("      <ShippingDiscountProfileID> string </ShippingDiscountProfileID>");
         request.append("      <ShippingServiceOptions> ShippingServiceOptionsType");
         request.append("        <FreeShipping> boolean </FreeShipping>");
         request.append("        <ShippingService> token </ShippingService>");
         request.append("        <ShippingServiceAdditionalCost> AmountType (double) </ShippingServiceAdditionalCost>");
         request.append("        <ShippingServiceCost> AmountType (double) </ShippingServiceCost>");
         request.append("        <ShippingServicePriority> int </ShippingServicePriority>");
         request.append("        <ShippingSurcharge> AmountType (double) </ShippingSurcharge>");
         request.append("      </ShippingServiceOptions>");
         request.append("      <!-- ... more ShippingServiceOptions nodes allowed here ... -->");
         request.append("      <ShippingType> ShippingTypeCodeType </ShippingType>");
         request.append("    </ShippingDetails>");
         request.append("    <ShippingPackageDetails> ShipPackageDetailsType");
         request.append("      <MeasurementUnit> MeasurementSystemCodeType </MeasurementUnit>");
         request.append("      <PackageDepth> MeasureType (decimal) </PackageDepth>");
         request.append("      <PackageLength> MeasureType (decimal) </PackageLength>");
         request.append("      <PackageWidth> MeasureType (decimal) </PackageWidth>");
         request.append("      <ShippingIrregular> boolean </ShippingIrregular>");
         request.append("      <ShippingPackage> ShippingPackageCodeType </ShippingPackage>");
         request.append("      <WeightMajor> MeasureType (decimal) </WeightMajor>");
         request.append("      <WeightMinor> MeasureType (decimal) </WeightMinor>");
         request.append("    </ShippingPackageDetails>");
         request.append("    <ShippingTermsInDescription> boolean </ShippingTermsInDescription>");
         request.append("    <ShipToLocations> string </ShipToLocations>");
         request.append("    <!-- ... more ShipToLocations values allowed here ... -->");*/
        //request.append("    <SKU>DR-BB1117</SKU>");
        /*request.append("    /*<SkypeContactOption> SkypeContactOptionCodeType </SkypeContactOption>");
         request.append("    <!-- ... more SkypeContactOption values allowed here ... -->");
         request.append("    <SkypeEnabled> boolean </SkypeEnabled>");
         request.append("    <SkypeID> string </SkypeID>");*/
        //request.append("    <StartPrice> AmountType (double) </StartPrice>");
        /*request.append("    /*<Storefront> StorefrontType");
         request.append("      <StoreCategory2ID> long </StoreCategory2ID>");
         request.append("      <StoreCategoryID> long </StoreCategoryID>");
         request.append("    </Storefront>");
         request.append("    <SubTitle> string </SubTitle>");
         request.append("    <TaxCategory> string </TaxCategory>");
         request.append("    <ThirdPartyCheckout> boolean </ThirdPartyCheckout>");
         request.append("    <ThirdPartyCheckoutIntegration> boolean </ThirdPartyCheckoutIntegration>");
         request.append("    <Title> string </Title>");
         request.append("    <UseRecommendedProduct> boolean </UseRecommendedProduct>");
         request.append("    <UseTaxTable> boolean </UseTaxTable>");
         request.append("    <UUID> UUIDType (string) </UUID>");*/
        request.append("    <Variations>");
        /*request.append("      /*<ModifyNameList> ModifyNameArrayType");
         request.append("        <ModifyName> ModifyNameType");
         request.append("          <Name> string </Name>");
         request.append("          <NewName> string </NewName>");
         request.append("        </ModifyName>");
         request.append("        <!-- ... more ModifyName nodes allowed here ... -->");
         request.append("      </ModifyNameList>");*/
         request.append("      <Pictures> PicturesType");
         request.append("        <VariationSpecificName>Colour</VariationSpecificName>");
         request.append("        <VariationSpecificPictureSet>");
         request.append("          <PictureURL>http://auih.merchantrunglobal.com/ImageHosting/ViewImage.aspx?GlobalID=1003&amp;MerchantID=5585&amp;ImageID=10479&amp;DisplaySize=-1</PictureURL>");
        //request.append("          <ExternalPictureURL>http://auih.merchantrunglobal.com/ImageHosting/ViewImage.aspx?GlobalID=1003&amp;MerchantID=5585&amp;ImageID=10479&amp;DisplaySize=-1</ExternalPictureURL>");
         request.append("          <!-- ... more PictureURL values allowed here ... -->");
         request.append("          <VariationSpecificValue>Blue</VariationSpecificValue>");
         request.append("        </VariationSpecificPictureSet>");
        request.append("        <VariationSpecificPictureSet>");
        request.append("          <PictureURL>http://auih.merchantrunglobal.com/ImageHosting/ViewImage.aspx?GlobalID=1003&amp;MerchantID=5585&amp;ImageID=10481&amp;DisplaySize=-1</PictureURL>");
        //request.append("          <ExternalPictureURL>http://auih.merchantrunglobal.com/ImageHosting/ViewImage.aspx?GlobalID=1003&amp;MerchantID=5585&amp;ImageID=10481&amp;DisplaySize=-1</ExternalPictureURL>");
        request.append("          <!-- ... more PictureURL values allowed here ... -->");
        request.append("          <VariationSpecificValue>Black</VariationSpecificValue>");
        request.append("        </VariationSpecificPictureSet>");
         //request.append("        <!-- ... more VariationSpecificPictureSet nodes allowed here ... -->");
         request.append("      </Pictures>");
/*        request.append("      <Variation>");
        request.append("        <Delete>false</Delete>");
        /*request.append("        /*<DiscountPriceInfo> DiscountPriceInfoType");
         request.append("          <MadeForOutletComparisonPrice> AmountType (double) </MadeForOutletComparisonPrice>");
         request.append("          <MinimumAdvertisedPrice> AmountType (double) </MinimumAdvertisedPrice>");
         request.append("          <MinimumAdvertisedPriceExposure> MinimumAdvertisedPriceExposureCodeType </MinimumAdvertisedPriceExposure>");
         request.append("          <OriginalRetailPrice> AmountType (double) </OriginalRetailPrice>");
         request.append("          <SoldOffeBay> boolean </SoldOffeBay>");
         request.append("          <SoldOneBay> boolean </SoldOneBay>");
         request.append("        </DiscountPriceInfo>");*/
/*        request.append("        <Quantity>5</Quantity>");
        request.append("        <SKU>HT-JM10292-BK</SKU>");
        request.append("        <StartPrice>16.99</StartPrice>");
        request.append("        <VariationSpecifics>");
        request.append("          <NameValueList>");
        request.append("            <Name>Colour</Name>");
        request.append("            <Value>Black</Value>");
        request.append("          </NameValueList>");
        request.append("        </VariationSpecifics>");
        request.append("      </Variation>");
        //request.append("      <!-- ... more Variation nodes allowed here ... -->");
        /*request.append("      /*<VariationSpecificsSet> NameValueListArrayType");
         request.append("        <NameValueList> NameValueListType");
         request.append("          <Name> string </Name>");
         request.append("          <Value> string </Value>");
         request.append("          <!-- ... more Value values allowed here ... -->");
         request.append("        </NameValueList>");
         request.append("        <!-- ... more NameValueList nodes allowed here ... -->");
         request.append("      </VariationSpecificsSet>");*/
        request.append("    </Variations>");
        /*request.append("    /*<VATDetails> VATDetailsType");
         request.append("      <BusinessSeller> boolean </BusinessSeller>");
         request.append("      <RestrictedToBusiness> boolean </RestrictedToBusiness>");
         request.append("      <VATPercent> float </VATPercent>");
         request.append("    </VATDetails>");
         request.append("    <VIN> string </VIN>");
         request.append("    <VRM> string </VRM>");*/
        request.append("  </Item>");
        /*request.append("  /*<!-- Standard Input Fields -->");
         request.append("  <ErrorLanguage> string </ErrorLanguage>");
         request.append("  <MessageID> string </MessageID>");
         request.append("  <Version> string </Version>");
         request.append("  <WarningLevel> WarningLevelCodeType </WarningLevel>");*/
        
        
        request.append("</ReviseFixedPriceItemRequest>");
        
        return request.toString();
	}
    
    
    public static String getMyMessagesRequestXML(Map mapContent)
	{
        StringBuffer request = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
        request.append("<GetMyMessagesRequest xmlns=\"urn:ebay:apis:eBLBaseComponents\">\r\n");
        request.append("  <RequesterCredentials>\r\n");
        request.append("    <eBayAuthToken>" + mapContent.get("token") + "</eBayAuthToken>\r\n");
        request.append("  </RequesterCredentials>\r\n");
        request.append("  <!-- Call-specific Input Fields -->");
        /*request.append("    <EndTime> dateTime </EndTime>\r\n");
        request.append("    <ExternalMessageIDs> MyMessagesExternalMessageIDArrayType\r\n");
        request.append("      <ExternalMessageID> MyMessagesExternalMessageIDType (string) </ExternalMessageID>\r\n");
        request.append("      <!-- ... more ExternalMessageID values allowed here ... -->\r\n");
        request.append("    </ExternalMessageIDs>\r\n");
        request.append("    <FolderID> long </FolderID>\r\n");
        request.append("    <IncludeHighPriorityMessageOnly> boolean </IncludeHighPriorityMessageOnly>\r\n");*/
        if (mapContent.get("messageIds") != null) { //if messageIds is not null -- START
            String[] messageIds = mapContent.get("messageIds").toString().split(",");
            request.append("    <MessageIDs>\r\n");
            for (int i = 0; i < messageIds.length; i++) {   //loop messageIds -- START
                String messageId = messageIds[i].trim();
                request.append("      <MessageID>" + messageId + "</MessageID>\r\n");
            }   //loop messageIds -- END
            request.append("    </MessageIDs>\r\n");
        }   //if messageIds is not null -- END
        
        /*request.append("    <Pagination> PaginationType\r\n");
        request.append("      <EntriesPerPage> int </EntriesPerPage>\r\n");
        request.append("      <PageNumber> int </PageNumber>\r\n");
        request.append("    </Pagination>\r\n");
        request.append("    <StartTime> dateTime </StartTime>\r\n");
        request.append("    <!-- Standard Input Fields -->\r\n");*/
        request.append("    <DetailLevel>" + mapContent.get("detailLevel") + "</DetailLevel>\r\n");
        /*request.append("    <!-- ... more DetailLevel values allowed here ... -->\r\n");
        request.append("    <ErrorLanguage> string </ErrorLanguage>\r\n");
        request.append("    <MessageID> string </MessageID>\r\n");
        request.append("    <OutputSelector> string </OutputSelector>\r\n");
        request.append("    <!-- ... more OutputSelector values allowed here ... -->\r\n");
        request.append("    <Version> string </Version>\r\n");
        request.append("    <WarningLevel> WarningLevelCodeType </WarningLevel>\r\n");*/
        
        request.append("</GetMyMessagesRequest>");
        
        return request.toString();
	}
    
    public static String getOrdersRequestXML(Map mapContent)
    {
        StringBuffer request = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
        request.append("<GetOrdersRequest xmlns=\"urn:ebay:apis:eBLBaseComponents\">\r\n");
        request.append("  <RequesterCredentials>\r\n");
        request.append("    <eBayAuthToken>" + mapContent.get("token") + "</eBayAuthToken>\r\n");
        request.append("  </RequesterCredentials>\r\n");
        request.append("    <!-- Call-specific Input Fields -->\r\n");
        //request.append("    <CreateTimeFrom> dateTime </CreateTimeFrom>\r\n");
        //request.append("    <CreateTimeTo> dateTime </CreateTimeTo>\r\n");
        request.append("    <IncludeFinalValueFee>true</IncludeFinalValueFee>\r\n");
        //request.append("    <ListingType> ListingTypeCodeType </ListingType>\r\n");
        //request.append("    <ModTimeFrom> dateTime </ModTimeFrom>\r\n");
        //request.append("    <ModTimeTo> dateTime </ModTimeTo>\r\n");
        request.append("    <NumberOfDays>2</NumberOfDays>\r\n");
        /*request.append("    <OrderIDArray> OrderIDArrayType\r\n");
        request.append("        <OrderID> OrderIDType (string) </OrderID>\r\n");
        request.append("        <!-- ... more OrderID values allowed here ... -->\r\n");
        request.append("    </OrderIDArray>\r\n");*/
        request.append("    <OrderRole>Seller</OrderRole>\r\n");
        request.append("    <OrderStatus>Completed</OrderStatus>\r\n");
        request.append("    <Pagination>\r\n");
        request.append("        <EntriesPerPage>100</EntriesPerPage>\r\n");
        request.append("        <PageNumber>" + mapContent.get("pageNumber") + "</PageNumber>\r\n");
        request.append("    </Pagination>\r\n");
        request.append("    <!-- Standard Input Fields -->\r\n");
        request.append("    <DetailLevel>ReturnAll</DetailLevel>\r\n");
        request.append("    <!-- ... more DetailLevel values allowed here ... -->\r\n");
        //request.append("    <ErrorLanguage> string </ErrorLanguage>\r\n");
        //request.append("    <MessageID> string </MessageID>\r\n");
        //request.append("    <OutputSelector> string </OutputSelector>\r\n");
        //request.append("    <!-- ... more OutputSelector values allowed here ... -->\r\n");
        //request.append("    <Version> string </Version>\r\n");
        //request.append("    <WarningLevel> WarningLevelCodeType </WarningLevel>\r\n");
        request.append("</GetOrdersRequest>\r\n");
        
        return request.toString();
    }
    
    public static String getCategoriesRequestXML(Map mapContent)
    {
        StringBuffer request = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
        request.append("<GetCategoriesRequest xmlns=\"urn:ebay:apis:eBLBaseComponents\">\r\n");
        request.append("  <RequesterCredentials>\r\n");
        request.append("    <eBayAuthToken>" + mapContent.get("token") + "</eBayAuthToken>\r\n");
        request.append("  </RequesterCredentials>\r\n");
        request.append("    <!-- Call-specific Input Fields -->\r\n");
        if (mapContent.get("categoryParentId") != null) {
            request.append("    <CategoryParent>" + mapContent.get("categoryParentId") + "</CategoryParent>\r\n");
        }
        request.append("    <CategorySiteID>" + mapContent.get("categorySiteId") + "</CategorySiteID>\r\n");
        if (mapContent.get("levelLimit") != null) {
            request.append("    <LevelLimit>" + mapContent.get("levelLimit") + "</LevelLimit>\r\n");
        }
        request.append("    <ViewAllNodes>" + mapContent.get("viewAllNodes") + "</ViewAllNodes>\r\n");
        request.append("    <!-- Standard Input Fields -->\r\n");
        request.append("    <DetailLevel>ReturnAll</DetailLevel>\r\n");
        request.append("    <!-- ... more DetailLevel values allowed here ... -->\r\n");
        //request.append("    <ErrorLanguage> string </ErrorLanguage>\r\n");
        //request.append("    <MessageID> string </MessageID>\r\n");
        //request.append("    <OutputSelector> string </OutputSelector>\r\n");
        //request.append("    <!-- ... more OutputSelector values allowed here ... -->\r\n");
        //request.append("    <Version> string </Version>\r\n");
        //request.append("    <WarningLevel> WarningLevelCodeType </WarningLevel>\r\n");
        request.append("</GetCategoriesRequest>\r\n");
        
        return request.toString();
    }
    
    public static String addFixedPriceItemRequestXMLOriginal(Map mapContent)
    {
        String description = mapContent.get("description").toString();
        StringBuffer request = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
        request.append("<AddFixedPriceItemRequest xmlns=\"urn:ebay:apis:eBLBaseComponents\">\r\n");
        request.append("  <RequesterCredentials>\r\n");
        request.append("    <eBayAuthToken>" + mapContent.get("token") + "</eBayAuthToken>\r\n");
        request.append("  </RequesterCredentials>\r\n");
        //request.append("  <!-- Call-specific Input Fields -->\r\n");
        request.append("  <Item>\r\n");
        /*request.append("    <ApplicationData> string </ApplicationData>\r\n");
        request.append("    <AutoPay>false</AutoPay>\r\n");
        request.append("    <BestOfferDetails>\r\n");
        request.append("      <BestOfferEnabled> boolean </BestOfferEnabled>\r\n");
        request.append("    </BestOfferDetails>\r\n");
        request.append("    <BuyerRequirementDetails> BuyerRequirementDetailsType\r\n");
        request.append("      <LinkedPayPalAccount> boolean </LinkedPayPalAccount>\r\n");
        request.append("      <MaximumBuyerPolicyViolations> MaximumBuyerPolicyViolationsType\r\n");
        request.append("        <Count> int </Count>\r\n");
        request.append("        <Period> PeriodCodeType </Period>\r\n");
        request.append("      </MaximumBuyerPolicyViolations>\r\n");
        request.append("      <MaximumItemRequirements> MaximumItemRequirementsType\r\n");
        request.append("        <MaximumItemCount> int </MaximumItemCount>\r\n");
        request.append("        <MinimumFeedbackScore> int </MinimumFeedbackScore>\r\n");
        request.append("      </MaximumItemRequirements>\r\n");
        request.append("      <MaximumUnpaidItemStrikesInfo> MaximumUnpaidItemStrikesInfoType\r\n");
        request.append("        <Count> int </Count>\r\n");
        request.append("        <Period> PeriodCodeType </Period>\r\n");
        request.append("      </MaximumUnpaidItemStrikesInfo>\r\n");
        request.append("      <MinimumFeedbackScore> int </MinimumFeedbackScore>\r\n");
        request.append("      <ShipToRegistrationCountry> boolean </ShipToRegistrationCountry>\r\n");
        request.append("      <VerifiedUserRequirements> VerifiedUserRequirementsType\r\n");
        request.append("        <MinimumFeedbackScore> int </MinimumFeedbackScore>\r\n");
        request.append("        <VerifiedUser> boolean </VerifiedUser>\r\n");
        request.append("      </VerifiedUserRequirements>\r\n");
        request.append("      <ZeroFeedbackScore> boolean </ZeroFeedbackScore>\r\n");
        request.append("    </BuyerRequirementDetails>\r\n");
        request.append("    <CategoryBasedAttributesPrefill> boolean </CategoryBasedAttributesPrefill>\r\n");
        request.append("    <CategoryMappingAllowed> boolean </CategoryMappingAllowed>\r\n");
        request.append("    <Charity> CharityType\r\n");
        request.append("      <CharityID> string </CharityID>\r\n");
        request.append("      <CharityNumber> int </CharityNumber>\r\n");
        request.append("      <DonationPercent> float </DonationPercent>\r\n");
        request.append("    </Charity>\r\n");
        request.append("    <ConditionDescription> string </ConditionDescription>\r\n");*/
        request.append("    <ConditionID>1000</ConditionID>\r\n");
        request.append("    <Country>CN</Country>\r\n");
        //request.append("    <CrossBorderTrade> string </CrossBorderTrade>\r\n");
        //request.append("    <!-- ... more CrossBorderTrade values allowed here ... -->\r\n");
        request.append("    <Currency>USD</Currency>\r\n");
        request.append("    <Description>" + description + "</Description>\r\n"); //TODO
        /*request.append("    <DisableBuyerRequirements> boolean </DisableBuyerRequirements>\r\n");
        request.append("    <DiscountPriceInfo> DiscountPriceInfoType\r\n");
        request.append("      <MadeForOutletComparisonPrice> AmountType (double) </MadeForOutletComparisonPrice>\r\n");
        request.append("      <MinimumAdvertisedPrice> AmountType (double) </MinimumAdvertisedPrice>\r\n");
        request.append("      <MinimumAdvertisedPriceExposure> MinimumAdvertisedPriceExposureCodeType </MinimumAdvertisedPriceExposure>\r\n");
        request.append("      <OriginalRetailPrice> AmountType (double) </OriginalRetailPrice>\r\n");
        request.append("      <SoldOffeBay> boolean </SoldOffeBay>\r\n");
        request.append("      <SoldOneBay> boolean </SoldOneBay>\r\n");
        request.append("    </DiscountPriceInfo>\r\n");*/
        request.append("    <DispatchTimeMax>1</DispatchTimeMax>\r\n");
        request.append("    <GetItFast>false</GetItFast>\r\n");
        request.append("    <GiftIcon>0</GiftIcon>\r\n");
        //request.append("    <GiftServices> GiftServicesCodeType </GiftServices>\r\n");
        //request.append("    <!-- ... more GiftServices values allowed here ... -->\r\n");
        request.append("    <HitCounter>RetroStyle</HitCounter>\r\n");
        request.append("    <IncludeRecommendations>false</IncludeRecommendations>\r\n");   //TODO
        /*request.append("    <InventoryTrackingMethod> InventoryTrackingMethodCodeType </InventoryTrackingMethod>\r\n");
        request.append("    <ItemCompatibilityList> ItemCompatibilityListType\r\n");
        request.append("      <Compatibility> ItemCompatibilityType\r\n");
        request.append("        <CompatibilityNotes> string </CompatibilityNotes>\r\n");
        request.append("        <NameValueList> NameValueListType\r\n");
        request.append("          <Name> string </Name>\r\n");
        request.append("          <Value> string </Value>\r\n");
        request.append("          <!-- ... more Value values allowed here ... -->\r\n");
        request.append("        </NameValueList>\r\n");
        request.append("        <!-- ... more NameValueList nodes allowed here ... -->\r\n");
        request.append("      </Compatibility>\r\n");
        request.append("      <!-- ... more Compatibility nodes allowed here ... -->\r\n");
        request.append("    </ItemCompatibilityList>\r\n");*/
        request.append("    <ItemSpecifics>\r\n");   //Brand, Model, Material, Color
        request.append("      <NameValueList>\r\n");
        request.append("        <Name>Material</Name>\r\n");
        request.append("        <Value>304 Medical Stainless steel</Value>\r\n");
        //request.append("        <!-- ... more Value values allowed here ... -->\r\n");
        request.append("      </NameValueList>\r\n");
        //request.append("      <!-- ... more NameValueList nodes allowed here ... -->\r\n");
        request.append("    </ItemSpecifics>\r\n");
        /*request.append("    <ListingCheckoutRedirectPreference> ListingCheckoutRedirectPreferenceType\r\n");
        request.append("      <ProStoresStoreName> string </ProStoresStoreName>\r\n");
        request.append("      <SellerThirdPartyUsername> string </SellerThirdPartyUsername>\r\n");
        request.append("    </ListingCheckoutRedirectPreference>\r\n");
        request.append("    <ListingDesigner> ListingDesignerType\r\n");
        request.append("      <LayoutID> int </LayoutID>\r\n");
        request.append("      <OptimalPictureSize> boolean </OptimalPictureSize>\r\n");
        request.append("      <ThemeID> int </ThemeID>\r\n");
        request.append("    </ListingDesigner>\r\n");
        request.append("    <ListingDetails> ListingDetailsType\r\n");
        request.append("      <BestOfferAutoAcceptPrice> AmountType (double) </BestOfferAutoAcceptPrice>\r\n");
        request.append("      <LocalListingDistance> string </LocalListingDistance>\r\n");
        request.append("      <MinimumBestOfferPrice> AmountType (double) </MinimumBestOfferPrice>\r\n");
        request.append("    </ListingDetails>\r\n");*/
        request.append("    <ListingDuration>GTC</ListingDuration>\r\n");
        //request.append("    <ListingEnhancement> ListingEnhancementsCodeType </ListingEnhancement>\r\n");
        //request.append("    <!-- ... more ListingEnhancement values allowed here ... -->\r\n");
        request.append("    <ListingType>FixedPriceItem</ListingType>\r\n");
        request.append("    <Location>Shanghai, China</Location>\r\n");
        //request.append("    <OutOfStockControl> boolean </OutOfStockControl>\r\n");
        request.append("    <PaymentMethods>PayPal</PaymentMethods>\r\n");
        //request.append("    <!-- ... more PaymentMethods values allowed here ... -->\r\n");
        request.append("    <PayPalEmailAddress>dee.pan@live.cn</PayPalEmailAddress>\r\n");
        /*request.append("    <PickupInStoreDetails> PickupInStoreDetailsType\r\n");
        request.append("      <EligibleForPickupInStore> boolean </EligibleForPickupInStore>\r\n");
        request.append("    </PickupInStoreDetails>\r\n");*/
        request.append("    <PictureDetails>\r\n");
        //request.append("      <ExternalPictureURL>http://images.tattootalks.com/Needles/RL-Series.jpg</ExternalPictureURL>\r\n");
        //request.append("      <GalleryDuration> token </GalleryDuration>\r\n");
        request.append("      <GalleryType>Gallery</GalleryType>\r\n");
        request.append("      <GalleryURL>http://images.tattootalks.com/Needles/RL-Series.jpg</GalleryURL>\r\n");
        request.append("      <PhotoDisplay>PicturePack</PhotoDisplay>\r\n");
        request.append("      <PictureSource>Vendor</PictureSource>\r\n");
        request.append("      <PictureURL>http://images.tattootalks.com/Needles/RL-Series.jpg</PictureURL>\r\n");
        //request.append("      <!-- ... more PictureURL values allowed here ... -->\r\n");
        request.append("    </PictureDetails>\r\n");
        //request.append("    <PostalCode> string </PostalCode>\r\n");
        request.append("    <PostCheckoutExperienceEnabled>false</PostCheckoutExperienceEnabled>\r\n");
        request.append("    <PrimaryCategory>\r\n");
        request.append("      <CategoryID>33918</CategoryID>\r\n");
        request.append("    </PrimaryCategory>\r\n");
        request.append("    <PrivateListing>true</PrivateListing>\r\n");
        /*request.append("    <PrivateNotes> string </PrivateNotes>\r\n");
        request.append("    <ProductListingDetails> ProductListingDetailsType\r\n");
        request.append("      <BrandMPN> BrandMPNType\r\n");
        request.append("        <Brand> string </Brand>\r\n");
        request.append("        <MPN> string </MPN>\r\n");
        request.append("      </BrandMPN>\r\n");
        request.append("      <EAN> string </EAN>\r\n");
        request.append("      <GTIN> string </GTIN>\r\n");
        request.append("      <IncludePrefilledItemInformation> boolean </IncludePrefilledItemInformation>\r\n");
        request.append("      <IncludeStockPhotoURL> boolean </IncludeStockPhotoURL>\r\n");
        request.append("      <ISBN> string </ISBN>\r\n");
        request.append("      <ListIfNoProduct> boolean </ListIfNoProduct>\r\n");
        request.append("      <ProductID> string </ProductID>\r\n");
        request.append("      <ProductReferenceID> string </ProductReferenceID>\r\n");
        request.append("      <ReturnSearchResultOnDuplicates> boolean </ReturnSearchResultOnDuplicates>\r\n");
        request.append("      <TicketListingDetails> TicketListingDetailsType\r\n");
        request.append("        <EventTitle> string </EventTitle>\r\n");
        request.append("        <PrintedDate> string </PrintedDate>\r\n");
        request.append("        <PrintedTime> string </PrintedTime>\r\n");
        request.append("        <Venue> string </Venue>\r\n");
        request.append("      </TicketListingDetails>\r\n");
        request.append("      <UPC> string </UPC>\r\n");
        request.append("      <UseFirstProduct> boolean </UseFirstProduct>\r\n");
        request.append("      <UseStockPhotoURLAsGallery> boolean </UseStockPhotoURLAsGallery>\r\n");
        request.append("    </ProductListingDetails>\r\n");
        request.append("    <Quantity> int </Quantity>\r\n");
        request.append("    <QuantityInfo> QuantityInfoType\r\n");
        request.append("      <MinimumRemnantSet> int </MinimumRemnantSet>\r\n");
        request.append("    </QuantityInfo>\r\n");
        request.append("    <QuantityRestrictionPerBuyer> QuantityRestrictionPerBuyerInfoType\r\n");
        request.append("      <MaximumQuantity> int </MaximumQuantity>\r\n");
        request.append("    </QuantityRestrictionPerBuyer>\r\n");*/
        request.append("    <ReturnPolicy>\r\n");
        request.append("      <Description><![CDATA[We accept returns if you are unhappy with the purchase only when the product is unused, unopened, still properly sealed, especially for consumable products like needles, etc. Please contact us via eBay message prior any returns.]]></Description>\r\n");
        //request.append("      <EAN> string </EAN>\r\n");
        request.append("      <RefundOption>MoneyBack</RefundOption>\r\n");
        //request.append("      <RestockingFeeValueOption> token </RestockingFeeValueOption>\r\n");
        request.append("      <ReturnsAcceptedOption>ReturnsAccepted</ReturnsAcceptedOption>\r\n");
        request.append("      <ReturnsWithinOption>Days_30</ReturnsWithinOption>\r\n");
        request.append("      <ShippingCostPaidByOption>Buyer</ShippingCostPaidByOption>\r\n");
        /*request.append("      <WarrantyDurationOption> token </WarrantyDurationOption>\r\n");
        request.append("      <WarrantyOfferedOption> token </WarrantyOfferedOption>\r\n");
        request.append("      <WarrantyTypeOption> token </WarrantyTypeOption>\r\n");*/
        request.append("    </ReturnPolicy>\r\n");
        /*request.append("    <ScheduleTime> dateTime </ScheduleTime>\r\n");
        request.append("    <SecondaryCategory> CategoryType\r\n");
        request.append("      <CategoryID> string </CategoryID>\r\n");
        request.append("    </SecondaryCategory>\r\n");
        request.append("    <SellerProfiles> SellerProfilesType\r\n");
        request.append("      <SellerPaymentProfile> SellerPaymentProfileType\r\n");
        request.append("        <PaymentProfileID> long </PaymentProfileID>\r\n");
        request.append("        <PaymentProfileName> string </PaymentProfileName>\r\n");
        request.append("      </SellerPaymentProfile>\r\n");
        request.append("      <SellerReturnProfile> SellerReturnProfileType\r\n");
        request.append("        <ReturnProfileID> long </ReturnProfileID>\r\n");
        request.append("        <ReturnProfileName> string </ReturnProfileName>\r\n");
        request.append("      </SellerReturnProfile>\r\n");
        request.append("      <SellerShippingProfile> SellerShippingProfileType\r\n");
        request.append("        <ShippingProfileID> long </ShippingProfileID>\r\n");
        request.append("        <ShippingProfileName> string </ShippingProfileName>\r\n");
        request.append("      </SellerShippingProfile>\r\n");
        request.append("    </SellerProfiles>\r\n");*/
        request.append("    <ShippingDetails>\r\n");
        /*request.append("      <CalculatedShippingRate> CalculatedShippingRateType\r\n");
        request.append("        <MeasurementUnit> MeasurementSystemCodeType </MeasurementUnit>\r\n");
        request.append("        <OriginatingPostalCode> string </OriginatingPostalCode>\r\n");
        request.append("        <PackageDepth> MeasureType (decimal) </PackageDepth>\r\n");
        request.append("        <PackageLength> MeasureType (decimal) </PackageLength>\r\n");
        request.append("        <PackageWidth> MeasureType (decimal) </PackageWidth>\r\n");
        request.append("        <PackagingHandlingCosts> AmountType (double) </PackagingHandlingCosts>\r\n");
        request.append("        <ShippingIrregular> boolean </ShippingIrregular>\r\n");
        request.append("        <ShippingPackage> ShippingPackageCodeType </ShippingPackage>\r\n");
        request.append("        <WeightMajor> MeasureType (decimal) </WeightMajor>\r\n");
        request.append("        <WeightMinor> MeasureType (decimal) </WeightMinor>\r\n");
        request.append("      </CalculatedShippingRate>\r\n");
        request.append("      <CODCost> AmountType (double) </CODCost>\r\n");*/
        //request.append("      <ExcludeShipToLocation> string </ExcludeShipToLocation>\r\n");    //TODO
        request.append("      <ExcludeShipToLocation>GT</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>VA</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>PG</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>SB</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>GP</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>SL</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>MH</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>NR</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>WF</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>GF</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>AI</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>GM</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>SV</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>FM</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>DO</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>MY</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>YT</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>CM</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>GY</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>MO</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>SR</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>OM</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>GE</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>TO</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>KE</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>NC</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>SM</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>GW</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>SN</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>ER</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>TG</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>BT</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>UZ</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>KN</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>MA</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>QA</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>FK</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>VC</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>BI</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>IQ</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>GQ</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>MR</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>BZ</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>PH</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>AW</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>UY</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>CD</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>EH</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>CG</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>AS</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>PF</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>CK</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>CO</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>KM</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>IS</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>MK</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>LI</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>BM</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>BJ</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>DZ</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>MS</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>ZM</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>SO</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>AG</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>VU</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>SZ</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>EC</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>ET</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>GG</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>MC</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>PK</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>LA</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>NE</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>TZ</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>PA</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>BF</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>KG</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>VE</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>GH</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>RE</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>DJ</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>CL</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>CV</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>CN</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>MQ</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>ML</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>MD</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>BW</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>PM</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>MG</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>KH</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>ID</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>VN</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>TJ</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>PY</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>KY</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>LB</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>SH</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>RW</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>LR</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>SC</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>BO</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>MV</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>BD</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>GI</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>LK</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>LY</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>NG</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>LS</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>CF</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>GA</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>ZW</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>LC</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>MU</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>GN</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>JO</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>CI</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>VG</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>TC</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>KI</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>TD</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>TM</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>AD</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>GD</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>CR</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>HT</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>IN</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>MX</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>GL</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>YE</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>AF</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>RS</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>KZ</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>Africa</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>ME</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>MN</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>AN</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>NP</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>BH</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>BS</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>TT</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>SJ</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>DM</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>PW</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>MW</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>BA</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>NI</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>AO</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>TN</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>UG</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>LU</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>WS</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>BB</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>TV</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>JM</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>EG</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>MZ</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>NA</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>NU</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>PE</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>HN</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>BN</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>ZA</ExcludeShipToLocation>\r\n");
        //request.append("      <!-- ... more ExcludeShipToLocation values allowed here ... -->\r\n");
        request.append("      <GlobalShipping>false</GlobalShipping>\r\n");
        /*request.append("      <InsuranceDetails> InsuranceDetailsType\r\n");
        request.append("        <InsuranceFee> AmountType (double) </InsuranceFee>\r\n");
        request.append("        <InsuranceOption> InsuranceOptionCodeType </InsuranceOption>\r\n");
        request.append("      </InsuranceDetails>\r\n");
        request.append("      <InsuranceFee> AmountType (double) </InsuranceFee>\r\n");
        request.append("      <InsuranceOption> InsuranceOptionCodeType </InsuranceOption>\r\n");
        request.append("      <InternationalInsuranceDetails> InsuranceDetailsType\r\n");
        request.append("        <InsuranceFee> AmountType (double) </InsuranceFee>\r\n");
        request.append("        <InsuranceOption> InsuranceOptionCodeType </InsuranceOption>\r\n");
        request.append("      </InternationalInsuranceDetails>\r\n");
        request.append("      <InternationalPromotionalShippingDiscount> boolean </InternationalPromotionalShippingDiscount>\r\n");*/
        request.append("      <InternationalShippingDiscountProfileID>0</InternationalShippingDiscountProfileID>\r\n");
        request.append("      <InternationalShippingServiceOption>\r\n");
        request.append("        <ShippingService>OtherInternational</ShippingService>\r\n");
        request.append("        <ShippingServiceAdditionalCost>4.99</ShippingServiceAdditionalCost>\r\n");
        request.append("        <ShippingServiceCost>4.99</ShippingServiceCost>\r\n");
        request.append("        <ShippingServicePriority>1</ShippingServicePriority>\r\n");
        request.append("        <ShipToLocation>Worldwide</ShipToLocation>\r\n");
        //request.append("        <!-- ... more ShipToLocation values allowed here ... -->\r\n");
        request.append("      </InternationalShippingServiceOption>\r\n");
        request.append("      <InternationalShippingServiceOption>\r\n");
        request.append("        <ShippingService>ExpeditedInternational</ShippingService>\r\n");
        request.append("        <ShippingServiceAdditionalCost>9.99</ShippingServiceAdditionalCost>\r\n");
        request.append("        <ShippingServiceCost>21.99</ShippingServiceCost>\r\n");
        request.append("        <ShippingServicePriority>2</ShippingServicePriority>\r\n");
        request.append("        <ShipToLocation>Worldwide</ShipToLocation>\r\n");
        //request.append("        <!-- ... more ShipToLocation values allowed here ... -->\r\n");
        request.append("      </InternationalShippingServiceOption>\r\n");
        /*request.append("      <!-- ... more InternationalShippingServiceOption nodes allowed here ... -->\r\n");
        request.append("      <PaymentInstructions> string </PaymentInstructions>\r\n");
        request.append("      <PromotionalShippingDiscount> boolean </PromotionalShippingDiscount>\r\n");
        request.append("      <RateTableDetails> RateTableDetailsType\r\n");
        request.append("        <DomesticRateTable> string </DomesticRateTable>\r\n");
        request.append("        <InternationalRateTable> string </InternationalRateTable>\r\n");
        request.append("      </RateTableDetails>\r\n");
        request.append("      <SalesTax>\r\n");
        request.append("        <SalesTaxPercent>0.0</SalesTaxPercent>\r\n");
        request.append("        <SalesTaxState> string </SalesTaxState>\r\n");
        request.append("        <ShippingIncludedInTax> boolean </ShippingIncludedInTax>\r\n");
        request.append("      </SalesTax>\r\n");
        request.append("      <ShippingDiscountProfileID> string </ShippingDiscountProfileID>\r\n");*/
        request.append("      <ShippingServiceOptions>\r\n");
        request.append("        <FreeShipping>true</FreeShipping>\r\n");
        request.append("        <ShippingService>ePacketChina</ShippingService>\r\n");
        request.append("        <ShippingServiceAdditionalCost>0</ShippingServiceAdditionalCost>\r\n");
        request.append("        <ShippingServiceCost>0</ShippingServiceCost>\r\n");
        request.append("        <ShippingServicePriority>1</ShippingServicePriority>\r\n");
        //request.append("        <ShippingSurcharge> AmountType (double) </ShippingSurcharge>\r\n");
        request.append("      </ShippingServiceOptions>\r\n");
        request.append("      <ShippingServiceOptions>\r\n");
        request.append("        <FreeShipping>false</FreeShipping>\r\n");
        request.append("        <ShippingService>StandardShippingFromOutsideUS</ShippingService>\r\n");
        request.append("        <ShippingServiceAdditionalCost>9.99</ShippingServiceAdditionalCost>\r\n");
        request.append("        <ShippingServiceCost>19.99</ShippingServiceCost>\r\n");
        request.append("        <ShippingServicePriority>2</ShippingServicePriority>\r\n");
        //request.append("        <ShippingSurcharge> AmountType (double) </ShippingSurcharge>\r\n");
        request.append("      </ShippingServiceOptions>\r\n");
        //request.append("      <!-- ... more ShippingServiceOptions nodes allowed here ... -->\r\n");
        request.append("      <ShippingType>Flat</ShippingType>\r\n");
        request.append("    </ShippingDetails>\r\n");
        /*request.append("    <ShippingPackageDetails> ShipPackageDetailsType\r\n");
        request.append("      <MeasurementUnit> MeasurementSystemCodeType </MeasurementUnit>\r\n");
        request.append("      <PackageDepth> MeasureType (decimal) </PackageDepth>\r\n");
        request.append("      <PackageLength> MeasureType (decimal) </PackageLength>\r\n");
        request.append("      <PackageWidth> MeasureType (decimal) </PackageWidth>\r\n");
        request.append("      <ShippingIrregular> boolean </ShippingIrregular>\r\n");
        request.append("      <ShippingPackage> ShippingPackageCodeType </ShippingPackage>\r\n");
        request.append("      <WeightMajor> MeasureType (decimal) </WeightMajor>\r\n");
        request.append("      <WeightMinor> MeasureType (decimal) </WeightMinor>\r\n");
        request.append("    </ShippingPackageDetails>\r\n");*/
        request.append("    <ShippingTermsInDescription>true</ShippingTermsInDescription>\r\n");
        //request.append("    <ShipToLocations> string </ShipToLocations>\r\n");
        //request.append("    <!-- ... more ShipToLocations values allowed here ... -->\r\n");
        request.append("    <Site>US</Site>\r\n");
        request.append("    <SKU>TT-NDRL</SKU>\r\n");
        /*request.append("    <SkypeContactOption> SkypeContactOptionCodeType </SkypeContactOption>\r\n");
        request.append("    <!-- ... more SkypeContactOption values allowed here ... -->\r\n");
        request.append("    <SkypeEnabled> boolean </SkypeEnabled>\r\n");
        request.append("    <SkypeID> string </SkypeID>\r\n");*/
        //request.append("    <StartPrice>5.99</StartPrice>\r\n");   //TODO
        /*request.append("    <Storefront> StorefrontType\r\n");
        request.append("      <StoreCategory2ID> long </StoreCategory2ID>\r\n");
        request.append("      <StoreCategoryID> long </StoreCategoryID>\r\n");
        request.append("    </Storefront>\r\n");
        request.append("    <SubTitle> string </SubTitle>\r\n");
        request.append("    <TaxCategory> string </TaxCategory>\r\n");
        request.append("    <ThirdPartyCheckout> boolean </ThirdPartyCheckout>\r\n");
        request.append("    <ThirdPartyCheckoutIntegration> boolean </ThirdPartyCheckoutIntegration>\r\n");*/
        request.append("    <Title>Tattoo Supply Sterile Disposable Round Liner Lining RL 1 box (50 pcs Needles)</Title>\r\n");
        /*request.append("    <UseRecommendedProduct> boolean </UseRecommendedProduct>\r\n");
        request.append("    <UseTaxTable> boolean </UseTaxTable>\r\n");
        request.append("    <UUID> UUIDType (string) </UUID>\r\n");*/
        request.append("    <Variations>\r\n");
        request.append("      <Pictures>\r\n");
        request.append("        <VariationSpecificName>Needle Type</VariationSpecificName>\r\n");
        request.append("        <VariationSpecificPictureSet>\r\n");
        request.append("          <PictureURL>http://images.tattootalks.com/Needles/1RL.jpg</PictureURL>\r\n");
        //request.append("          <!-- ... more PictureURL values allowed here ... -->\r\n");
        request.append("          <VariationSpecificValue>1 Round Liner (1RL)</VariationSpecificValue>\r\n");
        request.append("        </VariationSpecificPictureSet>\r\n");
        request.append("        <VariationSpecificPictureSet>\r\n");
        request.append("          <PictureURL>http://images.tattootalks.com/Needles/3RL.jpg</PictureURL>\r\n");
        request.append("          <VariationSpecificValue>3 Round Liner (3RL)</VariationSpecificValue>\r\n");
        request.append("        </VariationSpecificPictureSet>\r\n");
        request.append("        <VariationSpecificPictureSet>\r\n");
        request.append("          <PictureURL>http://images.tattootalks.com/Needles/4RL.jpg</PictureURL>\r\n");
        request.append("          <VariationSpecificValue>4 Round Liner (4RL)</VariationSpecificValue>\r\n");
        request.append("        </VariationSpecificPictureSet>\r\n");
        request.append("        <VariationSpecificPictureSet>\r\n");
        request.append("          <PictureURL>http://images.tattootalks.com/Needles/5RL.jpg</PictureURL>\r\n");
        request.append("          <VariationSpecificValue>5 Round Liner (5RL)</VariationSpecificValue>\r\n");
        request.append("        </VariationSpecificPictureSet>\r\n");
        request.append("        <VariationSpecificPictureSet>\r\n");
        request.append("          <PictureURL>http://images.tattootalks.com/Needles/7RL.jpg</PictureURL>\r\n");
        request.append("          <VariationSpecificValue>7 Round Liner (7RL)</VariationSpecificValue>\r\n");
        request.append("        </VariationSpecificPictureSet>\r\n");
        request.append("        <VariationSpecificPictureSet>\r\n");
        request.append("          <PictureURL>http://images.tattootalks.com/Needles/8RL.jpg</PictureURL>\r\n");
        request.append("          <VariationSpecificValue>8 Round Liner (8RL)</VariationSpecificValue>\r\n");
        request.append("        </VariationSpecificPictureSet>\r\n");
        request.append("        <VariationSpecificPictureSet>\r\n");
        request.append("          <PictureURL>http://images.tattootalks.com/Needles/9RL.jpg</PictureURL>\r\n");
        request.append("          <VariationSpecificValue>9 Round Liner (9RL)</VariationSpecificValue>\r\n");
        request.append("        </VariationSpecificPictureSet>\r\n");
        request.append("        <VariationSpecificPictureSet>\r\n");
        request.append("          <PictureURL>http://images.tattootalks.com/Needles/10RL.jpg</PictureURL>\r\n");
        request.append("          <VariationSpecificValue>10 Round Liner (10RL)</VariationSpecificValue>\r\n");
        request.append("        </VariationSpecificPictureSet>\r\n");
        request.append("        <VariationSpecificPictureSet>\r\n");
        request.append("          <PictureURL>http://images.tattootalks.com/Needles/11RL.jpg</PictureURL>\r\n");
        request.append("          <VariationSpecificValue>11 Round Liner (11RL)</VariationSpecificValue>\r\n");
        request.append("        </VariationSpecificPictureSet>\r\n");
        request.append("        <VariationSpecificPictureSet>\r\n");
        request.append("          <PictureURL>http://images.tattootalks.com/Needles/13RL.jpg</PictureURL>\r\n");
        request.append("          <VariationSpecificValue>13 Round Liner (13RL)</VariationSpecificValue>\r\n");
        request.append("        </VariationSpecificPictureSet>\r\n");
        request.append("        <VariationSpecificPictureSet>\r\n");
        request.append("          <PictureURL>http://images.tattootalks.com/Needles/14RL.jpg</PictureURL>\r\n");
        request.append("          <VariationSpecificValue>14 Round Liner (14RL)</VariationSpecificValue>\r\n");
        request.append("        </VariationSpecificPictureSet>\r\n");
        request.append("        <VariationSpecificPictureSet>\r\n");
        request.append("          <PictureURL>http://images.tattootalks.com/Needles/15RL.jpg</PictureURL>\r\n");
        request.append("          <VariationSpecificValue>15 Round Liner (15RL)</VariationSpecificValue>\r\n");
        request.append("        </VariationSpecificPictureSet>\r\n");
        request.append("        <VariationSpecificPictureSet>\r\n");
        request.append("          <PictureURL>http://images.tattootalks.com/Needles/18RL.jpg</PictureURL>\r\n");
        request.append("          <VariationSpecificValue>18 Round Liner (18RL)</VariationSpecificValue>\r\n");
        request.append("        </VariationSpecificPictureSet>\r\n");
        //request.append("        <!-- ... more VariationSpecificPictureSet nodes allowed here ... -->\r\n");
        request.append("      </Pictures>\r\n");
        request.append("      <Variation>\r\n");
        /*request.append("        <DiscountPriceInfo> DiscountPriceInfoType\r\n");
        request.append("          <MadeForOutletComparisonPrice> AmountType (double) </MadeForOutletComparisonPrice>\r\n");
        request.append("          <MinimumAdvertisedPrice> AmountType (double) </MinimumAdvertisedPrice>\r\n");
        request.append("          <MinimumAdvertisedPriceExposure> MinimumAdvertisedPriceExposureCodeType </MinimumAdvertisedPriceExposure>\r\n");
        request.append("          <OriginalRetailPrice> AmountType (double) </OriginalRetailPrice>\r\n");
        request.append("          <SoldOffeBay> boolean </SoldOffeBay>\r\n");
        request.append("          <SoldOneBay> boolean </SoldOneBay>\r\n");
        request.append("        </DiscountPriceInfo>\r\n");*/
        request.append("        <Quantity>5</Quantity>\r\n");
        request.append("        <SKU>TT-ND1201RL-BOX</SKU>\r\n");
        request.append("        <StartPrice>5.99</StartPrice>\r\n");   //TODO
        request.append("        <VariationSpecifics>\r\n");
        request.append("          <NameValueList>\r\n");
        request.append("            <Name>Needle Type</Name>\r\n");
        request.append("            <Value>1 Round Liner (1RL)</Value>\r\n");
        //request.append("            <!-- ... more Value values allowed here ... -->\r\n");
        request.append("          </NameValueList>\r\n");
        //request.append("          <!-- ... more NameValueList nodes allowed here ... -->\r\n");
        request.append("        </VariationSpecifics>\r\n");
        //request.append("        <!-- ... more VariationSpecifics nodes allowed here ... -->\r\n");
        request.append("      </Variation>\r\n");
        request.append("      <Variation>\r\n");
        request.append("        <Quantity>5</Quantity>\r\n");
        request.append("        <SKU>TT-ND1203RL-BOX</SKU>\r\n");
        request.append("        <StartPrice>5.99</StartPrice>\r\n");   //TODO
        request.append("        <VariationSpecifics>\r\n");
        request.append("          <NameValueList>\r\n");
        request.append("            <Name>Needle Type</Name>\r\n");
        request.append("            <Value>3 Round Liner (3RL)</Value>\r\n");
        request.append("          </NameValueList>\r\n");
        request.append("        </VariationSpecifics>\r\n");
        request.append("      </Variation>\r\n");
        request.append("      <Variation>\r\n");
        request.append("        <Quantity>5</Quantity>\r\n");
        request.append("        <SKU>TT-ND1204RL-BOX</SKU>\r\n");
        request.append("        <StartPrice>5.99</StartPrice>\r\n");   //TODO
        request.append("        <VariationSpecifics>\r\n");
        request.append("          <NameValueList>\r\n");
        request.append("            <Name>Needle Type</Name>\r\n");
        request.append("            <Value>4 Round Liner (4RL)</Value>\r\n");
        request.append("          </NameValueList>\r\n");
        request.append("        </VariationSpecifics>\r\n");
        request.append("      </Variation>\r\n");
        request.append("      <Variation>\r\n");
        request.append("        <Quantity>5</Quantity>\r\n");
        request.append("        <SKU>TT-ND1205RL-BOX</SKU>\r\n");
        request.append("        <StartPrice>5.99</StartPrice>\r\n");   //TODO
        request.append("        <VariationSpecifics>\r\n");
        request.append("          <NameValueList>\r\n");
        request.append("            <Name>Needle Type</Name>\r\n");
        request.append("            <Value>5 Round Liner (5RL)</Value>\r\n");
        request.append("          </NameValueList>\r\n");
        request.append("        </VariationSpecifics>\r\n");
        request.append("      </Variation>\r\n");
        request.append("      <Variation>\r\n");
        request.append("        <Quantity>5</Quantity>\r\n");
        request.append("        <SKU>TT-ND1207RL-BOX</SKU>\r\n");
        request.append("        <StartPrice>6.99</StartPrice>\r\n");   //TODO
        request.append("        <VariationSpecifics>\r\n");
        request.append("          <NameValueList>\r\n");
        request.append("            <Name>Needle Type</Name>\r\n");
        request.append("            <Value>7 Round Liner (7RL)</Value>\r\n");
        request.append("          </NameValueList>\r\n");
        request.append("        </VariationSpecifics>\r\n");
        request.append("      </Variation>\r\n");
        request.append("      <Variation>\r\n");
        request.append("        <Quantity>5</Quantity>\r\n");
        request.append("        <SKU>TT-ND1208RL-BOX</SKU>\r\n");
        request.append("        <StartPrice>6.99</StartPrice>\r\n");   //TODO
        request.append("        <VariationSpecifics>\r\n");
        request.append("          <NameValueList>\r\n");
        request.append("            <Name>Needle Type</Name>\r\n");
        request.append("            <Value>8 Round Liner (8RL)</Value>\r\n");
        request.append("          </NameValueList>\r\n");
        request.append("        </VariationSpecifics>\r\n");
        request.append("      </Variation>\r\n");
        request.append("      <Variation>\r\n");
        request.append("        <Quantity>5</Quantity>\r\n");
        request.append("        <SKU>TT-ND1209RL-BOX</SKU>\r\n");
        request.append("        <StartPrice>6.99</StartPrice>\r\n");   //TODO
        request.append("        <VariationSpecifics>\r\n");
        request.append("          <NameValueList>\r\n");
        request.append("            <Name>Needle Type</Name>\r\n");
        request.append("            <Value>9 Round Liner (9RL)</Value>\r\n");
        request.append("          </NameValueList>\r\n");
        request.append("        </VariationSpecifics>\r\n");
        request.append("      </Variation>\r\n");
        request.append("      <Variation>\r\n");
        request.append("        <Quantity>5</Quantity>\r\n");
        request.append("        <SKU>TT-ND1210RL-BOX</SKU>\r\n");
        request.append("        <StartPrice>6.99</StartPrice>\r\n");   //TODO
        request.append("        <VariationSpecifics>\r\n");
        request.append("          <NameValueList>\r\n");
        request.append("            <Name>Needle Type</Name>\r\n");
        request.append("            <Value>10 Round Liner (10RL)</Value>\r\n");
        request.append("          </NameValueList>\r\n");
        request.append("        </VariationSpecifics>\r\n");
        request.append("      </Variation>\r\n");
        request.append("      <Variation>\r\n");
        request.append("        <Quantity>5</Quantity>\r\n");
        request.append("        <SKU>TT-ND1211RL-BOX</SKU>\r\n");
        request.append("        <StartPrice>7.99</StartPrice>\r\n");   //TODO
        request.append("        <VariationSpecifics>\r\n");
        request.append("          <NameValueList>\r\n");
        request.append("            <Name>Needle Type</Name>\r\n");
        request.append("            <Value>11 Round Liner (11RL)</Value>\r\n");
        request.append("          </NameValueList>\r\n");
        request.append("        </VariationSpecifics>\r\n");
        request.append("      </Variation>\r\n");
        request.append("      <Variation>\r\n");
        request.append("        <Quantity>5</Quantity>\r\n");
        request.append("        <SKU>TT-ND1213RL-BOX</SKU>\r\n");
        request.append("        <StartPrice>7.99</StartPrice>\r\n");   //TODO
        request.append("        <VariationSpecifics>\r\n");
        request.append("          <NameValueList>\r\n");
        request.append("            <Name>Needle Type</Name>\r\n");
        request.append("            <Value>13 Round Liner (13RL)</Value>\r\n");
        request.append("          </NameValueList>\r\n");
        request.append("        </VariationSpecifics>\r\n");
        request.append("      </Variation>\r\n");
        request.append("      <Variation>\r\n");
        request.append("        <Quantity>5</Quantity>\r\n");
        request.append("        <SKU>TT-ND1214RL-BOX</SKU>\r\n");
        request.append("        <StartPrice>7.99</StartPrice>\r\n");   //TODO
        request.append("        <VariationSpecifics>\r\n");
        request.append("          <NameValueList>\r\n");
        request.append("            <Name>Needle Type</Name>\r\n");
        request.append("            <Value>14 Round Liner (14RL)</Value>\r\n");
        request.append("          </NameValueList>\r\n");
        request.append("        </VariationSpecifics>\r\n");
        request.append("      </Variation>\r\n");
        request.append("      <Variation>\r\n");
        request.append("        <Quantity>5</Quantity>\r\n");
        request.append("        <SKU>TT-ND1215RL-BOX</SKU>\r\n");
        request.append("        <StartPrice>7.99</StartPrice>\r\n");   //TODO
        request.append("        <VariationSpecifics>\r\n");
        request.append("          <NameValueList>\r\n");
        request.append("            <Name>Needle Type</Name>\r\n");
        request.append("            <Value>15 Round Liner (15RL)</Value>\r\n");
        request.append("          </NameValueList>\r\n");
        request.append("        </VariationSpecifics>\r\n");
        request.append("      </Variation>\r\n");
        request.append("      <Variation>\r\n");
        request.append("        <Quantity>5</Quantity>\r\n");
        request.append("        <SKU>TT-ND1218RL-BOX</SKU>\r\n");
        request.append("        <StartPrice>8.99</StartPrice>\r\n");   //TODO
        request.append("        <VariationSpecifics>\r\n");
        request.append("          <NameValueList>\r\n");
        request.append("            <Name>Needle Type</Name>\r\n");
        request.append("            <Value>18 Round Liner (18RL)</Value>\r\n");
        request.append("          </NameValueList>\r\n");
        request.append("        </VariationSpecifics>\r\n");
        request.append("      </Variation>\r\n");
        request.append("      <VariationSpecificsSet>\r\n");
        request.append("        <NameValueList>\r\n");
        request.append("          <Name>Needle Type</Name>\r\n");
        request.append("          <Value>1 Round Liner (1RL)</Value>\r\n");
        request.append("          <Value>3 Round Liner (3RL)</Value>\r\n");
        request.append("          <Value>4 Round Liner (4RL)</Value>\r\n");
        request.append("          <Value>5 Round Liner (5RL)</Value>\r\n");
        request.append("          <Value>7 Round Liner (7RL)</Value>\r\n");
        request.append("          <Value>8 Round Liner (8RL)</Value>\r\n");
        request.append("          <Value>9 Round Liner (9RL)</Value>\r\n");
        request.append("          <Value>10 Round Liner (10RL)</Value>\r\n");
        request.append("          <Value>11 Round Liner (11RL)</Value>\r\n");
        request.append("          <Value>13 Round Liner (13RL)</Value>\r\n");
        request.append("          <Value>14 Round Liner (14RL)</Value>\r\n");
        request.append("          <Value>15 Round Liner (15RL)</Value>\r\n");
        request.append("          <Value>18 Round Liner (18RL)</Value>\r\n");
        //request.append("          <!-- ... more Value values allowed here ... -->\r\n");
        request.append("        </NameValueList>\r\n");
        //request.append("        <!-- ... more NameValueList nodes allowed here ... -->\r\n");
        request.append("      </VariationSpecificsSet>\r\n");
        request.append("    </Variations>\r\n");
        /*request.append("    <VATDetails> VATDetailsType\r\n");
        request.append("      <BusinessSeller> boolean </BusinessSeller>\r\n");
        request.append("      <RestrictedToBusiness> boolean </RestrictedToBusiness>\r\n");
        request.append("      <VATPercent> float </VATPercent>\r\n");
        request.append("    </VATDetails>\r\n");
        request.append("    <VIN> string </VIN>\r\n");
        request.append("    <VRM> string </VRM>\r\n");*/
        request.append("  </Item>\r\n");
        request.append("  <!-- Standard Input Fields -->\r\n");
        //request.append("  <ErrorLanguage> string </ErrorLanguage>\r\n");
        //request.append("  <MessageID> string </MessageID>\r\n");
        //request.append("  <Version> string </Version>\r\n");
        //request.append("  <WarningLevel> WarningLevelCodeType </WarningLevel>\r\n");
        request.append("</AddFixedPriceItemRequest>\r\n");
        
        return request.toString();
    }
    
    public static String addFixedPriceItemRequestXML(Map mapContent)
    {
        String description = mapContent.get("description").toString();
        StringBuffer request = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
        request.append("<AddFixedPriceItemRequest xmlns=\"urn:ebay:apis:eBLBaseComponents\">\r\n");
        request.append("  <RequesterCredentials>\r\n");
        request.append("    <eBayAuthToken>" + mapContent.get("token") + "</eBayAuthToken>\r\n");
        request.append("  </RequesterCredentials>\r\n");
        request.append("  <Item>\r\n");
        request.append("    <ConditionID>1000</ConditionID>\r\n");
        request.append("    <Country>CN</Country>\r\n");
        request.append("    <Currency>USD</Currency>\r\n");
        request.append("    <Description>" + description + "</Description>\r\n"); //TODO
        request.append("    <DispatchTimeMax>1</DispatchTimeMax>\r\n");
        request.append("    <GetItFast>false</GetItFast>\r\n");
        request.append("    <GiftIcon>0</GiftIcon>\r\n");
        request.append("    <HitCounter>RetroStyle</HitCounter>\r\n");
        request.append("    <IncludeRecommendations>false</IncludeRecommendations>\r\n");   //TODO
        request.append("    <ItemSpecifics>\r\n");   //Brand, Model, Material, Color
        request.append("      <NameValueList>\r\n");
        request.append("        <Name>Material</Name>\r\n");
        request.append("        <Value>304 Medical Stainless steel</Value>\r\n");
        request.append("      </NameValueList>\r\n");
        request.append("    </ItemSpecifics>\r\n");
        request.append("    <ListingDuration>GTC</ListingDuration>\r\n");
        request.append("    <ListingType>FixedPriceItem</ListingType>\r\n");
        request.append("    <Location>Shanghai, China</Location>\r\n");
        request.append("    <PaymentMethods>PayPal</PaymentMethods>\r\n");
        request.append("    <PayPalEmailAddress>dee.pan@live.cn</PayPalEmailAddress>\r\n");
        request.append("    <PictureDetails>\r\n");
        request.append("      <GalleryType>Gallery</GalleryType>\r\n");
        request.append("      <GalleryURL>http://images.tattootalks.com/Needles/RM.jpg</GalleryURL>\r\n");
        request.append("      <PhotoDisplay>PicturePack</PhotoDisplay>\r\n");
        request.append("      <PictureSource>Vendor</PictureSource>\r\n");
        request.append("      <PictureURL>http://images.tattootalks.com/Needles/RM.jpg</PictureURL>\r\n");
        request.append("    </PictureDetails>\r\n");
        request.append("    <PostCheckoutExperienceEnabled>false</PostCheckoutExperienceEnabled>\r\n");
        request.append("    <PrimaryCategory>\r\n");
        request.append("      <CategoryID>33918</CategoryID>\r\n");
        request.append("    </PrimaryCategory>\r\n");
        request.append("    <PrivateListing>true</PrivateListing>\r\n");
        request.append("    <ReturnPolicy>\r\n");
        request.append("      <Description><![CDATA[We accept returns if you are unhappy with the purchase only when the product is unused, unopened, still properly sealed, especially for consumable products like needles, etc. Please contact us via eBay message prior any returns.]]></Description>\r\n");
        request.append("      <RefundOption>MoneyBack</RefundOption>\r\n");
        request.append("      <ReturnsAcceptedOption>ReturnsAccepted</ReturnsAcceptedOption>\r\n");
        request.append("      <ReturnsWithinOption>Days_30</ReturnsWithinOption>\r\n");
        request.append("      <ShippingCostPaidByOption>Buyer</ShippingCostPaidByOption>\r\n");
        request.append("    </ReturnPolicy>\r\n");
        request.append("    <ShippingDetails>\r\n");
        request.append("      <ExcludeShipToLocation>GT</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>VA</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>PG</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>SB</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>GP</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>SL</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>MH</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>NR</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>WF</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>GF</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>AI</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>GM</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>SV</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>FM</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>DO</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>MY</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>YT</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>CM</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>GY</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>MO</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>SR</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>OM</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>GE</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>TO</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>KE</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>NC</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>SM</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>GW</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>SN</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>ER</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>TG</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>BT</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>UZ</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>KN</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>MA</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>QA</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>FK</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>VC</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>BI</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>IQ</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>GQ</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>MR</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>BZ</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>PH</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>AW</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>UY</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>CD</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>EH</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>CG</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>AS</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>PF</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>CK</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>CO</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>KM</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>IS</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>MK</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>LI</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>BM</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>BJ</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>DZ</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>MS</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>ZM</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>SO</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>AG</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>VU</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>SZ</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>EC</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>ET</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>GG</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>MC</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>PK</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>LA</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>NE</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>TZ</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>PA</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>BF</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>KG</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>VE</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>GH</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>RE</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>DJ</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>CL</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>CV</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>CN</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>MQ</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>ML</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>MD</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>BW</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>PM</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>MG</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>KH</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>ID</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>VN</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>TJ</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>PY</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>KY</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>LB</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>SH</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>RW</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>LR</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>SC</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>BO</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>MV</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>BD</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>GI</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>LK</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>LY</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>NG</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>LS</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>CF</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>GA</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>ZW</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>LC</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>MU</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>GN</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>JO</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>CI</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>VG</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>TC</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>KI</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>TD</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>TM</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>AD</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>GD</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>CR</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>HT</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>IN</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>MX</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>GL</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>YE</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>AF</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>RS</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>KZ</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>Africa</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>ME</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>MN</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>AN</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>NP</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>BH</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>BS</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>TT</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>SJ</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>DM</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>PW</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>MW</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>BA</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>NI</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>AO</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>TN</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>UG</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>LU</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>WS</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>BB</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>TV</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>JM</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>EG</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>MZ</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>NA</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>NU</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>PE</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>HN</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>BN</ExcludeShipToLocation>\r\n");
        request.append("      <ExcludeShipToLocation>ZA</ExcludeShipToLocation>\r\n");
        request.append("      <GlobalShipping>false</GlobalShipping>\r\n");
        request.append("      <InternationalShippingDiscountProfileID>0</InternationalShippingDiscountProfileID>\r\n");
        request.append("      <InternationalShippingServiceOption>\r\n");
        request.append("        <ShippingService>OtherInternational</ShippingService>\r\n");
        request.append("        <ShippingServiceAdditionalCost>4.99</ShippingServiceAdditionalCost>\r\n");
        request.append("        <ShippingServiceCost>4.99</ShippingServiceCost>\r\n");
        request.append("        <ShippingServicePriority>1</ShippingServicePriority>\r\n");
        request.append("        <ShipToLocation>Worldwide</ShipToLocation>\r\n");
        request.append("      </InternationalShippingServiceOption>\r\n");
        request.append("      <InternationalShippingServiceOption>\r\n");
        request.append("        <ShippingService>ExpeditedInternational</ShippingService>\r\n");
        request.append("        <ShippingServiceAdditionalCost>9.99</ShippingServiceAdditionalCost>\r\n");
        request.append("        <ShippingServiceCost>21.99</ShippingServiceCost>\r\n");
        request.append("        <ShippingServicePriority>2</ShippingServicePriority>\r\n");
        request.append("        <ShipToLocation>Worldwide</ShipToLocation>\r\n");
        request.append("      </InternationalShippingServiceOption>\r\n");
        request.append("      <ShippingServiceOptions>\r\n");
        request.append("        <FreeShipping>true</FreeShipping>\r\n");
        request.append("        <ShippingService>ePacketChina</ShippingService>\r\n");
        request.append("        <ShippingServiceAdditionalCost>0</ShippingServiceAdditionalCost>\r\n");
        request.append("        <ShippingServiceCost>0</ShippingServiceCost>\r\n");
        request.append("        <ShippingServicePriority>1</ShippingServicePriority>\r\n");
        request.append("      </ShippingServiceOptions>\r\n");
        request.append("      <ShippingServiceOptions>\r\n");
        request.append("        <FreeShipping>false</FreeShipping>\r\n");
        request.append("        <ShippingService>StandardShippingFromOutsideUS</ShippingService>\r\n");
        request.append("        <ShippingServiceAdditionalCost>9.99</ShippingServiceAdditionalCost>\r\n");
        request.append("        <ShippingServiceCost>19.99</ShippingServiceCost>\r\n");
        request.append("        <ShippingServicePriority>2</ShippingServicePriority>\r\n");
        request.append("      </ShippingServiceOptions>\r\n");
        request.append("      <ShippingType>Flat</ShippingType>\r\n");
        request.append("    </ShippingDetails>\r\n");
        request.append("    <ShippingTermsInDescription>true</ShippingTermsInDescription>\r\n");
        request.append("    <Site>US</Site>\r\n");
        request.append("    <SKU>TT-NDF</SKU>\r\n");
        request.append("    <Title>Tattoo Supply Sterile Disposable Size 12 Curved Round Magnum Shader RM 1 box</Title>\r\n");
        request.append("    <Variations>\r\n");
        request.append("      <Pictures>\r\n");
        request.append("        <VariationSpecificName>Needle Type</VariationSpecificName>\r\n");
        request.append("        <VariationSpecificPictureSet>\r\n");
        request.append("          <PictureURL>http://images.tattootalks.com/Needles/5RM.jpg</PictureURL>\r\n");
        request.append("          <VariationSpecificValue>5RM - 50 pcs</VariationSpecificValue>\r\n");
        request.append("        </VariationSpecificPictureSet>\r\n");
        request.append("        <VariationSpecificPictureSet>\r\n");
        request.append("          <PictureURL>http://images.tattootalks.com/Needles/7RM.jpg</PictureURL>\r\n");
        request.append("          <VariationSpecificValue>7RM - 50 pcs</VariationSpecificValue>\r\n");
        request.append("        </VariationSpecificPictureSet>\r\n");
        request.append("        <VariationSpecificPictureSet>\r\n");
        request.append("          <PictureURL>http://images.tattootalks.com/Needles/9RM.jpg</PictureURL>\r\n");
        request.append("          <VariationSpecificValue>9RM - 50 pcs</VariationSpecificValue>\r\n");
        request.append("        </VariationSpecificPictureSet>\r\n");
        request.append("        <VariationSpecificPictureSet>\r\n");
        request.append("          <PictureURL>http://images.tattootalks.com/Needles/11RM.jpg</PictureURL>\r\n");
        request.append("          <VariationSpecificValue>11RM - 50 pcs</VariationSpecificValue>\r\n");
        request.append("        </VariationSpecificPictureSet>\r\n");
        request.append("        <VariationSpecificPictureSet>\r\n");
        request.append("          <PictureURL>http://images.tattootalks.com/Needles/13RM.jpg</PictureURL>\r\n");
        request.append("          <VariationSpecificValue>13RM - 50 pcs</VariationSpecificValue>\r\n");
        request.append("        </VariationSpecificPictureSet>\r\n");
        request.append("        <VariationSpecificPictureSet>\r\n");
        request.append("          <PictureURL>http://images.tattootalks.com/Needles/15RM.jpg</PictureURL>\r\n");
        request.append("          <VariationSpecificValue>15RM - 50 pcs</VariationSpecificValue>\r\n");
        request.append("        </VariationSpecificPictureSet>\r\n");
        request.append("        <VariationSpecificPictureSet>\r\n");
        request.append("          <PictureURL>http://images.tattootalks.com/Needles/17RM.jpg</PictureURL>\r\n");
        request.append("          <VariationSpecificValue>17RM - 50 pcs</VariationSpecificValue>\r\n");
        request.append("        </VariationSpecificPictureSet>\r\n");
        request.append("        <VariationSpecificPictureSet>\r\n");
        request.append("          <PictureURL>http://images.tattootalks.com/Needles/23RM.jpg</PictureURL>\r\n");
        request.append("          <VariationSpecificValue>23RM - 30 pcs</VariationSpecificValue>\r\n");
        request.append("        </VariationSpecificPictureSet>\r\n");
        request.append("        <VariationSpecificPictureSet>\r\n");
        request.append("          <PictureURL>http://images.tattootalks.com/Needles/25RM.jpg</PictureURL>\r\n");
        request.append("          <VariationSpecificValue>25RM - 30 pcs</VariationSpecificValue>\r\n");
        request.append("        </VariationSpecificPictureSet>\r\n");
        request.append("        <VariationSpecificPictureSet>\r\n");
        request.append("          <PictureURL>http://images.tattootalks.com/Needles/29RM.jpg</PictureURL>\r\n");
        request.append("          <VariationSpecificValue>29RM - 30 pcs</VariationSpecificValue>\r\n");
        request.append("        </VariationSpecificPictureSet>\r\n");
        request.append("        <VariationSpecificPictureSet>\r\n");
        request.append("          <PictureURL>http://images.tattootalks.com/Needles/35RM.jpg</PictureURL>\r\n");
        request.append("          <VariationSpecificValue>35RM - 30 pcs</VariationSpecificValue>\r\n");
        request.append("        </VariationSpecificPictureSet>\r\n");
        request.append("        <VariationSpecificPictureSet>\r\n");
        request.append("          <PictureURL>http://images.tattootalks.com/Needles/39RM.jpg</PictureURL>\r\n");
        request.append("          <VariationSpecificValue>39RM - 30 pcs</VariationSpecificValue>\r\n");
        request.append("        </VariationSpecificPictureSet>\r\n");
        request.append("        <VariationSpecificPictureSet>\r\n");
        request.append("          <PictureURL>http://images.tattootalks.com/Needles/49RM.jpg</PictureURL>\r\n");
        request.append("          <VariationSpecificValue>49RM - 30 pcs</VariationSpecificValue>\r\n");
        request.append("        </VariationSpecificPictureSet>\r\n");
        request.append("      </Pictures>\r\n");
        request.append("      <Variation>\r\n");
        request.append("        <Quantity>3</Quantity>\r\n");
        request.append("        <SKU>TT-ND1205RM-BOX</SKU>\r\n");
        request.append("        <StartPrice>5.99</StartPrice>\r\n");   //TODO
        request.append("        <VariationSpecifics>\r\n");
        request.append("          <NameValueList>\r\n");
        request.append("            <Name>Needle Type</Name>\r\n");
        request.append("            <Value>5RM - 50 pcs</Value>\r\n");
        request.append("          </NameValueList>\r\n");
        request.append("        </VariationSpecifics>\r\n");
        request.append("      </Variation>\r\n");
        request.append("      <Variation>\r\n");
        request.append("        <Quantity>3</Quantity>\r\n");
        request.append("        <SKU>TT-ND1207RM-BOX</SKU>\r\n");
        request.append("        <StartPrice>6.99</StartPrice>\r\n");   //TODO
        request.append("        <VariationSpecifics>\r\n");
        request.append("          <NameValueList>\r\n");
        request.append("            <Name>Needle Type</Name>\r\n");
        request.append("            <Value>7RM - 50 pcs</Value>\r\n");
        request.append("          </NameValueList>\r\n");
        request.append("        </VariationSpecifics>\r\n");
        request.append("      </Variation>\r\n");
        request.append("      <Variation>\r\n");
        request.append("        <Quantity>3</Quantity>\r\n");
        request.append("        <SKU>TT-ND1209RM-BOX</SKU>\r\n");
        request.append("        <StartPrice>6.99</StartPrice>\r\n");   //TODO
        request.append("        <VariationSpecifics>\r\n");
        request.append("          <NameValueList>\r\n");
        request.append("            <Name>Needle Type</Name>\r\n");
        request.append("            <Value>9RM - 50 pcs</Value>\r\n");
        request.append("          </NameValueList>\r\n");
        request.append("        </VariationSpecifics>\r\n");
        request.append("      </Variation>\r\n");
        request.append("      <Variation>\r\n");
        request.append("        <Quantity>3</Quantity>\r\n");
        request.append("        <SKU>TT-ND1211RM-BOX</SKU>\r\n");
        request.append("        <StartPrice>7.99</StartPrice>\r\n");   //TODO
        request.append("        <VariationSpecifics>\r\n");
        request.append("          <NameValueList>\r\n");
        request.append("            <Name>Needle Type</Name>\r\n");
        request.append("            <Value>11RM - 50 pcs</Value>\r\n");
        request.append("          </NameValueList>\r\n");
        request.append("        </VariationSpecifics>\r\n");
        request.append("      </Variation>\r\n");
        request.append("      <Variation>\r\n");
        request.append("        <Quantity>3</Quantity>\r\n");
        request.append("        <SKU>TT-ND1213RM-BOX</SKU>\r\n");
        request.append("        <StartPrice>7.99</StartPrice>\r\n");   //TODO
        request.append("        <VariationSpecifics>\r\n");
        request.append("          <NameValueList>\r\n");
        request.append("            <Name>Needle Type</Name>\r\n");
        request.append("            <Value>13RM - 50 pcs</Value>\r\n");
        request.append("          </NameValueList>\r\n");
        request.append("        </VariationSpecifics>\r\n");
        request.append("      </Variation>\r\n");
        request.append("      <Variation>\r\n");
        request.append("        <Quantity>3</Quantity>\r\n");
        request.append("        <SKU>TT-ND1215RM-BOX</SKU>\r\n");
        request.append("        <StartPrice>7.99</StartPrice>\r\n");   //TODO
        request.append("        <VariationSpecifics>\r\n");
        request.append("          <NameValueList>\r\n");
        request.append("            <Name>Needle Type</Name>\r\n");
        request.append("            <Value>15RM - 50 pcs</Value>\r\n");
        request.append("          </NameValueList>\r\n");
        request.append("        </VariationSpecifics>\r\n");
        request.append("      </Variation>\r\n");
        request.append("      <Variation>\r\n");
        request.append("        <Quantity>3</Quantity>\r\n");
        request.append("        <SKU>TT-ND1217RM-BOX</SKU>\r\n");
        request.append("        <StartPrice>8.99</StartPrice>\r\n");   //TODO
        request.append("        <VariationSpecifics>\r\n");
        request.append("          <NameValueList>\r\n");
        request.append("            <Name>Needle Type</Name>\r\n");
        request.append("            <Value>17RM - 50 pcs</Value>\r\n");
        request.append("          </NameValueList>\r\n");
        request.append("        </VariationSpecifics>\r\n");
        request.append("      </Variation>\r\n");
        request.append("      <Variation>\r\n");
        request.append("        <Quantity>3</Quantity>\r\n");
        request.append("        <SKU>TT-ND1223RM-BOX</SKU>\r\n");
        request.append("        <StartPrice>7.99</StartPrice>\r\n");   //TODO
        request.append("        <VariationSpecifics>\r\n");
        request.append("          <NameValueList>\r\n");
        request.append("            <Name>Needle Type</Name>\r\n");
        request.append("            <Value>23RM - 30 pcs</Value>\r\n");
        request.append("          </NameValueList>\r\n");
        request.append("        </VariationSpecifics>\r\n");
        request.append("      </Variation>\r\n");
        request.append("      <Variation>\r\n");
        request.append("        <Quantity>3</Quantity>\r\n");
        request.append("        <SKU>TT-ND1225RM-BOX</SKU>\r\n");
        request.append("        <StartPrice>7.99</StartPrice>\r\n");   //TODO
        request.append("        <VariationSpecifics>\r\n");
        request.append("          <NameValueList>\r\n");
        request.append("            <Name>Needle Type</Name>\r\n");
        request.append("            <Value>25RM - 30 pcs</Value>\r\n");
        request.append("          </NameValueList>\r\n");
        request.append("        </VariationSpecifics>\r\n");
        request.append("      </Variation>\r\n");
        request.append("      <Variation>\r\n");
        request.append("        <Quantity>3</Quantity>\r\n");
        request.append("        <SKU>TT-ND1229RM-BOX</SKU>\r\n");
        request.append("        <StartPrice>8.49</StartPrice>\r\n");   //TODO
        request.append("        <VariationSpecifics>\r\n");
        request.append("          <NameValueList>\r\n");
        request.append("            <Name>Needle Type</Name>\r\n");
        request.append("            <Value>29RM - 30 pcs</Value>\r\n");
        request.append("          </NameValueList>\r\n");
        request.append("        </VariationSpecifics>\r\n");
        request.append("      </Variation>\r\n");
        request.append("      <Variation>\r\n");
        request.append("        <Quantity>3</Quantity>\r\n");
        request.append("        <SKU>TT-ND1235RM-BOX</SKU>\r\n");
        request.append("        <StartPrice>8.99</StartPrice>\r\n");   //TODO
        request.append("        <VariationSpecifics>\r\n");
        request.append("          <NameValueList>\r\n");
        request.append("            <Name>Needle Type</Name>\r\n");
        request.append("            <Value>35RM - 30 pcs</Value>\r\n");
        request.append("          </NameValueList>\r\n");
        request.append("        </VariationSpecifics>\r\n");
        request.append("      </Variation>\r\n");
        request.append("      <Variation>\r\n");
        request.append("        <Quantity>3</Quantity>\r\n");
        request.append("        <SKU>TT-ND1239RM-BOX</SKU>\r\n");
        request.append("        <StartPrice>9.49</StartPrice>\r\n");   //TODO
        request.append("        <VariationSpecifics>\r\n");
        request.append("          <NameValueList>\r\n");
        request.append("            <Name>Needle Type</Name>\r\n");
        request.append("            <Value>39RM - 30 pcs</Value>\r\n");
        request.append("          </NameValueList>\r\n");
        request.append("        </VariationSpecifics>\r\n");
        request.append("      </Variation>\r\n");
        request.append("      <Variation>\r\n");
        request.append("        <Quantity>3</Quantity>\r\n");
        request.append("        <SKU>TT-ND1249RM-BOX</SKU>\r\n");
        request.append("        <StartPrice>9.99</StartPrice>\r\n");   //TODO
        request.append("        <VariationSpecifics>\r\n");
        request.append("          <NameValueList>\r\n");
        request.append("            <Name>Needle Type</Name>\r\n");
        request.append("            <Value>49RM - 30 pcs</Value>\r\n");
        request.append("          </NameValueList>\r\n");
        request.append("        </VariationSpecifics>\r\n");
        request.append("      </Variation>\r\n");
        request.append("      <VariationSpecificsSet>\r\n");
        request.append("        <NameValueList>\r\n");
        request.append("          <Name>Needle Type</Name>\r\n");
        request.append("          <Value>5RM - 50 pcs</Value>\r\n");
        request.append("          <Value>7RM - 50 pcs</Value>\r\n");
        request.append("          <Value>9RM - 50 pcs</Value>\r\n");
        request.append("          <Value>11RM - 50 pcs</Value>\r\n");
        request.append("          <Value>13RM - 50 pcs</Value>\r\n");
        request.append("          <Value>15RM - 50 pcs</Value>\r\n");
        request.append("          <Value>17RM - 50 pcs</Value>\r\n");
        request.append("          <Value>23RM - 30 pcs</Value>\r\n");
        request.append("          <Value>25RM - 30 pcs</Value>\r\n");
        request.append("          <Value>29RM - 30 pcs</Value>\r\n");
        request.append("          <Value>35RM - 30 pcs</Value>\r\n");
        request.append("          <Value>39RM - 30 pcs</Value>\r\n");
        request.append("          <Value>49RM - 30 pcs</Value>\r\n");
        request.append("        </NameValueList>\r\n");
        request.append("      </VariationSpecificsSet>\r\n");
        request.append("    </Variations>\r\n");
        request.append("  </Item>\r\n");
        request.append("  <!-- Standard Input Fields -->\r\n");
        request.append("</AddFixedPriceItemRequest>\r\n");
        
        return request.toString();
    }
    
    public static String shippingDetailsRequestXML(Map mapContent) {
        
        String shipServiceOptionXML = "ShippingServiceOptions";
        if (mapContent.get("area").equals("INTERNATIONAL")) {
            shipServiceOptionXML = "InternationalShippingServiceOption";
        }
        String freeShipping = "false";
        if (mapContent.get("freeShipping").toString().toLowerCase().equals("true")) {
            freeShipping = "true";
        }
        
        StringBuffer request = new StringBuffer("      <" + shipServiceOptionXML + ">\r\n");
        request.append("        <FreeShipping>" + freeShipping + "</FreeShipping>\r\n");
        request.append("        <ShippingService>" + mapContent.get("name") + "</ShippingService>\r\n");
        request.append("        <ShippingServiceAdditionalCost>" + mapContent.get("additionalCost") + "</ShippingServiceAdditionalCost>\r\n");
        request.append("        <ShippingServiceCost>" + mapContent.get("cost") + "</ShippingServiceCost>\r\n");
        request.append("        <ShippingServicePriority>" + mapContent.get("priority") + "</ShippingServicePriority>\r\n");
        request.append("        <ShipToLocation>" + mapContent.get("shipToLocation") + "</ShipToLocation>\r\n");
        request.append("      </" + shipServiceOptionXML + ">\r\n");
        
        return request.toString();
    }
    
    public static String getCategorySpecifics(Map mapContent)
    throws IOException {
        
        String requestXMLcode = null;
        try {
            //Building XML -- START
            Document rootDoc = UtilXml.makeEmptyXmlDocument("GetCategorySpecificsRequest");
            Element rootElem = rootDoc.getDocumentElement();
            rootElem.setAttribute("xmlns", "urn:ebay:apis:eBLBaseComponents");
            
            //RequesterCredentials -- START
            Element requesterCredentialsElem = UtilXml.addChildElement(rootElem, "RequesterCredentials", rootDoc);
            UtilXml.addChildElementValue(requesterCredentialsElem, "eBayAuthToken", mapContent.get("token").toString(), rootDoc);
            //RequesterCredentials -- END
            
            UtilXml.addChildElementValue(rootElem, "CategoryID", mapContent.get("categoryId").toString(), rootDoc);
            UtilXml.addChildElementValue(rootElem, "IncludeConfidence", "false", rootDoc);
            UtilXml.addChildElementValue(rootElem, "ExcludeRelationships", "false", rootDoc);
            
            requestXMLcode = UtilXml.writeXmlDocument(rootDoc);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return requestXMLcode;
    }
    
    public static String getStoreRequestXML(Map mapContent)
    throws IOException {
        
        String requestXMLcode = null;
        try {
            //Building XML -- START
            Document rootDoc = UtilXml.makeEmptyXmlDocument("GetStoreRequest");
            Element rootElem = rootDoc.getDocumentElement();
            rootElem.setAttribute("xmlns", "urn:ebay:apis:eBLBaseComponents");
            
            //RequesterCredentials -- START
            Element requesterCredentialsElem = UtilXml.addChildElement(rootElem, "RequesterCredentials", rootDoc);
            UtilXml.addChildElementValue(requesterCredentialsElem, "eBayAuthToken", mapContent.get("token").toString(), rootDoc);
            //RequesterCredentials -- END
            
            UtilXml.addChildElementValue(rootElem, "CategoryStructureOnly", "true", rootDoc);
            
            requestXMLcode = UtilXml.writeXmlDocument(rootDoc);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return requestXMLcode;
    }
    
/******Trading API XML - END *******/
	
/******Finding API XML - START *******/
	
/******Finding API XML - End *******/

/* Dateformat
Calender startDate = Calender.getInstance();
//get the date 120 days ago
startDate.set(Calender.DATE, startDate.get(Calender.DATE) - 120);
//format
DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'");
//get date obj from caleday
Date before = startDate.getTime();

System.out.println(sdf.format(before));
*/
}
