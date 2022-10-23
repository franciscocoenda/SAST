package es.technest.security.api.util.security;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.aop.target.CommonsPool2TargetSource;

import javax.crypto.Cipher;

@EqualsAndHashCode(callSuper = true)
public class CipherObjectPool extends CommonsPool2TargetSource {

    @Getter
    private String algorithmType;

    public CipherObjectPool(String targetBeanName, String algorithmType, Integer minIdle, Integer maxIdle) {
        setMaxSize(-1);
        setMaxWait(0);
        setMinIdle(minIdle);
        setMaxIdle(maxIdle);
        setTargetBeanName(targetBeanName);
        this.algorithmType = algorithmType;
    }

    @Override
    public Cipher getTarget() throws Exception {
        return (Cipher) super.getTarget();
    }

}
