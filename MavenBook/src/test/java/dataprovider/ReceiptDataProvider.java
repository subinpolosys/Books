package dataprovider;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.testng.SkipException;
import org.testng.annotations.DataProvider;

import model.ReceiptData;
import utils.PaymentExcelReader;

public class ReceiptDataProvider {
	@DataProvider(name = "receiptData", parallel = false)
	public Iterator<Object[]> getReceiptData() throws IOException {

	    List<Map<String, String>> rows =
	            PaymentExcelReader.getSheetData(
	                    System.getProperty("user.dir") +
	                    "/src/test/resources/ReceiptData.xlsx",
	                    "Receipt"
	            );
	    return rows.stream().map(row -> {
	        ReceiptData data = new ReceiptData();
	        data.customerName   = row.get("Customer Name");
	        data.receiptDate    = row.get("Receipt Date");
	        data.amountReceived = row.get("Amount Received");
	        data.paymentmode    = row.get("Payment Mode");
	        data.referenceNo    = row.get("Reference Number");
	        data.depositAccount = row.get("Deposit Account");
	        data.bankCharge     = row.get("Bank Charges");
	        data.invoiceNo      = row.get("Invoice No");
	        data.notes          = row.get("Notes");
	        if (data.customerName == null || data.customerName.isBlank()) {
	            throw new SkipException("Customer Name is empty");
	        }
	        return new Object[]{data};
	    }).iterator();
	}
}
