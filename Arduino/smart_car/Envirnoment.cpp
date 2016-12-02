#include "Envirnoment.h"
#include "Arduino.h"

Envirnoment::Envirnoment(){}

void Envirnoment::init(){
	this->setDistance(0.0);
	this->setTouched(false);
}

void Envirnoment::setDistance(float value){
	this->distance = value;
}
float getDistance(){
	return this->distance;
}

void setTouched(bool);
bool getTouched();