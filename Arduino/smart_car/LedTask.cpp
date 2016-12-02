#include "LedTask.h"
#include "Arduino.h"
#include "config.h"

LedTask::LedTask(int pinL1, int pinL2, Envirnoment* env){
	this->pinL1 = pinL1;
	this->pinL2 = pinL2;
	this->env = env;
}

void LedTask::init(int period){
	Task::init(period);
	l1 = new Led(this->pinL1); 
	l2 = new Led(this->pinL2); 
	currentTime = intialTime = 0;   
}

void LedTask::tick(){
	State currentState = env.getState();
	switch(currentState){
		case OFF:
			//Mettere tutto off
			l1->switchOff();
			l2->switchOff();
		break;
		case MOVING:
			l1->switchOn();
			float currentDistance = env->getDistance();
			if (currentDistance > DMAX){
				//Manda messaggio con scritto “Presenza veicolo - distanza: d”
			}
			if (currentDistance < DMIN){
				l2->switchOn();
			}
		break;
		case PARKING:
			//L1 deve pulsare 
			//Se è stato premuto attiva due secondi L2 e invia messaggio di posizione geografica + mail se c'è notifica

		break;
	}
}
