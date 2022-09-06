var pageNumber = 0
function loadChannelList() {

    const xhr = new XMLHttpRequest();

    xhr.open("GET", `http://localhost:9000/assure/channel-listings?pageNumber=${pageNumber}`, true);
 
    xhr.onload = function () {
        if (this.status === 200) {
          
            obj = JSON.parse(this.responseText);
            console.log(obj, "inLoadfuntion");
            
            let body = document.getElementById("binSkuTbody");

            str = ""
            for (var i = 0; i < obj.length; i++) {
                str += `<tr>
                                      <th scope="row">${obj[i]['channelId']}</th>
                                      <td>${obj[i]['channelSkuId']}</td>
                                      <td>${obj[i]['clientId']}</td>
                                      <td>${obj[i]['globalSkuId']}</td>
                                    </tr>`;
            }
            body.innerHTML = str;
            checkNextPageNotExist()
        }
        else {
            console.log("cannot fetch binSku");
        }
    }

    xhr.send();
}

function nextPage() {
    pageNumber += 1;
    console.log(document.getElementById("page"))
    document.getElementById("page").innerText = pageNumber + 1;
    loadChannelList()
}

function prevPage() {
    pageNumber -= 1;
    document.getElementById("page").innerText = pageNumber + 1;
    loadChannelList();
    if (pageNumber == 0) {
        document.getElementById("prevLi").className = "page-item disabled";
        document.getElementById("nextLi").className = "page-item";
    }
}

function checkNextPageNotExist() {

    const xhr = new XMLHttpRequest();
    xhr.open("GET", `http://localhost:9000/assure/channel-listings?pageNumber=${pageNumber + 1}`, true);
    // When response is ready
    xhr.onload = function () {
        if (this.status === 200) {
    
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
            console.log("cannot fetch channel listings");
        }
    }
    // At last send the request
    xhr.send();
}


function uploadChannelListFile() {
    let f = document.getElementById('formFile');
    if (f.files && f.files[0]) {
        console.log(f.files[0]['name'], f.files[0])
        var reader = new FileReader();
        reader.addEventListener('load', function (e) {
            let csvdata = e.target.result;
            channelListingUtil(csvdata);

        });
        reader.readAsBinaryString(f.files[0]);
    }
}

function uploadChannelList() {
    $('#uploadModal').modal('show');
    $('#uploadModalBody').html(getUploadModalBody());
}
function channelListingUtil(data) {
    let parsedata = [];
    let newLinebrk = data.split("\n");
    console.log(newLinebrk);
    head = newLinebrk[0].split(",");
    console.log(head)
    if (head[0] != "channelSkuId" || head[1] != "clientSkuId") {
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
        if (row.length != 2) {
            $.notify("Invalid format of csv");
            return;
        }

        if (row.includes('')) {
            $.notify("empty field in csv");
            return;
        }

        let jsonData = {};
        jsonData[head[0]] = row[0].toLowerCase().trim().toLowerCase();
        jsonData[head[1]] = row[1].toLowerCase().trim().toLowerCase();
        parsedata.push(jsonData);
    }
    console.table(parsedata);
    channelListingUploadCall(parsedata);

}

function channelListingUploadCall(parsedata) {
    let clientSkuId = $("#clientSkuId").val();
    if (isNaN(parseInt(clientSkuId)) || Number.isInteger(clientSkuId) || parseInt(clientSkuId) < 0) {
        $.notify("Enter valid clientSkuId");
        return;
    }

    
    let channelSkuId = $("#channelSkuId").val();
    if (isNaN(parseInt(channelSkuId)) || Number.isInteger(channelSkuId) || parseInt(channelSkuId) < 0) {
        $.notify("Enter valid channelSkuId");
        return;
    }

    $.ajax({
        type: "POST",
        contentType: 'application/json',
        url: `http://localhost:9000/assure/channel-listings`,
        data: JSON.stringify({
            clientSkuId: parseInt(clientSkuId),
            channelSkuId: parseInt(channelSkuId),
            channelListingFormList: parsedata
        }),
        processData: false,
        dataType: 'json',
        success: function (result) {
            $.notify("Channel Listings uploaded", "success");
            $('#uploadModal').modal('hide');
            loadChannelList();
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

function getUploadModalBody() {
    return `<form>
   
    <div class="form-group">
    <label for="clinetId" class="form-label">Client Id</label>
    <input class="form-control" type="number" id="clinetId" >
    </div>
    <div class="form-group">
    <label for="channelId" class="form-label">Channel Id</label>
    <input class="form-control" type="number" id="channelId" >
    </div>
  </div>
      <div class="form-group">
        <label for="formFile" class="form-label">Select csv file for upload</label>
        <input class="form-control" type="file" id="formFile" accept=".csv">
      </div>
      <div class="form-group">
        <a class="" href="/assure/static/channelListingTemplate.csv" download>Download Template</a>
        <a class="" id="errorCsv" href="#" style="float: right;">Download Errors</a>
      </div>

      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
        <button type="button" class="btn btn-primary" id="uploadModalBtn" onclick="uploadChannelListFile()">Upload</button>
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
