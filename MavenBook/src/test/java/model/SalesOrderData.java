package model;

import java.util.List;
import java.util.Map;

public class SalesOrderData {
	public String customerName;
    public String referenceNo;
    public String salesOrdertDate;
    public String paymentTerms;
    public String expDeliveryDate;
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
        return "SalesOrderData{" +
                "Customer='" + customerName + '\'' +
                '}';
    }
}
