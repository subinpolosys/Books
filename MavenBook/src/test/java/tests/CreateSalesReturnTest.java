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
            String customerName = salesReturn.get("customerName").toString();
            String invoiceNo = salesReturn.get("referenceNo").toString();
            String reason = salesReturn.get("subject").toString();
            String salesPerson = salesReturn.get("salesPerson").toString();
            String salesReturnDate = salesReturn.get("invoiceDate").toString();
            //String paymentTerms = salesInvoice.get("paymentTerms").toString();
            String referenceNumber = salesReturn.get("supplyDate").toString();
            String transactionType = salesReturn.get("transactionType").toString();
          
            if (customerName.isEmpty()||invoiceNo.isEmpty()) {
                throw new SkipException("❌ Skipping test: Customer Name is empty in Excel data");
            }
            @SuppressWarnings("unchecked")
            List<Map<String, String>> items = (List<Map<String, String>>) salesReturn.get("items");
            String[] itemNames = items.stream().map(i -> i.get("itemName")).toArray(String[]::new);
            String[] itemQtys = items.stream().map(i -> i.get("itemQty")).toArray(String[]::new);
            if (items == null || items.isEmpty()) {
                throw new SkipException("❌ Skipping test: No items(name or Qty) found for Customer: " + customerName);
            }
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
