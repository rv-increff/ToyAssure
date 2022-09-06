package assure.dto;

import assure.config.QaConfig;
import assure.util.AbstractTest;
import assure.spring.ApiException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = QaConfig.class, loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration("src/test/webapp")
@Transactional
public class BinDtoTest extends AbstractTest {

    @Autowired
    BinDto binDto;

    @Test
    public void add() throws ApiException {
        int n = 5;
        binDto.add(n);
        Assert.assertEquals(n,binDao.select(0,n).size());
    }

    @Test
    public void addNullError(){
        Integer n = -1;
        try{
            binDto.add(n);
            fail("error should be thrown");
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "Null obj");
        }
    }

    @Test
    public void addMaxLimitErrorTest(){
        try{
            binDto.add(1000);
            fail("error should be thrown");
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "Number of bins to create cannot exceed the limit : 100");
        }
    }

    @Test
    public void selectTest(){
        int n =5 ;
        for (int i = 0; i < n; i++) {
            binAdd();
        }
        Assert.assertEquals(n,binDto.select(0).size());
    }


}
