package assure.service;

import assure.config.QaConfig;
import assure.dao.AbstractTest;
import assure.pojo.PartyPojo;
import assure.spring.ApiException;
import assure.util.PartyType;
import commons.model.ErrorData;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static assure.util.RandomUtil.getRandomString;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = QaConfig.class, loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration("src/test/webapp")
@Transactional
public class PartyServiceTest extends AbstractTest {
    @Autowired
    public PartyService partyService;

    @Test
    public void addTest() throws ApiException {
        List<PartyPojo> partyPojoList = getPartyList(5);
        partyService.add(partyPojoList);

        Assert.assertEquals(partyPojoList,partySelect());
    }

    @Test
    public void addPairExistsErrorTest(){
        PartyPojo partyPojo = partyAdd();
        List<PartyPojo> partyPojoList = getPartyList(5);
        partyPojoList.add(partyPojo);
        try{
            partyService.add(partyPojoList);
        } catch (ApiException e) {
            ErrorData errorData = new ErrorData(partyPojoList.size(), "name - Type pair exists");
            Assert.assertEquals(e.getErrorFormList().get(0).toString(),errorData.toString());
        }
    }

    @Test
    public void selectByIdTest() throws ApiException {
        PartyPojo partyPojo = partyAdd();
        partyService.selectById(partyPojo.getId());
    }

    @Test
    public void selectByIdNotExistsErrorTest(){
        try{
            partyService.selectById(1L);
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(),"Party does not exist");
        }
    }

    @Test
    public void checkByIdAndTypeTest() throws ApiException {
        PartyPojo partyPojo = partyAdd();
        Assert.assertEquals(partyPojo.getId(),partyService.checkByIdAndType(partyPojo.getId(), partyPojo.getType()));
    }

    @Test
    public void checkByIdAndTypeWrongTypeTest() throws ApiException {
        PartyPojo partyPojo = partyAdd(getRandomString(), PartyType.CLIENT);
        try{
            partyService.checkByIdAndType(partyPojo.getId(), PartyType.CUSTOMER);
        }catch (ApiException e){
            Assert.assertEquals(e.getMessage(), PartyType.CUSTOMER + " does not exist");
        }
    }

    @Test
    public void select(){
        List<PartyPojo> partyPojoList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            partyPojoList.add(partyAdd());
        }
        Assert.assertEquals(new HashSet<>(partyPojoList),
                new HashSet<>(partyService.select(0, 5)));
    }

}