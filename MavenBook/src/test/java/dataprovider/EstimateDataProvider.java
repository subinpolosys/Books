package dataprovider;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;

import model.EstimateData;
import model.PurchaseOrderData;
import utils.ExcelMapper;
import utils.ExcelReader;

public class EstimateDataProvider {
	@DataProvider(name = "estimateData", parallel = false)
    public Iterator<Object[]> getEstimateData() throws IOException {
        List<Map<String, Object>> rows =
                ExcelReader.getMasterDetailData(
                        System.getProperty("user.dir") +
                        "/src/test/resources/EstimateData.xlsx",
                        "EstimateHeader",
                        "EstimateItems"
                );
        return rows.stream().map(row -> {
            EstimateData est = new EstimateData();
            est.customerName     = ExcelMapper.get(row, "Customer Name");
            est.referenceNo    = ExcelMapper.get(row, "Reference Number");
            est.estDate         = ExcelMapper.get(row, "Estimate Date");
//            est.paymentTerms  = ExcelMapper.get(row, "Payment Terms");
            est.expiryDate  = ExcelMapper.get(row, "Expiry Date");
            est.salesPerson=ExcelMapper.get(row, "Sales Person");
            est.subject=ExcelMapper.get(row, "Subject");
            est.taxType       = ExcelMapper.get(row, "Tax");
            est.priceList     = ExcelMapper.get(row, "Price List");
//          est.discountLevel = ExcelMapper.get(row, "Discount Level");
            est.saveAs        = ExcelMapper.get(row, "Save As");
//          est.discountAfterBeforeTax=ExcelMapper.get(row, "Discount After-Before Tax");
//            est.discountType=ExcelMapper.get(row, "Discount TType");
//            est.discountValue=ExcelMapper.get(row, "DiscountT");
//            est.discountAccount=ExcelMapper.get(row, "Discount Account");  
            est.termsAndconditions=ExcelMapper.get(row, "Terms And Conditions");
            @SuppressWarnings("unchecked")
            List<Map<String, String>> items =
                    (List<Map<String, String>>) row.get("items");
            if (est.customerName == null || est.customerName.isBlank()) {
                throw new SkipException("Customer Name is empty in Excel");
            }
            if (items == null || items.isEmpty()) {
                throw new SkipException("No items for Customer: " + est.customerName);
            }
            est.items = items;
            return new Object[] { est };
        }).iterator();
    }

}
