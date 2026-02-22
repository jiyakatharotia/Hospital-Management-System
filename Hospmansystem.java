/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Hospman;

/**
 *
 * @author Jiya
 */
import java.sql.Connection;
import java.util.Scanner;
import java.sql.*;
public class Hospmansystem {
    private static final String url="jdbc:mysql://localhost:3306/hospital";
    private static final String username="root";
    private static final String password="jiyak214";
    
    public static void main(String args[])
    {
        try{
            Class.forName("conn.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        Scanner scanner=new Scanner(System.in);
        try{
            Connection connection=DriverManager.getConnection(url,username,password);
            Patient patient=new Patient(connection,scanner);
            Doctor doctor=new Doctor(connection);
            while(true){
                System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patients");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit ");
                System.out.println("Enter your choice: ");
                int choice=scanner.nextInt();
                
                switch(choice){
                    case 1:
                        patient.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        patient.viewPatient();
                        System.out.println();
                        break;
                    case 3:
                        doctor.viewDoctors();
                        System.out.println();
                        break;
                    case 4:
                        bookAppointment(patient,doctor,connection,scanner);
                        System.out.println();
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Enter valid choice");
                                        
                }
            }
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
    public static void bookAppointment(Patient patient,Doctor doctor,Connection connection,Scanner scanner){
        System.out.print("Enter Patient id :");
        int patientId=scanner.nextInt();
        System.out.print("Enter Doctor id :");
        int doctorId=scanner.nextInt();
        System.out.print("Enter appointment date(YYYY-MM-DD): ");
        String appointmentDate=scanner.next();
        if(patient.getPatientById(patientId)&& doctor.getDoctorById(doctorId)){
            if(checkDoctorAvailability(doctorId,appointmentDate,connection)){
                String appointmentQuery="Insert into appointments(patient_Id,doctor_Id,appointment_data) values(?,?,?)";
            try {
            PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
            preparedStatement.setInt(1, patientId);
            preparedStatement.setInt(2, doctorId);
            preparedStatement.setString(3, appointmentDate);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Appointment Booked!");
            } else {
                System.out.println("Failed to Book Appointment!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
            
            }else {
        System.out.println("Doctor not available on this date!");
      }
        
        } else{
            System.out.println("Either doctor or patient doesn't exist! ");
        }
    }
    public static boolean checkDoctorAvailability(int doctorId,String appointmentDate,Connection connection){
        String query="Select count(*) from appointments where doctor_id=? and appointment_date=?";
        try{
            PreparedStatement preparedstatement=connection.prepareStatement(query);
            preparedstatement.setInt(1,doctorId);
            preparedstatement.setString(2,appointmentDate); 
            ResultSet resultset=preparedstatement.executeQuery();
            if(resultset.next())
            {
                int count=resultset.getInt(1);
                if(count==0){
                    return true;
                }
                else
                    return false;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
}
}

