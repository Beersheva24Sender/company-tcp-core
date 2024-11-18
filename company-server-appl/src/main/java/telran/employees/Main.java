package telran.employees;

import telran.io.Persistable;
import telran.net.TcpServer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final String FILE_NAME = "employees.data";
    private static final int PORT = 4000;
    private static final int SAVE_INTERVAL_SECONDS = 60; // Save every 60 seconds

    public static void main(String[] args) {
        Company company = new CompanyImpl();
        
        if (company instanceof Persistable persistable) {
            persistable.restoreFromFile(FILE_NAME);

            // Start periodic saving
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(() -> {
                try {
                    persistable.saveToFile(FILE_NAME);
                    System.out.println("Data saved successfully to " + FILE_NAME);
                } catch (Exception e) {
                    System.err.println("Error saving data: " + e.getMessage());
                }
            }, SAVE_INTERVAL_SECONDS, SAVE_INTERVAL_SECONDS, TimeUnit.SECONDS);
        }

        TcpServer tcpServer = new TcpServer(new CompanyProtocol(company), PORT);
        tcpServer.run();
    }
}
