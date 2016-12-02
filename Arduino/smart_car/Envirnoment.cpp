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
float Envirnoment::getDistance(){
	return this->distance;
}

void Envirnoment::setTouched(bool value){
	this->touch = value;
}
bool Envirnoment::getTouched(){
	return this->touch;
}