#include "Arduino.h"
#include "Environment.h"

void Environment::init(){
	this->setDistance(0.0);
	this->setTouched(false);
	this->setState(OFF);
}

void Environment::setDistance(float value){
	this->distance = value;
}
float Environment::getDistance(){
	return this->distance;
}

void Environment::setTouched(bool value){
	this->touch = value;
}
bool Environment::getTouched(){
	return this->touch;
}

void Environment::setState(State value){
	this->s = value;
}

State Environment::getState(){
	return this->s;
}
