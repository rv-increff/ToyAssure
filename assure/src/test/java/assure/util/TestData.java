package assure.util;

import assure.dao.*;
import assure.model.ChannelForm;
import commons.model.ChannelOrderForm;
import commons.model.OrderForm;
import assure.model.PartyForm;
import assure.pojo.*;
import commons.model.OrderItemForm;
import commons.model.OrderItemFormChannel;
import commons.util.InvoiceType;
import commons.util.OrderStatus;
import commons.util.PartyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static assure.util.RandomUtil.*;


@Service
public class TestData {
    private static final Integer PAGE_SIZE = 100;
    @Autowired
    private BinDao binDao;
    @Autowired
    private PartyDao partyDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ChannelDao channelDao;
    @Autowired
    private BinSkuDao binSkuDao;
    @Autowired
    private InventoryDao inventoryDao;
    @Autowired
    private ChannelListingDao channelListingDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderItemDao orderItemDao;

    public static List<PartyPojo> getPartyList(int n) {
        List<PartyPojo> partyPojoList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            PartyPojo partyPojo = new PartyPojo();

            PartyType type = PartyType.CLIENT;
            switch (RandomUtil.getRandomNumber() % 2) {
                case 0:
                    type = PartyType.CUSTOMER;
            }
            partyPojo.setName(RandomUtil.getRandomString());
            partyPojo.setType(type);
            partyPojoList.add(partyPojo);
        }

        return partyPojoList;
    }

    public PartyForm getPartyForm() {
        PartyForm partyForm = new PartyForm();
        PartyType type = PartyType.CLIENT;
        switch (RandomUtil.getRandomNumber() % 2) {
            case 0:
                type = PartyType.CUSTOMER;
        }
        partyForm.setName(RandomUtil.getRandomString());
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
        int ind = RandomUtil.getRandomNumber() % 2;
        PartyType partyType = PartyType.CLIENT;
        if (ind == 0) {
            partyType = PartyType.CUSTOMER;
        }

        return partyAdd(RandomUtil.getRandomString(), partyType);
    }

    public List<PartyPojo> partySelect() {
        return partyDao.select(0, PAGE_SIZE);
    }

    public ProductPojo productAdd() {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setClientSkuId(RandomUtil.getRandomString());
        productPojo.setClientId(RandomUtil.getRandomNumberLong());
        productPojo.setBrandId(RandomUtil.getRandomString());
        productPojo.setMrp(RandomUtil.getRandomNumberDouble());
        productPojo.setDescription(RandomUtil.getRandomString());
        productPojo.setName(RandomUtil.getRandomString());

        return productDao.add(productPojo);

    }

    public ProductPojo productAdd(Long clientId) {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setClientSkuId(RandomUtil.getRandomString().toUpperCase().trim());
        productPojo.setClientId(clientId);
        productPojo.setBrandId(RandomUtil.getRandomString());
        productPojo.setMrp(RandomUtil.getRandomNumberDouble());
        productPojo.setDescription(RandomUtil.getRandomString());
        productPojo.setName(RandomUtil.getRandomString());

        return productDao.add(productPojo);

    }

    public ProductPojo getProduct(Long clientId, String clientSkuID) {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setClientSkuId(clientSkuID);
        productPojo.setClientId(clientId);
        productPojo.setBrandId(RandomUtil.getRandomString());
        productPojo.setMrp((double) RandomUtil.getRandomNumber());
        productPojo.setDescription(RandomUtil.getRandomString());
        productPojo.setName(RandomUtil.getRandomString());

        return productPojo;

    }

    public List<ProductPojo> productSelect() {
        return productDao.select(0, PAGE_SIZE);
    }

    public BinSkuPojo binSkuAdd() {
        BinSkuPojo binSkuPojo = new BinSkuPojo();
        binSkuPojo.setBinId(RandomUtil.getRandomNumberLong());
        binSkuPojo.setGlobalSkuId(RandomUtil.getRandomNumberLong());
        binSkuPojo.setQuantity(RandomUtil.getRandomNumberLong());

        return binSkuDao.add(binSkuPojo);
    }

    public BinSkuPojo binSkuAdd(Long globalSkuId) {
        BinSkuPojo binSkuPojo = new BinSkuPojo();
        binSkuPojo.setBinId(RandomUtil.getRandomNumberLong());
        binSkuPojo.setGlobalSkuId(globalSkuId);
        binSkuPojo.setQuantity(RandomUtil.getRandomNumberLong());

        return binSkuDao.add(binSkuPojo);
    }

    public BinSkuPojo binSkuAddByBinId(Long binId) {
        BinSkuPojo binSkuPojo = new BinSkuPojo();
        binSkuPojo.setBinId(binId);
        binSkuPojo.setGlobalSkuId(RandomUtil.getRandomNumberLong());
        binSkuPojo.setQuantity(RandomUtil.getRandomNumberLong());

        return binSkuDao.add(binSkuPojo);
    }
 public BinSkuPojo binSkuAdd(Long binId, Long globalSkuId, Long qty) {
        BinSkuPojo binSkuPojo = new BinSkuPojo();
        binSkuPojo.setBinId(binId);
        binSkuPojo.setGlobalSkuId(globalSkuId);
        binSkuPojo.setQuantity(qty);

        return binSkuDao.add(binSkuPojo);
    }

    public BinSkuPojo getBinSku() {
        BinSkuPojo binSkuPojo = new BinSkuPojo();
        binSkuPojo.setBinId(RandomUtil.getRandomNumberLong());
        binSkuPojo.setGlobalSkuId(RandomUtil.getRandomNumberLong());
        binSkuPojo.setQuantity(RandomUtil.getRandomNumberLong());

        return binSkuPojo;
    }


    public List<BinSkuPojo> binSkuSelect() {
        return binSkuDao.select(0, PAGE_SIZE);
    }

    public ChannelPojo channelAdd() {
        ChannelPojo channelPojo = new ChannelPojo();
        int ind = RandomUtil.getRandomNumber() % 2;
        InvoiceType invoiceType = InvoiceType.SELF;
        if (ind == 0) {
            invoiceType = InvoiceType.CHANNEL;
        }
        channelPojo.setInvoiceType(invoiceType);
        channelPojo.setName(RandomUtil.getRandomString().toUpperCase());
        return channelDao.add(channelPojo);
    }

    public ChannelForm getChannelForm() {
        ChannelForm channelForm = new ChannelForm();
        int ind = RandomUtil.getRandomNumber() % 2;
        InvoiceType invoiceType = InvoiceType.SELF;
        if (ind == 0) {
            invoiceType = InvoiceType.CHANNEL;
        }
        channelForm.setInvoiceType(invoiceType);
        channelForm.setName(RandomUtil.getRandomString().toUpperCase());
        return channelForm;
    }

    public ChannelPojo getChannel() {
        ChannelPojo channelPojo = new ChannelPojo();
        int ind = RandomUtil.getRandomNumber() % 2;
        InvoiceType invoiceType = InvoiceType.SELF;
        if (ind == 0) {
            invoiceType = InvoiceType.CHANNEL;
        }
        channelPojo.setInvoiceType(invoiceType);
        channelPojo.setName(RandomUtil.getRandomString());
        return channelPojo;
    }


    public List<ChannelPojo> channelSelect() {
        return channelDao.select(0, PAGE_SIZE);
    }

    public InventoryPojo invAdd() {
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setFulfilledQuantity(RandomUtil.getRandomNumberLong());
        inventoryPojo.setAvailableQuantity(RandomUtil.getRandomNumberLong());
        inventoryPojo.setAllocatedQuantity(RandomUtil.getRandomNumberLong());
        inventoryPojo.setGlobalSkuId(RandomUtil.getRandomNumberLong());

        return inventoryDao.add(inventoryPojo);

    }

    public InventoryPojo getInv() {
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setFulfilledQuantity(RandomUtil.getRandomNumberLong());
        inventoryPojo.setAvailableQuantity(RandomUtil.getRandomNumberLong());
        inventoryPojo.setAllocatedQuantity(RandomUtil.getRandomNumberLong());
        inventoryPojo.setGlobalSkuId(RandomUtil.getRandomNumberLong());

        return inventoryPojo;

    }

    public List<InventoryPojo> invSelect() {
        return inventoryDao.select(0, PAGE_SIZE);
    }

    public ChannelListingPojo channelListAdd() {
        ChannelListingPojo channelListingPojo = new ChannelListingPojo();

        channelListingPojo.setChannelId(RandomUtil.getRandomNumberLong());
        channelListingPojo.setChannelSkuId(RandomUtil.getRandomString().toLowerCase());
        channelListingPojo.setClientId(RandomUtil.getRandomNumberLong());
        channelListingPojo.setGlobalSkuId(RandomUtil.getRandomNumberLong());

        return channelListingDao.add(channelListingPojo);
    }
    public ChannelListingPojo channelListAdd(Long channelId, Long clientId) {
        ChannelListingPojo channelListingPojo = new ChannelListingPojo();

        channelListingPojo.setChannelId(channelId);
        channelListingPojo.setChannelSkuId(RandomUtil.getRandomString().toLowerCase());
        channelListingPojo.setClientId(clientId);
        channelListingPojo.setGlobalSkuId(RandomUtil.getRandomNumberLong());

        return channelListingDao.add(channelListingPojo);
    }


    public ChannelListingPojo getChannelList() {
        ChannelListingPojo channelListingPojo = new ChannelListingPojo();

        channelListingPojo.setChannelId(RandomUtil.getRandomNumberLong());
        channelListingPojo.setChannelSkuId(RandomUtil.getRandomString());
        channelListingPojo.setClientId(RandomUtil.getRandomNumberLong());
        channelListingPojo.setGlobalSkuId(RandomUtil.getRandomNumberLong());

        return channelListingPojo;
    }

    public List<ChannelListingPojo> channelListSelect() {
        return channelListingDao.select(0, PAGE_SIZE);
    }

    public OrderPojo orderAdd() {
        OrderPojo orderPojo = new OrderPojo();
        int ind = RandomUtil.getRandomNumber() % 3;
        OrderStatus orderStatus = OrderStatus.CREATED;
        switch (ind) {
            case 1:
                orderStatus = OrderStatus.ALLOCATED;
            case 2:
                orderStatus = OrderStatus.FULFILLED;
        }
        orderPojo.setStatus(orderStatus);
        orderPojo.setChannelOrderId(RandomUtil.getRandomString());
        orderPojo.setInvoiceUrl(RandomUtil.getRandomString());
        orderPojo.setChannelId(RandomUtil.getRandomNumberLong());
        orderPojo.setClientId(RandomUtil.getRandomNumberLong());
        orderPojo.setCustomerId(RandomUtil.getRandomNumberLong());

        return orderDao.add(orderPojo);
    }
 public OrderPojo orderAdd(Long clientId, Long channelId) {
        OrderPojo orderPojo = new OrderPojo();
        int ind = RandomUtil.getRandomNumber() % 3;
        OrderStatus orderStatus = OrderStatus.CREATED;
        switch (ind) {
            case 1:
                orderStatus = OrderStatus.ALLOCATED;
            case 2:
                orderStatus = OrderStatus.FULFILLED;
        }
        orderPojo.setStatus(orderStatus);
        orderPojo.setChannelOrderId(RandomUtil.getRandomString());
        orderPojo.setInvoiceUrl(RandomUtil.getRandomString());
        orderPojo.setChannelId(channelId);
        orderPojo.setClientId(clientId);
        orderPojo.setCustomerId(RandomUtil.getRandomNumberLong());

        return orderDao.add(orderPojo);
    }

    public OrderPojo getOrder() {
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setChannelOrderId(RandomUtil.getRandomString());
        orderPojo.setInvoiceUrl(RandomUtil.getRandomString());
        orderPojo.setChannelId(RandomUtil.getRandomNumberLong());
        orderPojo.setClientId(RandomUtil.getRandomNumberLong());
        orderPojo.setCustomerId(RandomUtil.getRandomNumberLong());

        return orderDao.add(orderPojo);
    }

    public List<OrderPojo> orderSelect() {
        return orderDao.select(0, PAGE_SIZE);
    }

    public OrderItemPojo orderItemAdd(Long orderId) {
        OrderItemPojo orderItemPojo = new OrderItemPojo();

        orderItemPojo.setOrderId(orderId);
        orderItemPojo.setOrderedQuantity(RandomUtil.getRandomNumberLong());
        orderItemPojo.setAllocatedQuantity(RandomUtil.getRandomNumberLong());
        orderItemPojo.setFulfilledQuantity(RandomUtil.getRandomNumberLong());
        orderItemPojo.setSellingPricePerUnit(RandomUtil.getRandomNumberDouble());
        orderItemPojo.setSellingPricePerUnit(RandomUtil.getRandomNumberDouble());
        orderItemPojo.setGlobalSkuId(RandomUtil.getRandomNumberLong());

        return orderItemDao.add(orderItemPojo);
    }

    public OrderItemPojo getOrderItem(Long orderId) {
        OrderItemPojo orderItemPojo = new OrderItemPojo();

        orderItemPojo.setOrderId(orderId);
        orderItemPojo.setOrderedQuantity(RandomUtil.getRandomNumberLong());
        orderItemPojo.setAllocatedQuantity(RandomUtil.getRandomNumberLong());
        orderItemPojo.setFulfilledQuantity(RandomUtil.getRandomNumberLong());
        orderItemPojo.setSellingPricePerUnit(RandomUtil.getRandomNumberDouble());
        orderItemPojo.setSellingPricePerUnit(RandomUtil.getRandomNumberDouble());
        orderItemPojo.setGlobalSkuId(RandomUtil.getRandomNumberLong());

        return orderItemPojo;
    }

    public OrderItemForm getOrderItemForm() {
        OrderItemForm orderItemForm = new OrderItemForm();
        orderItemForm.setQuantity(RandomUtil.getRandomNumberLong());
        orderItemForm.setClientSkuId(RandomUtil.getRandomString());
        orderItemForm.setSellingPricePerUnit(RandomUtil.getRandomNumberDouble());
        return orderItemForm;
    }

    public OrderForm getOrderForm(String channelOrderId, Long clientId, Long customerId, List<OrderItemForm> orderItemFormList){
        OrderForm orderForm = new OrderForm();
        orderForm.setChannelOrderId(channelOrderId);
        orderForm.setClientId(clientId);
        orderForm.setCustomerId(customerId);
        orderForm.setOrderItemFormList(orderItemFormList);
        return orderForm;
    }

    public ChannelOrderForm getChannelOrderForm(Long channelId, Long clientId, Long customerId, String channelOrderId, List<OrderItemFormChannel> orderItemFormChannelList){
        ChannelOrderForm channelOrderForm = new ChannelOrderForm();

        channelOrderForm.setChannelId(channelId);
        channelOrderForm.setClientId(clientId);
        channelOrderForm.setCustomerId(customerId);
        channelOrderForm.setChannelOrderId(channelOrderId);
        channelOrderForm.setOrderItemFormChannelList(orderItemFormChannelList);
        return channelOrderForm;
    }

    public OrderItemFormChannel getOrderItemFormChannel(String ChannelSkuId, Long qty, Double sellingPrice){
        OrderItemFormChannel orderItemFormChannel = new OrderItemFormChannel();
        orderItemFormChannel.setChannelSkuId(ChannelSkuId);
        orderItemFormChannel.setQuantity(qty);
        orderItemFormChannel.setSellingPricePerUnit(sellingPrice);

        return orderItemFormChannel;
    }

    public InventoryPojo invAdd(Long gsku, Long availQty, Long allocatedQty, Long fulfilledQty){
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setGlobalSkuId(gsku);
        inventoryPojo.setAvailableQuantity(availQty);
        inventoryPojo.setAllocatedQuantity(allocatedQty);
        inventoryPojo.setFulfilledQuantity(fulfilledQty);

        return inventoryDao.add(inventoryPojo);
    }

}
