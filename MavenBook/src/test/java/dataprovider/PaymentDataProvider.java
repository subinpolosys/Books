package dataprovider;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.testng.SkipException;
import org.testng.annotations.DataProvider;

import model.PaymentData;
import model.ReceiptData;
import utils.PaymentExcelReader;

public class PaymentDataProvider {
	@DataProvider(name = "paymentData", parallel = false)
	public Iterator<Object[]> getPaymentData() throws IOException {

	    List<Map<String, String>> rows =
	            PaymentExcelReader.getSheetData(
	                    System.getProperty("user.dir") +
	                    "/src/test/resources/PaymentData.xlsx",
	                    "Payment"
	            );
	    return rows.stream().map(row -> {
	        PaymentData data = new PaymentData();
	        data.vendorName   = row.get("Customer Name");
	        data.paymentDate    = row.get("Receipt Date");
	        data.amountReceived = row.get("Amount Received");
	        data.paymentmode    = row.get("Payment Mode");
	        data.referenceNo    = row.get("Reference Number");
	        data.depositAccount = row.get("Deposit Account");
	        //data.bankCharge     = row.get("Bank Charges");
	        data.invoiceNo      = row.get("Invoice No");
	        data.notes          = row.get("Notes");
	        if (data.vendorName == null || data.vendorName.isBlank()) {
	            throw new SkipException("Vendor Name is empty");
	        }
	        return new Object[]{data};
	    }).iterator();
	}
}
