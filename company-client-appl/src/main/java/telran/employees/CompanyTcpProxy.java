package telran.employees;

import org.json.JSONArray;
import org.json.JSONObject;
import telran.net.TcpClient;
import java.util.Iterator;

public class CompanyTcpProxy implements Company {
    TcpClient tcpClient;

    public CompanyTcpProxy(TcpClient tcpClient) {
        this.tcpClient = tcpClient;
    }

    @Override
    public Iterator<Employee> iterator() {
        throw new UnsupportedOperationException("Unimplemented method 'iterator'");
    }

    @Override
    public void addEmployee(Employee empl) {
        tcpClient.sendAndReceive("addEmployee", empl.toString());
    }

    @Override
    public int getDepartmentBudget(String department) {
        String response = tcpClient.sendAndReceive("getDepartmentBudget", department);
        return Integer.parseInt(response);
    }

    @Override
    public String[] getDepartments() {
        String jsonStr = tcpClient.sendAndReceive("getDepartments", "");
        JSONArray jsonArray = new JSONArray(jsonStr);
        return jsonArray.toList().toArray(String[]::new);
    }

    @Override
    public Employee getEmployee(long id) {
        String response = tcpClient.sendAndReceive("getEmployee", String.valueOf(id));
        return Employee.getEmployeeFromJSON(response);
    }

    @Override
    public Manager[] getManagersWithMostFactor() {
        String response = tcpClient.sendAndReceive("getManagersWithMostFactor", "");
        Manager[] res = new JSONArray(response).toList().stream().map(Object::toString)
                .map(Employee::getEmployeeFromJSON).toArray(Manager[]::new);
        return res;
    }

    @Override
    public Employee removeEmployee(long id) {
        String response = tcpClient.sendAndReceive("removeEmployee", "" + id);
        return Employee.getEmployeeFromJSON(response);
    }
}
