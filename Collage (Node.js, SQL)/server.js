const express = require('express');
const app = express();
const port = 3000;
const fs = require('fs');
const http = require('http');
  
const WebSocket = require('ws');
const sqlite3 = require('sqlite3').verbose();
const path = require('path');

const multer = require('multer');
const sizeOf = require('image-size');

// These are for the weekly screenshot and canvas clear
const puppeteer = require('puppeteer');
const cron = require('node-cron');

cron.schedule('0 0 * * SUN', async function() {
    console.log('Running weekly tasks...');
    await captureAndSaveScreenshot();
    handleDeleteAllEvent();
});

async function captureAndSaveScreenshot() {
    console.log('inside capture and save');
    
    let browser;
    try {
        const browser = await puppeteer.launch({
            executablePath: '/usr/bin/chromium-browser'
        });
        console.log('after browser');

        const page = await browser.newPage();
        await page.goto('http://localhost:3000/collage');

        // Wait for the element to be rendered
        const imageAreaElement = await page.waitForSelector('#imageArea');

        // Get bounding box of the imageArea element
        const boundingBox = await imageAreaElement.boundingBox();

        const screenshotPath = path.join(__dirname, 'public', 'saved_screenshots', `screenshot_${Date.now()}.png`);
    
        // Capture screenshot of the bounding box of the imageArea
        await page.screenshot({ 
            path: screenshotPath,
            clip: {
                x: boundingBox.x,
                y: boundingBox.y,
                width: boundingBox.width,
                height: boundingBox.height
            }
        });

        console.log('Screenshot saved successfully at:', screenshotPath);
    } catch (error) {
        console.error('Error while capturing screenshot:', error);
    } finally {
        if (browser) {
            await browser.close();
        }
    }
}

// End weekly cleanup section



console.log(__dirname);

// MULTER STUFF

const storage = multer.diskStorage({
    destination: function (req, file, cb) {
        cb(null, './public/uploaded_images/')
    },
    filename: function (req, file, cb) {
        cb(null, file.originalname)
    }
});

const upload = multer({ storage: storage });

// END MULTER STUFF

// Define the route for '/collage'
app.get('/collage', function(req, res) {
    res.sendFile(path.join(__dirname, 'public', 'index.html')); 
});

// Expose the public folder
app.use('/collage', express.static(path.join(__dirname, 'public')));

let db;
let server;
let wss;

let currentlyConnectedUsersNum = 0;

const dbPromise = new Promise((resolve, reject) => {
    db = new sqlite3.Database('./images.db', (err) => {
        if (err) {
            console.error('Failed to connect to SQLite', err);
            reject(err);
        }
        console.log('Connected to the SQLite database.');
        resolve(db);
    });
});

const webSocketPromise = new Promise((resolve, reject) => {
    const server = http.createServer(app);
    wss = new WebSocket.Server({ server });

    wss.on('error', function error(err) {
        console.error('Failed to connect to WebSocket', err);
        reject(err);
    });

    server.listen(port, () => {
        console.log(`server is running, listing on port:${port}`);
        resolve(wss);  // Resolve the promise here
    });
});

Promise.all([dbPromise, webSocketPromise])
    .then(([db, wss]) => {
        console.log('Both SQLite3 and WebSocket connections have been established');

        // Endpoint to handle image uploads
        app.post('/collage/upload', upload.single('image'), (req, res) => {
            if (!req.file) {
                return res.status(400).send('No file uploaded.');
            }

            const imageFile = req.file.filename;

            // Get dimensions of uploaded image
            const dimensions = sizeOf(req.file.path);
            console.log(dimensions);
            const originalWidth = dimensions.width;
            const originalHeight = dimensions.height;
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

            // Fetch the maximum zIndex currently in the database
            const zIndexQuery = `SELECT MAX(zIndex) as maxZIndex FROM images`;
            db.get(zIndexQuery, [], (err, row) => {
                if (err) {
                    return res.status(500).send('Failed to retrieve max zIndex from database.');
                }
                
                // Compute the next zIndex
                const zIndex = (row && row.maxZIndex != null) ? row.maxZIndex + 1 : 0;

                // Insert into SQLite
                const sql = `INSERT INTO images(id, x, y, width, height, zIndex) VALUES(?, ?, ?, ?, ?, ?)`;
                db.run(sql, [imageFile, 0, 0, newWidth, newHeight, zIndex], function(err) {
                    if (err) {
                        return res.status(500).send('Failed to add image to database.');
                    }
                    res.status(200).send('Image uploaded and added to database.');
                });
            });
        });


        wss.on('connection', function connection(ws) {
            console.log('A new client connected!');

            // Increment the number of connected users and inform everyone on the socket, including self
            currentlyConnectedUsersNum++;
            wss.clients.forEach(function each(client) {
                // DON'T exclude the client that made the request
                if (client.readyState === WebSocket.OPEN) {
                    client.send(JSON.stringify({
                        type: 'updateCurrentlyConnectedUsersNum',
                        num: currentlyConnectedUsersNum,
                    }));
                }
            });

            db.all('SELECT id FROM images', [], (err, rows) => {
                if (err) {
                    throw err;
                }
                // rows will contain an array of results, e.g. [{id: 'Basketball.png'}, {id: 'coins.png'}]
    
                const imageNames = rows.map(row => row.id);
                console.log(imageNames);
    
                // Send image names only to this client
                ws.send(JSON.stringify({
                    type: 'initialImageNames',
                    images: imageNames
                }));
    
            });

            ws.on('message', function incoming(message) {
                //console.log('received: %s', message);
                
                const data = JSON.parse(message);
                
                if (data.type === "getInitialPositionAndSize") {
                    console.log("enter initial pos and size on server");
                    db.each('SELECT id, x, y, width, height, zIndex FROM images', (err, row) => {
                        if (err) {
                            throw err;
                        }

                        ws.send(JSON.stringify({
                            type: 'updateInitialPositionAndSize',
                            id: row.id,
                            x: row.x,
                            y: row.y,
                            width: row.width,
                            height: row.height,
                            zIndex: row.zIndex
                        }));
                    });
                } else if (data.type === "updatePositionOnSocketDragging") {
                    //console.log('--entering the updatePositionOnSocket block');

                    wss.clients.forEach(function each(client) {
                        // Exclude the client that made the request
                        if (client !== ws && client.readyState === WebSocket.OPEN) {
                            client.send(JSON.stringify({
                                type: 'updatePositionOnServerDragging',
                                id: data.id,
                                x: data.x,
                                y: data.y
                            }));
                        }
                    });
                } else if (data.type === "updateSizeOnSocketResizing") {
                    //console.log("server got updateSizeOnSocketResizing")
                    wss.clients.forEach(function each(client) {
                        // Exclude the client that made the request
                        if (client !== ws && client.readyState === WebSocket.OPEN) {
                            client.send(JSON.stringify({
                                type: 'updateSizeOnServerResizing',
                                id: data.id,
                                x: data.x,
                                y: data.y,
                                width: data.width,
                                height: data.height  
                            }));
                        }
                    });
                } else if (data.type === "broadcastFinalPosition") {
                    wss.clients.forEach(function each(client) {
                        // Exclude the client that made the request
                        if (client !== ws && client.readyState === WebSocket.OPEN) {
                            client.send(JSON.stringify({
                                type: 'updatePositionOnServerDragging',
                                id: data.id,
                                x: data.x,
                                y: data.y
                            }));
                        }
                    });
                } else if (data.type === "updatePositionInDatabase") {
                    let sql = `UPDATE images SET x = ?, y = ? WHERE id = ?`;
                    db.run(sql, [data.x, data.y, data.id], function(err) {
                        if (err) {
                            return console.error(err.message);
                        }
                        console.log(`Position updated for id: ${data.id}`);
                    });
                } else if (data.type === "updateSizeInDatabase") {
                    let sql = `UPDATE images SET x = ?, y = ?, width = ?, height = ? WHERE id = ?`;
                    db.run(sql, [data.x, data.y, data.width, data.height, data.id], function(err) {
                        if (err) {
                            return console.error(err.message);
                        }
                        console.log(`Size (and position) updated for id: ${data.id}`);
                    });
                } else if (data.type === "broadcastFinalSize") {
                    wss.clients.forEach(function each(client) {
                        // Exclude the client that made the request
                        if (client !== ws && client.readyState === WebSocket.OPEN) {
                            client.send(JSON.stringify({
                                type: 'updateSizeOnServerResizing',
                                id: data.id,
                                x: data.x,
                                y: data.y,
                                width: data.width,
                                height: data.height  
                            }));
                        }
                    });
                } else if (data.type === "newImageUploaded") {
                    wss.clients.forEach(function each(client) {
                        // Exclude the client that made the request
                        if (client !== ws && client.readyState === WebSocket.OPEN) {
                            client.send(JSON.stringify({
                                type: 'updateNewImageOnSocket',
                                imageName: data.imageName,
                                imageWidth: data.imageWidth,
                                imageHeight: data.imageHeight,
                                imageZIndex: data.zIndex
                            }));
                        }
                    });
                } else if (data.type === "deleteImage") {
                    // Send the delete instruction to all users on socket
                    wss.clients.forEach(function each(client) {
                        // Exclude the client that made the request
                        if (client !== ws && client.readyState === WebSocket.OPEN) {
                            client.send(JSON.stringify({
                                type: 'deleteImageOnSocket',
                                id: data.id
                            }));
                        }
                    });
                    
                    // Get the zIndex of the image that's going to be deleted
                    let fetchZIndex = `SELECT zIndex FROM images WHERE id = ?`;
                    db.get(fetchZIndex, [data.id], (err, row) => {
                        if (err) {
                            return console.error(err.message);
                        }
                        
                        const zIndexToDelete = row.zIndex;
                
                        // Delete image from database
                        let sql = `DELETE FROM images WHERE id = ?`;
                        db.run(sql, [data.id], function(err) {
                            if (err) {
                                return console.error(err.message);
                            }
                            console.log(`Image deleted from database for id: ${data.id}`);
                            
                            // Adjust the zIndex for images that had a higher zIndex than the deleted image
                            let adjustZIndex = `UPDATE images SET zIndex = zIndex - 1 WHERE zIndex > ?`;
                            db.run(adjustZIndex, [zIndexToDelete], function(err) {
                                if (err) {
                                    return console.error(`Failed to adjust zIndex after deletion. Error: ${err.message}`);
                                }
                                console.log(`zIndex adjusted for images after deletion of image with zIndex: ${zIndexToDelete}`);
                            });
                        });
                
                        // Delete image from server
                        const imagePath = path.join(__dirname, 'public', 'uploaded_images', data.id); 
                
                        // Delete the image from the server
                        fs.unlink(imagePath, (err) => {
                            if (err) {
                                console.error(`Error deleting the image with id: ${data.id}. Error: ${err.message}`);
                            } else {
                                console.log(`Image with id: ${data.id} successfully deleted from the server.`);
                            }
                        });
                    });
                } else if (data.type === "sendToFrontEvent") {
                    // Get the original zIndex of the image
                    let originalZIndex;
                    db.get("SELECT zIndex FROM images WHERE id = ?", [data.id], (err, row) => {
                        if (err) {
                            console.error(err.message);
                            return;
                        }
                        originalZIndex = row.zIndex;
                
                        // Get the maximum zIndex in the database
                        db.get("SELECT MAX(zIndex) AS maxZIndex FROM images", [], (err, row) => {
                            if (err) {
                                console.error(err.message);
                                return;
                            }
                            const maxZIndex = row.maxZIndex;
                
                            // Set the zIndex of the image sent to the front to maxZIndex
                            db.run("UPDATE images SET zIndex = ? WHERE id = ?", [maxZIndex, data.id], (err) => {
                                if (err) {
                                    console.error(err.message);
                                    return;
                                }
                
                                // Decrease the zIndex of images that were originally above the selected image by 1
                                db.run("UPDATE images SET zIndex = zIndex - 1 WHERE zIndex > ? AND id != ?", [originalZIndex, data.id], (err) => {
                                    if (err) {
                                        console.error(err.message);
                                    }
                                });
                            });
                        });
                    });
                    // Send the sendToFrontEvent to all users on socket
                    wss.clients.forEach(function each(client) {
                        // Exclude the client that made the request
                        if (client !== ws && client.readyState === WebSocket.OPEN) {
                            client.send(JSON.stringify({
                                type: 'sendToFrontEventOnSocket',
                                id: data.id
                            }));
                        }
                    });

                } else if (data.type === "sendToBackEvent") {
                    // Get the original zIndex of the image
                    let originalZIndex;
                    db.get("SELECT zIndex FROM images WHERE id = ?", [data.id], (err, row) => {
                        if (err) {
                            console.error(err.message);
                            return;
                        }
                        originalZIndex = row.zIndex;
                
                        // Set the zIndex of the image sent to the back to 0
                        db.run("UPDATE images SET zIndex = ? WHERE id = ?", [0, data.id], (err) => {
                            if (err) {
                                console.error(err.message);
                                return;
                            }
                
                            // Increase the zIndex of images that were originally below the selected image by 1
                            db.run("UPDATE images SET zIndex = zIndex + 1 WHERE zIndex < ? AND id != ?", [originalZIndex, data.id], (err) => {
                                if (err) {
                                    console.error(err.message);
                                }
                            });     
                        });
                    });
                    // Send the sendToFrontEvent to all users on socket
                    wss.clients.forEach(function each(client) {
                        // Exclude the client that made the request
                        if (client !== ws && client.readyState === WebSocket.OPEN) {
                            client.send(JSON.stringify({
                                type: 'sendToBackEventOnSocket',
                                id: data.id
                            }));
                        }
                    });

                } else if (data.type === "deleteAllEvent") {
                    handleDeleteAllEvent();
                }   
                    
            });

            ws.on('close', () => {
                console.log('Client disconnected');

                // Decrement the number of connected users and inform everyone on the socket, including self
                currentlyConnectedUsersNum--;
                wss.clients.forEach(function each(client) {
                    // DON'T exclude the client that made the request
                    if (client.readyState === WebSocket.OPEN) {
                        client.send(JSON.stringify({
                            type: 'updateCurrentlyConnectedUsersNum',
                            num: currentlyConnectedUsersNum,
                        }));
                    }
                });
            });
            
        });
    })
    .catch(err => {
        console.error('Failed to connect to either SQLite3 or WebSocket', err);
        process.exit(1);  // This will stop the server in case of a connection error
    });

// It's ok for this to be here since it's a function declaration, 
// not a function expression. 
function handleDeleteAllEvent() {
    // Remove everything from the database
    db.run("DELETE FROM images", (err) => {
        if (err) {
            console.error(err.message);
            return;
        }

        // Delete all files from the 'uploaded_images' directory
        const directoryPath = path.join(__dirname, 'public', 'uploaded_images');

        fs.readdir(directoryPath, (err, files) => {
            if (err) {
                console.error(`Error reading the directory: ${err.message}`);
                return;
            }

            // Loop through and delete each file
            files.forEach(file => {
                const filePath = path.join(directoryPath, file);
                fs.unlink(filePath, (err) => {
                    if (err) {
                        console.error(`Error deleting the file: ${file}. Error: ${err.message}`);
                    } else {
                        console.log(`File: ${file} successfully deleted from the server.`);
                    }
                });
            });
        });
    });

    // Inform everyone on the socket
    wss.clients.forEach(function each(client) {
        // DON'T exclude the client that made the request
        if (client.readyState === WebSocket.OPEN) {
            client.send(JSON.stringify({
                type: 'deleteAllEventOnSocket',
            }));
        }
    });
}

    







