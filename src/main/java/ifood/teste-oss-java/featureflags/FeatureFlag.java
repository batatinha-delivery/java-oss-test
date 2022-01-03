package ifood.teste-oss-java.featureflags;

import com.ifood.featureflags.interfaces.FeatureFlagKey;
import com.ifood.featureflags.interfaces.FeatureFlagSpecification;

// Define yours feature flag keys
public enum FeatureFlag implements FeatureFlagKey {

    // Define default value for key for cases of problems with local storage, this value will be used
    @FeatureFlagSpecification.BooleanDefaultValue(false)
    ACTIVE_ALWAYS_FALLBACK,
    ;

    // normally we use featureName with enum name
    @Override
    public String featureName() {
        return name();
    }
}
