package pl.mjedynak;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import org.aspectj.lang.annotation.Around;
import org.junit.Test;
import pl.mjedynak.aspect_and_metric_combined.aspect.AbstractMonitoredInvocationTimeAspect;
import pl.mjedynak.service.TimeService;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Set;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.util.ReflectionUtils.getAllDeclaredMethods;

public class ServiceIsCoveredWithAspectsJava8Test {

    @Test
    public void serviceIsCoveredWithAspects() throws Exception {
        String packageWithAspectClasses = "pl.mjedynak.aspect_and_metric_combined.aspect";
        Class<TimeService> interfaceThatShouldHaveAspects = TimeService.class;

        Set<String> methodNames = stream(getAllDeclaredMethods(interfaceThatShouldHaveAspects)).map(Method::getName).collect(toSet());
        ImmutableSet<ClassPath.ClassInfo> classes = ClassPath.from(Thread.currentThread().getContextClassLoader())
                .getTopLevelClasses(packageWithAspectClasses);

        classes.stream()
                .map(ClassPath.ClassInfo::load)
                .filter(clazz -> clazz.getSuperclass().equals(AbstractMonitoredInvocationTimeAspect.class))
                .map(this::getAroundAnnotation)
                .filter(Optional::isPresent)
                .forEach(annotation -> removeMethodNameIfCoveredByAnnotation(methodNames, annotation.get().value()));

        assertThat(methodNames).as("Not covered methods: " + methodNames).isEmpty();
    }

    private void removeMethodNameIfCoveredByAnnotation(Set<String> methodNames, String aroundAnnotationValue) {
        methodNames.forEach(methodName -> {
            if (aroundAnnotationValue.contains(methodName)) {
                methodNames.remove(methodName);
            }
        });
    }

    private Optional<Around> getAroundAnnotation(Class clazz) {
        return stream(getAllDeclaredMethods(clazz))
                .map(method -> method.getDeclaredAnnotation(Around.class))
                .findFirst();
    }
}
