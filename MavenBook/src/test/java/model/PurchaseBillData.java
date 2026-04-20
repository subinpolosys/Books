package model;
import java.util.List;
import java.util.Map;
public class PurchaseBillData {
	 	public String vendorName;
	 	public String entryDate;
	    public String piDate;
	    public String deliveryDate;
	    public String referenceNo;
	    public String paymentTerms;
	    public String taxType;
	    public String priceList;	    
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
	        return "PurchaseBillData{" +
	                "Vendor='" + vendorName + '\'' +
	                '}';
	    }
}
