package tests;

import base.BaseTest;
import pages.Login;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CreateESTTest extends BaseTest {
	 private Login loginPage;
	 
	String customerName="ABC super";
	String ES_No="";
	String[]itemName= {"lary","bp","puls"};
    String[]itemQtys= {"1","4","2"};
    int q=0;
    int searchListItemIndux=1;
	 
    private By itemAddField=By.xpath("//ul/li["+searchListItemIndux+"]/div/div/h1");
    private By termsField=By.xpath("//textarea[@id='terms_and_conditions']");
    int itemNumber=2;

 // Menu and Navigation
    private By salesMenuField = By.xpath("//div[@title='sales']/a[contains(text(),'Sales')]");
    private By estimateMenuField = By.xpath("//div[@title='estimates']/a[contains(text(),'Estimates')]");
    private By newEstimateButtonField = By.xpath("//button[@class='w-24 py-2 select-none bg-transparent flex items-center justify-center space-x-2 rounded-md']/p[contains(text(),'new')]");

    // Customer Selection
    private By customerDropdownField = By.xpath("//input[@placeholder='Select Customer Name']");
    private By customerSelectionField = By.xpath("//div[@class='flex flex-col gap-4 px-10 py-5']/div/div/div/div/div/div/div/ul/li[1]/div/div/h1");

    // Item Details and List
    private By itemDetailsField = By.xpath("//table[@class=' w-full ']/thead/tr[1]/td[1]");
    private By itemListField = By.xpath("//input[@placeholder='Type or click to add items']");
    private By itemSelectField = By.xpath("//ul[1]/li[" + itemNumber + "]"); // dynamic, can be handled later

    // Financial Summary
    private By closingBalanceField = By.xpath("//h2[contains(text(),'Closing Balance')]//following-sibling::h2[1]");
    private By totalAmountField = By.xpath("//tbody/tr[1]/td[6]/div[1]/div");

    // Action Buttons
    private By saveAsDraftButtonField = By.xpath("//button[contains(text(),'Save as Draft')]");
    private By reloadButtonField = By.xpath("//div[@class='flex justify-between pb-2']/div[2]/button");
  
    private By customerNoteField=By.xpath("//textarea[@id='customer notes']");
    
	String referenceno="";
  	String subject="Test Estimate";
  	private By estimateIDField = By.xpath("//input[@id='sales_estimate_no']");
	private By referenceNumberfield=By.id("reference"); 
	private By subjectField=By.id("subject"); 
	private By estimateNoinListfield=By.xpath("(//tr/td[3])[1]/div/div");
    @Test
    public void createEstimateTest() throws InterruptedException {
       

        WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(20));
       // wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@title='sales']")));
     	WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(30));
	    driver.findElement(salesMenuField).click();
	    driver.findElement(estimateMenuField).click();
		Thread.sleep(500);
		driver.findElement(newEstimateButtonField).click();
		Thread.sleep(5000);
		By selct_customer=customerDropdownField;
		driver.findElement(selct_customer).sendKeys(customerName);
		wait2.until(ExpectedConditions.visibilityOfElementLocated(customerSelectionField));
		driver.findElement(customerSelectionField).click();
        
		 WebElement estimateID=driver.findElement(estimateIDField);
			
			ES_No=estimateID.getAttribute("value");
			referenceno="1234567";
			driver.findElement(referenceNumberfield).sendKeys(referenceno);
			Thread.sleep(150);	
							/* 
				         * wait2.until(ExpectedConditions.visibilityOfElementLocated(By.id("date")));
						String val = dr.findElement(By.id("date")).getAttribute("value");
						System.out.println("Date : " + val);
						Thread.sleep(1500);
				         wait2.until(ExpectedConditions.visibilityOfElementLocated(By.id("expected_delivery_date")));
						dr.findElement(By.id("expected_delivery_date"));
						
						String dateValue = "2025-12-25"; // Must match HTML5 format
						JavascriptExecutor jsq = (JavascriptExecutor) dr;
				
						jsq.executeScript("document.getElementById('expected_delivery_date').removeAttribute('min');");
				
						jsq.executeScript(
								 "let field = document.getElementById('expected_delivery_date');" +
										    "field.removeAttribute('readonly');" +
										    "let nativeInputValueSetter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
										    "nativeInputValueSetter.call(field, arguments[0]);" + // Set the value like a real user
										    "field.dispatchEvent(new Event('input', { bubbles: true }));" + // React listens to this
										    "field.dispatchEvent(new Event('change', { bubbles: true }));", 
										    dateValue
								
						    
						);
						//Thread.sleep(5000);
						String val2 = dr.findElement(By.id("expected_delivery_date")).getAttribute("value");
						System.out.println("Date : " + val2);
						
						Thread.sleep(5000);
				         * 
				         * */
			
			JavascriptExecutor sub = (JavascriptExecutor) driver;
			WebElement subj  = driver.findElement(subjectField);
		    sub.executeScript("arguments[0].scrollIntoView();",subj);  
		    driver.findElement(subjectField).click();
			driver.findElement(subjectField).sendKeys(subject);
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement itemdetail  = driver.findElement(itemDetailsField);
		    js.executeScript("arguments[0].scrollIntoView();",itemdetail);  
			driver.findElement(itemDetailsField).click();  //#####  
			Thread.sleep(500);	
		
			for(int i=0;i<itemName.length;i++) {
				String qy="";
				if(i<itemQtys.length)
					qy=itemQtys[i];
				else qy="1";
			driver.findElement(itemListField).sendKeys(itemName[i]);
			Thread.sleep(500);
			wait2.until(ExpectedConditions.visibilityOfElementLocated(itemAddField));
//			Thread.sleep(2000);
			driver.findElement(itemAddField).click();
			Thread.sleep(500);
			q=i+1;
			wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
					(By.xpath("//tbody/tr["+q+"]/td[5]/div/input[@type='number']")));
			WebElement itQty=driver.findElement(By.xpath("//tbody/tr["+q+"]/td[4]/div/input[@type='number']"));
			itQty.clear();
			
			itQty.sendKeys(qy);
			System.out.println(itemName[i]+" : "+qy);
			Thread.sleep(500);
			}
			
			  WebDriverWait wait3 = new WebDriverWait(driver, Duration.ofSeconds(10));
				 WebElement closingBalance  = driver.findElement(closingBalanceField);
			  	 js.executeScript("arguments[0].scrollIntoView();",closingBalance);
			  	 String total=driver.findElement(closingBalanceField).getText();
				System.out.println("Closing balance : "+total);
			

				/*-----------------Notes------------------------------ */
				WebElement notes= driver.findElement(customerNoteField);
				notes.click();
				notes.clear();
				notes.sendKeys("Dear "+customerName+" "+ES_No+"  has been created by Automation");
				/*-----------------Notes------------------------------ */
				/* ----------------*/
						WebElement terms= driver.findElement(termsField);
						terms.click();
						terms.clear();
						terms.sendKeys("This is a system-generated document. Ensure accuracy before acceptance.");
						/* ----------------*/	
			
			WebElement draft=driver.findElement(saveAsDraftButtonField);   
			wait3.until(ExpectedConditions.visibilityOfAllElements(draft));
			Actions act=new Actions(driver);
			act.moveToElement(draft).perform();
			
			driver.findElement(saveAsDraftButtonField).click();
			
			String estimate_no="";
			try {
			    WebDriverWait waitestimate = new WebDriverWait(driver, Duration.ofSeconds(10));
			    
			    
			    estimate_no=waitestimate.until(ExpectedConditions.presenceOfElementLocated(estimateNoinListfield)).getText();
			   
			    
			    System.out.println("Created Estimate No: "+estimate_no);
			   // System.out.println("list Estimate time:"+currentDateTime());

			} catch (Exception e) {
			    e.printStackTrace();
			}

			assertEquals(ES_No, estimate_no,"Fail:");
		   Thread.sleep(5000);		
	   
        
    }
    
    
    @BeforeMethod
    public void beformethod() throws InterruptedException {
    	loginPage = new Login(driver);
    	loginPage.login();
    }
    @AfterMethod
    
	  public void afterMethod() {
    	//Login loginPage = new Login(driver);
    	loginPage.logout();
		}
    
    
    
}