package tests;

import base.BaseSetup;
import pages.CreateEstimatePage;
import pages.Login;
import utils.ExcelReader;

import org.testng.SkipException;
import org.testng.annotations.*;
import static org.testng.Assert.assertTrue;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CreateEstimateTest extends BaseSetup {
 
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

    	        System.out.println("Total records read: " + allEstimates.size());

    	        CreateEstimatePage estimatePage = new CreateEstimatePage(driver);

    	        for (Map<String, Object> estimate : allEstimates) {

    	            String customerName = estimate.get("customerName").toString();
    	            String referenceNo  = estimate.get("referenceNo").toString();
    	            String subject      = estimate.get("subject").toString();
    	            String salesPerson  = estimate.get("salesPerson").toString();
    	            String estimateDate = estimate.get("invoiceDate").toString();
    	            String expiryDate   = estimate.get("supplyDate").toString();

    	            @SuppressWarnings("unchecked")
    	            List<Map<String, String>> items =
    	                    (List<Map<String, String>>) estimate.get("items");

    	            if (items == null || items.isEmpty())
    	                throw new SkipException("No items for customer: " + customerName);

    	            String[] itemNames = items.stream().map(i -> i.get("itemName")).toArray(String[]::new);
    	            String[] itemQtys  = items.stream().map(i -> i.get("itemQty")).toArray(String[]::new);

    	            // --- UI actions ---
    	            estimatePage.navigateToNewEstimate();
    	            estimatePage.fillEstimateHeader(customerName, referenceNo, subject,
    	                    salesPerson, estimateDate, expiryDate);

    	            String estNo = estimatePage.estimatenumber();

    	            estimatePage.addItems(itemNames, itemQtys);
    	            estimatePage.addNotesAndTerms(
    	                    "Dear " + customerName + ", " + estNo + " created by Automation",
    	                    "This is a system-generated document."
    	            );

    	            estimatePage.saveAsDraft();

    	            assertTrue(estimatePage.verifyEstimateCreated(estNo),
    	                    "Estimate not found : " + estNo);
    	        }
    	    }
    	}

