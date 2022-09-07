var pageNumber = 0
function loadProduct() {
    const xhr = new XMLHttpRequest();

    // Open an obejct (GET/POST, PATH,
    // ASYN-TRUE/FALSE)
    xhr.open("GET", `http://localhost:9000/assure/products?pageNumber=${pageNumber}`, true);
    // When response is ready
    xhr.onload = function () {
        if (this.status === 200) {
            // Changing string data into JSON Object
            obj = JSON.parse(this.responseText);
            console.log(obj, "inLoadfuntion");
            // Getting the ul element
            let body = document.getElementById("productTbody");

            str = ""
            for (var i = 0; i < obj.length; i++) {
                str += `<tr>
                                      <td>${obj[i]['globalSkuId']}</td>
                                      <td>${obj[i]['clientSkuId']}</td>
                                      <td>${obj[i]['clientId']}</td>
                                      <td>${obj[i]['name']}</td>
                                      <td>${obj[i]['brandId']}</td>
                                      <td>${obj[i]['mrp']}</td>
                                      <td>${obj[i]['description']}</td>
                                      <td><button type='button' class='btn btn-primary' onclick=editProductModal(${obj[i]['globalSkuId']},"${obj[i]['clientSkuId']}","${obj[i]['name']}","${obj[i]['brandId']}","${obj[i]['mrp']}","${obj[i]['description']}")>Edit</button></td>
                                    </tr>`;
            }
            body.innerHTML = str;
            checkNextPageNotExist()
        }
        else {
            console.log("cannot fetch product");
        }
    }

    // At last send the request
    xhr.send();
}

function nextPage() {
    pageNumber += 1;
    console.log(document.getElementById("page"))
    document.getElementById("page").innerText = pageNumber + 1;
    loadProduct()

}

function prevPage() {
    pageNumber -= 1;
    document.getElementById("page").innerText = pageNumber + 1;
    loadProduct();
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
    xhr.open("GET", `http://localhost:9000/assure/products?pageNumber=${pageNumber + 1}`, true);
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
            console.log("cannot fetch product");
        }
    }

    // At last send the request
    xhr.send();
}


function uploadProductFile() {
    let f = document.getElementById('formFile');
    if (f.files && f.files[0]) {
        console.log(f.files[0]['name'], f.files[0])
        var reader = new FileReader();
        reader.addEventListener('load', function (e) {
            let csvdata = e.target.result;
            uploadProductUtil(csvdata);

        });
        reader.readAsBinaryString(f.files[0]);
    }
}

function uploadProducts() {
    $('#uploadModal').modal('show');
    $('#uploadModalBody').html(getUploadModalBody());

}
function uploadProductUtil(data) {
    let parsedata = [];
    let newLinebrk = data.split("\n");
    console.log(newLinebrk);
    head = newLinebrk[0].split(",");
    console.log(head)
    if (head[0] != "clientSkuId" || head[1] != "name" || head[2] != "brandId" || head[3] != "mrp" || head[4] != "description") {
        $.notify("Invalid format of csv");
        return;
    }
    if (newLinebrk.length < 2) {
        $.notify("Empty file");
        return;
    }
    if (newLinebrk.length > 101) {
        $.notify("Number of rows should be less than 100");
        return;
    }
    for (let i = 1; i < newLinebrk.length; i++) {
        row = newLinebrk[i].split(",");
        console.log(row);
        if(row.length == 1)continue;
        if (row.length != 5) {
            $.notify("Invalid format of csv");
            return;
        }

        if (row.includes('')) {
            $.notify("empty field in csv");
            return;
        }

        let jsonData = {};
        jsonData[head[0]] = row[0].toLowerCase().trim();
        jsonData[head[1]] = row[1].toLowerCase().trim();
        jsonData[head[2]] = row[2].toLowerCase().trim();
        jsonData[head[3]] = row[3].toLowerCase().trim();
        jsonData[head[4]] = row[4].toLowerCase().trim();
        parsedata.push(jsonData);
    }
    console.table(parsedata);
    productUploadCall(parsedata);

}

function productUploadCall(parsedata) {
    let partyId = $("#partyId").val();
    if (isNaN(parseInt(partyId)) || ! Number.isInteger(partyId) || parseInt(partyId) < 0) {
        $.notify("Enter valid partyId");
        return;
    }

    $.ajax({
        type: "POST",
        contentType: 'application/json',
        url: `http://localhost:9000/assure/products?partyId=${partyId}`,
        data: JSON.stringify(parsedata),
        processData: false,
        dataType: 'json',
        success: function (result) {
            console.log(result, "uploaded product")
            $.notify("Products uploaded", "success");
            $('#uploadModal').modal('hide');
            loadProduct();
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

function editProductModal(globalSkuId, clientSkuId, name, brandId, mrp, description) {
    $('#productEditModal').modal('show');
    $('#modalTitle').text('Edit Product');
    $('#productModalbody').html(getProductUpdateModal(globalSkuId, clientSkuId, name, brandId, mrp, description));

}
function editProductModalCall(globalSkuId) {
    let clientSkuId = $('#clientSkuId').val(); 
    let name = $('#name').val(); 
    let brandId = $('#brandId').val(); 
    let mrp = $('#mrp').val(); 
    let description = $('#description').val(); 

    if (isNaN(parseFloat(mrp)) || parseFloat(mrp) < 0) {
        $.notify("Enter valid MRP");
        return;
    }
console.log({
    clientSkuId : clientSkuId,
    name : name,
    brandId : brandId,
    mrp : mrp,
    descrption : description
})
    $.ajax({
        type: "PUT",
        contentType: 'application/json',
        url: `http://localhost:9000/assure/products/${globalSkuId}`,
        data: JSON.stringify({
            clientSkuId : clientSkuId,
            name : name,
            brandId : brandId,
            mrp : mrp,
            description : description
        }),
        processData: false,
        dataType: 'json',
        success: function (result) {
            console.log(result, "updated product")
            $.notify("Product updated", "success");
            $('#productEditModal').modal('hide');
            loadProduct();
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

function getBinModalBody() {
    return `<form id="editProductForm" >
    <div id="modalFormDataDiv">
      <div class="form-group">
        <label for="brandInput">Number of Bins</label>
        <input type="number" class="form-control" id="numberOfBins" name="brand" aria-describedby="text" placeholder="Enter number of Bins" autocomplete="off" minlength="1">
      </div>
    <div class="modal-footer">
      <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
      <button type="button" class="btn btn-primary" onclick="generateBinsCall()">Generate</button>
    </div>
  </form>`
}

function getProductUpdateModal(globalSkuId, clientSkuId, name, brandId, mrp, description) {
    return `<form id="editProductForm" >
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
      <button type="button" class="btn btn-primary" onclick="editProductModalCall(${globalSkuId})">Save</button>
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
        <a class="" href="/assure/static/productTemplate.csv" download>Download Template</a>
        <a class="" id="errorCsv" href="#" style="float: right;">Download Errors</a>
      </div>

      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
        <button type="button" class="btn btn-primary" id="uploadModalBtn" onclick="uploadProductFile()">Upload</button>
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

