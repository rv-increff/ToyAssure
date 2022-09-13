package assure.dto;

import assure.config.QaConfig;
import assure.dao.BinDao;
import assure.util.BaseTest;
import assure.util.TestData;
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
public class BinDtoTest extends BaseTest {

    @Autowired
    private BinDto binDto;
    @Autowired
    private BinDao binDao;
    @Autowired
    private TestData testData;


    @Test
    public void add() throws ApiException {
        int numberOfBins = 5;
        binDto.add(numberOfBins);
        Assert.assertEquals(numberOfBins, binDao.select(0, numberOfBins).size());
    }

    @Test
    public void addNegativeError(){
        Integer n = -1;
        try{
            binDto.add(n);
            fail("error should be thrown");
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "Number of bins should be greater than 0");
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
        int numberOfBins = 5 ;
        for (int i = 0; i < numberOfBins; i++) {
            testData.binAdd();
        }
        Assert.assertEquals(numberOfBins, binDto.select(0).size());
    }


}
