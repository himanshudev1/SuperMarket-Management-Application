/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emart.dao;

import emart.dbutil.DBConnection;
import emart.pojo.EmployeePojo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hkano
 */
public class EmployeeDAO {
    public static String getNextEmpId() throws SQLException
    {
        Connection conn=DBConnection.getConnection();
        Statement st=conn.createStatement();
        ResultSet rs=st.executeQuery("select max(empid) from employees");
        rs.next();
        String empid=rs.getString(1);
        int empno=Integer.parseInt(empid.substring(1));
        ++empno;
        return "E"+empno;
    }
    public static boolean addEmployee(EmployeePojo emp) throws SQLException
    {
        Connection conn=DBConnection.getConnection();
        PreparedStatement ps=conn.prepareStatement("insert into employees values(?,?,?,?)");
        ps.setString(1,emp.getEmpId());
        ps.setString(2,emp.getEmpName());
        ps.setString(3,emp.getJob());
        ps.setDouble(4,emp.getSalary());
        int res=ps.executeUpdate();
        return res==1;
    }
    public static List<EmployeePojo> getAllEmployees() throws SQLException
    {
        Connection conn=DBConnection.getConnection();
        Statement st=conn.createStatement();
        ResultSet rs=st.executeQuery("select * from employees order by empid");
        ArrayList<EmployeePojo> empList=new ArrayList<>();
        while(rs.next())
        {
            EmployeePojo emp=new EmployeePojo();
            emp.setEmpId(rs.getString(1));
            emp.setEmpName(rs.getString(2));
            emp.setJob(rs.getString(3));
            emp.setSalary(rs.getDouble(4));
            empList.add(emp);
        }
        return empList;
        }
    public static List<String> getAllEmpId() throws SQLException
    {
        Connection conn=DBConnection.getConnection();
        Statement st=conn.createStatement();
        ResultSet rs=st.executeQuery("select empid from employees order by empid");
        ArrayList<String> allId=new ArrayList<>();
        while(rs.next())
        {
            allId.add(rs.getString(1));
        }
        return allId;
    }
    public static EmployeePojo findEmpById(String empid) throws SQLException
    {
        Connection conn=DBConnection.getConnection();
        PreparedStatement ps=conn.prepareStatement("select * from employees where empid=?");
        ps.setString(1, empid);
        ResultSet rs=ps.executeQuery();
        rs.next();
        EmployeePojo e=new EmployeePojo();
        e.setEmpId(rs.getString(1));
        e.setEmpName(rs.getString(2));
        e.setJob(rs.getString(3));
        e.setSalary(rs.getDouble(4));
        return e;
    }
    public static boolean updateEmployee(EmployeePojo e) throws SQLException
    {
        Connection conn=DBConnection.getConnection();
        PreparedStatement ps=conn.prepareStatement("update employees set empname=?, job=?, salary=? where empid=?");
        ps.setString(1, e.getEmpName());
        ps.setString(2,e.getJob());
        ps.setDouble(3, e.getSalary());
        ps.setString(4, e.getEmpId());
        int x=ps.executeUpdate();
        if(x==0)
            return false;
        else
        {
            boolean res=UserDAO.isUserPresent(e.getEmpId());
            if(res==false)
                return true;
        ps=conn.prepareStatement("update users set username=?, usertype=? where empid=?");
        ps.setString(1, e.getEmpName());
        ps.setString(2,e.getJob());
        ps.setString(3, e.getEmpId());
        int y=ps.executeUpdate();
        return y==1;
    }
}
    public static boolean deleteEmployee(String empid) throws SQLException
    {
      Connection conn=DBConnection.getConnection();
        PreparedStatement ps=conn.prepareStatement("delete from employees where empid=?");
        ps.setString(1,empid);
        int x=ps.executeUpdate();
        return x==1;
    }
}
    

