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
									
									/*******************************************************************
									 *Klasa VideoRecording ka per detyre te regjistroj imazhe video    * 
									 *nga kamera e integruar(ose e jashtme) dhe regjistrimin e ekranit *
									 * ne modin fullscreen.Kjo klase eshte realizuar duke shfrytezuar  *
									 * librarite e gatshme: jniwrap-3.12.jar, jxcapture-samples.jar,   *
									 * jxcapturedemo.jar.                                              *  
									 *******************************************************************/

public class VideoRecording {
	private static int mode;								 //0 Per fullScreen; 1 vetem per regjistrimin e imazheve nga video e integruar ose e jashtme.
	private static  EncodingParameters encodingParameters;   //Parametrat Encodues te sinjalit te zerit dhe videos.
	private static VideoCapture objVideoCapture;			
	private static WebCameraView objShowCamera;				
	private static int cameraChoosen;						 //Kamera e zgjedhur.
	
	public void setMode(int mode,int cameraChoosen) {		//Metoda per percaktimin e modit te regjistrimit dhe kameres.	
		this.mode = mode;
		this.cameraChoosen = cameraChoosen;
	}
	
	public static void startRecording(JButton btnRegjistro) {
		try {
		if(mode==0) {
			objVideoCapture = VideoCapture.create();     						//Per modin  fullscreen video.
			objVideoCapture.setVideoSource(new FullScreen());						//Burimi i videos ne kete rast eshte vet ekrani.
			List<VideoSource> availableVideoSources = VideoSource.getAvailable();		
			if (availableVideoSources.isEmpty()) {
				throw new IllegalStateException("No external video sources available");
	        }
			ImageCapture webCameraImageCapture = ImageCapture.create();
			List<Codec> videoCodecs = objVideoCapture.getVideoCodecs();					//Koderet e videos.
			Codec videoCodec = videoCodecs.get(2);
		    JFileChooser objFileChooser = new JFileChooser();							//Per percaktimin e vendit ku do te ruhet fajlli.
		    objFileChooser.setDialogTitle("Zgjedhni lokacioni ku deshironi te ruani fajllin");
		    objFileChooser.showSaveDialog(null);
		    File path = objFileChooser.getSelectedFile();
		    encodingParameters = new EncodingParameters(new File(path.getPath()+".wmv"));
		    encodingParameters.setBitrate(1000000);
		    encodingParameters.setFramerate(10);
		    encodingParameters.setCodec(videoCodec);
		    
		     List<AudioSource> audioSources = AudioSource.getAvailable();				//Per regjistrimin e sinjalit te zerit.
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
	    
	    // Trego ekrani ne anen e majte ne pjesen e siperme.
	    objShowCamera = new WebCameraView(webCameraImageCapture);
	    objShowCamera.show();
	    objVideoCapture.start(encodingParameters);
		}
		
		else {  //Per rastin kur perdoruesi deshiron te regjistroj video nga kamera.
			 objVideoCapture = VideoCapture.create(VideoFormat.WMV);   //Per kamere ruaj formatin e videos .wmv. Video mund te ruhet edhe ne formate tjera.
			   List<VideoSource> availableVideoSources = VideoSource.getAvailable();
		        if (availableVideoSources.isEmpty()) {
		            JOptionPane.showMessageDialog(null, "Nuk eshte e kyqur asnje kamerë në këtë pajisje.");
		        }
		        else {
		        VideoSource webCamera = availableVideoSources.get(cameraChoosen); //Parametri cameraChoosen jepet nga perdoruesi ne frmVoIP.
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

	//Metoda ndaljen dhe ruajtjes e regjistrimit si dhe largimin e dritares nga ekrani.
	public static void stopRecording() {
		objVideoCapture.stop();
	    objShowCamera.hide();
	}
	
	//Klasa per paraqitjen e dritares ne ekran, qe tregon se ka filluar regjistrimi.
	   private static class WebCameraView {
	        private static final Dimension fullScreenDimension = new Dimension(64, 48);   //Dimensionet e dritares ne modin fullscreen
	        private static final Dimension videoScreenDimension = new Dimension(400,320); //Dimensionet e dritares ne modin kamera.
	        private static final Point locationOfFullScreen = new Point(0, 0);			  //Lokacioni i dritares ne fullscreen.
	        private static final Point locationOfVideoScreen = new Point(100,100);		  //Lokacioni i dritares ne kamere.
	        private static final int updateInterval = 100;								  //Intervali i perditesimit te dritares me imazhe.

	        private JWindow webCameraView;												 
	        private BufferedImage snapshot;
	        private Timer updateTimer;													  //Timeri eshte perdorur per te caktuar kohen e perditesimit te dritares me imazhe te reja.

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
//Metoda per paraqitjen e dritares me imazhe.
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
//Metoda per largimin e dritares.
	        public void hide() {
	            updateTimer.stop();
	            webCameraView.dispose();
	        }
	    }	
}
