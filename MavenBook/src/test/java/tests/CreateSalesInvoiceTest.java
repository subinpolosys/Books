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
import pages.CreateSalesInvoicePage;
import pages.Login;
import utils.ExcelReader;

public class CreateSalesInvoiceTest extends BaseTest{
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
    public void createSalesInvoiceTest() throws IOException, InterruptedException {
        String filePath = System.getProperty("user.dir") + "/src/test/resources/SalesInvoiceData.xlsx";
        List<Map<String, Object>> allSalesInvoice =
                ExcelReader.getMasterDetailData(filePath, "SalesInvoiceHeader", "SalesInvoiceItems");
        CreateSalesInvoicePage salesInvoicePage = new CreateSalesInvoicePage(driver);
        //debug
//        System.out.println("Total records read: " + allSalesInvoice.size());
//        allSalesInvoice.forEach(System.out::println);
        //
        for (Map<String, Object> salesInvoice : allSalesInvoice) {
        	String customerName     = ExcelReader.getValue(salesInvoice, "Customer Name");
        	String orderNo          = ExcelReader.getValue(salesInvoice, "Order Number");
        	String invoiceDate      = ExcelReader.getValue(salesInvoice, "Invoice Date");
        	String paymentTerms     = ExcelReader.getValue(salesInvoice, "Payment Terms");
        	String supplyDate       = ExcelReader.getValue(salesInvoice, "Supply Date");
        	String transactionType  = ExcelReader.getValue(salesInvoice, "Transaction Type");
        	String salesPerson      = ExcelReader.getValue(salesInvoice, "Sales Person");
        	String subject          = ExcelReader.getValue(salesInvoice, "Subject");
          
            if (customerName.isEmpty()) {
                throw new SkipException("❌ Skipping test: Customer Name is empty in Excel data");
            }
            @SuppressWarnings("unchecked")
            List<Map<String, String>> items = (List<Map<String, String>>) salesInvoice.get("items");
            
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
            
            salesInvoicePage.navigateToNewSalesInvoice();
            salesInvoicePage.fillSalesInvoiceHeader(customerName, orderNo, subject,salesPerson,invoiceDate,paymentTerms,supplyDate,transactionType);
            String SINo=salesInvoicePage.salesInvoiceNumber();
            salesInvoicePage.addItems(itemNames, itemQtys);
            salesInvoicePage.addNotesAndTerms(
                    "Dear " + customerName + ", " + SINo + " has been created by Automation",
                    "This is a system-generated document. Ensure accuracy before acceptance."
            );
            salesInvoicePage.saveAsDraft();
            SoftAssert soft = new SoftAssert();
            soft.assertTrue(salesInvoicePage.verifySalesInvoiceCreated(SINo),
                    "Sales Invoice not found or failed to create : " + SINo);
            soft.assertAll();
        }
    }
}
