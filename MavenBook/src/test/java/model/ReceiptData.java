package model;

import java.util.List;
import java.util.Map;

public class ReceiptData {
	public String customerName;
	public String receiptDate;
	public String amountReceived;
	public String paymentmode;
	public String referenceNo;
	public String depositAccount;
	public String bankCharge;
	public String invoiceNo;   
	public String notes;
	public List<Map<String, String>> items;
    @Override
    public String toString() {
        return "ReceiptData{" +
                "Customer='" + customerName + '\'' +
                '}';
    }
}
