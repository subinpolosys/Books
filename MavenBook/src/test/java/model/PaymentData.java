package model;

import java.util.List;
import java.util.Map;

public class PaymentData {
	public String vendorName;
	public String paymentDate;
	public String amountReceived;
	public String paymentmode;
	public String referenceNo;
	public String depositAccount;
	//public String bankCharge;
	public String invoiceNo;   
	public String notes;
	public List<Map<String, String>> items;
    @Override
    public String toString() {
        return "PaymentData{" +
                "Vendor='" + vendorName + '\'' +
                '}';
    }
}
