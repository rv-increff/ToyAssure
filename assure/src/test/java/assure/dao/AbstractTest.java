package assure.dao;

import assure.pojo.*;
import assure.util.InvoiceType;
import assure.util.OrderStatus;
import assure.util.PartyType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static assure.util.RandomUtil.*;

public class AbstractTest {
    private static final Integer PAGE_SIZE = 100;
    @Autowired
    public BinDao binDao;
    @Autowired
    public PartyDao partyDao;
    @Autowired
    public ProductDao productDao;
    @Autowired
    public ChannelDao channelDao;
    @Autowired
    public BinSkuDao binSkuDao;
    @Autowired
    public InventoryDao inventoryDao;
    @Autowired
    public ChannelListingDao channelListingDao;
    @Autowired
    public OrderDao orderDao;

    public BinPojo binAdd() {
        BinPojo binPojo = new BinPojo();
        return binDao.add(binPojo);
    }

    public List<BinPojo> binSelect() {
        return binDao.select(0, PAGE_SIZE);
    }

    public PartyPojo partyAdd(String name, PartyType type) {
        PartyPojo partyPojo = new PartyPojo();
        partyPojo.setName(name);
        partyPojo.setType(type);
        return partyDao.add(partyPojo);
    }

    public PartyPojo partyAdd() {
        int ind = getRandomNumber() % 2;
        PartyType partyType = PartyType.CLIENT;
        if (ind == 0) {
            partyType = PartyType.CUSTOMER;
        }

        return partyAdd(getRandomString(), partyType);
    }

    public ProductPojo productAdd() {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setClientSkuId(getRandomString());
        productPojo.setClientId(getRandomNumberLong());
        productPojo.setBrandId(getRandomString());
        productPojo.setMrp((double) getRandomNumber());
        productPojo.setDescription(getRandomString());
        productPojo.setName(getRandomString());

        return productDao.add(productPojo);

    }

    public List<ProductPojo> productSelect() {
        return productDao.select(0, PAGE_SIZE);
    }

    public BinSkuPojo binSkuAdd() {
        BinSkuPojo binSkuPojo = new BinSkuPojo();
        binSkuPojo.setBinId(getRandomNumberLong());
        binSkuPojo.setGlobalSkuId(getRandomNumberLong());
        binSkuPojo.setQuantity(getRandomNumberLong());

        return binSkuDao.add(binSkuPojo);
    }

    public List<BinSkuPojo> binSkuSelect() {
        return binSkuDao.select(0, PAGE_SIZE);
    }

    public ChannelPojo channelAdd() {
        ChannelPojo channelPojo = new ChannelPojo();
        int ind = getRandomNumber() % 2;
        InvoiceType invoiceType = InvoiceType.SELF;
        if (ind == 0) {
            invoiceType = InvoiceType.CHANNEL;
        }
        channelPojo.setInvoiceType(invoiceType);
        channelPojo.setName(getRandomString());
        return channelDao.add(channelPojo);
    }

    public List<ChannelPojo> channelSelect() {
        return channelDao.select(0, PAGE_SIZE);
    }

    public InventoryPojo invAdd() {
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setFulfilledQuantity(getRandomNumberLong());
        inventoryPojo.setAvailableQuantity(getRandomNumberLong());
        inventoryPojo.setAllocatedQuantity(getRandomNumberLong());
        inventoryPojo.setGlobalSkuId(getRandomNumberLong());

        return inventoryDao.add(inventoryPojo);

    }

    public List<InventoryPojo> invSelect() {
        return inventoryDao.select(0, PAGE_SIZE);
    }

    public ChannelListingPojo channelListAdd() {
        ChannelListingPojo channelListingPojo = new ChannelListingPojo();

        channelListingPojo.setChannelId(getRandomNumberLong());
        channelListingPojo.setChannelSkuId(getRandomString());
        channelListingPojo.setClientId(getRandomNumberLong());
        channelListingPojo.setGlobalSkuId(getRandomNumberLong());

        return channelListingDao.add(channelListingPojo);
    }

    public List<ChannelListingPojo> channelListSelect() {
        return channelListingDao.select(0, PAGE_SIZE);
    }

    public OrderPojo orderAdd(){
        OrderPojo orderPojo = new OrderPojo();
        int ind = getRandomNumber() % 3;
        OrderStatus orderStatus =  OrderStatus.CREATED;;
        switch (ind) {
            case 1:
                orderStatus = OrderStatus.ALLOCATED;
            case 2:
                orderStatus = OrderStatus.FULFILLED;
        }
        orderPojo.setStatus(orderStatus);
        orderPojo.setChannelOrderId(getRandomString());
        orderPojo.setInvoiceUrl(getRandomString());
        orderPojo.setChannelId(getRandomNumberLong());
        orderPojo.setClientId(getRandomNumberLong());
        orderPojo.setCustomerId(getRandomNumberLong());

        return orderDao.add(orderPojo);
    }

    public List<OrderStatus> orderSelect() {
        return orderDao.select(0, PAGE_SIZE);
    }
}
