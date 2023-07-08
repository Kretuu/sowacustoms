package pl.sowacustoms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.sowacustoms.panel.orders.Order;
import pl.sowacustoms.panel.orders.OrderRepository;

@Component
public class DatabaseLoader implements CommandLineRunner {

    private final OrderRepository repository;

    @Autowired
    public DatabaseLoader(OrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
