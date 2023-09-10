<?php
    if (isset($_GET['filename']) && isset($_GET['boxNumber'])) {
        $filename = basename($_GET['filename']);  // sanitize filename
        $boxNumber = intval($_GET['boxNumber']);  // sanitize boxNumber to ensure it is an integer
        $filepath = '/var/www/html/freewaterhouse/library/uploaded_files/box' . $boxNumber . '/' . $filename;

        if (file_exists($filepath)) {
            $fileSize = filesize($filepath);  // Save filesize before file download
            $fileType = mime_content_type($filepath);  // Get MIME type


            header('Content-Description: File Transfer');
            header('Content-Type: application/octet-stream');
            header('Content-Disposition: attachment; filename="' . basename($filepath) . '"');
            header('Expires: 0');
            header('Cache-Control: must-revalidate');
            header('Pragma: public');
            header('Content-Length: ' . filesize($filepath));

            readfile($filepath);

            // Log the download
            logToFile('File downloaded: ' . $filename . ', size: ' . filesize($filepath) . ', type: ' . $fileType);

            // Remove the file after downloading
            unlink($filepath);
            
            exit();
        }
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
