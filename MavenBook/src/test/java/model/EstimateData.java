package model;

import java.util.List;
import java.util.Map;

public class EstimateData {
	public String customerName;
    public String referenceNo;
    public String estDate;
   // public String paymentTerms;
    public String expiryDate;
    public String priceList;
    public String taxType;
    public String subject;
    public String salesPerson;
    //public String discountType;
    //public String discountValue;
    //public String discountAccount;
    public String customerNote;
    public String termsAndconditions;
    public String saveAs;
    public List<Map<String, String>> items;
    @Override
    public String toString() {
        return "EstimateData{" +
                "Customer='" + customerName + '\'' +
                '}';
    }
}
