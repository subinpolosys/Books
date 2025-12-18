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
import pages.CreateSalesReturnPage;
import pages.Login;
import utils.ExcelReader;

public class CreateSalesReturnTest extends BaseSetup {
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
    public void createSalesReturnTest() throws IOException, InterruptedException {
        String filePath = System.getProperty("user.dir") + "/src/test/resources/SalesReturnData.xlsx";
        List<Map<String, Object>> allSalesReturn =
                ExcelReader.getMasterDetailData(filePath, "SalesReturnHeader", "SalesReturnItems");
        CreateSalesReturnPage salesReturnPage = new CreateSalesReturnPage(driver);
        //debug
       // System.out.println("Total records read: " + allSalesReturn.size());
        //allSalesReturn.forEach(System.out::println);
        //
        for (Map<String, Object> salesReturn : allSalesReturn) {
        	String customerName       = ExcelReader.getValue(salesReturn, "Customer Name");
        	String invoiceNo          = ExcelReader.getValue(salesReturn, "Invoice Number");
        	String reason             = ExcelReader.getValue(salesReturn, "Reason");
        	String salesPerson        = ExcelReader.getValue(salesReturn, "Sales Person");
        	String salesReturnDate    = ExcelReader.getValue(salesReturn, "Return Date");
        	String referenceNumber    = ExcelReader.getValue(salesReturn, "Reference Number");
        	String transactionType    = ExcelReader.getValue(salesReturn, "Transaction Type");

            if (customerName.isEmpty()||invoiceNo.isEmpty()) {
                throw new SkipException("❌ Skipping test: Customer Name is empty in Excel data");
            }
            @SuppressWarnings("unchecked")
            List<Map<String, String>> items = (List<Map<String, String>>) salesReturn.get("items");
            
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
            salesReturnPage.navigateToNewSalesReturn();
            
            salesReturnPage.fillSalesReturnHeader(customerName, invoiceNo, reason,salesPerson,salesReturnDate,referenceNumber,transactionType);
            String SRNo=salesReturnPage.salesReturnNumber();
            //System.out.println(SRNo);
            salesReturnPage.addItems(itemNames, itemQtys);
            salesReturnPage.addNotesAndTerms(
                    "Dear " + customerName + ", " + SRNo + " has been created by Automation",
                    "This is a system-generated document. Ensure accuracy before acceptance."
            );
            salesReturnPage.saveAsDraft();
            SoftAssert soft = new SoftAssert();
            soft.assertTrue(salesReturnPage.verifySalesReturnCreated(SRNo),
                    "Sales Return not found or failed to create : " + SRNo);
            soft.assertAll();
        }
    }
}
