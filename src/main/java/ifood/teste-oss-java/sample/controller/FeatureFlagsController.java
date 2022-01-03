package ifood.teste-oss-java.sample.controller;

import com.ifood.featureflags.interfaces.FeatureFlagsResolver;
import ifood.teste-oss-java.featureflags.FeatureFlag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("feature-flags")
public class FeatureFlagsController {

    private final FeatureFlagsResolver resolver;

    public FeatureFlagsController(final FeatureFlagsResolver resolver) {
        this.resolver = resolver;
    }

    // list all feature flags locally
    // all flags should be in /toggles/feature-toggles.properties inside container value but if not, it will return the default value from @FeatureFlagSpecification.
    @GetMapping
    public List<Flag> getFeatureFlags() {
        return Stream.of(FeatureFlag.values())
                .map(key -> new Flag(key.featureName(), this.resolver.get(key)))
                .collect(Collectors.toList());
    }

    public static class Flag {
        private final String key;
        private final Object value;

        public Flag(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        public Object getValue() {
            return value;
        }

        public String getKey() {
            return key;
        }
    }
}