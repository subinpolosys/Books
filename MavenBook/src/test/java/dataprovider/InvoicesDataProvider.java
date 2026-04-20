package dataprovider;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import model.InvoicesData;
import utils.ExcelMapper;
import utils.ExcelReader;

public class InvoicesDataProvider {
	@DataProvider(name = "invoicesData", parallel = false)
    public Iterator<Object[]> getInvoicesData() throws IOException {
        List<Map<String, Object>> rows =
                ExcelReader.getMasterDetailData(
                        System.getProperty("user.dir") +
                        "/src/test/resources/SalesInvoiceData.xlsx",
                        "SalesInvoiceHeader",
                        "SalesInvoiceItems");
        return rows.stream().map(row -> {
        	InvoicesData inv = new InvoicesData();
        	inv.customerName     = ExcelMapper.get(row, "Customer Name");
        	inv.orderNo    = ExcelMapper.get(row, "Order Number");
        	inv.invoiceDate         = ExcelMapper.get(row, "Invoice Date");          
        	inv.supplyDate  = ExcelMapper.get(row, "Supply Date");
        	inv.paymentTerms  = ExcelMapper.get(row, "Payment Terms");
        	inv.salesPerson=ExcelMapper.get(row, "Sales Person");
        	inv.subject=ExcelMapper.get(row, "Subject");
        	inv.taxType       = ExcelMapper.get(row, "Tax");
        	inv.priceList     = ExcelMapper.get(row, "Price List");
            inv.transactionType = ExcelMapper.get(row, "Transaction Type");
        	inv.saveAs        = ExcelMapper.get(row, "Save As");
        	inv.customerNote=ExcelMapper.get(row, "Customer Notes");
        	inv.termsAndconditions=ExcelMapper.get(row, "Terms And Conditions");
            @SuppressWarnings("unchecked")
            List<Map<String, String>> items =
                    (List<Map<String, String>>) row.get("items");
            if (inv.customerName == null || inv.customerName.isBlank()) {
                throw new SkipException("Customer Name is empty in Excel");
            }
            if (items == null || items.isEmpty()) {
                throw new SkipException("No items for Customer: " + inv.customerName);
            }
            inv.items = items;
            return new Object[] { inv };
        }).iterator();
    }
}
