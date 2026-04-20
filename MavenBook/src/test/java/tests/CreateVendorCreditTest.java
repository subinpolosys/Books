package tests;

import java.util.List;
import java.util.Map;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import base.BaseSetup;
import base.BaseTest;
import pages.CreateVendorCreditPage;
import pages.Login;
import utils.ExcelReader;
public class CreateVendorCreditTest extends BaseTest {
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
    public void CreateVendorCreditTest() throws Exception {
    	System.out.println("________________Create Vendor Credit_______________");
        String filePath = System.getProperty("user.dir") + "/src/test/resources/VendorCreditData.xlsx";
        List<Map<String, Object>> allVendorCredit =
                ExcelReader.getMasterDetailData(filePath, "VendorCreditHeader", "VendorCreditItems");
        CreateVendorCreditPage vendorCreditPage = new CreateVendorCreditPage(driver);
        //debug
        //System.out.println("Total records read: " + allVendorCredit.size());
        //allVendorCredit.forEach(System.out::println);
       
        for (Map<String, Object> vendorCredit : allVendorCredit) {
        	String vendorName            = ExcelReader.getValue(vendorCredit, "Vendor Name");
        	String billNo                = ExcelReader.getValue(vendorCredit, "Bill Number");
        	String vendorCreditDate      = ExcelReader.getValue(vendorCredit, "VC Date");
        	String orderNumber           = ExcelReader.getValue(vendorCredit, "Order Number");           
        	String priceList	         = ExcelReader.getValue(vendorCredit, "Price List");
         	String taxType               = ExcelReader.getValue(vendorCredit, "Tax");
         	String customerNote          = ExcelReader.getValue(vendorCredit, "Customer Notes");
         	String terms                 = ExcelReader.getValue(vendorCredit, "Terms And Conditions");        	
         	String saveAs                = ExcelReader.getValue(vendorCredit, "Save As");
         	
            if (vendorName.isEmpty()||billNo.isEmpty()) {
                throw new SkipException("❌ Skipping test: Vendor Name is empty in Excel data");
            }
            @SuppressWarnings("unchecked")
            List<Map<String, String>> items = (List<Map<String, String>>) vendorCredit.get("items");
            if (items == null || items.isEmpty()) {
                throw new SkipException("❌ Skipping test: No items(name or Qty) found for Vendor: " + vendorName);
            }
         // ===== ITEM DATA (example headers) =====
            String[] itemNames = items.stream()
                    .map(i -> i.getOrDefault("Item Name", "").trim())
                    .toArray(String[]::new);

            String[] itemQtys = items.stream()
                    .map(i -> i.getOrDefault("Item Quantity", "").trim())
                    .toArray(String[]::new);
            String[] discountType=items.stream()
            		.map(i ->i.getOrDefault("Discount Type", "").trim())
            		.toArray(String[]::new);
            String[] discount=items.stream()
            		.map(i ->i.getOrDefault("Discount", "").trim())
            		.toArray(String[]::new);
            
            vendorCreditPage.navigateToNewVendorCredit();            
            vendorCreditPage.fillVendorCreditHeader(vendorName, billNo, vendorCreditDate,orderNumber,taxType,priceList);
            String VCNo=vendorCreditPage.vendorCreditNumber();
            //System.out.println(SRNo);
            vendorCreditPage.addItems(itemNames, itemQtys,discountType,discount);
            vendorCreditPage.addNotes(
                    "Dear " + vendorName + ", " + VCNo + " has been created by Automation"+customerNote);
                    
            
             
            vendorCreditPage.saveAsMethod(saveAs);
            SoftAssert soft = new SoftAssert();
            soft.assertTrue(vendorCreditPage.verifyVendorCreditCreated(VCNo),
                    "Vendor Credit not found or failed to create : " + VCNo);
            
            soft.assertAll();
        }
    }
}
