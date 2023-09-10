
// Declare an array to store the selected files for each box
var selectedFiles = Array(7).fill(null); // Create an array of size 7 filled with nulls


var numBoxes = 4;

for (let i = 1; i <= numBoxes; i++) {
    document.getElementById(`fileSelect${i}`).addEventListener("change", function(e) {
        // Get the first selected file
        var file = e.target.files[0];

        // If there is no file, log a message and return
        if (!file) {
            console.log("No file chosen");
            return;
        }

        // Check file size
        if (file.size > 1610612736) { // 1.5GB in bytes
            document.getElementById(`error${i}`).innerText = 'Your file is too big.'
            return;
        } else {
            // If the file size is acceptable, clear any previous error messages
            document.getElementById(`error${i}`).innerText = '';
        }

        // Store the selected file in the selectedFiles array for the corresponding box
        selectedFiles[i] = file;

        // Enable the Upload button
        document.getElementById(`uploadButton${i}`).disabled = false;
    });
}

// Listen for a click on the Upload button
for (let i = 1; i <= numBoxes; i++) {
    document.getElementById(`uploadButton${i}`).addEventListener("click", function() {
        // Create new formData instance
        var formData = new FormData();

        // Add the selected file to the formData
        formData.append("attachments[]", selectedFiles[i]);

        // Add boxNumber to formData
        var boxNumber = `${i}`; // The current box number
        formData.append("boxNumber", boxNumber);

        // Create new XMLHttpRequest
        var xhr = new XMLHttpRequest();

        // Open the connection
        xhr.open('POST', 'upload_handler.php', true);

        // Set the upload progress event
        xhr.upload.addEventListener("progress", function(e) {
            if (e.lengthComputable) {
                // Calculate the percentage of upload completed
                var percentComplete = e.loaded / e.total * 100;
                document.getElementById(`uploadProgressBar${i}`).style.width = percentComplete + '%';
                document.getElementById(`progressPercent${i}`).textContent = percentComplete.toFixed(2) + '%';

                // After reaching 100%, display the success message, wait for 3 seconds and then reset the progress bar, percentage text and the success message
                if (percentComplete === 100) {
                    document.getElementById(`uploadSuccessMessage${i}`).textContent = 'Upload Successful';
                    setTimeout(function() {
                        document.getElementById(`uploadProgressBar${i}`).style.width = '0%';
                        document.getElementById(`progressPercent${i}`).textContent = '';
                        document.getElementById(`uploadSuccessMessage${i}`).textContent = '';
                    }, 2300);
                }
            }
        }, false);

        // Set the callback for when the request completes
        xhr.onload = function() {
            if (this.status == 200) {
                var data = JSON.parse(this.response);
                var status = data.status;
                var msg = data.msg;

                // The data object contains the data returned by the server.
                // If the status is 1, the upload was successful.
                if (status == 1) {
                    console.log('Upload was successful')
                } else {
                    // If the status is not 1, an error occurred, so display an error message.
                    document.getElementById(`error${i}`).innerText = `Error: status ${status}, message: ${msg}`;
                }

                // Reset the selected file and disable the Upload button again
                selectedFiles[i] = null;
                document.getElementById(`uploadButton${i}`).disabled = true;

                // Clear the input field
                document.getElementById(`fileSelect${i}`).value = "";

                // Call the checkFileStatus function after upload is done.
                checkFileStatus(`${i}`); // Replace with the desired box number
            } else {
                console.log('File upload failed');
                console.log(error);
            }
        };

        // Send the request with the formData
        xhr.send(formData);
    });
}


// Function to check file status and adjust visibility of the upload and download buttons
function checkFileStatus(boxNumber) {
    fetch(`upload_handler.php?checkFile=true&boxNumber=${boxNumber}`)
        .then(response => response.json())
        .then(data => {
            var status = data.status;

            var fileSelect = document.getElementById(`fileSelect${boxNumber}`);
            var uploadButton = document.getElementById(`uploadButton${boxNumber}`);
            var downloadButton = document.getElementById(`downloadButton${boxNumber}`);

            if (status == 0) {
                // No file exists, so enable the file selection and upload buttons and hide the download button
                fileSelect.disabled = false;

                // Only enable the Upload button if a file is selected
                uploadButton.disabled = selectedFiles[boxNumber] ? false : true;

                downloadButton.disabled = true;
                downloadButton.onclick = null; // Remove any onclick event
                // console.log(boxNumber);
                document.getElementById(`fileInfo${boxNumber}`).innerText = '';
            } else {
                var file = data.file;
                var fileInfo = `<b>File Name:</b> ${file.filename}<br><b>File Size:</b> ${formatBytes(file.filesize)}<br><b>File Type:</b> ${file.filetype}`;

                // A file exists, so disable the file selection and upload buttons and show the download button
                fileSelect.disabled = true;
                uploadButton.disabled = true;
                downloadButton.disabled = false;
                //console.log(file)
                //console.log(file.filename)
                downloadButton.onclick = function() { 
                    window.location.href = "download_handler.php?filename=" + file.filename + "&boxNumber=" + boxNumber;
                    // Clear the file information as it is being downloaded
                    // document.getElementById(`fileInfo${boxNumber}`).innerText = '';

                    // After initiating the download, wait for 3 seconds before rechecking the file status
                    setTimeout(function() {
                        checkFileStatus(boxNumber);
                    }, 3000);

                    // document.getElementById(`fileInfo${boxNumber}`).innerText = '';
                };

                document.getElementById(`fileInfo${boxNumber}`).innerHTML = fileInfo;

            }
        })
        .catch(error => console.error('Error:', error));
}

// Check the file status when the page loads
window.onload = function() {
    for (let i = 1; i <= numBoxes; i++) {
        checkFileStatus(i.toString());
        startChecking(i.toString());
    }
};

// Function to start checking file status every 3 seconds
function startChecking(boxNumber) {
    setInterval(function() {
        checkFileStatus(boxNumber);
    }, 3000); // 3000 milliseconds = 3 seconds
}

// Add a function to format bytes into a readable format
function formatBytes(bytes, decimals = 2) {
    if (bytes === 0) return '0 Bytes';

    const k = 1024;
    const dm = decimals < 0 ? 0 : decimals;
    const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];

    const i = Math.floor(Math.log(bytes) / Math.log(k));

    return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
}

document.getElementById("toggleButton").addEventListener("click", function(e) {
    e.preventDefault(); 
    var moreText = document.getElementById("more");
    var button = document.getElementById("toggleButton");
    var computedStyle = window.getComputedStyle(moreText);
    if (computedStyle.display === "none") {
        moreText.style.display = "block";
        button.innerText = "About⬆";
    } else {
        moreText.style.display = "none";
        button.innerText = "About⬇";
    }
});

// var moreText = document.getElementById("more");
// var observer = new MutationObserver(function(mutations) {
//     mutations.forEach(function(mutation) {
//         if (mutation.target.style.display === 'block') {
//             mutation.target.scrollIntoView({behavior: "smooth"});
//         }
//     });
// });
// observer.observe(moreText, { attributes: true });

  
  
  
  
  
  

