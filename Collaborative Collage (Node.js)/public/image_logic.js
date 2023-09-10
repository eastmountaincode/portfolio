// Global variables

let shouldSendUpdateMove = true;
let shouldSendUpdateResize = true;

let selectedImage = null;
const deleteButton = document.getElementById('deleteButton');
const confirmDeleteButton = document.getElementById('confirmDelete');
const cancelDeleteButton = document.getElementById('cancelDelete');
const confirmText = document.getElementById('confirmText');
const imageArea = document.getElementById("imageArea");

const sendToFrontButton = document.getElementById('sendToFrontButton')
const sendToBackButton = document.getElementById('sendToBackButton')

const deleteAllButton = document.getElementById('deleteAllButton');
const confirmDeleteAllButton = document.getElementById('confirmDeleteAllButton');
const cancelDeleteAllButton = document.getElementById('cancelDeleteAllButton');
const confirmDeleteAllText = document.getElementById('confirmDeleteAllText');

const downloadScreenshotButton = document.getElementById('downloadScreenshotButton');

const currentlyConnectedUsersText = document.getElementById('currentlyConnectedUsersText');


let zIndexLedger = {};

// Create WebSocket connection.
const socket = new WebSocket('wss://freewaterhouse.com/ws');
 
// Connection opened
socket.addEventListener("open", (event) => {
    console.log("Connected to websocket server");

    socket.send(JSON.stringify({
        type: 'getInitialPositionAndSize'
    }));
});

// Listen for messages
// This is if we get a message from the server...
socket.addEventListener("message", (event) => {
    console.log("Received from server: ", event.data); 
    const data = JSON.parse(event.data);

    if (data.type === 'initialImageNames') {
        console.log('entered initial names');
        
        // Loop through each image name and create an img element
        data.images.forEach(imageName => {
            const imgElement = document.createElement("img");
            imgElement.src = `/collage/uploaded_images/${imageName}`;
            imgElement.alt = imageName;
            imgElement.id = imageName;

            // Append to the image area or any container you want
            imageArea.appendChild(imgElement);
        });
    } else if (data.type === 'updateInitialPositionAndSize') {
        console.log("updating initial position and size for", data.id);
        let image = document.getElementById(data.id);
        image.style.left = data.x + 'px';
        image.style.top = data.y + 'px';

        image.style.width = data.width + 'px';
        image.style.height = data.height + 'px';

        image.setAttribute("data-x", data.x);
        image.setAttribute("data-y", data.y);

        image.style.zIndex = data.zIndex;
        zIndexLedger[data.id] = data.zIndex;

    } else if (data.type === 'updatePositionOnServerDragging') {
        let image = document.getElementById(data.id);
        //console.log("Moving image to position: ", data.position);
        image.style.left = data.x + 'px';
        image.style.top = data.y + 'px';
        image.setAttribute("data-x", data.x);
        image.setAttribute("data-y", data.y);
    } else if (data.type === "updateSizeOnServerResizing") {
        let image = document.getElementById(data.id);
        image.style.width = data.width + 'px';
        image.style.height = data.height + 'px';

        image.style.left = data.x + 'px';
        image.style.top = data.y + 'px';
        image.setAttribute('data-x', data.x);
        image.setAttribute('data-y', data.y);
    } else if (data.type === "updateNewImageOnSocket") {
        const imageName = data.imageName;
        const imgElement = document.createElement("img");
        imgElement.src = `/collage/uploaded_images/${imageName}`;
        imgElement.alt = imageName;
        imgElement.id = imageName;

        imgElement.onload = function() {
            const originalWidth = this.naturalWidth;
            const originalHeight = this.naturalHeight;
            let newWidth, newHeight;

            if (originalWidth > originalHeight) {
                newWidth = 150;
                newHeight = (originalHeight / originalWidth) * 150;
            } else if (originalWidth < originalHeight) {
                newHeight = 150;
                newWidth = (originalWidth / originalHeight) * 150;
            } else {
                newWidth = 150;
                newHeight = 150;
            }

            this.style.width = newWidth + 'px';
            this.style.height = newHeight + 'px';

            this.style.left = 0 + 'px';
            this.style.top = 0 + 'px';

            this.setAttribute("data-x", 0);
            this.setAttribute("data-y", 0);

            this.style.zIndex = data.imageZIndex
            zIndexLedger[data.imageName] = data.imageZIndex;

        };

        // Append to the image area or any container you want
        imageArea.appendChild(imgElement);

    } else if (data.type === "deleteImageOnSocket") {
        let image = document.getElementById(data.id);
        
        // Check if the image that's being deleted is currently selected
        if (selectedImage && selectedImage.id === data.id) {
            // Reset the selected image and button states
            selectedImage = null;
            deleteButton.disabled = true;

            confirmDeleteButton.disabled = true;
            cancelDeleteButton.disabled = true;
            sendToFrontButton.disalbed = true;
            sendToBackButton.disabled = true;

            confirmText.style.opacity = '0.3';
        }

        // Determine the z-index of the deleted image
        const deletedZIndex = zIndexLedger[data.id];
        
        // Update z-indices in the ledger and on the actual elements
        for (let id in zIndexLedger) {
            if (zIndexLedger[id] > deletedZIndex) {
                zIndexLedger[id] -= 1;
                
                // Also update the actual image's z-index
                let affectedImage = document.getElementById(id);
                if (affectedImage) {
                    affectedImage.style.zIndex = zIndexLedger[id];
                }
            }
        }
    
        // Remove the deleted image from the zIndexLedger
        delete zIndexLedger[data.id];
        
        // Remove the image from the DOM
        image.remove();

    } else if (data.type === "sendToFrontEventOnSocket") {
        const image = document.getElementById(data.id);
        if (image) {
            const currentMaxZIndex = Math.max(...Object.values(zIndexLedger));
            const originalZIndex = parseInt(image.style.zIndex) || 0;
            
            // Iterate through zIndexLedger and decrement the zIndex for images
            // that originally had a z-index greater than the selected image.
    
            // updating socket zIndexLedger
            for (const [imageId, zIndex] of Object.entries(zIndexLedger)) {
                if (zIndex > originalZIndex) {
                    const imageElem = document.getElementById(imageId);
                    // update socket image
                    imageElem.style.zIndex = zIndex - 1;
                    // update socket ledger
                    zIndexLedger[imageId] = zIndex - 1;
                }
            }
    
            // Update the zIndexLedger for the selected image
            zIndexLedger[image.id] = currentMaxZIndex;
            // Actually update the image
            image.style.zIndex = currentMaxZIndex;
        }

    } else if (data.type === "sendToBackEventOnSocket") {
        const image = document.getElementById(data.id);

        if (image) {
            const originalZIndex = parseInt(image.style.zIndex) || 0;
            
            // Iterate through zIndexLedger and increment the zIndex for images
            // that originally had a z-index below than the selected image.
    
            // updating socket zIndexLedger
            for (const [imageId, zIndex] of Object.entries(zIndexLedger)) {
                if (zIndex < originalZIndex) {
                    const imageElem = document.getElementById(imageId);
                    // update socket image
                    imageElem.style.zIndex = zIndex + 1;
                    // update socket ledger
                    zIndexLedger[imageId] = zIndex + 1;
                }
            }
    
            // Update the zIndexLedger for the selected image
            zIndexLedger[image.id] = 0;
            // Actually update the image
            image.style.zIndex = 0;
        }

    } else if (data.type === "deleteAllEventOnSocket") {
        // empty the local zIndexLedger
        zIndexLedger = {};
        
        // Select the imageArea div
        const imageArea = document.getElementById('imageArea');

        // Retrieve all img elements inside imageArea
        const images = imageArea.querySelectorAll('img');

        // Iterate over the images to get their IDs and remove them
        images.forEach(image => {
            // You can access the image's ID with image.id
            console.log(`Deleting image with ID: ${image.id}`);  // Optional: Just to log the IDs being deleted
            
            // Remove the image from the DOM
            imageArea.removeChild(image);
        });

        if (selectedImage) {
            // Reset the selected image and button states
            selectedImage = null;
            deleteButton.disabled = true;

            confirmDeleteButton.disabled = true;
            cancelDeleteButton.disabled = true;
            sendToFrontButton.disabled = true;
            sendToBackButton.disabled = true;
            
            confirmText.style.opacity = '0.3';
        }
        
    } else if (data.type === "updateCurrentlyConnectedUsersNum") {
        currentlyConnectedUsersText.textContent = `Currently connected users: ${data.num}`;
    } else {
        console.error('Received unknown message type: ', data.type);
    }
});

socket.addEventListener('error', (error) => {
    console.error('WebSocket Error:', error);
});

document.addEventListener("DOMContentLoaded", function() { 

    interact("#imageArea img")
    .draggable({
        inertia: false,
        restrict: {
          restriction: "parent",
          elementRect: { top: 0, left: 0, bottom: 1, right: 1 },
          endOnly: false,
        },
        autoScroll: true,
  
        // Event listeners for dragmove and dragend
        listeners: {
            move(event) {
                var x = (parseFloat(event.target.getAttribute("data-x")) || 0) + event.dx;
                var y = (parseFloat(event.target.getAttribute("data-y")) || 0) + event.dy;
  
                event.target.style.left = x + "px";
                event.target.style.top = y + "px";

                // Update the position attributes
                event.target.setAttribute("data-x", x);
                event.target.setAttribute("data-y", y);

                if (shouldSendUpdateMove) {
                    socket.send(JSON.stringify({
                        type: 'updatePositionOnSocketDragging',
                        id: event.target.id,
                        x: x,
                        y: y  
                    }));
                    shouldSendUpdateMove = false;
                    setTimeout(() => {
                        shouldSendUpdateMove = true;
                    }, 25);
                }
            },
            end(event) {
                const target = event.target;
                const id = target.id;
                const x = parseFloat(target.getAttribute("data-x")) || 0;
                const y = parseFloat(target.getAttribute("data-y")) || 0;

                socket.send(JSON.stringify({
                    type: 'updatePositionInDatabase',
                    id: id,
                    x: x,
                    y: y
                }));

                // Broadcast the final position to all clients to ensure sync
                socket.send(JSON.stringify({
                    type: 'broadcastFinalPosition',
                    id: id,
                    x: x,
                    y: y
                }));

          }
        }
    })
    .resizable({
        preserveAspectRatio: true,
        edges: { left: true, right: true, bottom: true, top: true },
        modifiers: [
            // // Maintain the aspect ratio.
            // interact.modifiers.aspectRatio({
            //     ratio: 'preserve', // Preserve the aspect ratio
            // }),
            // Restrict the size.
            interact.modifiers.restrictSize({
                min: { width: 25, height: 25 } // Minimum width and height
            }),
            // Restrict the edges.
            interact.modifiers.restrictEdges({
                outer: 'parent'
            })
        ],
        listeners: {
            move(event) {
                var target = event.target,
                    x = (parseFloat(target.getAttribute('data-x')) || 0),
                    y = (parseFloat(target.getAttribute('data-y')) || 0);

                target.style.width = event.rect.width + 'px';
                target.style.height = event.rect.height + 'px';

                x += event.deltaRect.left;
                y += event.deltaRect.top;

                target.style.left = x + 'px';
                target.style.top = y + 'px';
                target.setAttribute('data-x', x);
                target.setAttribute('data-y', y);

                if (shouldSendUpdateResize) {
                    socket.send(JSON.stringify({
                        type: 'updateSizeOnSocketResizing',
                        id: event.target.id,
                        x: x,
                        y: y,
                        width: event.rect.width,
                        height: event.rect.height  
                    }));
                    shouldSendUpdateResize = false;
                    setTimeout(() => {
                        shouldSendUpdateResize = true;
                    }, 25);
                }
            },
            end(event) {
                const target = event.target;
                const id = target.id;
                const width = event.rect.width;
                const height = event.rect.height;
                
                x = (parseFloat(target.getAttribute('data-x')) || 0),
                y = (parseFloat(target.getAttribute('data-y')) || 0);

                x += event.deltaRect.left;
                y += event.deltaRect.top;

                socket.send(JSON.stringify({
                    type: 'updateSizeInDatabase',
                    id: id,
                    x: x,
                    y: y,
                    width: width,
                    height: height

                }));

                // Broadcast the final size to all clients to ensure sync
                socket.send(JSON.stringify({
                    type: 'broadcastFinalSize',
                    id: id,
                    x: x,
                    y: y,
                    width: width,
                    height: height
                }));

            }
        }
    })
    .on('tap', function(event) {
        if (selectedImage) {
            selectedImage.classList.remove('selected');
        }
        event.currentTarget.classList.add('selected');
        selectedImage = event.currentTarget;

        deleteButton.disabled = false; // enable the delete button on image selection
        sendToFrontButton.disabled = false;
        sendToBackButton.disabled = false;

        event.preventDefault();
    });

    // Delete button clicked
    deleteButton.addEventListener('click', function() {
        // this is a bit redundant since the button will only be
        // enabled in the first place if an image is selected, but
        // here we are
        if (selectedImage) {
            confirmDeleteButton.disabled = false;
            cancelDeleteButton.disabled = false;

            // Set opacity of the "Are you sure" text
            confirmText.style.opacity = '1.0';
        }
    });

    // Confirm deletion
    confirmDeleteButton.addEventListener('click', function() {
        if (selectedImage) {
            selectedImage.remove();
            // send deletion info to server...
            socket.send(JSON.stringify({
                type: 'deleteImage',
                id: selectedImage.id
            }));
        }

        // Determine the z-index of the deleted image
        const deletedZIndex = zIndexLedger[selectedImage.id];
        
        // Update z-indices in the ledger and on the actual elements
        for (let id in zIndexLedger) {
            if (zIndexLedger[id] > deletedZIndex) {
                zIndexLedger[id] -= 1;
                
                // Also update the actual image's z-index
                let affectedImage = document.getElementById(id);
                if (affectedImage) {
                    affectedImage.style.zIndex = zIndexLedger[id];
                }
            }
        }
    
        // Remove the deleted image from the zIndexLedger
        delete zIndexLedger[selectedImage.id];
        
        selectedImage = null;
        deleteButton.disabled = true;
        confirmDeleteButton.disabled = true;
        cancelDeleteButton.disabled = true;
        sendToFrontButton.disabled = true;
        sendToBackButton.disabled = true;

    });

    // Cancel deletion
    cancelDeleteButton.addEventListener('click', function() {
        confirmDeleteButton.disabled = true;
        cancelDeleteButton.disabled = true;

        confirmText.style.opacity = '0.3';
    });

    sendToFrontButton.addEventListener('click', function() {
        if (selectedImage) {
            console.log(zIndexLedger);
            const currentMaxZIndex = Math.max(...Object.values(zIndexLedger));
            const originalZIndex = parseInt(selectedImage.style.zIndex) || 0;
    
            // Iterate through zIndexLedger and decrement the zIndex for images
            // that originally had a z-index greater than the selected image.

            // updating local zIndexLedger
            for (const [imageId, zIndex] of Object.entries(zIndexLedger)) {
                if (zIndex > originalZIndex) {
                    const imageElem = document.getElementById(imageId);
                    // update local image
                    imageElem.style.zIndex = zIndex - 1;
                    // update local ledger
                    zIndexLedger[imageId] = zIndex - 1;
                }
            }

            // Update the zIndexLedger for the selected image
            zIndexLedger[selectedImage.id] = currentMaxZIndex;
            // Actually update the image
            selectedImage.style.zIndex = currentMaxZIndex;
    
            // Notify the server about the selected image's z-index change
            if (typeof socket !== 'undefined' && socket.readyState === WebSocket.OPEN) {
                socket.send(JSON.stringify({
                    type: 'sendToFrontEvent',
                    id: selectedImage.id,
                }));
            }
        }
    });

    sendToBackButton.addEventListener('click', function() {
        if (selectedImage) {
            const originalZIndex = parseInt(selectedImage.style.zIndex) || 0;
            
            // updating local zIndexLedger
            for (const [imageId, zIndex] of Object.entries(zIndexLedger)) {
                if (zIndex < originalZIndex) {
                    const imageElem = document.getElementById(imageId);
                    // update local image
                    imageElem.style.zIndex = zIndex + 1;
                    // update local ledger
                    zIndexLedger[imageId] = zIndex + 1;
                }
            }

            // Update the zIndexLedger for the selected image
            zIndexLedger[selectedImage.id] = 0;
            // Actually update the image
            selectedImage.style.zIndex = 0;

            // Notify the server about the selected image's z-index change
            if (typeof socket !== 'undefined' && socket.readyState === WebSocket.OPEN) {
                socket.send(JSON.stringify({
                    type: 'sendToBackEvent',
                    id: selectedImage.id,
                }));
            }
        }
    });

    deleteAllButton.addEventListener('click', function() {
        // Enable the confirmation buttons
        confirmDeleteAllButton.disabled = false;
        cancelDeleteAllButton.disabled = false;
    
        // Set opacity of the "Are you sure" text for deleting all images
        confirmDeleteAllText.style.opacity = '1.0';
    });

    cancelDeleteAllButton.addEventListener('click', function() {
        confirmDeleteAllButton.disabled = true;
        cancelDeleteAllButton.disabled = true;

        confirmDeleteAllText.style.opacity = '0.3';
    });

    confirmDeleteAllButton.addEventListener('click', function() {
        // empty the local zIndexLedger
        zIndexLedger = {};
        
        // Select the imageArea div
        const imageArea = document.getElementById('imageArea');

        // Retrieve all img elements inside imageArea
        const images = imageArea.querySelectorAll('img');

        // Iterate over the images to get their IDs and remove them
        images.forEach(image => {
            // You can access the image's ID with image.id
            console.log(`Deleting image with ID: ${image.id}`);  // Optional: Just to log the IDs being deleted
            
            // Remove the image from the DOM
            imageArea.removeChild(image);
        });

        // Notify the server about the selected image's z-index change
        if (typeof socket !== 'undefined' && socket.readyState === WebSocket.OPEN) {
            socket.send(JSON.stringify({
                type: 'deleteAllEvent'
            }));
        }

        if (selectedImage) {
            // Reset the selected image and button states
            selectedImage = null;
            deleteButton.disabled = true;
            confirmDeleteButton.disabled = true;
            cancelDeleteButton.disabled = true;
            sendToFrontButton.disalbed = true;
            sendToBackButton.disabled = true;
            confirmText.style.opacity = '0.3';
        }

        // Set the buttons back
        confirmDeleteAllButton.disabled = true;
        cancelDeleteAllButton.disabled = true;

        confirmDeleteAllText.style.opacity = '0.3';



    });

    downloadScreenshotButton.addEventListener('click', function() {
        var imageArea = document.getElementById('imageArea');
        
        // Store the original border style
        var originalBorderStyle = imageArea.style.border;
        
        // Modify the border style
        imageArea.style.border = 'none';
    
        html2canvas(imageArea).then(function(canvas) {
            var imgDataUrl = canvas.toDataURL();
            var a = document.createElement('a');
            a.href = imgDataUrl;
            a.download = 'screenshot.png';
            a.click();
    
            // Revert the border style back to its original state
            imageArea.style.border = originalBorderStyle;
        });
    });
    
});

document.addEventListener('click', function(event) {
    // Check if the clicked element is not an image inside the #imageArea
    if (!event.target.matches('#imageArea img') 
        && event.target !== selectedImage
        && !event.target.classList.contains('buttonCheck')) {

        // If there's a selected image, remove its 'selected' class
        if (selectedImage) {
            selectedImage.classList.remove('selected');
        }
        selectedImage = null; // Reset the selectedImage

        // If nothing is selected, then disable delete features
        deleteButton.disabled = true;
        confirmDeleteButton.disabled = true;
        cancelDeleteButton.disabled = true;
        sendToFrontButton.disabled = true;
        sendToBackButton.disabled = true;

        confirmText.style.opacity = '0.3';
    }
});

const modal = document.getElementById("disconnectModal");
const closeModal = document.querySelector(".close-btn");

socket.addEventListener("close", (event) => {
    console.log("WebSocket connection closed");
    modal.style.display = "block";

    // Get highest z-index
    const values = Object.values(zIndexLedger);
    const maxZIndex = values.length > 0 ? Math.max(...values) : undefined;

    modal.style.zIndex = maxZIndex + 1;

});

closeModal.addEventListener('click', function() {
    modal.style.display = "none";
});

document.getElementById("toggleButton").addEventListener("click", function(e) {
    e.preventDefault(); 
    var moreText = document.getElementById("more");
    var button = document.getElementById("toggleButton");
    var computedStyle = window.getComputedStyle(moreText);
    if (computedStyle.display === "none") {
        moreText.style.display = "block";
        button.innerText = "About / How To ⬆";
    } else {
        moreText.style.display = "none";
        button.innerText = "About / How To ⬇";
    }
});


