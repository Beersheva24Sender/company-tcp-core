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
        return jsonArray.toList().toArray(new String[0]);
    }

    @Override
    public Employee getEmployee(long id) {
        String response = tcpClient.sendAndReceive("getEmployee", String.valueOf(id));
        if (response == null || response.isEmpty()) {
            return null;
        }
        JSONObject json = new JSONObject(response);
        return fromJson(json);
    }

    @Override
    public Manager[] getManagersWithMostFactor() {
        String response = tcpClient.sendAndReceive("getManagersWithMostFactor", "");
        JSONArray jsonArray = new JSONArray(response);
        Manager[] managers = new Manager[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            managers[i] = Manager.fromJson(jsonArray.getJSONObject(i));
        }
        return managers;
    }

    @Override
    public Employee removeEmployee(long id) {
        String response = tcpClient.sendAndReceive("removeEmployee", String.valueOf(id));
        if (response == null || response.isEmpty()) {
            return null;
        }
        JSONObject json = new JSONObject(response);
        return fromJson(json);
    }

    public static Employee fromJson(JSONObject json) {

        long id = json.getLong("id");
        int basicSalary = json.getInt("basicSalary");
        String department = json.getString("department");
        return new Employee(id, basicSalary, department);
    }
}
