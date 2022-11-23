import java.util.ArrayList;

public class Message implements Runnable{ // MESSAGE CLASS IMPLEMENTED TO STORE ALL MESSAGE DATA INCLUDING USERNAME, ID, POSTDATE, SUBJECT, CONTENT

    public static ArrayList<Message> PublicMessages = new ArrayList<Message>();
    public static ArrayList<Message> Messages1= new ArrayList<Message>();
    public static ArrayList<Message> Messages2= new ArrayList<Message>();
    public static ArrayList<Message> Messages3= new ArrayList<Message>();
    public static ArrayList<Message> Messages4= new ArrayList<Message>();
    public static ArrayList<Message> Messages5= new ArrayList<Message>();

    public String user;
    public int ID;   
    public String postDate;
    public String subjectMessage;
    public String messageContent;
    public String groupNumber;   

    public Message(int ID, String user, String postDate, String subjectMessage, String messageContent, String groupNumber) //CONSTRUCTOR TO SAVE THESE THINGS TO THE OBJECT 
    {
        this.ID = ID; 
        this.user = user; 
        this.postDate = postDate; 
        this.subjectMessage = subjectMessage;
        this.messageContent = messageContent;
        this.groupNumber = groupNumber;
         
    }

    public void add(Message m, String groupNumber) //ADDS MESSAGES TO THE GLOBAL ARRAY THAT SAVES ALL THE MESSAGES
    {
        if(groupNumber.equals("0"))
        {
            PublicMessages.add(m); 
        }
        else if(groupNumber.equals("100"))
        {
            Messages1.add(m);
        }
        else if(groupNumber.equals("200"))
        {
            Messages2.add(m);
        }
        else if(groupNumber.equals("300"))
        {
            Messages3.add(m); 
        }
        else if(groupNumber.equals("400"))
        {
            Messages4.add(m); 
        }
        else if(groupNumber.equals("500"))
        {
            Messages5.add(m); 
        }
    }


    public Message(){}

    @Override
    public void run() {
        // TODO Auto-generated method stub
        
    }

    public String returnContents(int msgID, String groupNumber) // RETURNS CONTENTS OF A MESSAGE FROM THE MESSAGE ID 
    {

        if(groupNumber.equals("0"))
        {
            for(Message m: PublicMessages)
            {
                if(m.ID == msgID)
                {
                    return m.messageContent;
                }
            }
        }
        else if(groupNumber.equals("100"))
        {
            for(Message m: Messages1)
             {
                if(m.ID == msgID)
                {
                    return m.messageContent;
                }
            }
        }
        else if(groupNumber.equals("200"))
        {
            for(Message m: Messages2)
             {
                if(m.ID == msgID)
                {
                    return m.messageContent;
                }
            }
        }
        else if(groupNumber.equals("300"))
        {
            for(Message m: Messages3)
            {
               if(m.ID == msgID)
               {
                   return m.messageContent;
               }
           }
        }
        else if(groupNumber.equals("400"))
        {
            for(Message m: Messages4)
             {
                if(m.ID == msgID)
                {
                    return m.messageContent;
                }
            }
        }
        else if(groupNumber.equals("500"))
        {
            for(Message m: Messages5)
             {
                if(m.ID == msgID)
                {
                    return m.messageContent;
                }
            }
        }

        return "SERVER: Invalid Message ID"; 
    }

}

 
