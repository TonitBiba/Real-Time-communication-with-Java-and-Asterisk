import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import webphone.webphone;

public class SIPNotificationsUDP extends Thread
{
   boolean terminated = false;
   byte[] buf = null;
   DatagramSocket socket = null;
   DatagramPacket packet = null;

   public SIPNotificationsUDP(webphone webphoneobj_in){}
   
   public boolean Start()
   {
       try{
           InetSocketAddress localaddress = new InetSocketAddress("127.0.0.1", 19421);
           try{
               socket = new DatagramSocket(localaddress);
           }catch(Exception e)
           {
               socket = new DatagramSocket();
           }
           buf = new byte[3600];
           packet = new DatagramPacket(buf, buf.length);
           terminated = false;
           this.start();
           System.out.println("sip notifications started");
           return true;
       }catch(Exception e) {System.out.println("Exception at SIPNotificationsUDP Start: "+e.getMessage()+"\r\n"+e.getStackTrace()); }
       return false;
   }

   public boolean Send(String ip, int port, String msg)
   {
       try{
           byte[] buf = msg.getBytes();
           socket.send(new DatagramPacket(buf, buf.length,new InetSocketAddress(ip, port)));
           return true;
       }catch(Exception e) {}  
       return false;
   }

   public void Ping()
   {
       Send("127.0.0.1", 19422, "BOFCOMMANDBOFLINEfunction=API_TestEOFLINEEOFCOMMAND");
   }
   
   public void Stop()
   {
       try{
           terminated = true;
           if (socket != null)
               socket.close();
       }catch(Exception e) { }
   }

   public void run()
   {
        try{
            while (!terminated) {
                packet.setData(buf, 0, buf.length);
                packet.setLength(buf.length);
                socket.receive(packet);
                if (packet != null && packet.getLength() > 0) {
                    String str = new String(packet.getData(), 0,
                                            packet.getLength());
                    ProcessNotifications(str);
                }
            }
        }catch(Exception e)
        {
           if(!terminated) System.out.println("Exception at SIPNotificationsUDP run: "+e.getMessage()+"\r\n"+e.getStackTrace());
        }
    }

    public void ProcessNotifications(String msg)
    {
        try{
            System.out.println("\tREC FROM JVOIP: " + msg);
        }catch(Exception e) { System.out.println("Exception at SIPNotificationsUDP ProcessNotifications: "+e.getMessage()+"\r\n"+e.getStackTrace()); }
    }
}

