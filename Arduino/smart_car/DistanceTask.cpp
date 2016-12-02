#include "DistanceTask.h"
#include "Arduino.h"
#include "config.h"

DistanceTask::DistanceTask(int echoPin, int triggerPin, Envirnoment* env){
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
	State currentState = this->env.getState();
	switch(currentState){
		case OFF:
			break;
		case MOVING:
			break;
		case PARKING:
			break;
	}
}
