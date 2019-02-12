#include <ESP8266WiFi.h>
const char* ssid = "Saurabh";
const char* password = "12345678";
WiFiServer server(80);

void setup() {
  // put your setup code here, to run once:
Serial.begin(9600);
  pinMode(LED_BUILTIN, OUTPUT);
  
  // Connect to WiFi network
  Serial.println();
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);

  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.println("WiFi connected");

  // Start the server
  server.begin();
  Serial.println("Server started");

  // Print the IP address
  Serial.println(WiFi.localIP());

  
}

void loop() {
  // put your main code here, to run repeatedly:
int M= analogRead(A0);
  M = map(M,1024,0,0,100); 
  Serial.println(M);
  if(M<=20)
  {digitalWrite(LED_BUILTIN, LOW);}
  else
  {digitalWrite(LED_BUILTIN, HIGH);}
  delay(3000);
  
  // Check if a client has connected
  WiFiClient client = server.available();
  if (!client) {
    return;
  }
  
  // Wait until the client sends some data
  //Serial.println("new client");
  while (!client.available()) {
    delay(1);
  }
  
 
    String s = "<html lang=fr-FR><head><meta http-equiv='refresh' content='10'/>";
    s+="<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css'><script src='https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js'></script><script src='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js'></script>";
    s+="<title>My WebInfo</title>";
    s += "</head>";
    s+="<body>";
    s+="<div class='container'><div class='jumbotron'><h2><strong>WELCOME TO OUR WEB INTERFACE</strong></h2><h3><strong>Based on present sample ,current readings are listed below:</strong></h3></div><table class='table'><thead><tr><th>Sr No</th><th>Properties</th><th>Readings</th></tr></thead><tbody><tr><td>1</td><td>Moisture</td><td>";
        s+=M;
        s+="%</td></tr><tr><td>2</td><td>Salinity</td><td>";
        s+=M;
        s+="%</td></tr><tr><td>3</td><td>Acidity</td><td>";
        s+=M;
        s+="%</td></tr></tbody></table></div>";
    s+= "</body>";
    s += "</html>\n";
    client.print(s);
}
