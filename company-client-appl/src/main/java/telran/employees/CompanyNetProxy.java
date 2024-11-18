package telran.employees;

import org.json.JSONArray;
import telran.net.*;
import java.util.Iterator;

public class CompanyNetProxy implements Company {
    NetworkClient netClient;

    public CompanyNetProxy (NetworkClient netClient) {
        this.netClient = netClient;
    }

    @Override
    public Iterator<Employee> iterator() {
        throw new UnsupportedOperationException("Unimplemented method 'iterator'");
    }

    @Override
    public void addEmployee(Employee empl) {
        netClient.sendAndReceive("addEmployee", empl.toString());
    }

    @Override
    public int getDepartmentBudget(String department) {
        String response = netClient.sendAndReceive("getDepartmentBudget", department);
        return Integer.parseInt(response);
    }

    @Override
    public String[] getDepartments() {
        String jsonStr = netClient.sendAndReceive("getDepartments", "");
        JSONArray jsonArray = new JSONArray(jsonStr);
        return jsonArray.toList().toArray(String[]::new);
    }

    @Override
    public Employee getEmployee(long id) {
        String response = netClient.sendAndReceive("getEmployee", String.valueOf(id));
        return Employee.getEmployeeFromJSON(response);
    }

    @Override
    public Manager[] getManagersWithMostFactor() {
        String response = netClient.sendAndReceive("getManagersWithMostFactor", "");
        Manager[] res = new JSONArray(response).toList().stream().map(Object::toString)
                .map(Employee::getEmployeeFromJSON).toArray(Manager[]::new);
        return res;
    }

    @Override
    public Employee removeEmployee(long id) {
        String response = netClient.sendAndReceive("removeEmployee", "" + id);
        return Employee.getEmployeeFromJSON(response);
    }
}
