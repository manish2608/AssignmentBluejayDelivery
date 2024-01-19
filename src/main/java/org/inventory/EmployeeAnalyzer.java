package org.inventory;



import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class Employee {




    private String positionId;
    private Date startTime;
    private Date endTime;

    private String employeeName;



    public Employee(String employeeName, Date startTime,Date endTime ,String positionId) {

        this.employeeName = employeeName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.positionId=positionId;
    }


    public Date getStartTime(){
        return startTime;
    }
    public Date getEndTime(){
        return  endTime;
    }


    public String getPositionId() {
        return positionId;
    }

    public String getEmployeeName() {
        return employeeName;
    }
}

public class EmployeeAnalyzer {

    public static void main(String[] args) {
        String csvFile = "D:\\Convert\\input\\Assignment_Timecard.csv";


        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            String[] header = reader.readNext(); // Read header line

            List<Employee> employees = parseCSV(reader);

            analyzeEmployees(employees);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static List<Employee> parseCSV(CSVReader reader) throws CsvValidationException, IOException {
        List<Employee> employees = new ArrayList<>();
        String[] nextLine;
       SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        while ((nextLine = reader.readNext()) != null) {
            String name = nextLine[7];
            String positionId =nextLine[0];
            try {
                String dateStr = nextLine[2].trim();
                String dateStr1 = nextLine[3].trim();

               if (!dateStr.isEmpty() && !dateStr1.isEmpty()) {
                    Date startTime = dateFormat.parse(dateStr);
                    Date endTime = dateFormat.parse(dateStr1);
                    Employee  employee = new Employee(name,startTime,endTime,positionId);

                    employees.add(employee);
                }


            } catch (ParseException e) {
               // System.out.println("Unable to parse the date: " + e.getMessage());
            }






        }

        return employees;
    }

    private static void analyzeEmployees(List<Employee> employees) {
        System.out.println( "A) Has worked for 7 consecutive days.");
        System.out.println( "Employee Name"+"\t\t\t\t"+"Position Id");
        for (int i = 0; i < employees.size() - 1; i++) {
            Employee currentEmployee = employees.get(i);
            Employee nextEmployee = employees.get(i + 1);

            // a) Check for employees who have worked for 7 consecutive days
            if (daysBetween(currentEmployee.getEndTime(), nextEmployee.getStartTime()) == 1) {

                System.out.println(currentEmployee.getEmployeeName() + "\t\t\t"+currentEmployee.getPositionId());
            }
        }
        System.out.println();
        System.out.println("B) Has less than 10 hours between shifts.");
        System.out.println( "Employee Name"+"\t\t\t\t"+"Position Id");
            for (int i = 0; i < employees.size() - 1; i++) {
                Employee currentEmployee = employees.get(i);
                Employee nextEmployee = employees.get(i + 1);

            // b) Check for employees with less than 10 hours between shifts but greater than 1 hour
            long hoursBetween = hoursBetween(currentEmployee.getEndTime(), nextEmployee.getStartTime());
            if (hoursBetween > 1 && hoursBetween < 10) {
                System.out.println(currentEmployee.getEmployeeName() + "\t\t\t"+ currentEmployee.getPositionId());
            }
        }

            System.out.println();
        System.out.println("C) Has worked for more than 14 hours in a single shift.");
        System.out.println( "Employee Name"+"\t\t\t\t"+"Position Id");
        // c) Check for employees who have worked for more than 14 hours in a single shift
        for (Employee employee : employees) {
            long hoursWorked = hoursBetween(employee.getStartTime(), employee.getEndTime());
            if (hoursWorked > 14) {
                System.out.println(employee.getEmployeeName() +"\t \t\t"+ employee.getPositionId());
            }
        }
    }

    private static long daysBetween(Date startDate, Date endDate) {
        long diff = endDate.getTime() - startDate.getTime();
        return diff / (24 * 60 * 60 * 1000);
    }

    private static long hoursBetween(Date startDate, Date endDate) {
        long diff = endDate.getTime() - startDate.getTime();
        return diff / (60 * 60 * 1000);
    }
}

