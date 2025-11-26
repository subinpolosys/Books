package tests;

import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import base.BaseSetup;
import pages.CreateSalesInvoicePage;
import pages.Login;
import utils.ExcelReader;

public class CreateSalesInvoiceTest extends BaseSetup{
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
        System.out.println("Total records read: " + allSalesInvoice.size());
        allSalesInvoice.forEach(System.out::println);
        //
        for (Map<String, Object> salesInvoice : allSalesInvoice) {
            String customerName = salesInvoice.get("customerName").toString();
            String referenceNo = salesInvoice.get("referenceNo").toString();
            String subject = salesInvoice.get("subject").toString();
            String salesPerson = salesInvoice.get("salesPerson").toString();
            String invoiceDate = salesInvoice.get("invoiceDate").toString();
            String paymentTerms = salesInvoice.get("paymentTerms").toString();
            String supplyDate = salesInvoice.get("supplyDate").toString();
            String transactionType = salesInvoice.get("transactionType").toString();
          
            if (customerName.isEmpty()) {
                throw new SkipException("❌ Skipping test: Customer Name is empty in Excel data");
            }
            @SuppressWarnings("unchecked")
            List<Map<String, String>> items = (List<Map<String, String>>) salesInvoice.get("items");
            String[] itemNames = items.stream().map(i -> i.get("itemName")).toArray(String[]::new);
            String[] itemQtys = items.stream().map(i -> i.get("itemQty")).toArray(String[]::new);
            if (items == null || items.isEmpty()) {
                throw new SkipException("❌ Skipping test: No items(name or Qty) found for Customer: " + customerName);
            }
            salesInvoicePage.navigateToNewSalesInvoice();
            salesInvoicePage.fillSalesInvoiceHeader(customerName, referenceNo, subject,salesPerson,invoiceDate,paymentTerms,supplyDate,transactionType);
            String SONo=salesInvoicePage.salesInvoiceNumber();
            salesInvoicePage.addItems(itemNames, itemQtys);
            salesInvoicePage.addNotesAndTerms(
                    "Dear " + customerName + ", " + SONo + " has been created by Automation",
                    "This is a system-generated document. Ensure accuracy before acceptance."
            );
            salesInvoicePage.saveAsDraft();
            assertTrue(salesInvoicePage.verifySalesInvoiceCreated(SONo),
                    "Estimate not found or failed to create : " + SONo);
        }
    }
}
