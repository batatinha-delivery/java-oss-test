package ifood.teste.fallback.model;

public class FallbackResult {

    private final String identifier;

    public FallbackResult(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String toString() {
        return "FallbackResult{" +
                "identifier='" + identifier + '\'' +
                '}';
    }
}