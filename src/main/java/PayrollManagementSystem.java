import java.io.*;
import java.util.*;
public class PayrollManagementSystem {
 private final Scanner sc=new Scanner(System.in);
 private final ExcelEmployeeRepository repo=new ExcelEmployeeRepository("employees.xlsx");
 private final PayrollService payroll=new PayrollService();
 private final List<Employee> employees=new ArrayList<>();
 public static void main(String[] args){new PayrollManagementSystem().run();}
 private void run(){
  try{employees.addAll(repo.loadAll());}catch(Exception e){System.out.println("Cannot load Excel: "+e.getMessage());return;}
  System.out.println("Loaded "+employees.size()+" employee(s) from employees.xlsx");
  boolean running=true;
  while(running){menu();int choice=InputUtil.readInt(sc,"Choose an option: ");
   try{switch(choice){
    case 1:add();break;case 2:view();break;case 3:search();break;case 4:update();break;
    case 5:delete();break;case 6:payslip();break;case 7:report();break;
    case 8:save();running=false;System.out.println("Saved to employees.xlsx. Goodbye!");break;
    default:System.out.println("Choose 1 to 8.");
   }}catch(IOException e){System.out.println("Excel error: "+e.getMessage()+" (close workbook in Excel and retry)");}
    catch(IllegalArgumentException e){System.out.println("Invalid data: "+e.getMessage());}
  }sc.close();
 }
 private void menu(){System.out.println("\n--------------- MAIN MENU ---------------\n1. Add Employee\n2. View All Employees\n3. Search Employee by ID\n4. Update Employee\n5. Delete Employee\n6. Generate Payslip\n7. Payroll Summary Report\n8. Save and Exit\n-----------------------------------------");}
 private Employee byId(int id){for(Employee e:employees)if(e.getId()==id)return e;return null;}
 private Employee readDetails(int id){String n=InputUtil.readRequiredText(sc,"Name: "),d=InputUtil.readRequiredText(sc,"Department: "),t=InputUtil.readRequiredText(sc,"Designation: ");double b=InputUtil.readNonNegativeDouble(sc,"Basic salary (Rs.): "),a=InputUtil.readNonNegativeDouble(sc,"Allowances (Rs.): "),ded=InputUtil.readDeductions(sc,b+a);return new Employee(id,n,d,t,b,a,ded);}
 private void add()throws IOException{int id=InputUtil.readPositiveInt(sc,"Employee ID: ");if(byId(id)!=null){System.out.println("ID already exists.");return;}employees.add(readDetails(id));save();System.out.println("Added and saved to Excel.");}
 private void view(){if(employees.isEmpty()){System.out.println("No employees.");return;}employees.stream().sorted(Comparator.comparingInt(Employee::getId)).forEach(System.out::println);}
 private void search(){Employee e=byId(InputUtil.readPositiveInt(sc,"Employee ID: "));if(e==null)System.out.println("Not found.");else details(e);}
 private void update()throws IOException{int id=InputUtil.readPositiveInt(sc,"Employee ID to update: ");Employee old=byId(id);if(old==null){System.out.println("Not found.");return;}details(old);employees.set(employees.indexOf(old),readDetails(id));save();System.out.println("Updated.");}
 private void delete()throws IOException{int id=InputUtil.readPositiveInt(sc,"Employee ID to delete: ");Employee e=byId(id);if(e==null){System.out.println("Not found.");return;}if(InputUtil.readYesNo(sc,"Delete "+e.getName()+"?")){employees.remove(e);save();System.out.println("Deleted.");}}
 private void payslip(){Employee e=byId(InputUtil.readPositiveInt(sc,"Employee ID: "));if(e==null){System.out.println("Not found.");return;}String text=payroll.generatePayslip(e);System.out.print(text);if(InputUtil.readYesNo(sc,"Save payslip to text file?")){try(PrintWriter w=new PrintWriter(new FileWriter("payslip_"+e.getId()+".txt"))){w.print(text);System.out.println("Payslip saved.");}catch(IOException x){System.out.println(x.getMessage());}}}
 private void report(){if(employees.isEmpty()){System.out.println("No employees.");return;}double g=0,d=0,n=0;for(Employee e:employees){g+=e.getGrossSalary();d+=e.getDeductions();n+=e.getNetSalary();}System.out.println("Employee count: "+employees.size());System.out.printf("Gross total: Rs. %.2f%nDeductions: Rs. %.2f%nNet total: Rs. %.2f%n",g,d,n);}
 private void details(Employee e){System.out.println(e);System.out.printf("Basic: %.2f | Allowances: %.2f | Gross: %.2f | Deductions: %.2f | Net: %.2f%n",e.getBasicSalary(),e.getAllowances(),e.getGrossSalary(),e.getDeductions(),e.getNetSalary());}
 private void save()throws IOException{repo.saveAll(employees);}
}