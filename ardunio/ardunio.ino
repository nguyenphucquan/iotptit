#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include <DHT.h>

const char *ssid = "Khong Dat Pass";
const char *password = "passkhongdat";
const char *mqtt_broker = "192.168.1.223";
const char *mqtt_topic_led = "esp8266/led";
const char *mqtt_topic_fan = "esp8266/fan";
const char *mqtt_topic_ac = "esp8266/ac";  // Sửa chủ đề của điều hòa
const char *mqtt_topic_temp = "dht/temperature";
const char *mqtt_topic_hum = "dht/humidity";
const char *mqtt_topic_light = "light/lux";
const char *mqtt_topic_gas = "gas/sensor";

const int mqtt_port = 1883;
const int LED_PIN = 5;  // GPIO 5 D1
const int FAN_PIN = 13; // GPIO 13 D7
const int DHT_PIN = 14; // GPIO 14 D5
const int LDR_PIN = A0; // Chân analog A0 nối với cảm biến ánh sáng
const int AC_PIN = 2;   // GPIO 2 D4

#define DHTTYPE DHT11
DHT dht(DHT_PIN, DHTTYPE);

WiFiClient espClient;
PubSubClient mqttClient(espClient);

bool ledState = false;

void callback(char *topic, byte *payload, unsigned int length) {
  String message;
  for (int i = 0; i < length; i++) {
    message += (char)payload[i];
  }

  if (strcmp(topic, mqtt_topic_led) == 0) {
    Serial.print("Message arrived in topic: ");
    Serial.println(topic);
    Serial.print("Message: ");
    Serial.println(message);

    if (message == "on" && !ledState) {
      digitalWrite(LED_PIN, HIGH);
      ledState = true;
    }
    if (message == "off" && ledState) {
      digitalWrite(LED_PIN, LOW);
      ledState = false;
    }

    Serial.println("-----------------------");
  } else if (strcmp(topic, mqtt_topic_fan) == 0) {
    Serial.print("Message arrived in topic: ");
    Serial.println(topic);
    Serial.print("Message: ");
    Serial.println(message);

    if (message == "on") {
      digitalWrite(FAN_PIN, LOW);
    }
    if (message == "off") {
      digitalWrite(FAN_PIN, HIGH);
    }
    
    Serial.println("-----------------------");
  } else if (strcmp(topic, mqtt_topic_ac) == 0) {
    Serial.print("Message arrived in topic: ");
    Serial.println(topic);
    Serial.print("Message: ");
    Serial.println(message);

    if (message == "on") {
      digitalWrite(AC_PIN, HIGH);
    } else if (message == "off") {
      digitalWrite(AC_PIN, LOW);
    }

    Serial.println("-----------------------");
  }
}

void setup() {
  Serial.begin(9600);
  delay(500);

  pinMode(LED_PIN, OUTPUT);
  digitalWrite(LED_PIN, LOW);

  pinMode(AC_PIN, OUTPUT);
  digitalWrite(AC_PIN, LOW);

  pinMode(FAN_PIN, OUTPUT);
  digitalWrite(FAN_PIN, HIGH);

  dht.begin();

  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.println("Đang kết nối tới WiFi...");
  }
  Serial.println("Đã kết nối tới WiFi");

  mqttClient.setServer(mqtt_broker, mqtt_port);
  mqttClient.setCallback(callback);
  mqttClient.subscribe(mqtt_topic_led);
  mqttClient.subscribe(mqtt_topic_fan);
  mqttClient.subscribe(mqtt_topic_ac);  // Đăng ký chủ đề của điều hòa
  while (!mqttClient.connected()) {
    if (mqttClient.connect("esp8266-client")) {
      Serial.println("Đã kết nối tới MQTT broker");
      mqttClient.subscribe(mqtt_topic_led);
      mqttClient.subscribe(mqtt_topic_fan);
      mqttClient.subscribe(mqtt_topic_ac);  // Đăng ký chủ đề của điều hòa
    } else {
      Serial.print("Kết nối MQTT thất bại, rc=");
      Serial.print(mqttClient.state());
      Serial.println(" Đang thử lại...");
      delay(1000);
    }
  }
}

void loop() {
  mqttClient.loop();

  float temp = dht.readTemperature();
  float hum = dht.readHumidity();

  if (mqttClient.connected()) {
    mqttClient.publish(mqtt_topic_temp, String(temp).c_str());
    mqttClient.publish(mqtt_topic_hum, String(hum).c_str());
  }

  int lightLevel = analogRead(LDR_PIN);

  if (mqttClient.connected()) {
    mqttClient.publish(mqtt_topic_light, String(lightLevel).c_str());
  }

  int gasLevel = random(90, 100);

  if (mqttClient.connected()) {
    mqttClient.publish(mqtt_topic_gas, String(gasLevel).c_str());
  }
  delay(2000);
}
