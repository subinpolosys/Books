package pages;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.Utilities;

public class CreateItemPage {
	
	 private final WebDriver driver;
	    private final WebDriverWait wait;

	    public CreateItemPage(WebDriver driver) {
	        this.driver = driver;
	        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
	    }
	
	 // ──────────────── Navigation Elements ────────────────
	    private final By itemMasterMenuField = By.xpath("//a[contains(text(),'Item Master')]");
	    private final By itemsMenufield = By.xpath("//span[contains(text(),'Items')]");
	    private final By newItemButtonField = By.xpath("//p[contains(text(),'new')]");
	    
	    // ──────────────── Item Fields ────────────────
	    
	    private final By itemNameField = By.id("name");
	    private final By eanCodeField = By.id("sku");
	    private final By itemCodeField = By.id("item_code");
	    private final By arabicNameField = By.id("name_in_secondary_lang");
	    
	    private final By hsnCodeField = By.id("hsn_code");
	    private final By genBarCodeField = By.xpath("//button[@name='button' and contains(text(),'Generate')]");
	    
//	    private final By brandDropdownField = By.id("//button[@id='headlessui-combobox-button-:r2d:']/div[2]/div");
//	    private final By brandListField = By.id("//label[contains(text(),'Brand')]");
	    private final By searchBrandField = By.xpath("//label[contains(text(),'Brand')]/following-sibling::input[@type='text']");
	    private final By selectBrandField = By.xpath(" //ul/li[1]/span");
	  
	    private final By searchCategoryField = By.xpath("//label[text()='Category']/following-sibling::input[@type='text']");
	    private final By selectCategoryField = By.xpath("//ul/li[1]/span");
	    
	    private final By remarkField = By.id("remark");
	    
	    private final By searchBaseUnitField = By.xpath("//label[contains(text(),'Base Unit')]/following-sibling::input[@type='text']");
	    private final By selectBaseunitField = By.xpath("//ul/li[1]/span");
	    
	    // ──────────────── Item Price Fields ────────────────
	    
	    private final By sellingPriceField = By.id("default_selling_price");
	    private final By selesDescriptionField = By.id("sales_description");
	    private final By purchasePriceField = By.id("default_purchase_price");
	    private final By purchaseDescriptionField = By.id("purchase_description");
	    
	    // ──────────────── Inventory────────────────
	    
	    private final By openingStocQtykField = By.id("opening_stock_qty");
	    private final By openingStockRateField = By.id("opening_stock_rate");
	    private final By reorderPointField = By.id("reorder_point");
	  
	    private final By saveButtonField = By.name("default_submit");
	     
	    private final By createdItemNameField = By.xpath("//h1");
	 private final By nameAlreadyExistalertField=By.xpath("//div[@role='alert']/div[2]");
	    
	    
	    public void navigateToNewItem() throws InterruptedException {
	        wait.until(ExpectedConditions.elementToBeClickable(itemMasterMenuField)).click();
	        wait.until(ExpectedConditions.elementToBeClickable(itemsMenufield)).click();
	        wait.until(ExpectedConditions.elementToBeClickable(newItemButtonField)).click();
	        Thread.sleep(200);
	    }
	
	    public void createNewItem( String name,String eanCode,String hsnCode,String brand,String category,String remark,String baseUnit,String sellingPrice,String purchasePrice,
	    		String sellingDescription,String purchaseDescription,String openingStock,String openingStockRate,String reorderPoint) throws InterruptedException {
	    	//System.out.println(name+","+eanCode+","+hsnCode+","+brand+","+category+","+remark+","+baseUnit+","+sellingPrice+","+ purchasePrice+","+sellingDescription+","+purchaseDescription+","+openingStock+","+openingStockRate+","+reorderPoint);
	    	 Utilities.injectNetworkTracker(driver);
	    	if (name != null && !name.trim().isEmpty()) { 
	    		wait.until(ExpectedConditions.visibilityOfElementLocated(itemNameField)).sendKeys(name);
	    	
	    	}else {
	    		System.out.println("Item Name cannot be Empty !!!");
	    	}
	    	if (eanCode != null && !eanCode .trim().isEmpty()) {
	    		wait.until(ExpectedConditions.visibilityOfElementLocated(eanCodeField)).sendKeys(eanCode);
	    	}
	    	if (hsnCode != null && !hsnCode .trim().isEmpty()) {
	    		wait.until(ExpectedConditions.visibilityOfElementLocated(hsnCodeField)).sendKeys(hsnCode);
	    	}
	    	wait.until(ExpectedConditions.visibilityOfElementLocated(genBarCodeField)).click();
	    	
	    	if (brand != null && !brand .trim().isEmpty()) {
	    		Utilities.selectIfListed(driver, searchBrandField, selectBrandField,brand);
	    	}
	    	if (category != null && !category .trim().isEmpty()) {
	    		Utilities.selectIfListed(driver, searchCategoryField, selectCategoryField,category);
		    	}
	    	
	    	if (remark != null && !remark .trim().isEmpty())
	    		wait.until(ExpectedConditions.visibilityOfElementLocated(remarkField)).sendKeys(remark);
	    	
	    	if (baseUnit != null && !baseUnit .trim().isEmpty()) {
	    		Utilities.selectIfListed(driver, searchBaseUnitField, selectBaseunitField,baseUnit);
		    	}
	    	else {
	    		System.out.println("Base Unit cannot be Empty !!!");
	    	}
	    	
	    	
	    	if (sellingPrice != null && !sellingPrice .trim().isEmpty()) {
	    	wait.until(ExpectedConditions.visibilityOfElementLocated(sellingPriceField)).sendKeys(sellingPrice);
	    	}else {
	    		System.out.println("Selling Price cannot be Empty !!!");
	    	}
	    	if (purchasePrice != null && !purchasePrice .trim().isEmpty()) {
	    		wait.until(ExpectedConditions.visibilityOfElementLocated(purchasePriceField)).sendKeys(purchasePrice);
	    	}else {
	    		System.out.println("Purchase Price cannot be Empty !!!");
	    	}
	    	
	    	if (sellingDescription != null && !sellingDescription .trim().isEmpty()) {
	    		wait.until(ExpectedConditions.visibilityOfElementLocated(selesDescriptionField)).sendKeys(sellingDescription);
	    	}
	    	
	    	if (purchaseDescription != null && !purchaseDescription .trim().isEmpty()) {
	    		wait.until(ExpectedConditions.visibilityOfElementLocated(purchaseDescriptionField)).sendKeys(purchaseDescription);
	    	}
	    	
	    	JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement stocks  = driver.findElement(openingStocQtykField);
		    js.executeScript("arguments[0].scrollIntoView();",stocks);  
	    	
	    	
	    	if (openingStock != null && !openingStock  .trim().isEmpty()) {
	    		wait.until(ExpectedConditions.visibilityOfElementLocated(openingStocQtykField)).sendKeys(openingStock );
	    	}
	    	if (openingStockRate != null && !openingStockRate .trim().isEmpty()) {
	    		wait.until(ExpectedConditions.visibilityOfElementLocated(openingStockRateField)).sendKeys(openingStockRate);
	    	}
	    	if (reorderPoint!= null && !reorderPoint.trim().isEmpty()) {
	    		wait.until(ExpectedConditions.visibilityOfElementLocated(reorderPointField)).sendKeys(reorderPoint);
	    	}

	    }
	    public void clickSaveButton() {
	        try {
	        	Utilities.waitForPageToLoad(driver);
	        	
	            WebElement saveButton = wait.until(ExpectedConditions.visibilityOfElementLocated(saveButtonField));
	            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", saveButton);

	           
	                    saveButton.click();
	        }catch(Exception e) {
	        	
	        }
	        try {
	        	wait.until(ExpectedConditions.visibilityOfElementLocated(nameAlreadyExistalertField));
	        	boolean nameError=driver.findElement(nameAlreadyExistalertField).isDisplayed();
	        	if(nameError) {
	        	System.out.println(driver.findElement(nameAlreadyExistalertField).getText());
	        	}
	        }
	        catch(Exception e) {
	        	//System.out.println("yes");
	        }
	        
	        
	    }
	    public boolean verifyItemCreated(String expectedItemName) {
	    	try {
	        wait.until(ExpectedConditions.visibilityOfElementLocated(createdItemNameField));
	        String actualItemName = driver.findElement(createdItemNameField).getText();
	        System.out.println("Created Item : "+actualItemName);
	        return actualItemName.contains(expectedItemName);
	    	}catch(Exception e) {
	    		return false;
	    	}
	    	
	    }
	    
	    
	    
	    
	

}
