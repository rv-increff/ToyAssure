package assure.dao;

import assure.model.ChannelForm;
import assure.model.PartyForm;
import assure.pojo.*;
import assure.util.InvoiceType;
import assure.util.OrderStatus;
import assure.util.PartyType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
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
    @Autowired
    public OrderItemDao orderItemDao;

    public static List<PartyPojo> getPartyList(int n) {
        List<PartyPojo> partyPojoList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            PartyPojo partyPojo = new PartyPojo();

            PartyType type = PartyType.CLIENT;
            switch (getRandomNumber() % 2) {
                case 0:
                    type = PartyType.CUSTOMER;
            }
            partyPojo.setName(getRandomString());
            partyPojo.setType(type);
            partyPojoList.add(partyPojo);
        }

        return partyPojoList;
    }

    public PartyForm getPartyForm(){
        PartyForm partyForm = new PartyForm();
        PartyType type = PartyType.CLIENT;
        switch (getRandomNumber() % 2) {
            case 0:
                type = PartyType.CUSTOMER;
        }
        partyForm.setName(getRandomString());
        partyForm.setType(type);
        return partyForm;
    }
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

    public List<PartyPojo> partySelect() {
        return partyDao.select(0, PAGE_SIZE);
    }

    public ProductPojo productAdd() {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setClientSkuId(getRandomString());
        productPojo.setClientId(getRandomNumberLong());
        productPojo.setBrandId(getRandomString());
        productPojo.setMrp(getRandomNumberDouble());
        productPojo.setDescription(getRandomString());
        productPojo.setName(getRandomString());

        return productDao.add(productPojo);

    }
public ProductPojo productAdd(Long clientId) {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setClientSkuId(getRandomString());
        productPojo.setClientId(clientId);
        productPojo.setBrandId(getRandomString());
        productPojo.setMrp(getRandomNumberDouble());
        productPojo.setDescription(getRandomString());
        productPojo.setName(getRandomString());

        return productDao.add(productPojo);

    }

    public ProductPojo getProduct(Long clientId, String clientSkuID) {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setClientSkuId(clientSkuID);
        productPojo.setClientId(clientId);
        productPojo.setBrandId(getRandomString());
        productPojo.setMrp((double) getRandomNumber());
        productPojo.setDescription(getRandomString());
        productPojo.setName(getRandomString());

        return productPojo;

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

    public BinSkuPojo getBinSku() {
        BinSkuPojo binSkuPojo = new BinSkuPojo();
        binSkuPojo.setBinId(getRandomNumberLong());
        binSkuPojo.setGlobalSkuId(getRandomNumberLong());
        binSkuPojo.setQuantity(getRandomNumberLong());

        return binSkuPojo;
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
        channelPojo.setName(getRandomString().toUpperCase());
        return channelDao.add(channelPojo);
    }
  public ChannelForm getChannelForm() {
        ChannelForm channelForm = new ChannelForm();
        int ind = getRandomNumber() % 2;
        InvoiceType invoiceType = InvoiceType.SELF;
        if (ind == 0) {
            invoiceType = InvoiceType.CHANNEL;
        }
        channelForm.setInvoiceType(invoiceType);
        channelForm.setName(getRandomString().toUpperCase());
        return channelForm;
    }

    public ChannelPojo getChannel() {
        ChannelPojo channelPojo = new ChannelPojo();
        int ind = getRandomNumber() % 2;
        InvoiceType invoiceType = InvoiceType.SELF;
        if (ind == 0) {
            invoiceType = InvoiceType.CHANNEL;
        }
        channelPojo.setInvoiceType(invoiceType);
        channelPojo.setName(getRandomString());
        return channelPojo;
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

    public InventoryPojo getInv() {
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setFulfilledQuantity(getRandomNumberLong());
        inventoryPojo.setAvailableQuantity(getRandomNumberLong());
        inventoryPojo.setAllocatedQuantity(getRandomNumberLong());
        inventoryPojo.setGlobalSkuId(getRandomNumberLong());

        return inventoryPojo;

    }

    public List<InventoryPojo> invSelect() {
        return inventoryDao.select(0, PAGE_SIZE);
    }

    public ChannelListingPojo channelListAdd() {
        ChannelListingPojo channelListingPojo = new ChannelListingPojo();

        channelListingPojo.setChannelId(getRandomNumberLong());
        channelListingPojo.setChannelSkuId(getRandomString().toLowerCase());
        channelListingPojo.setClientId(getRandomNumberLong());
        channelListingPojo.setGlobalSkuId(getRandomNumberLong());

        return channelListingDao.add(channelListingPojo);
    }

    public ChannelListingPojo getChannelList() {
        ChannelListingPojo channelListingPojo = new ChannelListingPojo();

        channelListingPojo.setChannelId(getRandomNumberLong());
        channelListingPojo.setChannelSkuId(getRandomString());
        channelListingPojo.setClientId(getRandomNumberLong());
        channelListingPojo.setGlobalSkuId(getRandomNumberLong());

        return channelListingPojo;
    }

    public List<ChannelListingPojo> channelListSelect() {
        return channelListingDao.select(0, PAGE_SIZE);
    }

    public OrderPojo orderAdd() {
        OrderPojo orderPojo = new OrderPojo();
        int ind = getRandomNumber() % 3;
        OrderStatus orderStatus = OrderStatus.CREATED;
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

    public OrderPojo getOrder() {
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setChannelOrderId(getRandomString());
        orderPojo.setInvoiceUrl(getRandomString());
        orderPojo.setChannelId(getRandomNumberLong());
        orderPojo.setClientId(getRandomNumberLong());
        orderPojo.setCustomerId(getRandomNumberLong());

        return orderDao.add(orderPojo);
    }

    public List<OrderPojo> orderSelect() {
        return orderDao.select(0, PAGE_SIZE);
    }

    public OrderItemPojo orderItemAdd(Long orderId) {
        OrderItemPojo orderItemPojo = new OrderItemPojo();

        orderItemPojo.setOrderId(orderId);
        orderItemPojo.setOrderedQuantity(getRandomNumberLong());
        orderItemPojo.setAllocatedQuantity(getRandomNumberLong());
        orderItemPojo.setFulfilledQuantity(getRandomNumberLong());
        orderItemPojo.setSellingPricePerUnit(getRandomNumberDouble());
        orderItemPojo.setSellingPricePerUnit(getRandomNumberDouble());
        orderItemPojo.setGlobalSkuId(getRandomNumberLong());

        return orderItemDao.add(orderItemPojo);
    }


}
