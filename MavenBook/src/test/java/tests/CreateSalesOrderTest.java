package tests;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import base.BaseTest;
import pages.CreateSalesOrderPage;
import pages.Login;
import utils.ExcelReader;

public class CreateSalesOrderTest extends BaseTest {
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
    public void createSalesOrderTest() throws IOException, InterruptedException {
        String filePath = System.getProperty("user.dir") + "/src/test/resources/SalesOrderData.xlsx";
        List<Map<String, Object>> allSalesOrder =
                ExcelReader.getMasterDetailData(filePath, "SalesOrderHeader", "SalesOrderItems");
        CreateSalesOrderPage salesOrderPage = new CreateSalesOrderPage(driver);
        for (Map<String, Object> salesOrder : allSalesOrder) {
        	String customerName  = ExcelReader.getValue(salesOrder, "Customer Name");
        	String referenceNo   = ExcelReader.getValue(salesOrder, "Reference Number");
        	String soDate        = ExcelReader.getValue(salesOrder, "Sales Order Date");
        	String expDelivDate  =ExcelReader.getValue(salesOrder, "Expected Delivery Date");
        	String paymentTerms  = ExcelReader.getValue(salesOrder, "Payment Terms");
        	String salesPerson   = ExcelReader.getValue(salesOrder, "Sales Person");
        	String priceList	 = ExcelReader.getValue(salesOrder, "Price List");
        	String taxType       = ExcelReader.getValue(salesOrder, "Tax");
        	String customerNote  = ExcelReader.getValue(salesOrder, "Customer Notes");
        	String terms         = ExcelReader.getValue(salesOrder, "Terms And Conditions");
        	String saveAs        = ExcelReader.getValue(salesOrder, "Save As");
                       
            if (customerName.isEmpty()) {
                throw new SkipException("❌ Skipping test: Customer Name is empty in Excel data");
            }
            @SuppressWarnings("unchecked")
            List<Map<String, String>> items = (List<Map<String, String>>) salesOrder.get("items");
             if (items == null || items.isEmpty()) {
                throw new SkipException("❌ Skipping test: No items(name or Qty) found for Customer: " + customerName);
            }
          // ===== ITEM DATA (example headers) =====
             String[] itemNames = items.stream()
                     .map(i -> i.getOrDefault("Item Name", "").trim())
                     .toArray(String[]::new);
             String[] itemQtys = items.stream()
                     .map(i -> i.getOrDefault("Item Quantity", "").trim())
                     .toArray(String[]::new);
             String[] discountType=items.stream().map(i ->i.getOrDefault("Discount Type", "").trim()).toArray(String[]::new);
             String[] discount=items.stream().map(i ->i.getOrDefault("Discount", "").trim()).toArray(String[]::new);
             
            salesOrderPage.navigateToNewSalesOrder();
            salesOrderPage.fillSalesOrderHeader(customerName, referenceNo,soDate,expDelivDate,paymentTerms,salesPerson,taxType,priceList);
            String SONo=salesOrderPage.salesOrderNumber();
            salesOrderPage.addItems(itemNames, itemQtys,discountType,discount);
            salesOrderPage.addNotesAndTerms(
                    "Dear " + customerName + ", " + SONo + " has been created by Automation."+customerNote,
                    "This is a system-generated document. Ensure accuracy before acceptance."+terms);           
            salesOrderPage.saveAsMethod(saveAs);
            SoftAssert soft=new SoftAssert();
            soft.assertTrue(salesOrderPage.verifySalesOrderCreated(SONo),
                    "Sales Oreder not found or failed to create : " + SONo);
            soft.assertAll();
        }
    }
}
