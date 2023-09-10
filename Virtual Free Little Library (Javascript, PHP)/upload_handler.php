<?php
    header('Content-Type: application/json');

    // define the getFileStatus function
    // returns a 0 or a 1
    function getFileStatus($boxNumber) {
        $upload_directory = '/var/www/html/freewaterhouse/library/uploaded_files/box' . $boxNumber . '/';
        $files = glob($upload_directory . "*");
        
        // If no files, return status 0, else return status 1
        if (empty($files)) {
            return json_encode(array("status" => 0));
        } else {
            $filepath = $files[0];
            $filename = basename($filepath);

            // Here we return the additional file information along with the status and filename
            $fileInfo = [
                'filename' => $filename,
                'filesize' => filesize($filepath), // Add file size
                'filetype' => pathinfo($filepath, PATHINFO_EXTENSION) // Add file type
            ];

            return json_encode(['status' => 1, 'file' => $fileInfo]);
        }
    }
    
    if (isset($_GET['checkFile'])) {
        $boxNumber = $_GET['boxNumber']; // Get the box number from the query parameter
        $boxNumber = intval($_GET['boxNumber']);
        echo getFileStatus($boxNumber);
        exit(); // This prevents the rest of the script from executing when checkFile is set
    }
    

    // Check if files have been uploaded using the 'attachments' form field
    if (isset($_FILES['attachments'])) {
        $boxNumber = intval($_POST['boxNumber']); // Get the box number from the query parameter
        //echo json_encode($_FILES);
         
        // Initialize an empty message array
        $msg = "";

        // Define the directory where files will be uploaded
        $upload_directory = '/var/www/html/freewaterhouse/library/uploaded_files/box' . $boxNumber . '/';

        // Get the base name of the uploaded file (removes any directory paths from the filename)
        $filename = basename($_FILES['attachments']['name'][0]);

        // Construct the full target path for the uploaded file
        $targetFile = $upload_directory . $filename;

        // Check if a file with the same name already exists in the upload directory
        if (file_exists($targetFile)) {
            // File already exists, so return a JSON response with a status code of 0 and an error message
            $msg = array("status" => 0, "msg" => "File already exists!");
        } else {
            // No existing file found, so attempt to upload the file

            // Read the content of the uploaded temporary file
            //$tmp_file_contents = file_get_contents($_FILES['attachments']['tmp_name'][0]);

            // Try to move the uploaded temporary file to the target path
            if (move_uploaded_file($_FILES['attachments']['tmp_name'][0], $targetFile)) {
                // File upload succeeded, so adjust permissions to allow deletion
                chmod($targetFile, 0777);

                // File upload succeeded, so return a JSON response with a status code of 1, a success message, and the file path
                $msg = array("status" => 1, "msg" => "File Has Been Uploaded", "path" => $targetFile);

                // Log the upload
                logToFile('File uploaded: ' . $filename . ', size: ' . $_FILES['attachments']['size'][0] . ', type: ' . $_FILES['attachments']['type'][0]);
                
            } else {
                // File upload failed, so return a JSON response with a status code of -2, an error message, and the PHP error
                $msg = array("status" => -2, "msg" => "File upload failed", "error" => error_get_last());
            }
        }
        
        // Convert the message array to a JSON string and print it
        echo json_encode($msg);
        
    } else {
        // No files were uploaded, so return a JSON response with a status code of -1 and an error message
        $msg = array("status" => -1, "msg" => "No file received.");
        echo json_encode($msg);
    }

    function logToFile($message) {
        // Define the name of the file
        $file = 'log.txt';
    
        // Define the date format
        $date = date('Y-m-d H:i:s');
    
        // Write the date, IP address and message to the log file
        file_put_contents($file, $date . ' - ' . $_SERVER['REMOTE_ADDR'] . ' - ' . $message . "\n", FILE_APPEND);
    }
?>
