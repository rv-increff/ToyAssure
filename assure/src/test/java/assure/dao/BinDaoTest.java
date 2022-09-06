package assure.dao;

import assure.config.QaConfig;
import assure.pojo.BinPojo;
import assure.util.AbstractTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = QaConfig.class, loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration("src/test/webapp")
@Transactional
public class BinDaoTest extends AbstractTest {

    @Test
    public void addTest(){
        binAdd();
    }

    @Test
    public void selectTest(){
        List<BinPojo> binPojoList = new ArrayList<>();
        for(int i=0;i<5;i++)
            binPojoList.add(binAdd());

        Assert.assertEquals(binPojoList, binSelect());
    }

    @Test
    public void selectByIdTest(){
        BinPojo binPojo = binAdd();
        Assert.assertEquals(binPojo, binDao.selectById(binPojo.getBinId()));
    }


}
