package model;
import java.util.List;
import java.util.Map;
public class PurchaseOrderData {
	 public String vendorName;
	    public String referenceNo;
	    public String poDate;
	    public String paymentTerms;
	    public String deliveryDate;
	    public String priceList;
	    public String taxType;
	    public String discountLevel;
	    public String discountAfterBeforeTax;
	    public String discountType;
	    public String discountValue;
	    public String discountAccount;
	    public String customerNote;
	    public String terms;
	    public String saveAs;
	    public List<Map<String, String>> items;
	    @Override
	    public String toString() {
	        return "PurchaseOrderData{" +
	                "Vendor='" + vendorName + '\'' +
	                '}';
	    }
}
