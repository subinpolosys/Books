package tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import base.BaseTest;
import dataprovider.ExpenseDataProvider;
import drivers.DriverFactory;
import model.ExpenseData;
import pages.CreateExpensePage;
import pages.Login;

public class ExpenseTest extends BaseTest{
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
    @Test(dataProvider = "ExpenseData",
          dataProviderClass = ExpenseDataProvider.class)
    public void expenseTest(ExpenseData data) throws Exception {
    	CreateExpensePage expensePage = new CreateExpensePage(driver);
    	expensePage.navigateToNewExpense();   	
    	expensePage.fillExpenseFields(
    			 	data.amount,
    		        data.expenseDate,
    		        data.employee,
    		        data.expenseAccount,
    		        data.paidThroughAccount,
    		        data.currency,
    		        data.vendorName,
    		        data.taxTreatment,
    		        data.tax,
    		        data.inclusiveTax,
    		        data.referenceNumber,
    		        data.customer,
    		        data.billable,
    		        data.markup,
    		        data.notes);
    	
    	expensePage.save();
    	String actualMessage= expensePage.verifyExpenseCreated();
    	 System.out.println("Confirmation message:"+actualMessage);
 	    SoftAssert softAssert2 = new SoftAssert();
		softAssert2.assertTrue(actualMessage.contains("Created Successfully"), "Expected success message but got: " + actualMessage);
    		   	
    }
}
