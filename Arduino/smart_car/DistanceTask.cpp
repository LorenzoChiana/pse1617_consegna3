#include "Arduino.h"
#include "config.h"
#include "DistanceTask.h"
#include <stdio.h>
#include <math.h>

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
  	float currentDistance;
	State currentState = env->getState();
	char* distanceString;
	switch(currentState){
		case OFF:
			//Non rilevare
		break;
		case MOVEMENT:
			//Rileva la distanza		
			currentDistance = proximitySensor->getDistance();
			env->setDistance(currentDistance);

			if (currentDistance > DMAX){
				String d = String(currentDistance,3);
				String text = String("Presenza veicolo - distanza: " + d);
				//Manda messaggio con scritto “Presenza veicolo - distanza: d”
        		Msg* temp = new Msg(text);
				this->env->getChannel()->sendMsg(*temp);
				delete temp;
			}
		break;
		case PARK:
			//Non rilevare 
		break;
	}
}

