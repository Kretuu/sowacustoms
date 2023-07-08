package pl.sowacustoms.panel.orders;

import jakarta.persistence.*;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.DialectOverride;

import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order {

    private @Id @GeneratedValue int id;
    private long date = System.currentTimeMillis();
    private String name;

    private @Version @JsonIgnore int version;

    protected Order() {}

    public Order(String name) {
        this.date = System.currentTimeMillis();
        this.name = name;
    }

    public int getId() {
        return id;
    }


    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, date, name, version);
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) &&
                Objects.equals(date, order.date) &&
                Objects.equals(name, order.name) &&
                Objects.equals(version, order.version);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setName(String name) {
        this.name = name;
    }
}
