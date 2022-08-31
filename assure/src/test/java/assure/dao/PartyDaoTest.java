package assure.dao;

import assure.config.QaConfig;
import assure.pojo.PartyPojo;
import assure.util.PartyType;
import assure.util.RandomUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = QaConfig.class, loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration("src/test/webapp")
@Transactional
public class PartyDaoTest extends AbstractTest{

    @Test
    public void addTest(){
        String name = RandomUtil.getRandomString();
        PartyType partyType = PartyType.CLIENT;
        PartyPojo partyPojo = partyAdd(name,partyType);
        Assert.assertEquals(name,partyPojo.getName());
        Assert.assertEquals(partyType,partyPojo.getType());

    }

    @Test
    public void selectByIdTest(){
        PartyPojo partyPojo = partyAdd();
        PartyPojo partyPojo1 = partyDao.selectById(partyPojo.getId());
        Assert.assertEquals(partyPojo1,partyPojo);
    }

    @Test
    public void selectByNameAndPartyTypeTest(){
        PartyPojo partyPojo = partyAdd();
        PartyPojo partyPojo1 = partyDao.selectByNameAndPartyType(partyPojo.getName(), partyPojo.getType());
        Assert.assertEquals(partyPojo1,partyPojo);
    }
}
