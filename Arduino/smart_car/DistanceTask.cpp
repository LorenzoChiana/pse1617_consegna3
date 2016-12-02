#include "DetectPresenceTask.h"
#include "Arduino.h"
#include "config.h"

DetectPresenceTask::DetectPresenceTask(int echoPin, int triggerPin, GlobalState* Global){
	this->echoPin = echoPin;
	this->triggerPin = triggerPin;
  this->Global = Global;
}

void DetectPresenceTask::init(int period){
	Task::init(period);
	proximitySensor = new Sonar(this->echoPin, this->triggerPin); 
	this->isNear = false;   
}

void DetectPresenceTask::tick(){
	//Set the distance in global
	float distance = proximitySensor->getDistance();
	Global->setDistance(distance);
	//If he is near and leaves the DMAX_WC distance
	if (this->isNear){
		if (distance > DMAX_WC){
			Global->setProssimity(false);
			this->isNear = false;
			Global->setFlush(true);
		}
	} else {
	//If he isn't near and enters the DMIN_WC distance
		if (distance <= DMIN_WC){
			Global->setProssimity(true);
			this->isNear = true;
		}
	}
}
