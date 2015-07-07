package pl.mjedynak;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import org.aspectj.lang.annotation.Around;
import org.junit.Test;
import pl.mjedynak.aspect_and_metric_combined.aspect.AbstractMonitoredInvocationTimeAspect;
import pl.mjedynak.service.TimeService;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.util.ReflectionUtils.getAllDeclaredMethods;

public class ServiceIsCoveredWithAspectsTest {

    @Test
    public void serviceIsCoveredWithAspects() throws Exception {
        String packageWithAspectClasses = "pl.mjedynak.aspect_and_metric_combined.aspect";
        Class<TimeService> interfaceThatShouldHaveAspects = TimeService.class;
        Set<String> methodNames = getMethodNames(getAllDeclaredMethods(interfaceThatShouldHaveAspects));
        ImmutableSet<ClassPath.ClassInfo> classes = ClassPath.from(Thread.currentThread().getContextClassLoader())
                .getTopLevelClasses(packageWithAspectClasses);

        for (ClassPath.ClassInfo aClass : classes) {
            Optional<String> aroundAnnotationValue = getAroundAnnotationValue(Class.forName(aClass.getName()));
            if (aroundAnnotationValue.isPresent()) {
                removeMethodNameIfCoveredByAnnotation(methodNames, aroundAnnotationValue.get());
            }
        }

        assertThat(methodNames).as("Not covered methods: " + methodNames).isEmpty();
    }

    private Set<String> getMethodNames(Method[] methods) {
        Set<String> methodNames = new HashSet<>();
        for (Method method : methods) {
            methodNames.add(method.getName());
        }
        return methodNames;
    }

    private void removeMethodNameIfCoveredByAnnotation(Set<String> methodNames, String aroundAnnotationValue) {
        for (String methodName : methodNames) {
            if (aroundAnnotationValue.contains(methodName)) {
                methodNames.remove(methodName);
                break;
            }
        }
    }

    private Optional<String> getAroundAnnotationValue(Class clazz) {
        Optional<String> result = Optional.absent();
        Method[] aspectMethods = getAllDeclaredMethods(clazz);

        if (clazz.getSuperclass().equals(AbstractMonitoredInvocationTimeAspect.class)) {
            for (Method method : aspectMethods) {
                Around declaredAnnotation = method.getDeclaredAnnotation(Around.class);
                if (declaredAnnotation != null) {
                    result = Optional.of(declaredAnnotation.value());
                    break;
                }
            }
        }
        return result;
    }
}
