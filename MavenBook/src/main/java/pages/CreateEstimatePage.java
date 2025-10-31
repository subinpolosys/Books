package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Page Object for the Create Estimate module.
 * Handles navigation, data entry, and validation on the Estimate page.
 */
public class CreateEstimatePage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public CreateEstimatePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // ──────────────── Navigation Elements ────────────────
    private final By salesMenuField = By.xpath("//div[@title='sales']/a[contains(text(),'Sales')]");
    private final By estimateMenuField = By.xpath("//div[@title='estimates']/a[contains(text(),'Estimates')]");
    private final By newEstimateButtonField = By.xpath("//button/p[contains(text(),'new')]");

    // ──────────────── Header / Customer Fields ────────────────
    private final By customerDropdownField = By.xpath("//input[@placeholder='Select Customer Name']");
    private final By firstCustomerOptionField =  By.xpath("//div[@class='flex flex-col gap-4 px-10 py-5']/div/div/div/div/div/div/div/ul/li[1]/div/div/h1");
    private final By estimateNumberfield=By.id("sales_estimate_no");
    private final By referenceNumberField = By.id("reference");
    private final By subjectField = By.id("subject");

    // ──────────────── Item Fields ────────────────
    private final By itemDetailsField = By.xpath("//table[@class=' w-full ']/thead/tr[1]/td[1]");
    private final By itemListField = By.xpath("//input[@placeholder='Type or click to add items']");
    private final String itemSelectField = "//ul[1]/li[%d]"; // dynamic

    // ──────────────── Notes and Terms ────────────────
    private final By customerNoteField = By.id("customer notes");
    private final By termsField = By.id("terms_and_conditions");

    // ──────────────── Action Buttons ────────────────
    private final By saveAsDraftButtonField = By.xpath("//button[contains(text(),'Save as Draft')]");
    private final By estimateNoinListField = By.xpath("(//tr/td[3])[1]/div/div");

    // ──────────────── Actions ────────────────

    /** Navigate to Create Estimate Page */
    public void navigateToNewEstimate() {
        wait.until(ExpectedConditions.elementToBeClickable(salesMenuField)).click();
        wait.until(ExpectedConditions.elementToBeClickable(estimateMenuField)).click();
        wait.until(ExpectedConditions.elementToBeClickable(newEstimateButtonField)).click();
    }

    public String estimatenumber() {
    	String ESTNO=wait.until(ExpectedConditions.presenceOfElementLocated(estimateNumberfield)).getText();
    	return ESTNO;
    }
    /** Fill in header details (customer, ref no, subject) */
    public void fillEstimateHeader(String customerName, String referenceNo, String subject) {
        wait.until(ExpectedConditions.elementToBeClickable(customerDropdownField)).click();
        driver.findElement(customerDropdownField).sendKeys(customerName);
        
        wait.until(ExpectedConditions.elementToBeClickable(firstCustomerOptionField)).click();

        driver.findElement(referenceNumberField).sendKeys(referenceNo);
        
        JavascriptExecutor sub = (JavascriptExecutor) driver;
        WebElement subj  = driver.findElement(subjectField);
	    sub.executeScript("arguments[0].scrollIntoView();",subj);  
	    driver.findElement(subjectField).click();
        driver.findElement(subjectField).sendKeys(subject);
    }

    /** Add multiple items dynamically 
     * @throws InterruptedException */
    public void addItems(String[] itemNames, String[] itemQtys) throws InterruptedException {
    	
		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebElement itemdetail  = driver.findElement(itemDetailsField);
	    js.executeScript("arguments[0].scrollIntoView();",itemdetail);  
		driver.findElement(itemDetailsField).click();  //#####  
		Thread.sleep(500);	
    	
    	
        for (int i = 0; i < itemNames.length; i++) {
            wait.until(ExpectedConditions.elementToBeClickable(itemListField)).click();
            driver.findElement(itemListField).sendKeys(itemNames[i]);
            Thread.sleep(500);
            // Select item dynamically
            By selectItem = By.xpath(String.format(itemSelectField, 1));
            wait.until(ExpectedConditions.elementToBeClickable(selectItem)).click();
            Thread.sleep(500);
            // Fill item quantity
            WebElement qtyField = driver.findElement(By.xpath("//tbody/tr[" + (i + 1) + "]/td[4]//input"));
            qtyField.clear();
            qtyField.sendKeys(itemQtys[i]);
            Thread.sleep(500);
        }
    }

    /** Add optional notes and terms */
    public void addNotesAndTerms(String customerNote, String terms) {
        if (customerNote != null && !customerNote.isEmpty()) {
            driver.findElement(customerNoteField).sendKeys(customerNote);
        }
        if (terms != null && !terms.isEmpty()) {
            driver.findElement(termsField).sendKeys(terms);
        }
    }

    /** Save the estimate as draft */
    public void saveAsDraft() {
        wait.until(ExpectedConditions.elementToBeClickable(saveAsDraftButtonField)).click();
    }

    /** Verify estimate saved by checking list */
    public boolean verifyEstimateCreated(String expectedstimateNo) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(estimateNoinListField));
        String actualEstimateNo = driver.findElement(estimateNoinListField).getText();
        return actualEstimateNo.contains(expectedstimateNo);
    }
}
