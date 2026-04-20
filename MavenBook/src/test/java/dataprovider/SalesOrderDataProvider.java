package dataprovider;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.testng.SkipException;
import org.testng.annotations.DataProvider;

import model.SalesOrderData;
import utils.ExcelMapper;
import utils.ExcelReader;

public class SalesOrderDataProvider {
	@DataProvider(name = "salesOrderData", parallel = false)
    public Iterator<Object[]> getSalesOrderData() throws IOException {
        List<Map<String, Object>> rows =
                ExcelReader.getMasterDetailData(
                        System.getProperty("user.dir") +
                        "/src/test/resources/SalesOrderData.xlsx",
                        "SalesOrderHeader",
                        "SalesOrderItems"
                );
        return rows.stream().map(row -> {
        	SalesOrderData est = new SalesOrderData();
            est.customerName     = ExcelMapper.get(row, "Customer Name");
            est.referenceNo    = ExcelMapper.get(row, "Reference Number");
            est.salesOrdertDate         = ExcelMapper.get(row, "Sales Order Date");          
            est.expDeliveryDate  = ExcelMapper.get(row, "Expected Delivery Date");
            est.paymentTerms  = ExcelMapper.get(row, "Payment Terms");
            est.salesPerson=ExcelMapper.get(row, "Sales Person");

            est.taxType       = ExcelMapper.get(row, "Tax");
            est.priceList     = ExcelMapper.get(row, "Price List");

            est.saveAs        = ExcelMapper.get(row, "Save As");
 
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

