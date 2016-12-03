#ifndef __ENVIRONMENT__
#define __ENVIRONMENT__

#include "config.h"
#include "MsgService.h"

class Environment {
	State s; 

	float distance;
	bool touch;

	MsgService* channel;
	void initChannel(int RXpin, int TXpin);	
public:
	void init(int RXpin, int TXpin);

	void setDistance(float);
	float getDistance();

	void setTouched(bool);
	bool getTouched();

	State getState();
	void setState(State value);

	MsgService* getChannel();
};

#endif
