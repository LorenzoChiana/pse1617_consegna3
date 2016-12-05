#include "Arduino.h"
#include "Environment.h"

void Environment::init(int TXpin, int RXpin){
	this->setDistance(0.0);
	this->setTouched(false);
	this->setState(OFF);
	this->initChannel(TXpin, RXpin);
	lastMsg = "";
	avalible = false;
}

void Environment::initChannel(int TXpin, int RXpin){
	this->channel = new MsgService(TXpin, RXpin);
	this->channel->init();
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

MsgService* Environment::getChannel(){
	return this->channel;
}

void Environment::updateLastMsg(String msg){
	this->lastMsg = msg;
}

String Environment::getLastMsg(){
	return this->lastMsg;
}

bool Environment::isMsgAvalible(){
	return this->avalible;
}

void Environment::setMsgAvalible(bool val){
	this->avalible = val;
}


