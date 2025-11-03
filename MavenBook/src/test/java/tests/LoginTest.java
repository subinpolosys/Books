package tests;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import base.BaseSetup;
import pages.Login;
import pages.LoginTPage;
import utils.ConfigReader;

public class LoginTest extends BaseSetup {
	private Login loginPage;
	 private String eid = ConfigReader.get("username");
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
 public void loginTest() throws InterruptedException {
	  
	  LoginTPage loginP = new LoginTPage(driver);
	 // loginP.verifyuser("");
	  
	  assertTrue(loginP.verifyuser(eid),
              "Estimate not found or failed to login : " + eid);
 }
 
}



