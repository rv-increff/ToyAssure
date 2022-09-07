var pageNumber = 0
function loadParty() {
    // Instantiate an new XHR Object
    const xhr = new XMLHttpRequest();

    // Open an obejct (GET/POST, PATH,
    // ASYN-TRUE/FALSE)
    xhr.open("GET", `http://localhost:9000/assure/parties?pageNumber=${pageNumber}`, true);
    // When response is ready
    xhr.onload = function () {
        if (this.status === 200) {
            // Changing string data into JSON Object
            obj = JSON.parse(this.responseText);
            console.log(obj, "inLoadfuntion");
            // Getting the ul element
            let body = document.getElementById("partyTbody");

            str = ""
            for (var i = 0; i < obj.length; i++) {
                str += `<tr>
                                      <td>${obj[i]['name']}</td>
                                      <td>${obj[i]['type']}</td>
                                    </tr>`;
            }
            body.innerHTML = str;
            checkNextPageNotExist()
        }
        else {
            console.log("cannot fetch party");
        }
    }

    // At last send the request
    xhr.send();
}

function nextPage() {
    pageNumber += 1;
    console.log(document.getElementById("page"))
    document.getElementById("page").innerText = pageNumber + 1;
    loadParty()

}

function prevPage() {
    pageNumber -= 1;
    document.getElementById("page").innerText = pageNumber + 1;
    loadParty();
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
    xhr.open("GET", `http://localhost:9000/assure/parties?pageNumber=${pageNumber + 1}`, true);
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
            console.log("cannot fetch binSku");
        }
    }

    // At last send the request
    xhr.send();
}

function addParty(){
    $('#partyEditModal').modal('show');
    $('#modalTitle').text('Add Party');
    $('#partyModalbody').html(getPartyModal());
}

function addPartyCall(){
    let name = $('#name').val().trim().toLowerCase();
    let type = $('#type').val();

    $.ajax({
        type: "POST",
        contentType: 'application/json',
        url: `http://localhost:9000/assure/parties`,
        data: JSON.stringify([{
            name: name,
            type : type
        }]),
        processData: false,
        dataType: 'json',
        success: function (result) {
            console.log(result, "party added")
            $.notify("Party added", "success");
            $('#partyEditModal').modal('hide');
            loadParty() ;
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

function getPartyModal(){
    return `<form>
    <div>
    <div class="form-group">
    <label for="name" class="form-label">Name</label>
    <input class="form-control" type="text" id="name" >
  </div>
      <div class="form-group">
        <label for="formFile" class="form-label">Party Type</label>
        <select name="type" id="type">
        <option value="CLIENT" >CLIENT</option>
        <option value="CUSTOMER" >CUSTOMER</option>
        </select>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
        <button type="button" class="btn btn-primary" id="uploadModalBtn" onclick="addPartyCall()">Save</button>
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