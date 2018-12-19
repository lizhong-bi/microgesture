package com.example.lizhongbi.fragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Process;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class canvasFragment extends Fragment {


    public canvasFragment() {
        // Required empty public constructor
    }
    float initialx;
    float initialy;
    MotionEvent currEvent;
    Canvas currCanvas;
    String fromMain;
    int index = 1;
    File gpxfile;
    FileWriter writer;
    File log;
    FileWriter logger;
    String name;
    boolean isFirst = true;
    private Paint paint = new Paint();
    private Path path = new Path();
    //String picsrc = "R.drawable.line";
    //String [] picsrcs= {"R.drawable.line","R.drawable.aOmmlx","R.drawable.a2256cd1f5f775d3","R.drawable.line","R.drawable.curves","R.drawable.horizontalline","R.drawable.images"};
    String[] names;
    boolean reset = true;
    int curr = -1;
    int totalLength;

    File previousFile;
    int previousIndex;
    String previousName;
    boolean isPrev = false;
    boolean redraw = false;
    TouchEventView event;

    File [] orientations;
    int currentSubdir = 0;
    String [] folderNames;
    int [] folderLength;
    Drawable[] drawables;
    File rootfile = null;
    File currfolder;
    int lengthOfDir;
    AlertDialog.Builder builder;
    boolean proceed = true;
    boolean isInc = false;

    //keeps track of the overall time it took
    long begintime;
    long endtime;
    File generalcsv;
    FileWriter generalcsvwriter;
    int trialnumber = 0;
    String orientation;
    File [] types;
    File [] angles;
    int typeCount = 0;
    int angleCount = 0;
    int orientationCount = 0;
    boolean isNewOrientation = false;
    boolean isNewType = false;
    File newOrientation;
    File newType;

    int patternnum;
    String generalCSVContent = "";
    boolean isFirstTime = true;
    int pattern;
    File flag;
    FileWriter flagwriter;

    Date currentTime;
    float previousx;
    float previousy;
    int totalDisplacement;
    float velocity;
    Date beginrealtime;
    Date endrealtime;
    int lengthadded;
    int lengthsub=0;

    File errorlog;
    FileWriter errorlogwriter;

    Drawable background;
    String backgroundPath;



    public void setVar(String message)
    {
        int space = 0;
        for(int count = 0; count < message.length(); count++)
        {
            if(message.charAt(count) == ' ')
            {
                space = count;
                break;
            }
        }

        fromMain = message.substring(0,space);
        patternnum = Integer.parseInt(message.substring(space+1));
        pattern = patternnum;
        if(pattern >= 9)
        {
            pattern -= 8;
        }
        if(pattern >= 5)
        {
            //FixedFirst = false;
            pattern -= 4;
        }
        System.out.println("******************************************************* Here ****************************************************************");
        System.out.println("*******************************************************"+  message + "****************************************************************");
        System.out.println("******************************************************* Here ****************************************************************");
        System.out.println("*******************************************************" +  fromMain + "****************************************************************");
        System.out.println("******************************************************* Here ****************************************************************");
        System.out.println("*******************************************************" +  pattern + "****************************************************************");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_canvas, container, false);

        ConstraintLayout constraintLayout = (ConstraintLayout) rootView.findViewById(R.id.frameLayout2);
        try {
            event = new TouchEventView(getContext(),null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        constraintLayout.addView(event);

        return rootView;

    }

    public void changeData()
    {
        isPrev = true;
        gpxfile = previousFile;
        redraw = true;
        event.redo();
        //com.example.lizhongbi.fragment.TouchEventView.
        //System.out.println("previous");
    }



    public class TouchEventView extends View {


        class point

        {
            float xval;
            float yval;
        }

        public void setFolder()
        {

            System.out.println("******************************************************* Here ****************************************************************");
            File[] dir = angles[currentSubdir].listFiles();
            int numOfFiles = folderLength[currentSubdir];
            drawables = new Drawable[numOfFiles];
            names = new String[numOfFiles];
            totalLength = numOfFiles;
            for(int count =0; count < numOfFiles; count++)
            {
                String picPath = dir[count].getPath();
                Bitmap bitmap = BitmapFactory.decodeFile(picPath);
                drawables[count] = new BitmapDrawable(getResources(),bitmap);
                names[count] = dir[count].getName().substring(0,dir[count].getName().length()-4);
            }

            if(isNewOrientation)
            {
                newOrientation = new File(rootfile,orientations[orientationCount].getName());
                if (!newOrientation.exists()) {
                    newOrientation.mkdir();
                }
                newType = new File(newOrientation,types[typeCount].getName());
                if (!newType.exists()) {
                    newType.mkdir();
                }
                currfolder = new File(newType,folderNames[currentSubdir]);
                if (!currfolder.exists()) {
                    currfolder.mkdir();
                }
                isNewOrientation = false;
            }
            else if(isNewType)
            {
                newType = new File(newOrientation, types[typeCount].getName());
                if (!newType.exists()) {
                    newType.mkdir();
                }
                currfolder = new File(newType,folderNames[currentSubdir]);
                if (!currfolder.exists()) {
                    currfolder.mkdir();
                }
                isNewType = false;
            }
            else {
                currfolder = new File(newType, folderNames[currentSubdir]);
                if (!currfolder.exists()) {
                    currfolder.mkdir();
                }
            }
            try {
                log = new File(currfolder, "log.txt");
                logger = new FileWriter(log);
                isFirst = false;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public TouchEventView(Context ctx, AttributeSet attrs) throws IOException {
            super(ctx,attrs);
            paint.setAntiAlias(true);
            paint.setColor(Color.WHITE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5f);
            this.setBackgroundColor(Color.WHITE);


            String path = Environment.getExternalStorageDirectory().toString()+"/Pictures";
            backgroundPath = Environment.getExternalStorageDirectory().toString()+"/Documents/static.jpg";
            background = Drawable.createFromPath(backgroundPath);
            System.out.println("*******************************************************" + path);

            isNewOrientation = true;
            File pictures = new File(path);
            orientations = pictures.listFiles();
            if(patternnum > 8)
            {
                File temp = orientations[0];
                orientations[0] = orientations[1];
                orientations[1] = temp;
            }
            types = orientations[orientationCount].listFiles();
            /**if(!FixedFirst) {
                File temp = types[0];
                types[0] = types[1];
                types[1] = temp;
            }*/
            angles = types[typeCount].listFiles();
            if(pattern == 1)
            {
                System.out.println("******************************************************* inside pattern 1 ****************************************************************");
                System.out.println("*******************************************************" +  pattern + "****************************************************************");
                angles[0] = types[typeCount].listFiles()[3];
                angles[1] = types[typeCount].listFiles()[2];
                angles[2] = types[typeCount].listFiles()[1];
                angles[3] = types[typeCount].listFiles()[0];
            }
            else if(pattern == 2)
            {
                System.out.println("******************************************************* inside pattern 2 ****************************************************************");
                System.out.println("*******************************************************" +  pattern + "****************************************************************");
                angles[0] = types[typeCount].listFiles()[0];
                angles[1] = types[typeCount].listFiles()[3];
                angles[2] = types[typeCount].listFiles()[2];
                angles[3] = types[typeCount].listFiles()[1];
            }
            else if(pattern == 3)
            {
                System.out.println("******************************************************* inside pattern 3 ****************************************************************");
                System.out.println("*******************************************************" +  pattern + "****************************************************************");
                angles[0] = types[typeCount].listFiles()[1];
                angles[1] = types[typeCount].listFiles()[0];
                angles[2] = types[typeCount].listFiles()[3];
                angles[3] = types[typeCount].listFiles()[2];
            }
            else
            {
                System.out.println("******************************************************* inside pattern 4 ****************************************************************");
                System.out.println("*******************************************************" +  pattern + "****************************************************************");
                angles[0] = types[typeCount].listFiles()[2];
                angles[1] = types[typeCount].listFiles()[1];
                angles[2] = types[typeCount].listFiles()[0];
                angles[3] = types[typeCount].listFiles()[3];
            }
            lengthOfDir = angles.length;
            folderNames = new String [lengthOfDir];
            folderLength = new int [lengthOfDir];
            for(int count = 0; count < lengthOfDir; count++)
            {
                folderNames[count] = angles[count].getName();
                folderLength[count] = angles[count].listFiles().length;
                //.out.println(folderNames[count]);
                //System.out.println(folderLength[count]);
            }

            //create the main directory folder
            if(isFirst) {
                String csvname = fromMain + ".csv";
                //gpxfile = new File(currfolder, name);
                rootfile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fromMain);
                if (!rootfile.exists()) {
                    rootfile.mkdir();
                    generalcsv = new File(rootfile, csvname);
                    errorlog = new File(rootfile,"errorlog.txt");
                    generalcsvwriter = new FileWriter(generalcsv);
                    errorlogwriter = new FileWriter(errorlog);
                    generalcsvwriter.append("Trial Number,Orientation,Length,Angle,Name,Start Time, End Time, Start Time(milliseconds), End Time(milliseconds),Velocity \n");
                    generalcsvwriter.flush();
                    System.out.println("******************************************************* create dir ****************************************************************");
                }
                else
                {
                    generalcsv = new File(rootfile, csvname);
                    generalcsvwriter = new FileWriter(generalcsv,true);
                    generalcsvwriter.flush();
                    File currorientationdir = new File(rootfile,orientations[orientationCount].getName());
                    File currtypedir = new File(currorientationdir,types[typeCount].getName());
                    File currangledir = new File(currtypedir,angles[currentSubdir].getName());
                    File targetFile = new File(currangledir,"Done.txt");
                   // if(targetFile.exists()) {
                   //     lengthsub = angles[currentSubdir].listFiles().length;
                    //    trialnumber += lengthsub;
                   // }
                    while(targetFile.exists())
                    {
                        lengthadded = angles[currentSubdir].listFiles().length;
                        trialnumber+=lengthadded;
                        //trialnumber++;
                        currentSubdir++;
                        if(currentSubdir == types[typeCount].listFiles().length)
                        {
                            currentSubdir = 0;
                            typeCount++;
                            if (typeCount == orientations[orientationCount].listFiles().length) {
                                orientationCount++;
                                typeCount = 0;

                                types = orientations[orientationCount].listFiles();
                                /**if(!FixedFirst) {
                                    File temp = types[0];
                                    types[0] = types[1];
                                    types[1] = temp;
                                }*/
                                angles = types[typeCount].listFiles();
                                if(pattern == 1)
                                {
                                    System.out.println("******************************************************* inside pattern 1 ****************************************************************");
                                    System.out.println("*******************************************************" +  pattern + "****************************************************************");
                                    angles[0] = types[typeCount].listFiles()[3];
                                    angles[1] = types[typeCount].listFiles()[2];
                                    angles[2] = types[typeCount].listFiles()[1];
                                    angles[3] = types[typeCount].listFiles()[0];
                                }
                                else if(pattern == 2)
                                {
                                    System.out.println("******************************************************* inside pattern 2 ****************************************************************");
                                    System.out.println("*******************************************************" +  pattern + "****************************************************************");
                                    angles[0] = types[typeCount].listFiles()[0];
                                    angles[1] = types[typeCount].listFiles()[3];
                                    angles[2] = types[typeCount].listFiles()[2];
                                    angles[3] = types[typeCount].listFiles()[1];
                                }
                                else if(pattern == 3)
                                {
                                    System.out.println("******************************************************* inside pattern 3 ****************************************************************");
                                    System.out.println("*******************************************************" +  pattern + "****************************************************************");
                                    angles[0] = types[typeCount].listFiles()[1];
                                    angles[1] = types[typeCount].listFiles()[0];
                                    angles[2] = types[typeCount].listFiles()[3];
                                    angles[3] = types[typeCount].listFiles()[2];
                                }
                                else
                                {
                                    System.out.println("******************************************************* inside pattern 4 ****************************************************************");
                                    System.out.println("*******************************************************" +  pattern + "****************************************************************");
                                    angles[0] = types[typeCount].listFiles()[2];
                                    angles[1] = types[typeCount].listFiles()[1];
                                    angles[2] = types[typeCount].listFiles()[0];
                                    angles[3] = types[typeCount].listFiles()[3];
                                }
                                lengthOfDir = angles.length;
                                folderNames = new String [lengthOfDir];
                                folderLength = new int [lengthOfDir];
                                for(int count = 0; count < lengthOfDir; count++)
                                {
                                    folderNames[count] = angles[count].getName();
                                    folderLength[count] = angles[count].listFiles().length;
                                    //.out.println(folderNames[count]);
                                    //System.out.println(folderLength[count]);
                                }

                                if (orientationCount == 2) {
                                    int pid = Process.myPid();
                                    Process.killProcess(pid);
                                } else {
                                    currorientationdir = new File(rootfile, orientations[orientationCount].getName());
                                    currtypedir = new File(currorientationdir, types[typeCount].getName());
                                    currangledir = new File(currtypedir,angles[currentSubdir].getName());
                                    targetFile = new File(currangledir,"Done.txt");
                                    //if(targetFile.exists()) {
                                    //    lengthsub = angles[currentSubdir].listFiles().length;
                                    //    trialnumber += lengthsub;
                                    //}
                                }
                            }
                            else {
                                angles = types[typeCount].listFiles();
                                if(pattern == 1)
                                {
                                    System.out.println("******************************************************* inside pattern 1 ****************************************************************");
                                    System.out.println("*******************************************************" +  pattern + "****************************************************************");
                                    angles[0] = types[typeCount].listFiles()[3];
                                    angles[1] = types[typeCount].listFiles()[2];
                                    angles[2] = types[typeCount].listFiles()[1];
                                    angles[3] = types[typeCount].listFiles()[0];
                                }
                                else if(pattern == 2)
                                {
                                    System.out.println("******************************************************* inside pattern 2 ****************************************************************");
                                    System.out.println("*******************************************************" +  pattern + "****************************************************************");
                                    angles[0] = types[typeCount].listFiles()[0];
                                    angles[1] = types[typeCount].listFiles()[3];
                                    angles[2] = types[typeCount].listFiles()[2];
                                    angles[3] = types[typeCount].listFiles()[1];
                                }
                                else if(pattern == 3)
                                {
                                    System.out.println("******************************************************* inside pattern 3 ****************************************************************");
                                    System.out.println("*******************************************************" +  pattern + "****************************************************************");
                                    angles[0] = types[typeCount].listFiles()[1];
                                    angles[1] = types[typeCount].listFiles()[0];
                                    angles[2] = types[typeCount].listFiles()[3];
                                    angles[3] = types[typeCount].listFiles()[2];
                                }
                                else
                                {
                                    System.out.println("******************************************************* inside pattern 4 ****************************************************************");
                                    System.out.println("*******************************************************" +  pattern + "****************************************************************");
                                    angles[0] = types[typeCount].listFiles()[2];
                                    angles[1] = types[typeCount].listFiles()[1];
                                    angles[2] = types[typeCount].listFiles()[0];
                                    angles[3] = types[typeCount].listFiles()[3];
                                }
                                lengthOfDir = angles.length;
                                folderNames = new String [lengthOfDir];
                                folderLength = new int [lengthOfDir];
                                for(int count = 0; count < lengthOfDir; count++)
                                {
                                    folderNames[count] = angles[count].getName();
                                    folderLength[count] = angles[count].listFiles().length;
                                    //.out.println(folderNames[count]);
                                    //System.out.println(folderLength[count]);
                                }

                                currtypedir = new File(currorientationdir, types[typeCount].getName());
                                currangledir = new File(currtypedir,angles[currentSubdir].getName());
                                targetFile = new File(currangledir,"Done.txt");
                                //if(targetFile.exists()) {
                                //    lengthsub = angles[currentSubdir].listFiles().length;
                                //    trialnumber += lengthsub;
                                //}
                            }
                        }
                        else
                        {
                            currangledir = new File(currtypedir,angles[currentSubdir].getName());
                            targetFile = new File(currangledir,"Done.txt");
                            //if(targetFile.exists()) {
                            //    lengthsub = angles[currentSubdir].listFiles().length;
                             //   trialnumber += lengthsub;
                            //}
                        }


                     /**   angleCount++;
                        if(angleCount == angles[angleCount].listFiles().length)
                        {
                            angleCount = 0;
                            typeCount++;
                            if (typeCount == orientations[orientationCount].listFiles().length) {
                                orientationCount++;
                                typeCount = 0;
                                if (orientationCount == 2) {
                                    int pid = Process.myPid();
                                    Process.killProcess(pid);
                                } else {
                                    currorientationdir = new File(rootfile, orientations[orientationCount].getName());
                                    currtypedir = new File(currorientationdir, types[typeCount].getName());
                                    currangledir = new File(currtypedir,angles[angleCount].getName());
                                    targetFile = new File(currangledir,"Done.txt");
                                }
                            }
                            else {
                                currtypedir = new File(currorientationdir, types[typeCount].getName());
                                currangledir = new File(currtypedir,angles[angleCount].getName());
                                targetFile = new File(currangledir,"Done.txt");
                            }
                        }
                        else
                        {
                            currangledir = new File(currtypedir,angles[angleCount].getName());
                            targetFile = new File(currangledir,"Done.txt");
                        }
                      */
                    }
                    System.out.println("*************-------------------------------------------------------------------------------------------------------------------"+ trialnumber);
                    System.out.println("*************-------------------------------------------------------------------------------------------------------------------"+ lengthsub);
                    //trialnumber-=lengthsub;
                    System.out.println("*************-------------------------------------------------------------------------------------------------------------------"+ trialnumber);
                }
            }

            //making the general csv
           /** String csvname = fromMain + ".csv";
            //gpxfile = new File(currfolder, name);
            generalcsv = new File(rootfile, csvname);
            generalcsvwriter = new FileWriter(generalcsv);
            generalcsvwriter.append("Trial Number,Orientation,Length,Angle,Name,Start Time, End Time,Velocity \n");
            generalcsvwriter.flush();
            */

            setFolder();

            //The alert System
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this.getContext(), android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(this.getContext());
            }
            builder.setTitle("Note")
                    .setMessage("Please press 'CANCEL' to go to previos image, or rest for 30 seconds and press 'OK' when you are ready to proceed to the next directory. ")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            /**curr=previousIndex;
                            isPrev = true;
                            gpxfile = previousFile;
                            redraw = true;
                            proceed=true;
                            event.redo();*/
                           /** new CountDownTimer(30000,1000){
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    builder.setMessage("seconds remaining: " + millisUntilFinished / 1000);
                                   // builder.show();
                                }

                                @Override
                                public void onFinish() {
                                    builder.setMessage("countdown complete, you may resume");
                                    builder.show();
                                }
                            }.start();*/
                            currentSubdir++;
                            proceed=true;
                            curr=0;
                            isInc = true;
                            try {
                                flag = new File(currfolder,"Done.txt");
                                flagwriter = new FileWriter(flag);
                                flagwriter.append("Done");
                                flagwriter.flush();
                                flagwriter.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //if(currentSubdir == folderLength[angleCount]) {
                            if(currentSubdir == types[typeCount].listFiles().length) {
                                //angleCount++;
                                //if(angleCount == types[typeCount].listFiles().length) {
                                    currentSubdir = 0;
                                    typeCount++;
                                   // angleCount = 0;
                                    if (typeCount == orientations[orientationCount].listFiles().length) {
                                        orientationCount++;
                                        typeCount = 0;
                                        if (orientationCount == 2) {
                                            try {
                                                generalcsvwriter.append(trialnumber + "," + orientations[orientationCount].getName() + "," + types[typeCount].getName() + "," + angles[currentSubdir].getName() + "," + names[curr] + "," + begintime + "," + endtime + "," + beginrealtime + "," + endrealtime + "," + velocity + "\n");
                                                generalcsvwriter.flush();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            int pid = Process.myPid();
                                            Process.killProcess(pid);
                                        }
                                        else {
                                            isNewOrientation = true;
                                            types = orientations[orientationCount].listFiles();
                                           /** if(!FixedFirst) {
                                                File temp = types[0];
                                                types[0] = types[1];
                                                types[1] = temp;

                                                //types = new File [2];
                                               // types[0] = (orientations[orientationCount].listFiles())[1];
                                               // types[1] = (orientations[orientationCount].listFiles())[0];
                                            }*/
                                            //currentSubdir = 0;
                                            angles = types[typeCount].listFiles();
                                            if(pattern == 1)
                                            {
                                                System.out.println("******************************************************* inside pattern 1 ****************************************************************");
                                                System.out.println("*******************************************************" +  pattern + "****************************************************************");
                                                angles[0] = types[typeCount].listFiles()[3];
                                                angles[1] = types[typeCount].listFiles()[2];
                                                angles[2] = types[typeCount].listFiles()[1];
                                                angles[3] = types[typeCount].listFiles()[0];
                                            }
                                            else if(pattern == 2)
                                            {
                                                System.out.println("******************************************************* inside pattern 2 ****************************************************************");
                                                System.out.println("*******************************************************" +  pattern + "****************************************************************");
                                                angles[0] = types[typeCount].listFiles()[0];
                                                angles[1] = types[typeCount].listFiles()[3];
                                                angles[2] = types[typeCount].listFiles()[2];
                                                angles[3] = types[typeCount].listFiles()[1];
                                            }
                                            else if(pattern == 3)
                                            {
                                                System.out.println("******************************************************* inside pattern 3 ****************************************************************");
                                                System.out.println("*******************************************************" +  pattern + "****************************************************************");
                                                angles[0] = types[typeCount].listFiles()[1];
                                                angles[1] = types[typeCount].listFiles()[0];
                                                angles[2] = types[typeCount].listFiles()[3];
                                                angles[3] = types[typeCount].listFiles()[2];
                                            }
                                            else
                                            {
                                                System.out.println("******************************************************* inside pattern 4 ****************************************************************");
                                                System.out.println("*******************************************************" +  pattern + "****************************************************************");
                                                angles[0] = types[typeCount].listFiles()[2];
                                                angles[1] = types[typeCount].listFiles()[1];
                                                angles[2] = types[typeCount].listFiles()[0];
                                                angles[3] = types[typeCount].listFiles()[3];
                                            }
                                            lengthOfDir = angles.length;
                                            folderNames = new String[lengthOfDir];
                                            folderLength = new int[lengthOfDir];
                                            for (int count = 0; count < lengthOfDir; count++) {
                                                folderNames[count] = angles[count].getName();
                                                folderLength[count] = angles[count].listFiles().length;
                                                //.out.println(folderNames[count]);
                                                //System.out.println(folderLength[count]);
                                            }
                                            setFolder();
                                            event.redo();
                                        }
                                    }
                                    else {
                                        isNewType = true;
                                        types = orientations[orientationCount].listFiles();
                                        /**if(!FixedFirst) {
                                            File temp = types[0];
                                            types[0] = types[1];
                                            types[1] = temp;

                                            //types = new File [2];
                                            // types[0] = (orientations[orientationCount].listFiles())[1];
                                            // types[1] = (orientations[orientationCount].listFiles())[0];
                                        }*/
                                        angles = types[typeCount].listFiles();
                                        if(pattern == 1)
                                        {
                                            System.out.println("******************************************************* inside pattern 1 ****************************************************************");
                                            System.out.println("*******************************************************" +  pattern + "****************************************************************");
                                            angles[0] = types[typeCount].listFiles()[3];
                                            angles[1] = types[typeCount].listFiles()[2];
                                            angles[2] = types[typeCount].listFiles()[1];
                                            angles[3] = types[typeCount].listFiles()[0];
                                        }
                                        else if(pattern == 2)
                                        {
                                            System.out.println("******************************************************* inside pattern 2 ****************************************************************");
                                            System.out.println("*******************************************************" +  pattern + "****************************************************************");
                                            angles[0] = types[typeCount].listFiles()[0];
                                            angles[1] = types[typeCount].listFiles()[3];
                                            angles[2] = types[typeCount].listFiles()[2];
                                            angles[3] = types[typeCount].listFiles()[1];
                                        }
                                        else if(pattern == 3)
                                        {
                                            System.out.println("******************************************************* inside pattern 3 ****************************************************************");
                                            System.out.println("*******************************************************" +  pattern + "****************************************************************");
                                            angles[0] = types[typeCount].listFiles()[1];
                                            angles[1] = types[typeCount].listFiles()[0];
                                            angles[2] = types[typeCount].listFiles()[3];
                                            angles[3] = types[typeCount].listFiles()[2];
                                        }
                                        else
                                        {
                                            System.out.println("******************************************************* inside pattern 4 ****************************************************************");
                                            System.out.println("*******************************************************" +  pattern + "****************************************************************");
                                            angles[0] = types[typeCount].listFiles()[2];
                                            angles[1] = types[typeCount].listFiles()[1];
                                            angles[2] = types[typeCount].listFiles()[0];
                                            angles[3] = types[typeCount].listFiles()[3];
                                        }
                                        lengthOfDir = angles.length;
                                        folderNames = new String[lengthOfDir];
                                        folderLength = new int[lengthOfDir];
                                        for (int count = 0; count < lengthOfDir; count++) {
                                            folderNames[count] = angles[count].getName();
                                            folderLength[count] = angles[count].listFiles().length;
                                            //.out.println(folderNames[count]);
                                            //System.out.println(folderLength[count]);
                                        }
                                        setFolder();
                                        event.redo();
                                    }
                               // }
                              //  else
                             //   {
                              //      System.out.println("******************************************************* new angle folder" );
                              //      setFolder();
                              //      event.redo();
                              //  }
                            }
                            else {
                                setFolder();
                                event.redo();
                            }
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                          /**  currentSubdir++;
                            if(currentSubdir == subdirs.length)
                                System.exit(1);
                            else {
                                curr = 0;
                                setFolder();
                                proceed=true;
                            }*/
                            curr=previousIndex;
                            isPrev = true;
                            gpxfile = previousFile;
                            redraw = true;
                            proceed=true;
                            event.redo();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert);
            /**
             *  String path = Environment.getExternalStorageDirectory().toString()+"/Pictures";
             *             System.out.println("*******************************************************" + path);
             *             File picdir = new File(path);
             *             File[] pics = picdir.listFiles();
             *             drawables = new Drawable[pics.length];
             *             names = new String[pics.length];
             *             totalLength = pics.length;
             *             for(int count =0; count < pics.length; count++)
             *             {
             *                 String picPath = pics[count].getPath();
             *                 Bitmap bitmap = BitmapFactory.decodeFile(picPath);
             *                 drawables[count] = new BitmapDrawable(getResources(),bitmap);
             *                 names[count] = pics[count].getName().substring(0,pics[count].getName().length()-4);
             *             }
             */

        }

        public void calldialog()
        {
            builder.show();
        }
        @Override
        protected void onDraw(Canvas canvas){
            System.out.println(curr);
            currCanvas = canvas;
            canvas.drawPath(path,paint);
            if(reset) {
                if(isPrev)
                {
                    trialnumber--;
                    isPrev = false;
                    curr = previousIndex ;
                    proceed = true;

                }
                else {
                    System.out.println("******************************************************* herherherhehrer "+ trialnumber );
                    if(isFirstTime)
                    {
                        isFirstTime = false;
                    }
                   /** else {
                        try {

                            generalcsvwriter.append(trialnumber + "," + orientations[orientationCount].getName() + "," + types[typeCount].getName() + "," + angles[currentSubdir].getName() + "," + names[curr] + "," + begintime + "," + endtime + "," + beginrealtime + "," + endrealtime + "," + velocity +  "\n");
                            generalcsvwriter.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }*/
                    previousIndex = curr;
                    curr++;
                    if(isInc) {
                        curr--;
                        isInc = false;
                    }
                    if(curr == totalLength)
                    {
                        proceed=false;
                        System.out.println("here");
                        builder.show();

                        //curr=0;
                        //setFolder();
                        //builder.show();
                    }
                }
            }
            if(proceed) {
                Drawable d = drawables[curr];
                //System.out.println(d.getBounds());
                background.setBounds(0,0,canvas.getWidth(),canvas.getHeight());
                background.draw(canvas);
                System.out.println("*******************************************************" +d.getIntrinsicHeight());
                System.out.println("*******************************************************" +d.getIntrinsicWidth());
                d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());

                //d.setBounds(0, 0, canvas.getHeight(), canvas.getWidth());
                //d.setBounds(100,100,1200,1200);
                d.draw(canvas);

                reset = false;
                proceed = true;
            }
        }


        public void redo()
        {
            reset = true;
            invalidate();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event)
        {
            currEvent = event;
            currentTime = Calendar.getInstance().getTime();
            float xPos = event.getX();
            float yPos = event.getY();
            //com.example.lizhongbi.fragment.TouchEventView.point p;
            switch(event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    //file = new File(getContext().getFilesDir(), "mydir");
                    //file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),fromMain);
                    /** if (!file.exists()) {
                     file.mkdir();
                     System.out.println("******************************************************* create dir"  + " ****************************************************************");
                     }**/
                    begintime = System.currentTimeMillis();
                    try {
                        if(!redraw) {
                            name = names[curr] + ".csv";
                            previousName = name;
                            gpxfile = new File(currfolder, name);
                            writer = new FileWriter(gpxfile);
                            index ++;
                            logger.append(name + "\n");
                            logger.flush();
                        }
                        else
                        {
                            redraw = false;
                            gpxfile = new File(currfolder,previousName);
                            writer = new FileWriter(gpxfile);
                        }
                        //logger.append(name + "\n");
                        //logger.flush();
                        writer.append("Trial Number, Orientation, Length, Angle, X-pos,Y-pos,Timestamp \n");
                        writer.flush();
                        //System.out.println(getContext().getFilesDir());
                        System.out.println("******************************************************* " + name + " ****************************************************************");
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                    beginrealtime = Calendar.getInstance().getTime();
                    path.moveTo(xPos, yPos);
                    initialx= xPos;
                    initialy = yPos;
                    previousx = xPos;
                    previousy = yPos;
                    try {
                        writer.append(trialnumber+1 + "," + orientations[orientationCount].getName() + "," + types[typeCount].getName() + "," + angles[currentSubdir].getName() + "," + 0 + "," + 0 + "," + Calendar.getInstance().getTime() + "\n ");
                        writer.flush();
                        //writer.append(xPos + "," + yPos + "," + currentTime + " \n ");
                        //writer.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //System.out.println(xPos + "," + yPos + "," + currentTime);
                    return true;
                case MotionEvent.ACTION_MOVE:
                    path.lineTo(xPos,yPos);
                    double dist = Math.sqrt(Math.pow(xPos-previousx,2) + Math.pow(yPos-previousy,2));
                    previousx = xPos;
                    previousy = yPos;
                    totalDisplacement += dist;
                    try {
                        writer.append(trialnumber+1 + "," + orientations[orientationCount].getName() + "," + types[typeCount].getName() + "," + angles[currentSubdir].getName() + "," + (xPos - initialx) + "," + (yPos - initialy) + "," + Calendar.getInstance().getTime() + "\n ");
                        writer.flush();
                        //writer.append(xPos + "," + yPos + "," + currentTime + "\n");
                        //writer.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //System.out.println(xPos + " " + yPos + "," + currentTime);
                    break;
                case MotionEvent.ACTION_UP:
                    try {
                        //path.lineTo(xPos,yPos);
                        endtime = System.currentTimeMillis();
                        //velocity = 0;
                        velocity = (totalDisplacement*1000)/(Math.abs(begintime-endtime));
                        endrealtime = Calendar.getInstance().getTime();
                        //writer.append(trialnumber + "," + orientations[orientationCount].getName() + "," + types[typeCount].getName() + "," + angles[angleCount].getName() + "," + (xPos - initialx) + "," + (yPos - initialy) + "," + currentTime + " \n ");
                        //writer.flush();
                        trialnumber++;
                        generalCSVContent = trialnumber + "," + orientations[orientationCount].getName() + "," + types[typeCount].getName() + "," + angles[currentSubdir].getName() + "," + names[curr] + "," + begintime + "," + endtime + "," + velocity + "\n";
                        try {
                            generalcsvwriter.append(trialnumber + "," + orientations[orientationCount].getName() + "," + types[typeCount].getName() + "," + angles[currentSubdir].getName() + "," + names[curr] + "," + begintime + "," + endtime + "," + beginrealtime + "," + endrealtime + "," + velocity +  "\n");
                            generalcsvwriter.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //generalcsvwriter.append("Trial Number,Orientation,Length,Angle,Name,Start Time, End Time,Velocity \n");
                        //generalcsvwriter.append(trialnumber + "," + orientations[orientationCount].getName() + "," + types[typeCount].getName() + "," + angles[angleCount].getName() + "," + names[curr] + "," + begintime + "," + endtime + ",\n");
                        writer.close();
                        FileInputStream is = new FileInputStream(gpxfile);
                        BufferedReader reader = new BufferedReader(new InputStreamReader((is)));
                        String line = reader.readLine();
                        //System.out.println("reached");
                        while(line != null)
                        {
                            System.out.println(line);
                            line = reader.readLine();
                        }
                        is = new FileInputStream(log);
                        reader = new BufferedReader(new InputStreamReader((is)));
                        line = reader.readLine();
                        while(line != null)
                        {
                            System.out.println(line);
                            line = reader.readLine();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    gpxfile = null;
                    reset = true;
                    //invalidate();
                    break;
                default:
                    return false;
            }
            //schedule a repaint
            invalidate();
            return true;
        }
    }



}
