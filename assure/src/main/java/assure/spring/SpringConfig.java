package assure.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan("assure")
@EnableTransactionManagement
@PropertySources({
        @PropertySource(value = "classpath:properties/assure.properties") //
})
public class SpringConfig {


}