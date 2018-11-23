import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JWindow;
import javax.swing.Timer;
import com.teamdev.jxcapture.Codec;
import com.teamdev.jxcapture.EncodingParameters;
import com.teamdev.jxcapture.ImageCapture;
import com.teamdev.jxcapture.VideoCapture;
import com.teamdev.jxcapture.audio.AudioCodec;
import com.teamdev.jxcapture.audio.AudioEncodingParameters;
import com.teamdev.jxcapture.audio.AudioSource;
import com.teamdev.jxcapture.video.FullScreen;
import com.teamdev.jxcapture.video.VideoFormat;
import com.teamdev.jxcapture.video.VideoSource;

public class VideoRecording {
	private static int mode;								 //0 Per fullScreen; 1 vetem per regjistrimin e imazheve nga video e integruar ose e jashtme.
	private static  EncodingParameters encodingParameters;   
	private static VideoCapture objVideoCapture;			
	private static WebCameraView objShowCamera;				
	private static int cameraChoosen;						
	
	public void setMode(int mode,int cameraChoosen) {			
		this.mode = mode;
		this.cameraChoosen = cameraChoosen;
	}
	
	public static void startRecording(JButton btnRegjistro) {
		try {
		if(mode==0) {
			objVideoCapture = VideoCapture.create();     						//Per modin  fullscreen video.
			objVideoCapture.setVideoSource(new FullScreen());						
			List<VideoSource> availableVideoSources = VideoSource.getAvailable();		
			if (availableVideoSources.isEmpty()) {
				throw new IllegalStateException("No external video sources available");
	        }
			ImageCapture webCameraImageCapture = ImageCapture.create();
			List<Codec> videoCodecs = objVideoCapture.getVideoCodecs();					
			Codec videoCodec = videoCodecs.get(2);
		    JFileChooser objFileChooser = new JFileChooser();							
		    objFileChooser.setDialogTitle("Zgjedhni lokacioni ku deshironi te ruani fajllin");
		    objFileChooser.showSaveDialog(null);
		    File path = objFileChooser.getSelectedFile();
		    encodingParameters = new EncodingParameters(new File(path.getPath()+".wmv"));
		    encodingParameters.setBitrate(1000000);
		    encodingParameters.setFramerate(10);
		    encodingParameters.setCodec(videoCodec);
		    
		     List<AudioSource> audioSources = AudioSource.getAvailable();				
		    for (AudioSource audioSource : audioSources) {
		       }
		    if (audioSources.isEmpty()) {
		        JOptionPane.showMessageDialog(null, "Nuk ka asnje burim te audios.");
		    } else {
		        AudioSource audioSource = audioSources.get(0);
		        objVideoCapture.setAudioSource(audioSource);
	        List<AudioCodec> audioCodecs = objVideoCapture.getAudioCodecs();
	        if (audioSources.isEmpty()) {
	            JOptionPane.showMessageDialog(null, "Nuk eshte gjetur asnje koder i audios");
	        	} 
	        else {
	            for (AudioCodec audioCodec : audioCodecs) {
	            }
	            AudioEncodingParameters audioEncoding = new AudioEncodingParameters();
	            AudioCodec audioCodec = audioCodecs.get(0);
	            audioEncoding.setCodec(audioCodec);
	            encodingParameters.setAudioEncoding(audioEncoding);
	        }
	    }
	    
	    objShowCamera = new WebCameraView(webCameraImageCapture);
	    objShowCamera.show();
	    objVideoCapture.start(encodingParameters);
		}
		
		else {  
			 objVideoCapture = VideoCapture.create(VideoFormat.WMV);   
			   List<VideoSource> availableVideoSources = VideoSource.getAvailable();
		        if (availableVideoSources.isEmpty()) {
		            JOptionPane.showMessageDialog(null, "Nuk eshte e kyqur asnje kamerë në këtë pajisje.");
		        }
		        else {
		        VideoSource webCamera = availableVideoSources.get(cameraChoosen); 
		        objVideoCapture.setVideoSource(webCamera);

		        java.util.List<Codec> videoCodecs = objVideoCapture.getVideoCodecs();
		        if (videoCodecs.isEmpty()) {
		            JOptionPane.showMessageDialog(null, "Nuk eshte gjetur asnje koder per video");
		        }
		        else {
		        Codec videoCodec = videoCodecs.get(2);
		        
			    JFileChooser objFileChooser = new JFileChooser();
			    objFileChooser.setDialogTitle("Zgjedhni lokacioni ku deshironi te ruani video fajllin");
			    objFileChooser.showSaveDialog(null);
			    File path = objFileChooser.getSelectedFile();
		        
		        EncodingParameters encodingParameters = new EncodingParameters(new File(path.getPath()+".wmv"));
		        encodingParameters.setBitrate(500000);
		        encodingParameters.setFramerate(10);
		        encodingParameters.setKeyFrameInterval(1);
		        encodingParameters.setCodec(videoCodec);

			     List<AudioSource> audioSources = AudioSource.getAvailable();
			    for (AudioSource audioSource : audioSources) {
			       }
			    if (audioSources.isEmpty()) {
			        JOptionPane.showMessageDialog(null, "Nuk ka asnje burim te audios.");
			    } else {
			        AudioSource audioSource = audioSources.get(0);
			        objVideoCapture.setAudioSource(audioSource);

		       List<AudioCodec> audioCodecs = objVideoCapture.getAudioCodecs();
		       if (audioSources.isEmpty()) {
		           JOptionPane.showMessageDialog(null, "Nuk eshte gjetur asnje koder i audios");
		       	} 
		       else {
		           for (AudioCodec audioCodec : audioCodecs) {}
		           AudioEncodingParameters audioEncoding = new AudioEncodingParameters();
		           AudioCodec audioCodec = audioCodecs.get(0);
		           audioEncoding.setCodec(audioCodec);
		           encodingParameters.setAudioEncoding(audioEncoding);
		       }
		   } 
			    ImageCapture objImageCapture = ImageCapture.create(webCamera);
		        objVideoCapture.setEncodingParameters(encodingParameters);
		        objVideoCapture.start();
		        objShowCamera = new WebCameraView(objImageCapture);
		        objShowCamera.show();
		        }
		        }
		}
		}catch(Exception e) {
			System.out.println("Gabim gjate regjistrimit: "+e);
			btnRegjistro.doClick();
		}
	}

	
	public static void stopRecording() {
		objVideoCapture.stop();
	    objShowCamera.hide();
	}
	
	
	   private static class WebCameraView {
	        private static final Dimension fullScreenDimension = new Dimension(64, 48);   
	        private static final Dimension videoScreenDimension = new Dimension(400,320); 
	        private static final Point locationOfFullScreen = new Point(0, 0);			  
	        private static final Point locationOfVideoScreen = new Point(100,100);		  
	        private static final int updateInterval = 100;								 

	        private JWindow webCameraView;												 
	        private BufferedImage snapshot;
	        private Timer updateTimer;													  

	        public WebCameraView(final ImageCapture webCameraCapture) {
	            webCameraView = new JWindow() {
	                @Override
	                public void paint(Graphics g) {
	                    if (snapshot != null) {
	                    	if(mode==0) {
	                    		g.drawImage(snapshot, 0, 0, fullScreenDimension.width, fullScreenDimension.height, null);	
	                    	}
	                    	else {
	                    		g.drawImage(snapshot, 0, 0, videoScreenDimension.width, videoScreenDimension.height, null);
	                    	}	
	                    }
	                }
	            };
	            updateTimer = new Timer(updateInterval, new ActionListener() {
	                public void actionPerformed(ActionEvent e) {
	                    webCameraCapture.takeSnapshot();
	                    if (snapshot != null) {
	                        snapshot.flush();
	                    }
	                    snapshot = webCameraCapture.getImage();
	                    webCameraView.repaint();
	                }
	            });
	        }
	        public void show() {
	        	if(mode==0) {
		        	webCameraView.setLocation(locationOfFullScreen);
		            webCameraView.setSize(fullScreenDimension);	  
	        	}
	        	else {
		        	webCameraView.setLocation(locationOfVideoScreen);
		            webCameraView.setSize(videoScreenDimension);	  
	        	}
      		
	            webCameraView.setAlwaysOnTop(true);
	            webCameraView.setVisible(true);
	            updateTimer.start();
	        }
	        public void hide() {
	            updateTimer.stop();
	            webCameraView.dispose();
	        }
	    }	
}
