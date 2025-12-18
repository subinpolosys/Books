package tests;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import base.BaseSetup;
import pages.CreatePurchaseOrderPage;
import pages.Login;
import utils.ExcelReader;

public class CreatePurchaseOrderTest extends BaseSetup{
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
    public void createPurchaseOrderTest() throws IOException, InterruptedException {
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
            purchaseOrderPage.navigateToNewPurchaseOrder();
            purchaseOrderPage.fillPurchaseOrderHeader(vendorName, referenceNo, poDate,paymentTerms,deliveryDate);
            String PONo=purchaseOrderPage.purchaseOrderNumber();
            purchaseOrderPage.addItems(itemNames, itemQtys);
            purchaseOrderPage.addNotesAndTerms(
                    "Dear " + vendorName + ", " + PONo + " has been created by Automation",
                    "This is a system-generated document. Ensure accuracy before acceptance."
            );
            purchaseOrderPage.saveAsDraft();
            SoftAssert soft=new SoftAssert();
            soft.assertTrue(purchaseOrderPage.verifyPurchaseOrderCreated(PONo),
                    "Purchase Order not found or failed to create : " + PONo);
            soft.assertAll();
        }
    }
}

