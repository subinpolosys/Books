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
import pages.CreateDeliverynotePage;
import pages.Login;
import utils.ExcelReader;

public class CreateDeliverynoteTest extends BaseSetup {
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
	    public void createDeliverynoteTest() throws IOException, InterruptedException {
	        String filePath = System.getProperty("user.dir") + "/src/test/resources/DeliverynoteData.xlsx";
	        List<Map<String, Object>> allDeliverynote =
	                ExcelReader.getMasterDetailData(filePath, "DeliverynoteHeader", "deliverynoteItems");
	        CreateDeliverynotePage deliverynotePage = new CreateDeliverynotePage(driver);
	        for (Map<String, Object> dnote : allDeliverynote) {
	            String customerName = dnote .get("customerName").toString();
	            String referenceNo = dnote .get("referenceNo").toString();
	            
	            String dnDate=dnote .get("invoiceDate").toString();
	            String dnType=dnote.get("transactionType").toString();
	            
	            
	            if (customerName.isEmpty()) {
	                throw new SkipException("❌ Skipping test: Customer Name is empty in Excel data");
	            }
	            @SuppressWarnings("unchecked")
	            List<Map<String, String>> items = (List<Map<String, String>>) dnote.get("items");
	            String[] itemNames = items.stream().map(i -> i.get("itemName")).toArray(String[]::new);
	            String[] itemQtys = items.stream().map(i -> i.get("itemQty")).toArray(String[]::new);
	            if (items == null || items.isEmpty()) {
	                throw new SkipException("❌ Skipping test: No items(name or Qty) found for Customer: " + customerName);
	            }
	            deliverynotePage.navigateToNewDeliverynote();
	            deliverynotePage.fillEstimateHeader(customerName, referenceNo,dnDate,dnType);
	            String DNNo=deliverynotePage.delivernoteNumber();
	            deliverynotePage.addItems(itemNames, itemQtys);
	            deliverynotePage.addNotesAndTerms(
	                    "Dear " + customerName + ", " + DNNo + " has been created by Automation",
	                    "This is a system-generated document. Ensure accuracy before acceptance."
	            );
	            deliverynotePage.saveAsDraft();
	            assertTrue(deliverynotePage.verifyDeliverynoteCreated(DNNo),
	                    "Delivery Note not found or failed to create : " + DNNo);
	        }
	    }
	}
