package assure.service;

import assure.config.BaseTest;
import assure.util.TestData;
import assure.pojo.PartyPojo;
import assure.spring.ApiException;
import assure.util.PartyType;
import commons.model.ErrorData;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static assure.util.RandomUtil.getRandomString;
import static org.junit.Assert.fail;

public class PartyServiceTest extends BaseTest {
    @Autowired
    private PartyService partyService;
    @Autowired
    private TestData testData;

    @Test
    public void addTest() throws ApiException {
        List<PartyPojo> partyPojoList = testData.getPartyList(5);
        partyService.add(partyPojoList);

        Assert.assertEquals(partyPojoList,testData.partySelect());
    }

    @Test
    public void addPairExistsErrorTest(){
        PartyPojo partyPojo = testData.partyAdd();
        List<PartyPojo> partyPojoList = testData.getPartyList(5);
        partyPojoList.add(partyPojo);
        try{
            partyService.add(partyPojoList);
            fail("error not thrown");
        } catch (ApiException e) {
            ErrorData errorData = new ErrorData(partyPojoList.size(), "name - Type pair exists");
            Assert.assertEquals(e.getErrorFormList().get(0).toString(),errorData.toString());
        }
    }

    @Test
    public void selectByIdTest() throws ApiException {
        PartyPojo partyPojo = testData.partyAdd();
        partyService.selectById(partyPojo.getId());
    }

    @Test
    public void selectByIdNotExistsErrorTest(){
        try{
            partyService.selectById(1L);
            fail("error not thrown");
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(),"Party does not exist");
        }
    }

    @Test
    public void checkByIdAndTypeTest() throws ApiException {
        PartyPojo partyPojo = testData.partyAdd();
        Assert.assertEquals(partyPojo.getId(),partyService.checkByIdAndType(partyPojo.getId(), partyPojo.getType()));
    }

    @Test
    public void checkByIdAndTypeWrongTypeTest() throws ApiException {
        PartyPojo partyPojo = testData.partyAdd(getRandomString(), PartyType.CLIENT);
        try{
            partyService.checkByIdAndType(partyPojo.getId(), PartyType.CUSTOMER);
            fail("error not thrown");
        }catch (ApiException e){
            Assert.assertEquals(e.getMessage(), PartyType.CUSTOMER + " does not exist");
        }
    }

    @Test
    public void select(){
        List<PartyPojo> partyPojoList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            partyPojoList.add(testData.partyAdd());
        }
        Assert.assertEquals(new HashSet<>(partyPojoList),
                new HashSet<>(partyService.select(0, 5)));
    }

}
