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
import pages.CreatePurchaseBillPage;
import pages.Login;
import utils.ExcelReader;

public class CreatePurchaseBillTest extends BaseSetup{
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
//        System.out.println("Total records read: " + allSalesInvoice.size());
//        allSalesInvoice.forEach(System.out::println);
        //
        for (Map<String, Object> purchaseBill : allPurchaseBill) {
            String vendorName = purchaseBill.get("customerName").toString();
            String referenceNo = purchaseBill.get("referenceNo").toString();
            String subject = purchaseBill.get("subject").toString();
            String salesPerson = purchaseBill.get("salesPerson").toString();
            String billDate = purchaseBill.get("invoiceDate").toString();
            String paymentTerms = purchaseBill.get("paymentTerms").toString();
            String supplyDate = purchaseBill.get("supplyDate").toString();
            String transactionType = purchaseBill.get("transactionType").toString();
            if (vendorName.isEmpty()) {
                throw new SkipException("❌ Skipping test: Vendor Name is empty in Excel data");
            }
            @SuppressWarnings("unchecked")
            List<Map<String, String>> items = (List<Map<String, String>>) purchaseBill.get("items");
            String[] itemNames = items.stream().map(i -> i.get("itemName")).toArray(String[]::new);
            String[] itemQtys = items.stream().map(i -> i.get("itemQty")).toArray(String[]::new);
            if (items == null || items.isEmpty()) {
                throw new SkipException("❌ Skipping test: No items(name or Qty) found for Customer: " + vendorName);
            }
            purchaseBillPage.navigateToNewPurchaseBill();
            purchaseBillPage.fillPurchaseBillHeader(vendorName, referenceNo, subject,salesPerson,billDate,paymentTerms,supplyDate,transactionType);
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
