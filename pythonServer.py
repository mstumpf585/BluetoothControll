from Adafruit_MotorHAT import Adafruit_MotorHAT, Adafruit_DCMotor

import os
import time
import atexit
from bluetooth import *

#create object 
mh = Adafruit_MotorHAT(addr=0x60)

#auto disable motors 

def turnOffMotors():
    mh.getMotor(1).run(Adafruit_MotorHAT.RELEASE)
    mh.getMotor(2).run(Adafruit_MotorHAT.RELEASE)
    mh.getMotor(3).run(Adafruit_MotorHAT.RELEASE)
    mh.getMotor(4).run(Adafruit_MotorHAT.RELEASE)

atexit.register(turnOffMotors)

################################# DC motor test!
myMotor = mh.getMotor(3)

# set the speed to start, from 0 (off) to 255 (max speed)
myMotor.setSpeed(150)
myMotor.run(Adafruit_MotorHAT.FORWARD);
# turn on motor
myMotor.run(Adafruit_MotorHAT.RELEASE);

#while True:
#   print(read_temp())  
#   time.sleep(1)


server_sock=BluetoothSocket( RFCOMM )
server_sock.bind(("",PORT_ANY))
server_sock.listen(1)

port = server_sock.getsockname()[1]

uuid = "94f39d29-7d6d-437d-973b-fba39e49d4ee"

advertise_service( server_sock, "motorPiControl",
                   service_id = uuid,
                   service_classes = [ uuid, SERIAL_PORT_CLASS ],
                   profiles = [ SERIAL_PORT_PROFILE ], 
#                   protocols = [ OBEX_UUID ] 
                    )

print "Waiting for connection on RFCOMM channel %d" % port

client_sock, client_info = server_sock.accept()
print "Accepted connection from ", client_info

while True:           

    print "top"
    try:
            data = client_sock.recv(1024)
            if len(data) == 0: break
            print "received [%s]" % data

            if data == 'fwd':
                data = 'fwd!'
                print "Forward! "
                myMotor.run(Adafruit_MotorHAT.FORWARD)
                
            elif data == 'left':
            
                data = 'left!'
            
            elif data == 'right':
            
                data = 'right!'

            elif data == 'rev':

                data = 'rev!'
                print "Backward! "
                myMotor.run(Adafruit_MotorHAT.BACKWARD)
                        
            else:
                data = 'something is up!'
                print "Release"
                myMotor.run(Adafruit_MotorHAT.RELEASE)
                time.sleep(1.0)
                        
               # client_sock.send(data)
               # print "sending [%s]" % data

    except IOError:
        print "hit except"
        pass

    except KeyboardInterrupt:

        print "disconnected"
        myMotor.run(Adafruit_MotorHAT.RELEASE)
        client_sock.close()
        server_sock.close()
        print "all done"

        break

        
