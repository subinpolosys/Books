package dataprovider;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import model.VendorCreditData;
import utils.ExcelMapper;
import utils.ExcelReader;

public class VendorCreditDataProvider {
	@DataProvider(name = "vendorCreditData", parallel = false)
    public Iterator<Object[]> getVendorCreditData() throws IOException {
        List<Map<String, Object>> rows =
                ExcelReader.getMasterDetailData(
                        System.getProperty("user.dir") +
                        "/src/test/resources/VendorCreditData.xlsx",
                        "VendorCreditHeader",
                        "VendorCreditItems");
        return rows.stream().map(row -> {
        	VendorCreditData vc = new VendorCreditData();
        	vc.vendorName     = ExcelMapper.get(row, "Vendor Name");
        	vc.billNumber	  = ExcelMapper.get(row, "Bill Number");
        	vc.orderNumber    = ExcelMapper.get(row, "Order Number");
        	vc.vcDate         = ExcelMapper.get(row, "VC Date");
        	vc.taxType        = ExcelMapper.get(row, "Tax");
        	vc.priceList      = ExcelMapper.get(row, "Price List");
        	vc.saveAs         = ExcelMapper.get(row, "Save As");
        	vc.customerNote	  = ExcelMapper.get(row, "Customer Notes");
        	vc.terms		  = ExcelMapper.get(row, "Terms And Conditions");
        	 @SuppressWarnings("unchecked")
            List<Map<String, String>> items =
                    (List<Map<String, String>>) row.get("items");
            if (vc.vendorName == null || vc.vendorName.isBlank()) {
                throw new SkipException("Vendor Name is empty in Excel");
            }
            if (items == null || items.isEmpty()) {
                throw new SkipException("No items for vendor: " + vc.vendorName);
            }
            vc.items = items;
            return new Object[] { vc };
        }).iterator();
    }
	
}
