package model;

import java.util.List;
import java.util.Map;

public class VendorCreditData {
	public String vendorName;
 	public String billNumber;
    public String vcDate;
    public String orderNumber;
    public String taxType;
    public String priceList;	        
    public String customerNote;
    public String terms;
    public String saveAs;
    public List<Map<String, String>> items;
    
    @Override
    public String toString() {
        return "PurchaseBillData{" +
                "Vendor='" + vendorName + '\'' +
                '}';
    }
}
