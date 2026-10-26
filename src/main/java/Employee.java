import java.io.Serializable;
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int id;
    private String name, department, designation;
    private double basicSalary, allowances, deductions;
    public Employee(int id, String name, String department, String designation, double basicSalary, double allowances, double deductions) {
        if (id <= 0) throw new IllegalArgumentException("ID must be positive.");
        this.id=id; this.name=required(name); this.department=required(department); this.designation=required(designation);
        setBasicSalary(basicSalary); setAllowances(allowances); setDeductions(deductions);
    }
    private static String required(String s) { if (s==null || s.trim().isEmpty()) throw new IllegalArgumentException("Text fields cannot be empty."); return s.trim(); }
    private static void valid(double n) { if (!Double.isFinite(n)||n<0) throw new IllegalArgumentException("Salary amounts must be non-negative."); }
    public int getId(){return id;} public String getName(){return name;} public String getDepartment(){return department;}
    public String getDesignation(){return designation;} public double getBasicSalary(){return basicSalary;}
    public double getAllowances(){return allowances;} public double getDeductions(){return deductions;}
    public double getGrossSalary(){return basicSalary+allowances;} public double getNetSalary(){return getGrossSalary()-deductions;}
    public void setBasicSalary(double n){valid(n);basicSalary=n;check();}
    public void setAllowances(double n){valid(n);allowances=n;check();}
    public void setDeductions(double n){valid(n);deductions=n;check();}
    private void check(){if(deductions>basicSalary+allowances) throw new IllegalArgumentException("Deductions exceed gross salary.");}
    public String toString(){return String.format("ID:%d | %s | %s | %s | Net: Rs. %.2f",id,name,department,designation,getNetSalary());}
}