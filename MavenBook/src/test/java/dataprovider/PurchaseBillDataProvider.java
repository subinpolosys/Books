package dataprovider;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import model.PurchaseBillData;
import utils.ExcelMapper;
import utils.ExcelReader;

public class PurchaseBillDataProvider {
	@DataProvider(name = "purchaseBillData", parallel = false)
    public Iterator<Object[]> getPurchaseBillData() throws IOException {
        List<Map<String, Object>> rows =
                ExcelReader.getMasterDetailData(
                        System.getProperty("user.dir") +
                        "/src/test/resources/PurchaseBillData.xlsx",
                        "PurchaseBillHeader",
                        "PurchaseBillItems");
        return rows.stream().map(row -> {
            PurchaseBillData pi = new PurchaseBillData();
            pi.vendorName     = ExcelMapper.get(row, "Vendor Name");
            pi.entryDate	  = ExcelMapper.get(row, "Entry Date");
            pi.referenceNo    = ExcelMapper.get(row, "Reference Number");
            pi.piDate         = ExcelMapper.get(row, "Invoice Date");
            pi.paymentTerms  = ExcelMapper.get(row, "Payment Terms");
            pi.deliveryDate  = ExcelMapper.get(row, "Expected Delivery Date");
            pi.taxType       = ExcelMapper.get(row, "Tax");
            pi.priceList     = ExcelMapper.get(row, "Price List");
            pi.customerNote  =ExcelMapper.get(row, "Customer Notes");
            pi.discountLevel = ExcelMapper.get(row, "Discount Level");
            pi.saveAs        = ExcelMapper.get(row, "Save As");
            pi.discountAfterBeforeTax=ExcelMapper.get(row, "Discount After-Before Tax");
            pi.discountType=ExcelMapper.get(row, "Discount TType");
            pi.discountValue=ExcelMapper.get(row, "DiscountT");
            pi.discountAccount=ExcelMapper.get(row, "Discount Account");           
            @SuppressWarnings("unchecked")
            List<Map<String, String>> items =
                    (List<Map<String, String>>) row.get("items");
            if (pi.vendorName == null || pi.vendorName.isBlank()) {
                throw new SkipException("Vendor Name is empty in Excel");
            }
            if (items == null || items.isEmpty()) {
                throw new SkipException("No items for vendor: " + pi.vendorName);
            }
            pi.items = items;
            return new Object[] { pi };
        }).iterator();
    }
	
}
