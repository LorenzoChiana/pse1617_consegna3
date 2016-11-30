#include "PIRSensor.h"
#include "Arduino.h"

PIRSensor::PIRSensor(int pin){
  this->pin = pin;
  pinMode(pin, INPUT);     
} 
  
bool PIRSensor::detected(){
  return digitalRead(pin) == HIGH;
}


