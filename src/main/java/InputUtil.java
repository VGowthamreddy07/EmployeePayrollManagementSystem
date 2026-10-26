import java.util.Scanner;
public final class InputUtil {
 private InputUtil(){}
 public static String readRequiredText(Scanner s,String p){while(true){System.out.print(p);String v=s.nextLine().trim();if(!v.isEmpty())return v;System.out.println("Cannot be empty.");}}
 public static int readInt(Scanner s,String p){while(true){System.out.print(p);try{return Integer.parseInt(s.nextLine().trim());}catch(Exception e){System.out.println("Enter a whole number.");}}}
 public static int readPositiveInt(Scanner s,String p){while(true){int n=readInt(s,p);if(n>0)return n;System.out.println("Must be greater than zero.");}}
 public static double readNonNegativeDouble(Scanner s,String p){while(true){System.out.print(p);try{double n=Double.parseDouble(s.nextLine().trim());if(Double.isFinite(n)&&n>=0)return n;}catch(Exception e){}System.out.println("Enter a non-negative number.");}}
 public static double readDeductions(Scanner s,double max){while(true){double n=readNonNegativeDouble(s,"Deductions (max "+max+"): Rs. ");if(n<=max)return n;System.out.println("Deductions cannot exceed gross salary.");}}
 public static boolean readYesNo(Scanner s,String p){while(true){System.out.print(p+" (y/n): ");String a=s.nextLine().trim();if(a.equalsIgnoreCase("y")||a.equalsIgnoreCase("yes"))return true;if(a.equalsIgnoreCase("n")||a.equalsIgnoreCase("no"))return false;}}
}