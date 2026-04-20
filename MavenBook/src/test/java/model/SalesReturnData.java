package model;

import java.util.List;
import java.util.Map;

public class SalesReturnData {
	public String customerName;
    public String invoiceNo;    
    public String reason;
    public String salesPerson; 
    public String returnDate;
    public String refNo;
    public String transactionType;   
    public String taxType;
    public String customerNote;
    public String termsAndconditions;
    public String saveAs;
    public String priceList;
    public List<Map<String, String>> items;
    @Override
    public String toString() {
        return "SalesReturnData{" +
                "Customer='" + customerName + '\'' +
                '}';
    }
}
