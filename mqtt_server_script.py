#!/usr/bin/env python
import RPi.GPIO as GPIO
import paho.mqtt.client as mqtt

# The callback for when the client receives a CONNACK response from the server.
def on_connect(client, userdata, flags, rc):
    print("Connected with result code "+str(rc))

    # Subscribing in on_connect() means that if we lose the connection and
    # reconnect then subscriptions will be renewed.
    client.subscribe("blink")

# The callback for when a PUBLISH message is received from the server.
def on_message(client, userdata, msg):
    if str(msg.payload)=="start":
        print "test"
        GPIO.output(16, 1)
    elif str(msg.payload)=="stop":
        GPIO.output(16, 0)
        
GPIO.setmode(GPIO.BCM)
GPIO.setup(16, GPIO.OUT)
client = mqtt.Client()
client.on_connect = on_connect
client.on_message = on_message

client.connect("192.168.3.33", 1883, 60)

# Blocking call that processes network traffic, dispatches callbacks and
# handles reconnecting.
# Other loop*() functions are available that give a threaded interface and a
# manual interface.
client.loop_forever()