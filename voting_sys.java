package Project;

import java.util.Scanner;
import java.sql.*;

public class voting_sys {
    static int oragnizer_choice;
    static String oragnizer_choice1;

    public static void main(String[] args) throws Exception{
        Scanner sc=new Scanner(System.in);
        System.out.println("********** Voting System **********\n");
        Organizer a=new Organizer();
        
        do 
        {
            // Add voter details
            System.out.println("***** Add Voter Details ****");
            a.voter();

            // Asking want to add more voter info
            System.out.println("\nWant to add more voter info(yes/no)");
            oragnizer_choice1=sc.next().toLowerCase();
            
            if (oragnizer_choice1.equals("yes")){
                continue;
            }
            else{
                // Else asking to fill Candidate info
                System.out.println("\nWant to add candidate info(yes/no)");
                oragnizer_choice1=sc.next().toLowerCase();

                if (oragnizer_choice1.equals("yes"))
                { 
                    // used do-while to add more candidate info
                    do 
                    {
                        a.candidate();
                        System.out.println("\nWant to add more candidate info(yes/no)");
                        oragnizer_choice1=sc.next().toLowerCase();

                        if (oragnizer_choice1.equals("yes")){
                            continue;
                        }

                        // Else Starting Election
                        else{

                            System.out.println("\nWant to start election(yes/no)");     
                            oragnizer_choice1=sc.next().toLowerCase();

                            if (oragnizer_choice1.equals("yes")){

                                System.out.println("\n****** Election Started ******\n");
                                a.Election();
                                break;
                            }
                            break;
                            }
                                            
                    } while(true);
                }
                       
                }
                        
            }while(true);
            
        }

    }

    

class Organizer{
    Scanner sc=new Scanner(System.in);
    void voter(){
        // Taking voter info
        System.out.println("\n**** Fill the Voter info ****");

        System.out.print("Name : ");
        String name=sc.next();
        
        System.out.print("Voter ID : ");
        Integer voter_id=sc.nextInt();
        
        System.out.print("Password : ");
        String password =sc.next();

        try
        {
            // connection setup with database
            Class.forName("com.mysql.jdbc.Driver");  
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/voting_sys","root","zxcvbnm0987654321@");  
            Statement stmt=con.createStatement();  
            
            // storing voter info in database
            stmt.executeUpdate("insert into voter_info (name,id,password) Values('"+name+"'," +voter_id+",'"+password+"')");
            // notifing that info inserted
            System.out.println("\nSuccessfully inserted record!!!!");
            con.close();
        }
        catch(Exception e)
        {
            System.out.println(e);
            System.out.println("Exception handled");
        }

    }

    void candidate(){
        // Taking candidate info
        System.out.println("\n**** Fill the Candidate info ****");

        System.out.print("Name : ");
        String name=sc.next();
              
        System.out.print("Candidate ID : ");
        Integer candidate_id=sc.nextInt();
        
        
        try
        {
            // Connection setup
            Class.forName("com.mysql.jdbc.Driver");  
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/voting_sys","root","zxcvbnm0987654321@");  
            Statement stmt=con.createStatement();  
                       
            // 
            stmt.executeUpdate("insert into candidate_info (name,id)Values('"+name+"'," +candidate_id+")");
            // notifing that info inserted
            System.out.println("\nSuccessfully inserted record!!!!");
            con.close();
        }
        catch(Exception e)
        {
            System.out.println(e);
            System.out.println("Exception handled");
        }
        
    }
    
    void Election()  throws SQLException{
        // used do-while for multiple voting
        do{
            System.out.print("\nVoter ID : ");
            
            Integer voter_id=sc.nextInt();
            System.out.print("Password : ");
            String password =sc.next();
       
            try
            {
                // connection setup
                Class.forName("com.mysql.jdbc.Driver");  
                Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/voting_sys","root","zxcvbnm0987654321@");  
                Statement stmt=con.createStatement();  
                
                // fetching data from database
                ResultSet rs;
                rs=stmt.executeQuery("select * from voter_info where id="+voter_id);
                // used while (rs.next()) for visiting all records
                while (rs.next()) 
                {
                    // storing values in variables
                    int id=rs.getInt("id");
                    int vote_status=rs.getInt("voting_status");
                    String pass=rs.getString("password");

                        // validating voter data from database
                        if(voter_id==id && password.equals(pass) && vote_status==0){   
                            
                            System.out.println("\nLogin Succesfully....");
                            ResultSet rs1=stmt.executeQuery("select * from candidate_info"); 
                            System.out.println("\nCandidate Name : ");
                    
                            while (rs1.next()) {
                                System.out.println(rs1.getString("sr")+", "+rs1.getString("name"));
                                
                            }

                            System.out.println("\nVote for the Candidate (Enter the serial number)");
                            int ch=sc.nextInt();
                            rs1=stmt.executeQuery("select votes from candidate_info where id="+ch); 
                            // adding votes to Candidate account
                            rs1.next();
                                int count= rs1.getInt("votes");
                                count=count+1;
                                stmt.executeUpdate("Update candidate_info SET votes = "+count+" where id="+ch);
                                stmt.executeUpdate("Update voter_info SET voting_status = 1 where id="+voter_id);
                                System.out.println("voted succesfully...");
                                break;
                                    
                        }
                        else if(voter_id==id && password.equals(pass) && vote_status==1){
                            System.out.println("Already voted...");
                            break;
                        }
                        else{
                            System.out.println("Incorrect Voter ID and Password");
                            System.out.println("Please enter correct Voter ID and Password");
                            // Election();
                            continue;
                        }
                    }
                    rs.close();
                }
                catch(Exception e)
                {
                    System.out.println(e);
                    System.out.println("Exception handled");
                }
                finally{
                    // used finally in case of error occured it will still run
                    System.out.println("\nWant to vote (yes/no)");
                    String voter_choice=sc.next().toLowerCase();
                    if (voter_choice.equals("yes")){
                        continue;

                    }
                    else
                    {
                        System.out.println("\nWant to know the Result(yes/no)");
                        String voter_choice1=sc.next().toLowerCase();
                        
                        if (voter_choice1.equals("yes")){
                            Result();
                            System.exit(0);
                        }
                        else{System.exit(0);}
                        
                    }

                }

        }while(true);

    }

    void Result() {
        try {
            int n1;
            // connection setup
            
            Class.forName("com.mysql.jdbc.Driver");  
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/voting_sys","root","zxcvbnm0987654321@");  
            Statement stmt=con.createStatement();

            ResultSet rs,rs1;
            rs=stmt.executeQuery("select * from candidate_info");
            // displaying result
            System.out.println();
            System.out.println("Sr."+"  "+"Candidate Name "+"  "+" Votes");
            while (rs.next()) {
                System.out.println(rs.getString("sr")+"    "+rs.getString("name")+"                "+rs.getInt("votes"));
            }
            // used to find out winner
            rs=stmt.executeQuery("select * from candidate_info");
            rs.next();
            int element []= {rs.getInt("votes")};          
            for (int i=0;i<element.length;i++){
                for(int j=i+1;j<element.length;j++){
                    if (element[i]<element[j]){
                        n1=element[j];
                        element[j]=element[i];
                        element[i]=n1;
                    }
                }
            }     
            // Displaying Winner   
            n1=element[0];  
            rs1=stmt.executeQuery("select name from candidate_info where votes="+n1);
            while (rs1.next()) {
                System.out.println("\nBahu matane vijaye .....");
                System.out.println(rs1.getString("name"));
                }
                    
        } catch (Exception e) {
            System.out.println(e);
        }
        finally{
            //used to clear database
            try {
                Class.forName("com.mysql.jdbc.Driver");  
                Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/voting_sys","root","zxcvbnm0987654321@");  
                Statement stmt=con.createStatement();
                stmt.executeUpdate("truncate table voter_info");
                stmt.executeUpdate("truncate table candidate_info");
                
            } catch (Exception e) {
                System.out.println(e);
            }

        }


    }
    
}

    