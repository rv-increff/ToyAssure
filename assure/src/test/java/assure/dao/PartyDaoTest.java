package assure.dao;

import assure.pojo.PartyPojo;
import assure.config.BaseTest;
import assure.util.TestData;
import commons.util.PartyType;
import assure.util.RandomUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PartyDaoTest extends BaseTest {

    @Autowired
    private TestData testData;
    @Autowired
    private PartyDao partyDao;

    @Test
    public void addTest(){
        String name = RandomUtil.getRandomString();
        PartyType partyType = PartyType.CLIENT;
        PartyPojo partyPojo = testData.partyAdd(name, partyType);
        Assert.assertEquals(name,partyPojo.getName());
        Assert.assertEquals(partyType,partyPojo.getType());

    }

    @Test
    public void selectByIdTest(){
        PartyPojo partyPojo = testData.partyAdd();
        PartyPojo partyPojo1 = partyDao.selectById(partyPojo.getId());
        Assert.assertEquals(partyPojo1,partyPojo);
    }

    @Test
    public void selectByNameAndPartyTypeTest(){
        PartyPojo partyPojo = testData.partyAdd();
        PartyPojo partyPojo1 = partyDao.selectByNameAndPartyType(partyPojo.getName(), partyPojo.getType());
        Assert.assertEquals(partyPojo1,partyPojo);
    }
}
