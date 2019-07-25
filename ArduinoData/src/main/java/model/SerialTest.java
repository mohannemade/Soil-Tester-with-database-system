package model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import gnu.io.CommPortIdentifier; 
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent; 
import gnu.io.SerialPortEventListener; 
import java.util.Enumeration;


public class SerialTest implements SerialPortEventListener {
	
SerialPort serialPort;
private int i=1;
    /** The port we're normally going to use. */
private static final String PORT_NAMES[] = {
        "COM4", // Windows
};
private BufferedReader input;
private OutputStream output;
private static final int TIME_OUT = 2000;
private static final int DATA_RATE = 9600;

public void initialize() {
    CommPortIdentifier portId = null;
    Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

    //First, Find an instance of serial port as set in PORT_NAMES.
    while (portEnum.hasMoreElements()) {
        CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
        for (String portName : PORT_NAMES) {
            if (currPortId.getName().equals(portName)) {
                portId = currPortId;
                break;
            }
        }
    }
    if (portId == null) {
        System.out.println("Could not find COM port.");
        return;
    }

    try {
        serialPort = (SerialPort) portId.open(this.getClass().getName(),
                TIME_OUT);
        serialPort.setSerialPortParams(DATA_RATE,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);

        // open the streams
        input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
        output = serialPort.getOutputStream();

        serialPort.addEventListener(this);
        serialPort.notifyOnDataAvailable(true);
    } catch (Exception e) {
        System.err.println(e.toString());
    }
}


public synchronized void close() {
    if (serialPort != null) {
        serialPort.removeEventListener();
        serialPort.close();
    }
}

public synchronized void serialEvent(SerialPortEvent oEvent) {
	
   if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
        try {
        	Connection con= DriverManager.getConnection("jdbc:h2:tcp://localhost/~/javatest","Sa@123","Sa@123");
            String inputLine=null;
           int id=0;
           String str;
            if (input.ready()) {
            	id=getInt();
                inputLine = input.readLine();
                str=getDateTime();
                            System.out.println(id+" %Moisture- "+inputLine+ "-"+str);  
                            String s1="INSERT INTO TEST5 values (?,?,?)";
                            PreparedStatement ps=con.prepareStatement(s1);
                            ps.setInt(1,id);
                            ps.setString(2,inputLine);
                            ps.setString(3,str);
                            ps.execute();
                            //n++;
            }
            
           

        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
    // Ignore all the other eventTypes, but you should consider the other ones.
}

public static void main(String[] args) throws Exception {
    SerialTest main = new SerialTest();
    main.initialize();
    
    
    
    
    Thread t=new Thread() {
        public void run() {
            //the following line will keep this app alive for 1000    seconds,
            //waiting for events to occur and responding to them    (printing incoming messages to console).
            try {Thread.sleep(10);} catch (InterruptedException    ie) {}
        }
    };
    t.start();
    System.out.println("Started");
    
}

	public int getInt()
	{
		return i++;
	}
	
	public String getDateTime()
	{
		 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		   LocalDateTime now = LocalDateTime.now();
		String str=dtf.format(now);
		  return str;
	}
}

