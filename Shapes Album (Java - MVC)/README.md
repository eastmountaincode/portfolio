README

The purpose of the Photo Album is to display snapshots. The photo album is built according to the Model-Controller-View architecture paradigm, and there are two types of views: Graphical and Web.

GRAPHICAL VIEW

<img width="664" alt="Screen Shot 2022-07-17 at 2 21 50 PM" src="https://user-images.githubusercontent.com/59405316/179419504-5db4c871-52af-458a-a771-60fe8918ca92.png">

WEB VIEW

<img width="811" alt="Screen Shot 2022-07-17 at 2 22 35 PM 1" src="https://user-images.githubusercontent.com/59405316/179419574-bfd4832d-07fb-4e1c-b6d7-341dff70a8dd.png">


GUIDELINES FOR USE

Make sure the input .txt file is in the same directory as the .jar file when you run it.

NOTE: the files demo_input_blank_model.txt and demo_input.txt are in the parent directory in order for TestWebView to be able to find the input files. When running the .jar file in the resources folder, please put the input files in the resources folder.

ENTRY POINT

Our entry point to the program is PhotoAlbumMain in the util package. The command arguments are parsed, converting them from a string array to a hash map. A model is created, and a controller is created which gets the model and the parsed arguments. The controller uses the parsed arguments to find the mandatory input file, parse it, and make changes to the model according to the commands in the input file. Thus, by the time we get to creating a view, the model has undergone all the changes it will undergo. 

GRAPHICAL VIEW

Based on the parsed arguments, we either make a graphical view or a web view. If there are max dimensions in the parsed arguments, we make a GraphicalView using a constructor that takes in Features and max dimensions. Otherwise we make a GraphicalView with a constructor that takes in Features. The Features is the only way the view interacts with other packages.

The graphical view displays the first snapshot in the model upon initialization, or a blank window and a warning message if there are no snapshots in the model. The graphical view has three panels: information (for snapshot ID and description), shape zone (for displaying shapes), and button (for buttons). The buttons panel has access to the view's Features so that when buttons are pressed appropriate actions can be carried out.

WEB VIEW

If the parsed arguments specify that a web view should be made, a new web view is constructed with Features and a string which is the name of the html file that should be created. The web view essentially creates an html file based on what is in the model. For each snapshot, each shape is added to the html file using SVG tags. The resultant html file will be created in the resources package. 

NOTES FOR USING .JAR FILE

When running the .jar file from the command line, input the arguments in the following form:

java -jar NameOfJARFile.jar -in "name-of-command-file" -view "type-of-view" -out "where-output-should-go" xmax ymax 

for example:

java -jar ShapesAlbum.jar -in "demo_input.txt" -view "graphical"

xmax and ymax are optional, they specify the bounds of the view window. The -out tag is ignored for the graphical view since it is not used, but it is required for the web view. 
