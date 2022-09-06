package assure.service;

import assure.config.QaConfig;
import assure.util.AbstractTest;
import assure.pojo.BinPojo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = QaConfig.class, loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration("src/test/webapp")
@Transactional
public class BinServiceTest extends AbstractTest {
    @Autowired
    private BinService binService;

    @Test
    public void addTest(){
        Assert.assertEquals(binService.add(5),binSelect());
    }

    @Test
    public void selectTest(){
        BinPojo binPojo = binAdd();
        Assert.assertEquals(binPojo,binService.select(0,1).get(0));
    }

    @Test
    public void selectByIdTest(){
        BinPojo binPojo = binAdd();
        Assert.assertEquals(binPojo,binService.selectById(binPojo.getBinId()));
    }



}
