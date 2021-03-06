package org.placebooks.www;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Gallery;
import android.widget.MediaController;
import android.widget.VideoView;
import android.widget.TextView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class VideoViewer extends Activity {
	
 private String videoFile;
 private String packagePath;
 private VideoView videoView;
 private MediaController ctlr;
	
	
	 @Override
 	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);	//icicle

	        //View v = new View(this);
	        //LinearLayout ll = new LinearLayout(this);
	        getWindow().setWindowAnimations(0);	//do not animate the view when it gets pushed on the screen

	        setContentView(R.layout.videoview);
	        
	        
	         // get the extras (video filename) out of the new intent
	        Intent intent = getIntent();
	        if(intent != null) videoFile = intent.getStringExtra("video");
	        if(intent != null) packagePath = intent.getStringExtra("path");
	        
	        File clip=new File(Environment.getExternalStorageDirectory(), "/placebooks/unzipped" + packagePath + "/" + videoFile);

	        try{
				if (clip.exists()) {
	
						videoView = new VideoView(VideoViewer.this);
						videoView.setVideoPath(clip.getAbsolutePath());
	
						videoView.setLayoutParams(new Gallery.LayoutParams(
								LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	
						ctlr=new MediaController(VideoViewer.this);
						ctlr.setMediaPlayer(videoView);
						videoView.setMediaController(ctlr);
						videoView.requestFocus();
						videoView.start();
	                    setContentView(videoView);
		        
		        
				}
				else{
					TextView tv = new TextView(this);
					tv.setText("File does not exist");
					setContentView(tv);
					
					
				}
	        }
	        
	        catch (OutOfMemoryError E) {
			    // release some (all) of the above objects
					System.out.println("Out of Memory Exception");
					TextView txtView = new TextView(VideoViewer.this);
					txtView.setText("Error: Out of Memory - video file is too big to load!");
					setContentView(txtView);
			}
	 }
	 
	 @Override
     public void onDestroy() {
       super.onDestroy();
	      videoView = null;
	      packagePath = null;
	      videoFile = null;
	      ctlr = null;

         System.gc();	//call the garbage collector
         finish();	//close the activity
       
	   }
	 
}
