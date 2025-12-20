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
import base.BaseTest;
import pages.CreatePurchaseBillPage;
import pages.Login;
import utils.ExcelReader;
public class CreatePurchaseBillTest extends BaseTest{
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
    public void createPurchasebillTest() throws IOException, InterruptedException {
        String filePath = System.getProperty("user.dir") + "/src/test/resources/PurchaseBillData.xlsx";
        List<Map<String, Object>> allPurchaseBill =
                ExcelReader.getMasterDetailData(filePath, "PurchaseBillHeader", "PurchaseBillItems");
        CreatePurchaseBillPage purchaseBillPage = new CreatePurchaseBillPage(driver);
        //debug
//        System.out.println("Total records read: " + allPurchaseBill.size());
//        allPurchaseBill.forEach(System.out::println);
        //
        for (Map<String, Object> purchaseBill : allPurchaseBill) {

            // ===== MASTER DATA (EXACT Excel headers) =====
            String vendorName      = ExcelReader.getValue(purchaseBill, "Vendor Name");
            String entryDate     = ExcelReader.getValue(purchaseBill, "Entry Date");
            String invoiceDate         = ExcelReader.getValue(purchaseBill, "Invoice Date");
            String expectedDeliveryDate     = ExcelReader.getValue(purchaseBill, "Expected Delivery Date");  
            String referenceNumber    = ExcelReader.getValue(purchaseBill, "Reference Number");
            String paymentTerms      = ExcelReader.getValue(purchaseBill, "Payment Terms");
           
            // ===== Mandatory validation =====
            if (vendorName.isEmpty()||referenceNumber.isEmpty()) {
                throw new SkipException(
                        "❌ Skipping test: Customer Name OR Reference Number is empty in Excel"
                );
            }

            // ===== ITEMS =====
            @SuppressWarnings("unchecked")
            List<Map<String, String>> items =
                    (List<Map<String, String>>) purchaseBill.get("items");

            if (items == null || items.isEmpty()) {
                throw new SkipException(
                        "❌ Skipping test: No items found for Customer: " + vendorName
                );
            }

            // ===== ITEM DATA (example headers) =====
            String[] itemNames = items.stream()
                    .map(i -> i.getOrDefault("Item Name", "").trim())
                    .toArray(String[]::new);

            String[] itemQtys = items.stream()
                    .map(i -> i.getOrDefault("Item Quantity", "").trim())
                    .toArray(String[]::new);
        
            purchaseBillPage.navigateToNewPurchaseBill();
            purchaseBillPage.fillPurchaseBillHeader(vendorName,entryDate,invoiceDate,expectedDeliveryDate ,referenceNumber,paymentTerms);
            String billNo=purchaseBillPage.purchaseBillNumber();
            purchaseBillPage.addItems(itemNames, itemQtys);
            purchaseBillPage.addNotesAndTerms(
                    "Dear " + vendorName + ", " + billNo + " has been created by Automation",
                    "This is a system-generated document. Ensure accuracy before acceptance."
            );
            purchaseBillPage.saveAsDraft();
            SoftAssert soft = new SoftAssert();
            soft.assertTrue(purchaseBillPage.verifyPurchaseBillCreated(billNo),
                    "Purchase Bill not found or failed to create : " + billNo);
            soft.assertAll();
        }
    }
}
