package model;

import java.util.List;
import java.util.Map;

public class ExpenseData {
	 public String amount;
	    public String expenseDate;
	    public String employee;
	    public String expenseAccount;
	    public String paidThroughAccount;
	    public String currency;
	    public String vendorName;
	    public String taxTreatment;
	    public String tax;
	    public String inclusiveTax;
	    public String referenceNumber;
	    public String customer;
	    public String billable;
	    public String markup;
	    public String notes;
	public List<Map<String, String>> items;
    @Override
    public String toString() {
        return "ExpenseData{" +
                "Amount='" + amount + '\'' +
                '}';
   }
}