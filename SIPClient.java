import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.teamdev.jxcapture.video.VideoSource;

import webphone.*;

										/*********************************************************
										 * Klasa per krijimin e SIP(Session Initiated Protocol)  *
										 * klientave. Kjo klase eshte krijuar duke shfrytezuar   *
										 * funksionet e gatshme të JVoIP.jar.Ne kete klase       *
										 * mundesohet krijimi i lidhjes me serverin e hostuar    *
										 * ne VMware(Asterisk), si dhe te gjitha funksionalitetet* 
										 * qe lidhen me komunikimin permes zerit ne kohe reale.  *
										 *********************************************************/

public class SIPClient {
private static String password;						//Paswordi i regjistruar ne Asterisk. Ne menyre te nenkuptuar eshte 0000, por mund te ndryshohet sipas kerkeses.
public static String extension;						//Numri unik i cili perdoret per te regjistruar dhe thirrur personat.
private static webphone objWebPhone;				//Instancimi i klases webPhone nga libraria JVoIP.jar.
private static int activeLine;   					//Linja aktive qe do perdoret per te bere thirrje.

//Ne momentin qe thirret klasa SIPClient duhet qe te jepet extension dhe fjalekalimi perkates.
	public SIPClient(String extension,String password) {
		this.extension=extension;
		this.password = password;
		connectWithServer();   
	}
//Metoda per krijimin e lidhjes me server.
	private void connectWithServer() {
		try {
			objWebPhone = new webphone();
			objWebPhone.API_SetParameter("logconsole", "false");			//Te mos paraqitet dritarja per log-a.
			objWebPhone.API_SetParameter("polling", "3");					//
			objWebPhone.API_SetParameter("startsipstack", "1"); 
			objWebPhone.API_SetParameter("register", "2"); 					
			objWebPhone.API_SetParameter("serveraddress", "10.20.35.57");	//Ip adresa e serverit Asterisk.
			objWebPhone.API_SetParameter("username", extension);			//Username eshte extensionin i perdoruesit.
			objWebPhone.API_SetParameter("sippassword",password);			//sippassword eshte fjalekalimi 0000.
			objWebPhone.API_SetParameter("loglevel", "1");					//Per fazen testuese eshte 5(te gjitha njoftimet te paraqiten ne logconsole), per fazen finale eshte 1(Nuk ka nevoj te paraqiten njoftimet).
			objWebPhone.API_Start();
			this.activeLine = objWebPhone.API_GetLine();		
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Error :"+e);
		}	
	}
//Metoda per thirrjen e personave ne varesi te extensionit.
	public void call(String extension) {
		objWebPhone.API_Call(activeLine, extension);	
	}
//Metoda per ndaljen e thirrjes aktuale.
	public void endCall() {
		objWebPhone.API_Hangup(-1);
	}
//Metoda per te caktuar pajisjet e zerit.
	public void microphoneSpeaker(){
		objWebPhone.API_AudioDevice();
	}
//Metoda per te krijuar grup me bashkebisedues.
	public void conferenceCall(String extension) {
		objWebPhone.API_Conf(extension);
	}
//Metoda per transferimin e thirrjes.
	public void transferCall(String extension) {
	objWebPhone.API_TransferDialog();
	}
}















