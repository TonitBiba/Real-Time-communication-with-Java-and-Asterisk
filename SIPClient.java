import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.teamdev.jxcapture.video.VideoSource;
import webphone.*;
							
public class SIPClient {
private static String password;				
public static String extension;				
private static webphone objWebPhone;			
private static int activeLine;   			

	public SIPClient(String extension,String password) {
		this.extension=extension;
		this.password = password;
		connectWithServer();   
	}

	private void connectWithServer() {
		try {
			objWebPhone = new webphone();
			objWebPhone.API_SetParameter("logconsole", "false");			
			objWebPhone.API_SetParameter("polling", "3");				
			objWebPhone.API_SetParameter("startsipstack", "1"); 
			objWebPhone.API_SetParameter("register", "2"); 					
			objWebPhone.API_SetParameter("serveraddress", "10.20.35.57");	
			objWebPhone.API_SetParameter("username", extension);			
			objWebPhone.API_SetParameter("sippassword",password);			
			objWebPhone.API_SetParameter("loglevel", "1");				
			objWebPhone.API_Start();
			this.activeLine = objWebPhone.API_GetLine();		
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Error :"+e);
		}	
	}

	public void call(String extension) {
		objWebPhone.API_Call(activeLine, extension);	
	}

	public void endCall() {
		objWebPhone.API_Hangup(-1);
	}

	public void microphoneSpeaker(){
		objWebPhone.API_AudioDevice();
	}

	public void conferenceCall(String extension) {
		objWebPhone.API_Conf(extension);
	}

	public void transferCall(String extension) {
	objWebPhone.API_TransferDialog();
	}
}















