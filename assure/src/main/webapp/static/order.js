var pageNumber = 0

localStorage.setItem("orderItems", JSON.stringify([]));

function loadOrder() {
    const xhr = new XMLHttpRequest();

    xhr.open("GET", `http://localhost:9000/assure/orders?pageNumber=${pageNumber}`, true);

    xhr.onload = function () {
        if (this.status === 200) {

            obj = JSON.parse(this.responseText);
            console.log(obj, "inLoadfuntion");

            let body = document.getElementById("orderTbody");

            str = ""
            for (var i = 0; i < obj.length; i++) {
                str += `<tr>
                                      <td>${obj[i]['id']}</td>
                                      <td>${obj[i]['clientId']}</td>
                                      <td>${obj[i]['customerId']}</td>
                                      <td>${obj[i]['channelId']}</td>
                                      <td>${obj[i]['channelOrderId']}</td>
                                      <td>${obj[i]['status']}</td>
                                      <td><button type='button' class='btn btn-info' onclick=viewOrder(${obj[i]['id']})>VIEW</button></td>`;


                if (obj[i]['status'] == "CREATED") {
                    str += `<td><button type='button' class='btn btn-warning' onclick=allocateOrder(${obj[i]['id']})>ALLOCATED</button></td>
                    </tr>`
                }
                else if (obj[i]['status'] == "ALLOCATED") {
                    str += `<td><button type='button' class='btn btn-success' onclick=fullfillOrder(${obj[i]['id']})>FULFILL</button></td>
                    </tr>`
                } else {
                    str += `<td><button type='button' class='btn btn-primary' onclick=getInvoice(${obj[i]['id']})>INVOICE</button></td>
                    </tr>`
                }
            }
            body.innerHTML = str;
            checkNextPageNotExist()
        }
        else {
            console.log("cannot fetch order");
        }
    }

    xhr.send();
}

function nextPage() {
    pageNumber += 1;
    console.log(document.getElementById("page"))
    document.getElementById("page").innerText = pageNumber + 1;
    loadOrder()

}

function prevPage() {
    pageNumber -= 1;
    document.getElementById("page").innerText = pageNumber + 1;
    loadOrder();
    if (pageNumber == 0) {
        document.getElementById("prevLi").className = "page-item disabled";
        document.getElementById("nextLi").className = "page-item";
    }
}

function checkNextPageNotExist() {
    // Instantiate an new XHR Object
    const xhr = new XMLHttpRequest();

    // Open an obejct (GET/POST, PATH,
    // ASYN-TRUE/FALSE)
    xhr.open("GET", `http://localhost:9000/assure/orders?pageNumber=${pageNumber + 1}`, true);
    // When response is ready
    xhr.onload = function () {
        if (this.status === 200) {
            // Changing string data into JSON Object
            obj = JSON.parse(this.responseText);
            console.log(obj, obj.length === 0);
            if (obj.length === 0) {
                document.getElementById("prevLi").className = "page-item";
                document.getElementById("nextLi").className = "page-item disabled";
                console.log("in next check")
            } else {
                document.getElementById("prevLi").className = "page-item";
            }

        }
        else {
            console.log("cannot fetch order");
        }
    }
    xhr.send();
}

function allocateOrder(id) {
    $.ajax({
        type: "PATCH",
        contentType: 'application/json',
        url: `http://localhost:9000/assure/orders`,
        data: JSON.stringify({
            "orderId": id,
            "updateStatusTo": "ALLOCATED"
        }),
        processData: false,
        dataType: 'json',
        success: function (result) {
            console.log(result, "order allocated")
            $.notify(`Order ${id} allocated`, "success");
            $('#uploadModal').modal('hide');
            loadOrder();
        },
        error: function (xhr, status, error) {
            console.log(status, error, xhr)

            if (xhr['responseJSON']['errorType'] === 0) {
                $.notify(xhr['responseJSON']['description']);
                return;
            }
            $.notify("Error occurred download error list file");
            $('#errorCsv').click(function () {
                writeFileData(xhr['responseJSON']['description'], "error");
            });
        }
    });
}

function fullfillOrder(id) {
    $.ajax({
        type: "PATCH",
        contentType: 'application/json',
        url: `http://localhost:9000/assure/orders`,
        data: JSON.stringify({
            "orderId": id,
            "updateStatusTo": "FULFILLED"
        }),
        processData: false,
        dataType: 'json',
        success: function (result) {
            console.log(result, "order fulfilled")
            $.notify(`Order ${id} fulfilled`, "success");
            $('#uploadModal').modal('hide');
            loadOrder();
            getInvoice(id);
        },
        error: function (xhr, status, error) {
            console.log(status, error, xhr)
            if (xhr['responseJSON']['errorType'] === 0) {
                $.notify(xhr['responseJSON']['description']);
                return;
            }
            $.notify("Error occurred download error list file");
            $('#errorCsv').click(function () {
                writeFileData(xhr['responseJSON']['description'], "error");
            });
        }
    });
}

function getInvoice(id) {
    $.ajax({
        contentType: 'application/json', xhr: function () {
            const xhr = new XMLHttpRequest();
            xhr.responseType = 'blob'
            return xhr;
        },
        success: function (data) {
            // data.append("pdf", blob, "invoice.pdf");
            console.log(data)
            var file = new Blob([data], { type: 'application/octet-stream' },);
            file.name = "invoice.pdf"
            var fileURL = URL.createObjectURL(file);
            setTimeout(() => {
                window.open(fileURL, '_blank');
            })
            console.log(fileURL);
        },
        error: function (e) {
            console.log(e)
            return ""

        },
        processData: false,
        type: 'GET',
        url: `http://localhost:9000/assure/orders/${id}/get-invoice`
    });
}

function addOrder() {
    obj = JSON.parse(localStorage.getItem("orderItems"));
    console.log(obj);
    console.log(obj, "inLoadfuntion");
    $('#orderItemModal').modal('show');
    $('#modalTitle').text(`Add order`);
    $('#orderDetails').html(getOrderDetailModal());
    $('#orderItemModal-body').html(getAddOrderModalBody());
    loadOrderItemCart()
}

function showOrderModal(){
    loadOrderItemCart();
    $('#orderItemModal').modal('show');
}

function loadOrderItemCart(){
    let body = document.getElementById("orderItemTbody");

    str = ""
    for (var i = 0; i < obj.length; i++) {
        str += `<tr>
                                  <td>${obj[i]['clientSkuId']}</td>
                                  <td>${obj[i]['quantity']}</td>
                                  <td>${obj[i]['sellingPricePerUnit']}</td>
                                  <td><button type="button" class="btn btn-primary" onclick="editOrderItem(${i},'${obj[i]['clientSkuId']}',${obj[i]['quantity']},${obj[i]['sellingPricePerUnit']})">Edit</button</td>
                                  <td><button type="button" class="btn btn-primary" onclick="deleteOrderItem(${i})">Delete</button</td>
                                  </tr>`;
    }
    str += ` <div style="padding-top:1rem;padding-bottom:1rem;">
    <button type="button" class="btn btn-primary" onclick="addOrderItem()">Add</button>`;
    str += ` 
    <button type="button" class="btn btn-primary" onclick="placeOrder()">Place Order</button>
    <a class="" id="errorCsv" href="#" style="float: right;">Download Errors</a><div>`;
    body.innerHTML = str;
}
function placeOrder(){
    let channelOrderId = $('#channelOrderId').val();
    let clientId = $('#clientId').val();
    let customerId = $('#customerId').val();
    let orderItemFormList = JSON.parse(localStorage.getItem("orderItems"));
    //validation 
    if ( !Number.isInteger(parseFloat(clientId)) || parseInt(clientId) < 0) {
        $.notify("Enter valid client ID");
        return;
    }
    if ( !Number.isInteger(parseFloat(customerId)) || parseInt(customerId) < 0) {
        $.notify("Enter valid customer ID");
        return;
    }
    if(channelOrderId.trim().length==0){
        $.notify("Channel order ID cannot be blank ");
        return;
    }
    if(orderItemFormList.length==0){
        $.notify("Add Items");
        return;
    }

//ajax call;
$.ajax({
    type: "POST",
    contentType: 'application/json',
    url: `http://localhost:9000/assure/orders`,
    data: JSON.stringify({
        clientId : clientId,
        channelOrderId : channelOrderId,
        customerId : customerId,
        orderItemFormList : orderItemFormList
    }),
    processData: false,
    dataType: 'json',
    success: function (result) {
        console.log(result, "order placed");
        $.notify(`Order Placed`, "success");
        $('#orderItemModal').modal('hide');
        loadOrder();
    },
    error: function (xhr, status, error) {
        console.log(status, error, xhr)
        if (xhr['responseJSON']['errorType'] === 0) {
            $.notify(xhr['responseJSON']['description']);
            return;
        }
        $.notify("Error occurred download error list file");
        $('#errorCsv').click(function () {
            writeFileData(xhr['responseJSON']['description'], "error");
        });
    }
});

}

function addOrderItem(){
    $('#orderItemModal').modal('hide');
    $('#orderItemAddModal').modal('show');
    $('#orderItemModalbody').html(getAddOrderItem());
}

function editOrderItem(id, clientSkuId,qty,price){
    $('#orderItemModal').modal('hide');
    $('#orderItemAddModal').modal('show');
    $('#orderItemModalbody').html(getAddOrderItemEdit(id,clientSkuId,qty,price));
    
}
function saveAddEdit(id){
    //validations
      
    obj = JSON.parse(localStorage.getItem("orderItems"));
    clientSkuId = $('#clientSkuId').val();
    qty = $('#qty').val();
    price = $('#price').val();

    if ( !Number.isInteger(parseFloat(qty)) || parseInt(qty) < 0) {
        $.notify("Enter valid quantity");
        return;
    }

    
    if (isNaN(parseFloat(price)) || parseFloat(price) < 0) {
        $.notify("Enter valid Price per unit ");
        return;
    }

    if(clientSkuId.trim().length==0){
        $.notify("Client SKU ID cannot be blank ");
        return;
    }

    $('#orderItemModal').modal('show');
    $('#orderItemAddModal').modal('hide');

    obj[id] = {
        clientSkuId : clientSkuId,
        quantity : qty,
        sellingPricePerUnit : price
    }
    localStorage.setItem("orderItems", JSON.stringify(obj));
    showOrderModal();

}

function deleteOrderItem(id){
    obj = JSON.parse(localStorage.getItem("orderItems"));
    obj.splice(id,1);
    localStorage.setItem("orderItems", JSON.stringify(obj));
    showOrderModal();

}

function cancelAdd(){
    $('#orderItemModal').modal('show');
    $('#orderItemAddModal').modal('hide');

}

function saveAdd(){

    //validations
    obj = JSON.parse(localStorage.getItem("orderItems"));
    clientSkuId = $('#clientSkuId').val();
    qty = $('#qty').val();
    price = $('#price').val();

    if (!Number.isInteger(parseFloat(qty)) || parseInt(qty) < 0) {
        $.notify("Enter valid quantity");
        return;
    }
    
    if (isNaN(parseFloat(price)) || parseFloat(price) < 0) {
        $.notify("Enter valid Price per unit ");
        return;
    }
    if(clientSkuId.trim().length==0){
        $.notify("Client SKU ID cannot be blank ");
        return;
    }
    $('#orderItemModal').modal('show');
    $('#orderItemAddModal').modal('hide');

    obj.push({
        clientSkuId : clientSkuId,
        quantity : qty,
        sellingPricePerUnit : price
    });
    localStorage.setItem("orderItems", JSON.stringify(obj));
    showOrderModal();

}

function getAddOrderItem(){
    return ` <form >
    <div class="form-group ">
    <label for="clientSkuId">Client SKU ID</label>
    <input type="text" class="form-control" id="clientSkuId" name="clientSkuId" aria-describedby="text" placeholder="Enter Client SKU ID" autocomplete="off" minlength="1" maxlength="20" >
  </div> 
  <div class="form-group ">
  <label for="qty">Quantity</label>
  <input type="number" class="form-control" id="qty" name="qty" aria-describedby="text" placeholder="Enter Quantity" autocomplete="off" minlength="1" maxlength="20" >
</div>
 <div class="form-group">
<label for="price">Price Per Unit</label>
<input type="number" class="form-control" id="price" name="price" aria-describedby="text" placeholder="Enter Price Per Unit" autocomplete="off" minlength="1" maxlength="20" >
</div>
<div class="modal-footer">
        <button type="button" class="btn btn-primary" onclick= "saveAdd()">Save</button>
        <button type="button" class="btn btn-secondary" onclick="cancelAdd()" >Close</button>
      </div>
</form>`
}
function getAddOrderItemEdit(id,clientSkuId,qty,price){
    return ` <form >
    <div class="form-group ">
    <label for="clientSkuId">Client SKU ID</label>
    <input type="text" class="form-control" id="clientSkuId" name="clientSkuId" aria-describedby="text" placeholder="Enter Client SKU ID" autocomplete="off" minlength="1" maxlength="20" value="${clientSkuId}">
  </div> 
  <div class="form-group ">
  <label for="qty">Quantity</label>
  <input type="number" class="form-control" id="qty" name="qty" aria-describedby="text" placeholder="Enter Client ID" autocomplete="off" minlength="1" maxlength="20" value=${qty}>
</div>
 <div class="form-group">
<label for="price">Price Per Unit</label>
<input type="number" class="form-control" id="price" name="price" aria-describedby="text" placeholder="Enter Customer ID" autocomplete="off" minlength="1" maxlength="20" value="${price}">
</div>
<div class="modal-footer">
        <button type="button" class="btn btn-primary" onclick= "saveAddEdit(${id})">Save</button>
        <button type="button" class="btn btn-secondary" onclick="cancelAdd()" >Close</button>
      </div>
</form>`
}

function getAddOrderModalBody() {
    return `
    
    <div class="jumbotron" id="orderItemView">
    <table class="table">
      <thead class="thead-dark">
        <tr>
          <th scope="col">Client SKU ID</th>
          <th scope="col">Quantity</th>
          <th scope="col">Selling Price</th>
          <th scope="col">Edit</th>
          <th scope="col">Delete</th>
        </tr>
    </thead>

    <tbody id="orderItemTbody">
    </tbody>
</table>
</div>`
}

function getOrderDetailModal(){
    return `
    <form >
    <div class="form-group ">
    <label for="channelOrderId">Channel Order ID</label>
    <input type="text" class="form-control" id="channelOrderId" name="channelOrderId" aria-describedby="text" placeholder="Enter Channel Order ID" autocomplete="off" minlength="1" maxlength="20" >
  </div> 
  <div class="form-group ">
  <label for="clientId">Client ID</label>
  <input type="number" class="form-control" id="clientId" name="clientId" aria-describedby="text" placeholder="Enter Client ID" autocomplete="off" minlength="1" maxlength="20" >
</div>
 <div class="form-group">
<label for="customerId">Customer ID</label>
<input type="number" class="form-control" id="customerId" name="customerId" aria-describedby="text" placeholder="Enter Customer ID" autocomplete="off" minlength="1" maxlength="20" >
</div>
</form>
`
}

function viewOrder(orderId) {
    console.log("in view order")
    $.ajax({
        type: "GET",
        contentType: 'application/json',
        url: `http://localhost:9000/assure/orders/${orderId}/order-items`,

        processData: false,
        dataType: 'json',
        success: function (result) {
            obj = result;
            console.log(obj);
            console.log(obj, "inLoadfuntion");
            $('#orderItemModal').modal('show');
            $('#orderDetails').html('')
            $('#modalTitle').text(`Order Id - ${orderId}`);
            $('#orderItemModal-body').html(getOrderItemModalBody());
            let body = document.getElementById("orderItemTbody");

            str = ""
            for (var i = 0; i < obj.length; i++) {
                str += `<tr>
                                          <td>${obj[i]['id']}</td>
                                          <td>${obj[i]['clientSkuId']}</td>
                                          <td>${obj[i]['orderedQuantity']}</td>
                                          <td>${obj[i]['allocatedQuantity']}</td>
                                          <td>${obj[i]['fulfilledQuantity']}</td>
                                          <td>${obj[i]['sellingPricePerUnit']}</td>
                                          </tr>`;
            }
        
            body.innerHTML = str;
        },
        error: function (xhr, status, error) {
            console.log(status, error, xhr)

            if (xhr['responseJSON']['errorType'] === 0) {
                $.notify(xhr['responseJSON']['description']);
                return;
            }
            $.notify("Error occurred download error list file");
            $('#errorCsv').click(function () {
                writeFileData(xhr['responseJSON']['description'], "error");
            });
        }
    });

}
function getOrderItemModalBody() {
    return `<div class="jumbotron" id="orderItemView">
    <table class="table">
      <thead class="thead-dark">
        <tr>
          <th scope="col">ID</th>
          <th scope="col">Client SKU ID</th>
          <th scope="col">Ordered Quantity</th>
          <th scope="col">Allocated Quantity</th>
          <th scope="col">Fulfilled Quantity</th>
          <th scope="col">Selling Price</th>
        </tr>
    </thead>

    <tbody id="orderItemTbody">
    </tbody>
</table>
</div>`
}

function getOrderUpdateModal(globalSkuId, clientSkuId, name, brandId, mrp, description) {
    return `<form id="editOrderForm" >
    <div id="modalFormDataDiv">
      <div class="form-group">
        <label for="">Client SKU ID</label>
        <input type="text" class="form-control" id="clientSkuId" name="clientSkuId" aria-describedby="text" placeholder="Enter ClientSkuId" autocomplete="off" minlength="1" maxlength="20" value="${clientSkuId}">
      </div>
      <div class="form-group">
        <label for="">Name</label>
        <input type="text" class="form-control" id="name" name="name" aria-describedby="text" placeholder="Enter name" autocomplete="off" minlength="1" maxlength="20" value="${name}">
      </div>
      <div class="form-group">
        <label for="brandId">Brand ID</label>
        <input type="text" class="form-control" id="brandId" name="brandId" aria-describedby="text" placeholder="Enter brand ID" autocomplete="off" minlength="1" maxlength="20" value="${brandId}">
      </div>
      <div class="form-group">
        <label for="mrp">MRP</label>
        <input type="number" class="form-control" id="mrp" name="mrp" aria-describedby="text" placeholder="Enter MRP" autocomplete="off" minlength="1" value="${mrp}" max="1000000">
        </div>
      <div class="form-group">
        <label for="description">Description</label>
        <input type="text" class="form-control" id="description" name="description" aria-describedby="text" placeholder="Enter description" autocomplete="off" minlength="1" maxlength="40" value="${description}">
      </div>
    <div class="modal-footer">
      <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
      <button type="button" class="btn btn-primary" onclick="editOrderModalCall(${globalSkuId})">Save</button>
    </div>
  </form>`
}

function getUploadModalBody() {
    return `<form>
   
    <div class="form-group">
    <label for="partyId" class="form-label">Party Id</label>
    <input class="form-control" type="number" id="partyId" >
    </div>
  </div>
      <div class="form-group">
        <label for="formFile" class="form-label">Select csv file for upload</label>
        <input class="form-control" type="file" id="formFile" accept=".csv">
      </div>
      <div class="form-group">
        <a class="" href="/assure/static/orderTemplate.csv" download>Download Template</a>
        <a class="" id="errorCsv" href="#" style="float: right;">Download Errors</a>
      </div>

      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
        <button type="button" class="btn btn-primary" id="uploadModalBtn" onclick="uploadOrderFile()">Upload</button>
      </div>
    </div>

  </form>`
}
function writeFileData(arr, fname) {
    console.log(fname, " in write file data")
    var config = {
        quoteChar: '',
        escapeChar: '',
        delimiter: ","
    };

    var data = Papa.unparse(arr, config);
    var blob = new Blob([data], { type: 'text/csv;charset=utf-8;' });
    var fileUrl = null;

    if (navigator.msSaveBlob) {
        fileUrl = navigator.msSaveBlob(blob, `${fname}.csv`);
    } else {
        fileUrl = window.URL.createObjectURL(blob);
    }
    var tempLink = document.createElement('a');
    tempLink.href = fileUrl;
    tempLink.setAttribute('download', `${fname}.csv`);
    tempLink.click();
}

function writeFileData(arr, fname) {
    console.log(fname, " in write file data")
    var config = {
        quoteChar: '',
        escapeChar: '',
        delimiter: ","
    };

    var data = Papa.unparse(arr, config);
    var blob = new Blob([data], { type: 'text/csv;charset=utf-8;' });
    var fileUrl = null;

    if (navigator.msSaveBlob) {
        fileUrl = navigator.msSaveBlob(blob, `${fname}.csv`);
    } else {
        fileUrl = window.URL.createObjectURL(blob);
    }
    var tempLink = document.createElement('a');
    tempLink.href = fileUrl;
    tempLink.setAttribute('download', `${fname}.csv`);
    tempLink.click();
}

