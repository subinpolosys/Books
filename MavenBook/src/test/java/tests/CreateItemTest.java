package tests;

import static org.testng.Assert.assertTrue;

import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import base.BaseSetup;
import base.BaseTest;
import pages.CreateItemPage;
import pages.Login;
import utils.ItemExcelReader;

public class CreateItemTest extends BaseTest {
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

   @DataProvider(name = "ItemData")
   public Object[][] getItemData() throws Exception {
       String filePath = "src/test/resources/Items.xlsx";
       String sheetName = "Items";
       return ItemExcelReader.getTestData(filePath, sheetName);
   }
  @Test(dataProvider = "ItemData")
  public void createItemTest (
		  String name,
          String eanCode,
          String hsnCode,
          String brand,
          String category,
          String remark,
          String baseUnit,
          String sellingPrice,
          String purchasePrice,
          String sellingDescription,
          String purchaseDescription,
          String openingStock,
          String openingStockRate,
          String reorderPoint
          ) throws InterruptedException {	  
	  if (name == null || name.trim().isEmpty()) {
	        throw new SkipException("Item Name cannot be empty — skipping test case.");
	    }
	  if (baseUnit == null || baseUnit.trim().isEmpty()) {
	        throw new SkipException("Base Unit cannot be empty — skipping test case.");
	    }
	  if (sellingPrice == null || sellingPrice.trim().isEmpty()||purchasePrice == null || purchasePrice.trim().isEmpty()) {
	        throw new SkipException("Selling OR Purchase Price cannot be empty — skipping test case.");
	    }
	  CreateItemPage createItemPage=new CreateItemPage(driver);
	  createItemPage.navigateToNewItem();
	  createItemPage.createNewItem(name,eanCode,hsnCode,brand,category,remark, baseUnit,sellingPrice, purchasePrice,sellingDescription,purchaseDescription,openingStock,openingStockRate,reorderPoint);
	  createItemPage.clickSaveButton();
	  SoftAssert soft=new SoftAssert();
	  soft.assertTrue(createItemPage.verifyItemCreated(name),
              "Item not found or failed to create : " + name);
	  soft.assertAll();
  }
}
