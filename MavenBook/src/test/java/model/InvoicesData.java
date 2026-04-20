package model;

import java.util.List;
import java.util.Map;

public class InvoicesData {
	public String customerName;
    public String orderNo;
    public String invoiceDate;
    public String paymentTerms;
    public String supplyDate;
    public String transactionType;
    public String salesPerson;   
    public String subject;   
    public String taxType;
    public String priceList;
    public String customerNote;
    public String termsAndconditions;
    public String saveAs;
    public List<Map<String, String>> items;
    @Override
    public String toString() {
        return "InvoicesData{" +
                "Customer='" + customerName + '\'' +
                '}';
    }
}
