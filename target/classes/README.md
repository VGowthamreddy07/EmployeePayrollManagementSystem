# Employee Payroll System — Excel integration

## Run
Use JDK 8+ and Maven. Open this folder as a Maven project in IntelliJ IDEA/Eclipse, let Maven download dependencies, then run `PayrollManagementSystem`.

Terminal build:
```bash
mvn clean compile
```
The Apache POI dependency is declared in `pom.xml`; include Maven dependencies on the classpath when running from a terminal.

## Excel workbook
The application creates `employees.xlsx` in its working directory. Worksheet: `Employees`.
Columns: Employee ID, Name, Department, Designation, Basic Salary, Allowances, Deductions, Gross Salary, Net Salary.
Add/update/delete save the current records to the workbook. The workbook is loaded when the Java app starts. Changes you make manually in Excel while the app is running are not reloaded until you restart the app. Close Excel before saving from Java to avoid file-lock errors.

## Beyond the PDF
The uploaded PDF describes employee record management, file handling, collections and reports. This version adds `.xlsx` read/write using Apache POI, salary formulas, payslip text export and payroll totals. It does not implement login/authentication, tax, PF, ESI, or statutory payroll rules. Use sample data only unless proper security controls are added.
