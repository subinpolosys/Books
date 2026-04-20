package tests;
import java.util.List;
import java.util.Map;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import base.BaseSetup;
import base.BaseTest;
import pages.CreatePurchaseOrderPage;
import pages.Login;
import utils.ExcelReader;

public class CreatePurchaseOrderTest extends BaseTest{
	private Login loginPage;
    @BeforeMethod
    public void beforeMethod() throws InterruptedException {
        loginPage = new Login(driver);
        loginPage.login();
    }
   @AfterMethod
    public void afterMethod() {
        loginPage.logout();
   }
    @Test
    public void createPurchaseOrderTest() throws Exception {
    	System.out.println("_______________Create Purchase Order__________________");
        String filePath = System.getProperty("user.dir") + "/src/test/resources/PurchaseOrderData.xlsx";
        List<Map<String, Object>> allPurchaseOrder =
                ExcelReader.getMasterDetailData(filePath, "PurchaseOrderHeader", "PurchaseOrderItems");
        CreatePurchaseOrderPage purchaseOrderPage = new CreatePurchaseOrderPage(driver);
        for (Map<String, Object> purchaseOrder : allPurchaseOrder) {
        	String vendorName      = ExcelReader.getValue(purchaseOrder, "Vendor Name");
        	String referenceNo     = ExcelReader.getValue(purchaseOrder, "Reference Number");
        	String poDate          = ExcelReader.getValue(purchaseOrder, "Purchase Order Date");
        	String paymentTerms    = ExcelReader.getValue(purchaseOrder, "Payment Terms");
        	String deliveryDate    = ExcelReader.getValue(purchaseOrder, "Exp delivery Date");
        	String priceList	   = ExcelReader.getValue(purchaseOrder, "Price List");
        	String taxType         = ExcelReader.getValue(purchaseOrder, "Tax");
        	String discountLevel   = ExcelReader.getValue(purchaseOrder,"Discount Level");
        	String discountAfterBeforeTax   = ExcelReader.getValue(purchaseOrder,"Discount After-Before Tax");
        	String discountTtype   = ExcelReader.getValue(purchaseOrder, "Discount TType");
        	String discountT       = ExcelReader.getValue(purchaseOrder, "DiscountT");
        	String discountAccount = ExcelReader.getValue(purchaseOrder, "Discount Account");        	
        	String customerNote    = ExcelReader.getValue(purchaseOrder, "Customer Notes");
        	String terms           = ExcelReader.getValue(purchaseOrder, "Terms And Conditions");        	
        	String saveAs          = ExcelReader.getValue(purchaseOrder, "Save As");
        	
            if (vendorName.isEmpty()) {
                throw new SkipException("❌ Skipping test: Customer Name is empty in Excel data");
            }
            @SuppressWarnings("unchecked")
            List<Map<String, String>> items = (List<Map<String, String>>) purchaseOrder.get("items");
            
            if (items == null || items.isEmpty()) {
                throw new SkipException("❌ Skipping test: No items(name or Qty) found for Customer: " + vendorName);
            }
            String[] itemNames = items.stream()
                    .map(i -> i.getOrDefault("Item Name", "").trim())
                    .toArray(String[]::new);
            String[] itemQtys = items.stream()
                    .map(i -> i.getOrDefault("Item Quantity", "").trim())
                    .toArray(String[]::new);
            String[] discountType=items.stream()
            		.map(i ->i.getOrDefault("Discount Type", "").trim())
            		.toArray(String[]::new);
            String[] discount=items.stream()
            		.map(i ->i.getOrDefault("Discount", "").trim())
            		.toArray(String[]::new);
            
            purchaseOrderPage.navigateToNewPurchaseOrder();
            purchaseOrderPage.fillPurchaseOrderHeader(vendorName, referenceNo, poDate,paymentTerms,deliveryDate,taxType,priceList,discountLevel);
            String PONo=purchaseOrderPage.purchaseOrderNumber();
            purchaseOrderPage.addItems(itemNames, itemQtys,discountType,discount,10);
            purchaseOrderPage.addNotesAndTerms(
                    "Dear " + vendorName + ", " + PONo + " has been created by Automation."+customerNote,
                    "This is a system-generated document. Ensure accuracy before acceptance."+terms
            );
            if((discountLevel == null 
                    || discountLevel.trim().isEmpty() 
                    || discountLevel.equalsIgnoreCase("At transaction level"))
                &&
                discountAfterBeforeTax != null && !discountAfterBeforeTax.trim().isEmpty()
                &&
                discountTtype != null && !discountTtype.trim().isEmpty()
                &&
                discountT != null && !discountT.trim().isEmpty()
                &&
                discountAccount != null && !discountAccount.trim().isEmpty()) {
            purchaseOrderPage.applyTransactionLevelDiscount(discountAfterBeforeTax, discountTtype,discountT,discountAccount);
            }
            purchaseOrderPage.saveAsMethod(saveAs);
            SoftAssert soft=new SoftAssert();
            soft.assertTrue(purchaseOrderPage.verifyPurchaseOrderCreated(PONo),
                    "Purchase Order not found or failed to create : " + PONo);
            soft.assertAll();
        }
    }
}

