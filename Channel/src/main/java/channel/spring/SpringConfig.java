package channel.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan("channel")
@EnableTransactionManagement
@PropertySources({
        @PropertySource(value = "classpath:properties/channel.properties") //
})
public class SpringConfig {


}