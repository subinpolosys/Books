package dataprovider;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.testng.SkipException;
import org.testng.annotations.DataProvider;

import model.ExpenseData;
import utils.PaymentExcelReader;

public class ExpenseDataProvider {
	@DataProvider(name = "ExpenseData", parallel = false)
	public Iterator<Object[]> getExpenseData() throws IOException {

	    List<Map<String, String>> rows =
	            PaymentExcelReader.getSheetData(
	                    System.getProperty("user.dir") +
	                    "/src/test/resources/ExpenseData.xlsx",
	                    "Expense");

	    return rows.stream()

	            // Filter invalid rows
	            .filter(row -> {

	                String amount = row.get("Amount");

	                if (amount == null || amount.isBlank()) {
	                    System.out.println("Skipping row: Amount is empty.");
	                    return false;
	                }

	                return true;
	            })

	            // Map valid rows
	            .map(row -> {

	                ExpenseData data = new ExpenseData();

	                data.amount = row.get("Amount");
	                data.expenseDate = row.get("Expense Date");
	                data.employee = row.get("Employee");
	                data.expenseAccount = row.get("Expense Account");
	                data.paidThroughAccount = row.get("Paid Through Account");
	                data.currency = row.get("Currency");
	                data.vendorName = row.get("Vendor Name");
	                data.taxTreatment = row.get("Tax Treatment");
	                data.tax = row.get("Tax");
	                data.inclusiveTax = row.get("Inclusive Tax");
	                data.referenceNumber = row.get("Reference Number");
	                data.customer = row.get("Customer");
	                data.billable = row.get("Billable");
	                data.markup = row.get("Markup");
	                data.notes = row.get("Notes");

	                return new Object[] { data };
	            })

	            .iterator();
	}
//	@DataProvider(name = "ExpenseData", parallel = false)
//	public Iterator<Object[]> getExpenseData() throws IOException {
//
//	    List<Map<String, String>> rows =
//	            PaymentExcelReader.getSheetData(
//	                    System.getProperty("user.dir") +
//	                    "/src/test/resources/ExpenseData.xlsx","Expense");
//	    return rows.stream().map(row -> {
//	        ExpenseData data = new ExpenseData();
//	        data.amount             = row.get("Amount");
//	        data.expenseDate        = row.get("Expense Date");
//	        data.employee           = row.get("Employee");
//	        data.expenseAccount     = row.get("Expense Account");
//	        data.paidThroughAccount = row.get("Paid Through Account");
//	        data.currency           = row.get("Currency");
//	        data.vendorName         = row.get("Vendor Name");
//	        data.taxTreatment       = row.get("Tax Treatment");
//	        data.tax                = row.get("Tax");
//	        data.inclusiveTax       = row.get("Inclusive Tax");
//	        data.referenceNumber    = row.get("Reference Number");
//	        data.customer           = row.get("Customer");
//	        data.billable           = row.get("Billable");
//	        data.markup             = row.get("Markup");
//	        data.notes              = row.get("Notes");
//
//	        if (data.amount == null || data.amount.isBlank()) {
//	            throw new SkipException("Amount is empty");
//	        }
//	        return new Object[] { data };
//	    }).iterator();
//	}
}
