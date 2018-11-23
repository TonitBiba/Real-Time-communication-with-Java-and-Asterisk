import webphone.webphone;
public class SIPNotifications extends Thread
{
   boolean terminated = false;
   webphone webphoneobj = null;
   
   public SIPNotifications(webphone webphoneobj_in)
   {
       webphoneobj = webphoneobj_in;
   }

   public boolean Start()
   {

       try{
           this.start();
           System.out.println("sip notifications started");
           return true;
       }catch(Exception e) {System.out.println("Exception at SIPNotifications Start: "+e.getMessage()+"\r\n"+e.getStackTrace()); }
       return false;
   }

   public void Stop()
   {
       try{
           terminated = true;
       }catch(Exception e) {}
   }

   public void run()
   {
        try{
           String sipnotifications = "";
           String[] notarray = null;

           while (!terminated)
           {
                  sipnotifications = webphoneobj.API_GetNotificationsSync();
                  if (sipnotifications != null && sipnotifications.length() > 0)
                  {
                      System.out.println("\tREC FROM JVOIP: " + sipnotifications);
                      notarray = sipnotifications.split("\r\n");
                      if (notarray == null || notarray.length < 1)
                      {
                         if(!terminated) Thread.sleep(1); 
                      }
                      else
                      {
                        for (int i = 0; i < notarray.length; i++)
                        {
                            if (notarray[i] != null && notarray[i].length() > 0)
                            {
                                ProcessNotifications(notarray[i]);
                            }
                        }
                      }
                  }
                  else
                  {
                      if(!terminated) Thread.sleep(1);
                  }
           	}

        }catch(Exception e)
        {
           if(!terminated) System.out.println("Exception at SIPNotifications run: "+e.getMessage()+"\r\n"+e.getStackTrace());
        }
    }

    public void ProcessNotifications(String msg)
    {
        try{
        }catch(Exception e) { System.out.println("Exception at SIPNotifications ProcessNotifications: "+e.getMessage()+"\r\n"+e.getStackTrace()); }
    }
}

