import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
										/**************************************************************
										 * Klasa per pranimin dhe dergimin e fajllave permes soketave.*
										 * Ne rastin tone server soketa pranon fajlla, kurse klient   *
										 * soketa dergon fajlla. Ne kete rast i bie qe server soketa  *
										 * eshte qdo here ne pritje per hapjen e soketes me klient,   *
										 * kurse klient soketa hapet vetem ne ate rast kur klienti    *
										 * deshiron te dergoj fajlla. 								  *
										 **************************************************************/

									   /*!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
										 ! Ky program garanton dergimin e fajllave deri në 1 Giga Byte,  !
										 ! nën kushte specifike të përcaktuara nga zhvilluesi i programit!
										 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/


public class SendReceiveFiles {
	
	public void sendFiles(Socket objSocket,String ipAddressServer,int port) throws UnknownHostException, IOException {
		objSocket = new Socket(ipAddressServer,port); 
		JFileChooser objFileChooser = new JFileChooser();
		objFileChooser.showOpenDialog(null);
		File objFile = objFileChooser.getSelectedFile();
		File transferFile = new File (objFile.getPath()); 
		byte [] byteArray = new byte [(int)transferFile.length()];
		FileInputStream objFileInput = new FileInputStream(transferFile); 
		BufferedInputStream objBufferedInput = new BufferedInputStream(objFileInput); 
		objBufferedInput.read(byteArray,0,byteArray.length); 
		OutputStream os = objSocket.getOutputStream(); 
		os.write(byteArray,0,byteArray.length);
		os.flush(); 
		objSocket.close(); 
	}
	
//Funksioni per pranimin e fajllave(Serveri).
	public void receiveFiles(Socket objSocket, JProgressBar objProgressBar) throws IOException {
		objProgressBar.setValue(0);		
		int progressBarValues=0;
		int fileSize = 102400000;		
		int bytesReadPerSocket =0;				
		int actualBytesRead = 0;
		byte [] byteArray = new byte [fileSize];
		progressBarValues=2;
		objProgressBar.setValue(progressBarValues);
		InputStream objInputStream = objSocket.getInputStream();
		int yesOrNo = JOptionPane.showConfirmDialog(null, "A deshironi të pranoni video fajllin nga perdoruesi :"+objSocket+"?","Konfirmo",JOptionPane.YES_NO_OPTION);
		if(yesOrNo == 0) {
			JFileChooser objFileChooser = new JFileChooser();
			objFileChooser.showSaveDialog(null);
			File path = objFileChooser.getSelectedFile();
			FileOutputStream objFileOutput = new FileOutputStream(path.getPath());
			BufferedOutputStream objBufferedOutput = new BufferedOutputStream(objFileOutput);
			bytesReadPerSocket = objInputStream.read(byteArray, 0, byteArray.length);
			actualBytesRead = bytesReadPerSocket;
			do {
				bytesReadPerSocket = objInputStream.read(byteArray,actualBytesRead,(byteArray.length-actualBytesRead));
				if(bytesReadPerSocket >= 0)
					actualBytesRead += bytesReadPerSocket;
				progressBarValues += 1;
				objProgressBar.setValue(progressBarValues);
			} while(bytesReadPerSocket > -1);
			objProgressBar.setValue(100);
			objBufferedOutput.write(byteArray,0,actualBytesRead);
			objBufferedOutput.flush();
			objBufferedOutput.close();
			objSocket.close();
		}
		else {
			objSocket.close();
		}
	}

}
