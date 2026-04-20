package tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import base.BaseTest;
import dataprovider.EstimateDataProvider;
import drivers.DriverFactory;
import model.EstimateData;
import pages.CreateEstimatePage;
import pages.Login;

public class EstimateTest extends BaseTest{
	private Login login;
    @BeforeMethod
    public void loginToApp() throws InterruptedException {
        login = new Login(driver);
        login.login();
    }
  
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        try { login.logout(); } catch (Exception ignored) {}
        DriverFactory.quitDriver();
    }
    @Test(dataProvider = "estimateData",
          dataProviderClass = EstimateDataProvider.class)
    public void estimateTest(EstimateData data) throws Exception {
        CreateEstimatePage estPage =
                new  CreateEstimatePage(driver);
        estPage.navigateToNewEstimate();
        estPage.fillEstimateHeader(
        		data.customerName,
        		data.referenceNo,
        		data.subject, 
        		data.salesPerson,         		
        		data.estDate,
        		data.expiryDate,        		
        		data.taxType, 
        		data.priceList
        		);
        String estNumber = estPage.estimatenumber();
        String[] itemNames = data.items.stream()
                .map(i -> i.get("Item Name"))
                .toArray(String[]::new);
        String[] quantities = data.items.stream()
                .map(i -> i.get("Item Quantity"))
                .toArray(String[]::new);
        String[] discountType = data.items.stream()
                .map(i -> i.get("Discount Type"))
                .toArray(String[]::new);
        String[] discount = data.items.stream()
                .map(i -> i.get("Discount"))
                .toArray(String[]::new);
        estPage.addItems(itemNames, quantities, discountType, discount);
        estPage.addNotesAndTerms(data.customerNote, data.termsAndconditions);
        estPage.saveAsMethod(data.saveAs);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(
                estPage.verifyEstimateCreated(estNumber),
                "Estimate creation failed: " + estNumber
        );
        softAssert.assertAll();
	
    }	
}
