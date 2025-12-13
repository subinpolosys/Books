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
import pages.CreateVendorCreditPage;
import pages.Login;
import utils.ExcelReader;

public class CreateVendorCreditTest extends BaseSetup {
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
    public void CreateVendorCreditTest() throws IOException, InterruptedException {
        String filePath = System.getProperty("user.dir") + "/src/test/resources/VendorCreditData.xlsx";
        List<Map<String, Object>> allVendorCredit =
                ExcelReader.getMasterDetailData(filePath, "VendorCreditHeader", "VendorCreditItems");
        CreateVendorCreditPage vendorCreditPage = new CreateVendorCreditPage(driver);
        //debug
        //System.out.println("Total records read: " + allVendorCredit.size());
        //allVendorCredit.forEach(System.out::println);
       
        for (Map<String, Object> vendorCredit : allVendorCredit) {
            String vendorName = vendorCredit.get("customerName").toString();
            String billNo = vendorCredit.get("referenceNo").toString();          
            String vendorCreditDate = vendorCredit.get("invoiceDate").toString();           
            String orderNumber = vendorCredit.get("supplyDate").toString();            
          
            if (vendorName.isEmpty()||billNo.isEmpty()) {
                throw new SkipException("❌ Skipping test: Vendor Name is empty in Excel data");
            }
            @SuppressWarnings("unchecked")
            List<Map<String, String>> items = (List<Map<String, String>>) vendorCredit.get("items");
            String[] itemNames = items.stream().map(i -> i.get("itemName")).toArray(String[]::new);
            String[] itemQtys = items.stream().map(i -> i.get("itemQty")).toArray(String[]::new);
            if (items == null || items.isEmpty()) {
                throw new SkipException("❌ Skipping test: No items(name or Qty) found for Vendor: " + vendorName);
            }
            vendorCreditPage.navigateToNewVendorCredit();
            
            vendorCreditPage.fillVendorCreditHeader(vendorName, billNo, vendorCreditDate,orderNumber);
            String VCNo=vendorCreditPage.vendorCreditNumber();
            //System.out.println(SRNo);
            vendorCreditPage.addItems(itemNames, itemQtys);
            vendorCreditPage.addNotes(
                    "Dear " + vendorName + ", " + VCNo + " has been created by Automation",
                    "This is a system-generated document. Ensure accuracy before acceptance."
            );
            vendorCreditPage.saveAsDraft();
            SoftAssert soft = new SoftAssert();
            soft.assertTrue(vendorCreditPage.verifyVendorCreditCreated(VCNo),
                    "Vendor Credit not found or failed to create : " + VCNo);
            
            soft.assertAll();
        }
    }
}
