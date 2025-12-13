package tests;

import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import base.BaseSetup;
import pages.CreateSalesOrderPage;
import pages.Login;
import utils.ExcelReader;

public class CreateSalesOrderTest extends BaseSetup {
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
            String customerName = salesOrder.get("customerName").toString();
            String referenceNo = salesOrder.get("referenceNo").toString();
            String subject = salesOrder.get("subject").toString();
            String salesPerson = salesOrder.get("salesPerson").toString();
            String soDate = salesOrder.get("invoiceDate").toString();
            String paymentTerms = salesOrder.get("paymentTerms").toString();
            String deliveryDate = salesOrder.get("supplyDate").toString();
                       
            if (customerName.isEmpty()) {
                throw new SkipException("❌ Skipping test: Customer Name is empty in Excel data");
            }
            @SuppressWarnings("unchecked")
            List<Map<String, String>> items = (List<Map<String, String>>) salesOrder.get("items");
            String[] itemNames = items.stream().map(i -> i.get("itemName")).toArray(String[]::new);
            String[] itemQtys = items.stream().map(i -> i.get("itemQty")).toArray(String[]::new);
            if (items == null || items.isEmpty()) {
                throw new SkipException("❌ Skipping test: No items(name or Qty) found for Customer: " + customerName);
            }
            salesOrderPage.navigateToNewSalesOrder();
            salesOrderPage.fillSalesOrderHeader(customerName, referenceNo, subject,salesPerson,soDate,paymentTerms,deliveryDate);
            String SONo=salesOrderPage.salesOrderNumber();
            salesOrderPage.addItems(itemNames, itemQtys);
            salesOrderPage.addNotesAndTerms(
                    "Dear " + customerName + ", " + SONo + " has been created by Automation",
                    "This is a system-generated document. Ensure accuracy before acceptance."
            );
            salesOrderPage.saveAsDraft();
            SoftAssert soft=new SoftAssert();
            soft.assertTrue(salesOrderPage.verifySalesOrderCreated(SONo),
                    "Sales Oreder not found or failed to create : " + SONo);
            soft.assertAll();
        }
    }
}
