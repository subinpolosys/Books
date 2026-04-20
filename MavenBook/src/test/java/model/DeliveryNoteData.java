package model;

import java.util.List;
import java.util.Map;

public class DeliveryNoteData {
	public String customerName;
    public String referenceNo;
    public String dnDate;
    public String dnType;
    public String taxType;
    public String priceList;
    public String customerNote;
    public String termsAndconditions;
    public String saveAs;
    public List<Map<String, String>> items;
    @Override
    public String toString() {
        return "DeliveryNoteData{" +
                "Customer='" + customerName + '\'' +
                '}';
    }
}
