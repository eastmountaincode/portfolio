<?php
session_start();
if(isset($_GET['logout'])){    
    
    //Simple exit message 
    //$logout_message = "<div class='msgln'><span class='left-info'>User <b class='user-name-left'>". $_SESSION['name'] ."</b> has left the chat session.</span><br></div>";
    file_put_contents("chat_log.html", $logout_message, FILE_APPEND | LOCK_EX);
    
    session_destroy();
    header("Location: lending_library.php"); //Redirect the user 
}
if(isset($_POST['enter'])){
    if($_POST['name'] != ""){
        $_SESSION['name'] = stripslashes(htmlspecialchars($_POST['name']));
        echo json_encode(array('error' => false));
    } else {
        echo json_encode(array('error' => true, 'message' => 'Please type in a name'));
    }
    exit(); // Ensure no further output
}
function loginForm(){
    echo 
    '<div id="chat_loginform"> 
        <p>Enter a name to enter the chat...</p> 
        <form id="loginForm" action="lending_library.php" method="post"> 
        <label for="name">Name &mdash;</label> 
        <input type="text" name="name" id="name" /> 
        <input type="submit" name="enter" id="enter" value="Enter" /> 
        </form> 
    </div>';
}
?>

<!DOCTYPE html>
<html lang="en">
    <title>Virtual Little Free Library</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" type="text/css" href="stylesheet.css">
    <link rel="stylesheet" type="text/css" href="chat_style.css">
</head>
<body>
    <br>
    <h3>
        <pre>
█░█ █ █▀█ ▀█▀ █░█ ▄▀█ █░░
▀▄▀ █ █▀▄ ░█░ █▄█ █▀█ █▄▄

█░░ █ ▀█▀ ▀█▀ █░░ █▀▀
█▄▄ █ ░█░ ░█░ █▄▄ ██▄

█▀▀ █▀█ █▀▀ █▀▀  
█▀░ █▀▄ ██▄ ██▄  

█░░ █ █▄▄ █▀█ ▄▀█ █▀█ █▄█
█▄▄ █ █▄█ █▀▄ █▀█ █▀▄ ░█░
        </pre>
    </h3>


    
    <!-- <img class="book_shelf" src="../images/book_shelf.png" alt="some books"> -->
    <a href="#" id="toggleButton">About⬇</a>
    <div id="more">
        Like a <a href="https://littlefreelibrary.org/">Little Free Library</a>, but on the internet! 
        Leave a song, poem, drawing, pdf, movie, zip file, whatever! Check out what other people have left, 
        but remember, if you download the file, it will be removed from the box, just like in real life.
        <br> 
        <br>
        If you want to chat about community-activated public art, please email me at <span id="email"></span>.
        <br>
        <br>
        <a href="https://ko-fi.com/goodbyeoblivion">Buy me a coffee</a>
        <br>
        <a href="https://www.instagram.com/ndrewboylan/">Instagram</a>
    </div>
    <br>
    <div class="library_wrapper">
        <div class="library">
            <div class="roof"></div>


            <!-- BEGINNING OF TABLE UPLOAD/DOWNLOAD AREA -->
            <div class="table-container">
                <div class="table">
                    <div class="row">
                        <div class="cell box1" id = "uploadArea1">
                            <h3>
                            <pre>

█▄▄ █▀█ ▀▄▀   █▀█ █▄░█ █▀▀
█▄█ █▄█ █░█   █▄█ █░▀█ ██▄
                            </pre>
                            </h3>
                            <div id="fileInfo1"></div>
                            <br>
                            <div class="buttonDiv">
                                <input type="file" id="fileSelect1" name="attachments[]">
                                <button id="uploadButton1" disabled>Upload</button>
                                <button id="downloadButton1" disabled>Download</button>
                            </div>
                            <div id="uploadProgressBar1" style="width: 0%; height: 20px; background: green;"></div>
                            <p id="progressPercent1"></p>
                            <p id="uploadSuccessMessage1"></p>
                            <p id="error1"></p>
                        </div>
                    
                    
                        <div class="cell box2" id="uploadArea2">
                            <h3>
                            <pre>

█▄▄ █▀█ ▀▄▀   ▀█▀ █░█░█ █▀█
█▄█ █▄█ █░█   ░█░ ▀▄▀▄▀ █▄█
                            </pre>
                            </h3>
                            <div id="fileInfo2"></div>
                            <br>
                            <div class="buttonDiv">
                                <input type="file" id="fileSelect2" name="attachments[]">
                                <button id="uploadButton2" disabled>Upload</button>
                                <button id="downloadButton2" disabled>Download</button>
                            </div>
                            <div id="uploadProgressBar2" style="width: 0%; height: 20px; background: green;"></div>
                            <p id="progressPercent2"></p>
                            <p id="uploadSuccessMessage2"></p>
                            <p id="error2"></p>
                        </div>
                        
                    </div>
                    <div class="row"> 
                        <div class="cell box3" id = "uploadArea3">
                            <h3>
                            <pre>

█▄▄ █▀█ ▀▄▀   ▀█▀ █░█ █▀█ █▀▀ █▀▀
█▄█ █▄█ █░█   ░█░ █▀█ █▀▄ ██▄ ██▄
                            </pre>
                            </h3>
                            <div id="fileInfo3"></div>
                            <br>
                            <div class="buttonDiv">
                                <input type="file" id="fileSelect3" name="attachments[]">
                                <button id="uploadButton3" disabled>Upload</button>
                                <button id="downloadButton3" disabled>Download</button>
                            </div>
                            <div id="uploadProgressBar3" style="width: 0%; height: 20px; background: green;"></div>
                            <p id="progressPercent3"></p>
                            <p id="uploadSuccessMessage3"></p>
                            <p id="error3"></p>
                        </div>
                        <div class="cell box4" id="uploadArea4">
                            <h3>
                            <pre>
                                
█▄▄ █▀█ ▀▄▀   █▀▀ █▀█ █░█ █▀█
█▄█ █▄█ █░█   █▀░ █▄█ █▄█ █▀▄
                            </pre>
                            </h3>
                            <div id="fileInfo4"></div>
                            <br>
                            <div class="buttonDiv">
                                <input type="file" id="fileSelect4" name="attachments[]">
                                <button id="uploadButton4" disabled>Upload</button>
                                <button id="downloadButton4" disabled>Download</button>
                            </div>
                            <div id="uploadProgressBar4" style="width: 0%; height: 20px; background: green;"></div>
                            <p id="progressPercent4"></p>
                            <p id="uploadSuccessMessage4"></p>
                            <p id="error4"></p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="trunk"></div>
        </div>
    </div>
    <br>
    <br>
    <h3>
        <pre>
█▀▀ █░█ ▄▀█ ▀█▀
█▄▄ █▀█ █▀█ ░█░
        </pre>
    </h3>

    <div id="chat_wrapper">
        <?php
        if(!isset($_SESSION['name'])){
            loginForm();
        } else {
        ?>
        <div id="chat_menu">
            <p class="chat_welcome">Welcome, <b><?php echo $_SESSION['name']; ?></b></p>
            <p class="chat_logout"><a id="chat_exit" href="#">Exit Chat</a></p>
        </div>
        <div id="chatbox">
            <?php
            if(file_exists("chat_log.html") && filesize("chat_log.html") > 0){
                
                $contents = file_get_contents("chat_log.html");
                echo $contents;
            }
            ?>
        </div>

        <form name="chat_message" action="">
            <input name="chat_usermsg" type="text" id="chat_usermsg" />
            <input name="chat_submitmsg" type="submit" id="chat_submitmsg" value="Send" />
        </form>
        <?php
        }
        ?>
    </div>

    <script src="main.js"></script>

    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script type="text/javascript">
        // jQuery Document 
        $(document).ready(function () {
            if ($("#chatbox")[0]) {
                var chatboxScrollHeight = $("#chatbox")[0].scrollHeight;
                $("#chatbox").animate({ scrollTop: chatboxScrollHeight }, 'normal');
            }
            $("#loginForm").on('submit', function(e) {
                e.preventDefault();
                var name = $('#name').val();
                $.ajax({
                    type: "POST",
                    url: "lending_library.php",
                    data: { enter: true, name: name },
                    dataType: "json",
                    success: function(response) {
                        if (response.error) {
                            alert(response.message);
                        } else {
                            //window.scrollTo(0,document.body.scrollHeight);
                            location.reload();
                        }
                    }
                });
            });

            $("#chat_submitmsg").click(function () {
                var clientmsg = $("#chat_usermsg").val();
                $.post("chat_post.php", { text: clientmsg });
                $("#chat_usermsg").val("");
                return false;
            });
            function loadLog() {
                var oldscrollHeight;
                if ($("#chatbox")[0]) {
                    oldscrollHeight = $("#chatbox")[0].scrollHeight - 20; //Scroll height before the request 
                }

                $.ajax({
                    url: "chat_log.html",
                    cache: false,
                    success: function (html) {
                        $("#chatbox").html(html); //Insert chat log into the #chatbox div 
                        //Auto-scroll 
                        var newscrollHeight;
                        if ($("#chatbox")[0]) {
                            newscrollHeight = $("#chatbox")[0].scrollHeight - 20; //Scroll height after the request 
                            if(newscrollHeight > oldscrollHeight){
                                $("#chatbox").animate({ scrollTop: newscrollHeight }, 'normal'); //Autoscroll to bottom of div 
                            }
                        }
                    }
                });
            }
            setInterval (loadLog, 2500);
            $("#chat_exit").click(function () {
                var exit = confirm("Are you sure you want to end the session?");
                if (exit == true) {
                    window.location = "lending_library.php?logout=true";
                }
            });
        });
    </script>
    <script type="text/javascript">
        var user = 'andreweboylan';
        var domain = 'gmail.com';
        var element = document.getElementById('email');
        element.innerHTML = '<a href="mailto:' + user + '@' + domain + '">' + user + '@' + domain + '</a>';

    </script>

</body>
</html>



