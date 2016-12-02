#ifndef __ENVIRONMENT__
#define __ENVIRONMENT__

#include "config.h"

class Environment {
	State s; 

	float distance;
	bool touch;
	
public:

	void setDistance(float);
	float getDistance();

	void setTouched(bool);
	bool getTouched();

	State getState();
	void setState(State);
}

#endif
