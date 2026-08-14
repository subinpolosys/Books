package dataprovider;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.testng.SkipException;
import org.testng.annotations.DataProvider;

import model.LandedcostPIData;
import utils.ExcelMapper;
import utils.ExcelReader;

public class LandedcostPIDataProvider {
	@DataProvider(name = "LandedcostPIData", parallel = false)
	public Iterator<Object[]> getLandedcostPIData() throws IOException {

	    List<Map<String, Object>> rows =
	            ExcelReader.getMasterDetailData(
	                    System.getProperty("user.dir") +
	                    "/src/test/resources/LandedcostPIData.xlsx",
	                    "PurchaseBillHeader",
	                    "PurchaseBillItems");

	    return rows.stream()

	            // Filter invalid rows
	            .filter(row -> {

	                String vendorName = ExcelMapper.get(row, "Vendor Name");

	                @SuppressWarnings("unchecked")
	                List<Map<String, String>> items =
	                        (List<Map<String, String>>) row.get("items");

	                if (vendorName == null || vendorName.isBlank()) {
	                    System.out.println("Skipping row: Vendor Name is empty.");
	                    return false;
	                }

	                if (items == null || items.isEmpty()) {
	                    System.out.println("Skipping vendor: " + vendorName + " (No items)");
	                    return false;
	                }

	                return true;
	            })

	            // Map valid rows
	            .map(row -> {

	                LandedcostPIData pi = new LandedcostPIData();

	                pi.vendorName = ExcelMapper.get(row, "Vendor Name");
	                pi.entryDate = ExcelMapper.get(row, "Entry Date");
	                pi.referenceNo = ExcelMapper.get(row, "Reference Number");
	                pi.piDate = ExcelMapper.get(row, "Invoice Date");
	                pi.paymentTerms = ExcelMapper.get(row, "Payment Terms");
	                pi.deliveryDate = ExcelMapper.get(row, "Expected Delivery Date");
	                pi.taxType = ExcelMapper.get(row, "Tax");
	                pi.priceList = ExcelMapper.get(row, "Price List");
	                pi.customerNote = ExcelMapper.get(row, "Customer Notes");
	                pi.discountLevel = ExcelMapper.get(row, "Discount Level");
	                pi.saveAs = ExcelMapper.get(row, "Save As");
	                pi.discountAfterBeforeTax = ExcelMapper.get(row, "Discount After-Before Tax");
	                pi.discountType = ExcelMapper.get(row, "Discount TType");
	                pi.discountValue = ExcelMapper.get(row, "DiscountT");
	                pi.discountAccount = ExcelMapper.get(row, "Discount Account");

	                @SuppressWarnings("unchecked")
	                List<Map<String, String>> items =
	                        (List<Map<String, String>>) row.get("items");

	                pi.items = items;

	                return new Object[] { pi };
	            })

	            .iterator();
	}
//	@DataProvider(name = "LandedcostPIData", parallel = false)
//    public Iterator<Object[]> getLandedcostPIData() throws IOException {
//        List<Map<String, Object>> rows =
//                ExcelReader.getMasterDetailData(
//                        System.getProperty("user.dir") +
//                        "/src/test/resources/LandedcostPIData.xlsx",
//                        "PurchaseBillHeader",
//                        "PurchaseBillItems");
//        return rows.stream().map(row -> {
//        	LandedcostPIData pi = new LandedcostPIData();
//            pi.vendorName     = ExcelMapper.get(row, "Vendor Name");
//            pi.entryDate	  = ExcelMapper.get(row, "Entry Date");
//            pi.referenceNo    = ExcelMapper.get(row, "Reference Number");
//            pi.piDate         = ExcelMapper.get(row, "Invoice Date");
//            pi.paymentTerms  = ExcelMapper.get(row, "Payment Terms");
//            pi.deliveryDate  = ExcelMapper.get(row, "Expected Delivery Date");
//            pi.taxType       = ExcelMapper.get(row, "Tax");
//            pi.priceList     = ExcelMapper.get(row, "Price List");
//            pi.customerNote  =ExcelMapper.get(row, "Customer Notes");
//            pi.discountLevel = ExcelMapper.get(row, "Discount Level");
//            pi.saveAs        = ExcelMapper.get(row, "Save As");
//            pi.discountAfterBeforeTax=ExcelMapper.get(row, "Discount After-Before Tax");
//            pi.discountType=ExcelMapper.get(row, "Discount TType");
//            pi.discountValue=ExcelMapper.get(row, "DiscountT");
//            pi.discountAccount=ExcelMapper.get(row, "Discount Account");           
//            @SuppressWarnings("unchecked")
//            List<Map<String, String>> items =
//                    (List<Map<String, String>>) row.get("items");
//            if (pi.vendorName == null || pi.vendorName.isBlank()) {
//                throw new SkipException("Vendor Name is empty in Excel");
//            }
//            if (items == null || items.isEmpty()) {
//                throw new SkipException("No items for vendor: " + pi.vendorName);
//            }
//            pi.items = items;
//            return new Object[] { pi };
//        }).iterator();
//    }
//	
}
