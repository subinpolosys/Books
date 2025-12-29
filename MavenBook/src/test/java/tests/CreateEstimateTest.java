package tests;

import base.BaseTest;
import pages.CreateEstimatePage;
import pages.Login;
import utils.ExcelReader;

import org.testng.SkipException;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import static org.testng.Assert.assertTrue;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CreateEstimateTest extends BaseTest {
 
    	    private Login loginPage;

    	    @BeforeMethod
    	    public void beforeMethod() throws InterruptedException {
    	        loginPage = new Login(driver);
    	        loginPage.login();   // only once
    	    }

    	    @AfterMethod
    	    public void afterMethod() {
    	        loginPage.logout();  // only once
    	    }

    	    @Test
    	    public void createEstimateTest() throws Exception {
    	        String filePath = System.getProperty("user.dir") + "/src/test/resources/EstimateData.xlsx";
    	        List<Map<String, Object>> allEstimates =
    	                ExcelReader.getMasterDetailData(filePath, "EstimateHeader", "EstimateItems");
    	       // System.out.println("Total records read: " + allEstimates.size());

    	        CreateEstimatePage estimatePage = new CreateEstimatePage(driver);

    	        for (Map<String, Object> estimate : allEstimates) {

    	        	String customerName  = ExcelReader.getValue(estimate, "Customer Name");
    	        	String referenceNo   = ExcelReader.getValue(estimate, "Reference Number");
    	        	String subject       = ExcelReader.getValue(estimate, "Subject");
    	        	String salesPerson   = ExcelReader.getValue(estimate, "Sales Person");
    	        	String estimateDate  = ExcelReader.getValue(estimate, "Estimate Date");
    	        	String expiryDate    = ExcelReader.getValue(estimate, "Expiry Date");
    	        	String priceList	 = ExcelReader.getValue(estimate, "Price List");
    	        	String taxType       = ExcelReader.getValue(estimate, "Tax");
    	        	String customerNote  = ExcelReader.getValue(estimate, "Customer Notes");
    	        	String terms         = ExcelReader.getValue(estimate, "Terms And Conditions");
    	        	String saveAs        = ExcelReader.getValue(estimate, "Save As");

    	            @SuppressWarnings("unchecked")
    	            List<Map<String, String>> items =
    	                    (List<Map<String, String>>) estimate.get("items");

    	            if (items == null || items.isEmpty())
    	                throw new SkipException("No items for customer: " + customerName);
    	            // ===== ITEM DATA (example headers) =====
    	             String[] itemNames = items.stream()
    	                     .map(i -> i.getOrDefault("Item Name", "").trim())
    	                     .toArray(String[]::new);

    	             String[] itemQtys = items.stream()
    	                     .map(i -> i.getOrDefault("Item Quantity", "").trim())
    	                     .toArray(String[]::new);
    	             String[] discountType=items.stream().map(i ->i.getOrDefault("Discount Type", "").trim()).toArray(String[]::new);
    	             String[] discount=items.stream().map(i ->i.getOrDefault("Discount", "").trim()).toArray(String[]::new);
    	             
    	            // --- UI actions ---
    	            estimatePage.navigateToNewEstimate();
    	            estimatePage.fillEstimateHeader(customerName, referenceNo, subject,
    	                    salesPerson, estimateDate, expiryDate,taxType,priceList);
    	            String estNo = estimatePage.estimatenumber();
    	            estimatePage.addItems(itemNames, itemQtys,discountType,discount);
    	            estimatePage.addNotesAndTerms(
    	               "Dear " + customerName + ", " + estNo + " created by Automation. "+customerNote,
    	               "This is a system-generated document. "+terms);   	            
    	            estimatePage.saveAsMethod(saveAs);
    	            SoftAssert soft=new SoftAssert();
    	            soft.assertTrue(estimatePage.verifyEstimateCreated(estNo),
    	                    "Estimate not found : " + estNo);
    	            soft.assertAll();
    	        }
    	    }
    	}

