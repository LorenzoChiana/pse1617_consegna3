#include "Arduino.h"
#include "config.h"
#include "LedTask.h"

LedTask::LedTask(int pinL1, int pinL2, Environment* env){
	this->pinL1 = pinL1;
	this->pinL2 = pinL2;
	this->env = env;
}

void LedTask::init(int period){
	Task::init(period);
	l1 = new Led(this->pinL1); 
	l2 = new Led(this->pinL2); 
	currentTime = initialTime = 0;   
}

void LedTask::tick(){
  float currentDistance;
	State currentState = env->getState();
	switch(currentState){
		case OFF:
			//Mettere tutto off
			l1->switchOff();
			l2->switchOff();
		break;
		case MOVEMENT:
			l1->switchOn();
			currentDistance = env->getDistance();
			if (currentDistance > DMAX){
				//Manda messaggio con scritto “Presenza veicolo - distanza: d”
			}
			if (currentDistance < DMIN){
				l2->switchOn();
			}
		break;
		case PARK:
			//L1 deve pulsare 
			//Se è stato premuto attiva due secondi L2 e invia messaggio di posizione geografica + mail se c'è notifica

		break;
	}
}
