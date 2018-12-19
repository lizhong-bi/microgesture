package com.example.lizhongbi.fragment;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import android.view.View;
import android.view.View.OnTouchListener;

import static android.content.Context.MODE_PRIVATE;

public class TouchEventView extends View {

    String folderName;
    File folder;
    int index = 1;
    File file = null;
    File gpxfile;
    FileWriter writer;
    File log;
    FileWriter logger;
    String name;
    boolean isFirst = true;
    boolean var = false;
    private Paint paint = new Paint();
    private Path path = new Path();
    //String picsrc = "R.drawable.line";
    //String [] picsrcs= {"R.drawable.line","R.drawable.aOmmlx","R.drawable.a2256cd1f5f775d3","R.drawable.line","R.drawable.curves","R.drawable.horizontalline","R.drawable.images"};
    Drawable[] drawables = new Drawable[6];
    boolean reset = true;
    int curr = 0;

    File previousFile;
    int previousIndex;
    String previousName;
    boolean isPrev = false;
    boolean redraw = false;

    class point
    {
        float xval;
        float yval;
    }
    ArrayList<com.example.lizhongbi.fragment.TouchEventView.point> coordinates = new ArrayList<com.example.lizhongbi.fragment.TouchEventView.point>();
    ArrayList<ArrayList<com.example.lizhongbi.fragment.TouchEventView.point>> list = new ArrayList<ArrayList<com.example.lizhongbi.fragment.TouchEventView.point>>();
    public void onTouch(View v,Canvas canvas)
    {

        //int index = (int) (Math.random()*6);
        //picsrc = picsrcs[index];
        Drawable d = getResources().getDrawable(R.drawable.line);
        //ImageView firstImage = (ImageView) findViewById(R.id.image);
        //int imageResource = getResources().getIdentifier(picsrc, null, this.getPackageName());
        //firstImage.setImageResource(imageResource);
        d.setBounds(0,0,500,500);
        d.draw(canvas);
        if(!folder.exists())
        {
            var = folder.mkdir();
            folder.exists();
        }

    }

    public TouchEventView(Context ctx, AttributeSet attrs, String folderName){
        super(ctx,attrs);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5f);
        this.setBackgroundColor(Color.WHITE);
    }
    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawPath(path,paint);
        drawables[0] = getResources().getDrawable(R.drawable.line);
        drawables[1] = getResources().getDrawable(R.drawable.pic1);
        drawables[2] = getResources().getDrawable(R.drawable.pic2);
        drawables[3] = getResources().getDrawable(R.drawable.curves);
        drawables[4] = getResources().getDrawable(R.drawable.horizontalline);
        drawables[5] = getResources().getDrawable(R.drawable.images);
        if(reset) {
            if(isPrev)
            {
                isPrev = false;
                curr = previousIndex ;
            }
            else {
                previousIndex = curr;
                int index = (int) (Math.random() * 6);
                curr = index;
            }
        }
        Drawable d = drawables[curr];
        d.setBounds(0, 200, 1100, 1200);
        d.draw(canvas);
        reset=false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        /**if(folder == null) {
         //folder = new File(Environment.getRootDirectory().getAbsolutePath() + File.separator + "system" + File.separator + "trace");
         folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "trace");
         if (!folder.exists()) {
         try {
         folder.mkdir();
         System.out.println("******************************************************* create folder ****************************************************************");
         } catch (Exception e) {
         e.printStackTrace();
         }
         }
         }
         if(file == null) {
         file = new File(Environment.getRootDirectory().getAbsolutePath() + File.separator + "storage" + File.separator + "self" + File.separator + "primary" + File.separator + "trace","N_folder1_pic.csv");
         if (!file.exists()) {
         try {
         file.createNewFile();
         fileWriter  = new FileWriter(file);
         bfWriter = new BufferedWriter(fileWriter);
         System.out.println("******************************************************* create file no" + index + " ****************************************************************");
         index++;
         } catch (IOException e) {
         e.printStackTrace();
         }
         }
         }**/
        //if(file == null) {
        //}
        /**file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"");
         System.out.println(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString());
         File [] files = file.listFiles();
         System.out.println("print now:::::::::::::::::");
         for(int count = 0; count < files.length; count++)
         {
         System.out.println(files[count].getName());
         }**/

        if(isFirst) {
            // file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"");
            // System.out.println(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString());
            // File [] files = file.listFiles();
            // System.out.println("print now:::::::::::::::::");
            //for(int count = 0; count < files.length; count++)
            // {

            //    System.out.println(files[count].getName());
            // }
            //file = new File(getContext().getFilesDir(), "mydir");
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"mydir");
            if (!file.exists()) {
                file.mkdir();
                System.out.println("******************************************************* create dir ****************************************************************");
            }
            try {
                log = new File(file, "log.txt");
                logger = new FileWriter(log);
                isFirst = false;
                System.out.println(getContext().getFilesDir());
                System.out.println("******************************************************* created log ****************************************************************");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Date currentTime = Calendar.getInstance().getTime();
        float xPos = event.getX();
        float yPos = event.getY();
        //com.example.lizhongbi.fragment.TouchEventView.point p;
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //file = new File(getContext().getFilesDir(), "mydir");
                file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"mydir");
                /** if (!file.exists()) {
                 file.mkdir();
                 System.out.println("******************************************************* create dir"  + " ****************************************************************");
                 }**/

                try {
                    if(!redraw) {
                        name = "N_trace_pic" + index + ".csv";
                        previousName = name;
                        gpxfile = new File(file, name);
                        writer = new FileWriter(gpxfile);
                        index ++;
                    }
                    else
                    {
                        redraw = false;
                        gpxfile = new File(file,previousName);
                        writer = new FileWriter(gpxfile);
                    }
                    logger.append(name + "\n");
                    logger.flush();
                    writer.append("X-pos,Y-pos,Timestamp \n");
                    writer.flush();
                    //System.out.println(getContext().getFilesDir());
                    System.out.println("******************************************************* " + name + " ****************************************************************");
                } catch (Exception e) {
                    e.printStackTrace();

                }
                path.moveTo(xPos, yPos);
                try {
                    writer.append(xPos + " " + yPos + "," + currentTime + " \n ");
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //System.out.println(xPos + " " + yPos + "," + currentTime);
                return true;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(xPos,yPos);
                try {
                    writer.append(xPos + " " + yPos + "," + currentTime + "\n");
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //System.out.println(xPos + " " + yPos + "," + currentTime);
                break;
            case MotionEvent.ACTION_UP:
                list.add(coordinates);
                //coordinates.clear();
                try {
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