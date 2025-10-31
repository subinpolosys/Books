package tests;

import base.BaseSetup;
import pages.CreateEstimatePage;
import pages.Login;
import utils.ExcelReader;

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
        loginPage.login();
    }

   @AfterMethod
    public void afterMethod() {
        loginPage.logout();
   }

    @Test
    public void createEstimateTest() throws IOException, InterruptedException {
        String filePath = System.getProperty("user.dir") + "/src/test/resources/EstimateData.xlsx";
        List<Map<String, Object>> allEstimates =
                ExcelReader.getMasterDetailData(filePath, "EstimateHeader", "EstimateItems");

        CreateEstimatePage estimatePage = new CreateEstimatePage(driver);

        for (Map<String, Object> estimate : allEstimates) {
            String customerName = estimate.get("customerName").toString();
            String referenceNo = estimate.get("referenceNo").toString();
            String subject = estimate.get("subject").toString();

            @SuppressWarnings("unchecked")
            List<Map<String, String>> items = (List<Map<String, String>>) estimate.get("items");
            String[] itemNames = items.stream().map(i -> i.get("itemName")).toArray(String[]::new);
            String[] itemQtys = items.stream().map(i -> i.get("itemQty")).toArray(String[]::new);

            // ✅ Correct method name here
            estimatePage.navigateToNewEstimate();

            estimatePage.fillEstimateHeader(customerName, referenceNo, subject);
            String EstNo=estimatePage.estimatenumber();
            estimatePage.addItems(itemNames, itemQtys);
            estimatePage.addNotesAndTerms(
                    "Dear " + customerName + ", " + EstNo + " has been created by Automation",
                    "This is a system-generated document. Ensure accuracy before acceptance."
            );
            estimatePage.saveAsDraft();

            assertTrue(estimatePage.verifyEstimateCreated(EstNo),
                    "Estimate not found or failed to create : " + EstNo);
        }
    }
}
