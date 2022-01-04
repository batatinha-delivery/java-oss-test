package ifood.teste.sample.model;

import java.util.Optional;

public class Sample {

    private String id;
    private String name;

    public Sample() {
    }

    public Sample(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String truncateName() {
        return Optional.ofNullable(this.name)
                .map(value -> value.contains(" ")
                        ? value.substring(0, value.indexOf(" "))
                        : this.name)
                .orElse("");
    }

    // override toString in entity class to omit sensitive information
    @Override
    public String toString() {
        return "Sample{" +
                "id='" + id + '\'' +
                ", name='" + truncateName() + '\'' +
                '}';
    }
}
