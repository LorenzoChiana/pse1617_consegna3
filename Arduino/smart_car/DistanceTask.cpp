#include "Arduino.h"
#include "config.h"
#include "DistanceTask.h"

DistanceTask::DistanceTask(int echoPin, int triggerPin, Environment* env){
	this->echoPin = echoPin;
	this->triggerPin = triggerPin;
	this->env = env;
}

void DistanceTask::init(int period){
	Task::init(period);
	proximitySensor = new Sonar(this->echoPin, this->triggerPin); 
	this->isNear = false;   
}

void DistanceTask::tick(){
	State currentState = env->getState();
	switch(currentState){
		case OFF:
			//Non rilevare
		break;
		case MOVEMENT:
			//Rileva la distanza
			env->setDistance(proximitySensor->getDistance());
		break;
		case PARK:
			//Non rilevare 
		break;
	}
}
