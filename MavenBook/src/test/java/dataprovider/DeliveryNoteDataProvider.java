package dataprovider;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.testng.SkipException;
import org.testng.annotations.DataProvider;

import model.DeliveryNoteData;
import utils.ExcelMapper;
import utils.ExcelReader;

public class DeliveryNoteDataProvider {
	@DataProvider(name = "deliveryNoteData", parallel = false)
    public Iterator<Object[]> getDeliveryNoterData() throws IOException {
        List<Map<String, Object>> rows =
                ExcelReader.getMasterDetailData(
                        System.getProperty("user.dir") +
                        "/src/test/resources/DeliveryNoteData.xlsx",
                        "DeliveryNoteHeader",
                        "DeliveryNoteItems"
                );
        return rows.stream().map(row -> {
        	DeliveryNoteData dn = new DeliveryNoteData();
        	dn.customerName     = ExcelMapper.get(row, "Customer Name");
        	dn.referenceNo    = ExcelMapper.get(row, "Reference");
        	dn.dnDate    = ExcelMapper.get(row, "Date");
        	dn.dnType    = ExcelMapper.get(row, "Challan Type");
        	dn.taxType       = ExcelMapper.get(row, "Tax");
        	dn.priceList     = ExcelMapper.get(row, "Price List");
            dn.customerNote = ExcelMapper.get(row, "Customer Notes");
        	dn.saveAs        = ExcelMapper.get(row, "Save As");
        	dn.termsAndconditions=ExcelMapper.get(row, "Terms And Conditions");
            @SuppressWarnings("unchecked")
            List<Map<String, String>> items =
                    (List<Map<String, String>>) row.get("items");
            if (dn.customerName == null || dn.customerName.isBlank()) {
                throw new SkipException("Customer Name is empty in Excel");
            }
            if (items == null || items.isEmpty()) {
                throw new SkipException("No items for Customer: " + dn.customerName);
            }
            dn.items = items;
            return new Object[] { dn };
        }).iterator();
    }
}
