package dataprovider;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import model.PurchaseOrderData;
import utils.ExcelMapper;
import utils.ExcelReader;
public class PurchaseOrderDataProvider {
    @DataProvider(name = "purchaseOrderData", parallel = false)
    public Iterator<Object[]> getPurchaseOrderData() throws IOException {
        List<Map<String, Object>> rows =
                ExcelReader.getMasterDetailData(
                        System.getProperty("user.dir") +
                        "/src/test/resources/PurchaseOrderData.xlsx",
                        "PurchaseOrderHeader",
                        "PurchaseOrderItems"
                );
        return rows.stream().map(row -> {
            PurchaseOrderData po = new PurchaseOrderData();
            po.vendorName     = ExcelMapper.get(row, "Vendor Name");
            po.referenceNo    = ExcelMapper.get(row, "Reference Number");
            po.poDate         = ExcelMapper.get(row, "Purchase Order Date");
            po.paymentTerms  = ExcelMapper.get(row, "Payment Terms");
            po.deliveryDate  = ExcelMapper.get(row, "Exp delivery Date");
            po.taxType       = ExcelMapper.get(row, "Tax");
            po.priceList     = ExcelMapper.get(row, "Price List");
            po.discountLevel = ExcelMapper.get(row, "Discount Level");
            po.saveAs        = ExcelMapper.get(row, "Save As");
            po.discountAfterBeforeTax=ExcelMapper.get(row, "Discount After-Before Tax");
            po.discountType=ExcelMapper.get(row, "Discount TType");
            po.discountValue=ExcelMapper.get(row, "DiscountT");
            po.discountAccount=ExcelMapper.get(row, "Discount Account");           
            @SuppressWarnings("unchecked")
            List<Map<String, String>> items =
                    (List<Map<String, String>>) row.get("items");
            if (po.vendorName == null || po.vendorName.isBlank()) {
                throw new SkipException("Vendor Name is empty in Excel");
            }
            if (items == null || items.isEmpty()) {
                throw new SkipException("No items for vendor: " + po.vendorName);
            }
            po.items = items;
            return new Object[] { po };
        }).iterator();
    }
}
