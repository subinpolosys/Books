package dataprovider;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import model.SalesReturnData;
import utils.ExcelMapper;
import utils.ExcelReader;

public class SalesReturnDataProvider {
	@DataProvider(name = "salesReturnData", parallel = false)
    public Iterator<Object[]> getInvoicesData() throws IOException {
        List<Map<String, Object>> rows =
                ExcelReader.getMasterDetailData(
                        System.getProperty("user.dir") +
                        "/src/test/resources/SalesReturnData.xlsx",
                        "SalesReturnHeader",
                        "SalesReturnItems");
        return rows.stream().map(row -> {
        	SalesReturnData sr = new SalesReturnData();
        	sr.customerName    = ExcelMapper.get(row, "Customer Name");
        	sr.invoiceNo       = ExcelMapper.get(row, "Invoice Number");
        	sr.reason  = ExcelMapper.get(row, "Reason");
        	sr.salesPerson=ExcelMapper.get(row, "Sales Person");
        	sr.returnDate  = ExcelMapper.get(row, "Return Date");
        	sr.refNo=ExcelMapper.get(row, "Reference Number");
        	sr.transactionType = ExcelMapper.get(row, "Transaction Type");
        	sr.taxType       = ExcelMapper.get(row, "Tax");
        	sr.customerNote=ExcelMapper.get(row, "Customer Notes");
        	sr.termsAndconditions=ExcelMapper.get(row, "Terms And Conditions");
        	sr.saveAs        = ExcelMapper.get(row, "Save As");
        	sr.priceList     = ExcelMapper.get(row, "Price List");
        	 @SuppressWarnings("unchecked")
            List<Map<String, String>> items =
                    (List<Map<String, String>>) row.get("items");
            if (sr.customerName == null || sr.customerName.isBlank()) {
                throw new SkipException("Customer Name is empty in Excel");
            }
            if (items == null || items.isEmpty()) {
                throw new SkipException("No items for Customer: " + sr.customerName);
            }
            sr.items = items;
            return new Object[] { sr };
        }).iterator();
    }
}
