# microgesture
For ura project

Note that I have only uploaded the src folder of the entire project, which is comprised of if not most, all of the files that I have changed and added from the default folder that was generated by Android studio when I first started the project.

Descriptions of the java files in use for the project:

The MainActivity.java file is the first file that would run when the application opens up and the corresponding layout for this would be landing_page.xml . The layout would have two blank lines for users to fill out and a continue button below the two lines. The first line is where the user would enter the name of the folder they would like the data to be placed and the second line is the pattern number that the program will display. The java would start another activity when the user press continue and pass those two sets of information along.

After the user have pressed continue, landingPage.java would be opened, this would serve as an intermediary page that would hold the two fragements that we are going to use in our program, which are the previous button at the top of the screen and the canvas in which data will be collected.

The buttonFragment.java is the corresponding code for the button, it is a common button class that contains the basics methods like onclick, onclicklistener, and it's purpose is to feed the signal into the canvas when the user would like to redo a trace.

The canvasFragment.java is the most crucial part of this project, its main purpose is to gather the data from the trace and then place it in its correspodning file and folder. There are several functions within that work together to achieve this goal. The onTouchEvent function is the function that is in charge of collection all the data of a trace, it will generate the file when the user places their finger down on the screen, and will collect the data in a timely manner until the user finishes tracing through the data. The onDraw function is in charge of repainting the canvas, meaning it will refresh the canvas to the next correct image when the user is done tracing through the current one. The TouchEventView function sets the overall structure up in the beginning. Which includes setting up the folder structure, making sure that the pattern we are displaying matches the one the user chose, and setting up corresponding file to collect the overall data, and making sure the correct pictures and folders are in place for the user to begin their data collection. And the setFolder function will help find the correspdoning folder once a folder's pictures have all been traced.
