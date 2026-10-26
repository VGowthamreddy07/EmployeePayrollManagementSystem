import java.time.LocalDate;
public class PayrollService {
 public String generatePayslip(Employee e){return "\n========== PAYSLIP ==========\nDate: "+LocalDate.now()+"\nID: "+e.getId()+"\nName: "+e.getName()+"\nDepartment: "+e.getDepartment()+"\nDesignation: "+e.getDesignation()+"\nBasic: Rs. "+e.getBasicSalary()+"\nAllowances: Rs. "+e.getAllowances()+"\nGross: Rs. "+e.getGrossSalary()+"\nDeductions: Rs. "+e.getDeductions()+"\nNET: Rs. "+e.getNetSalary()+"\n=============================\n";}
}