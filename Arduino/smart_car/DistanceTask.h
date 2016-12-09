#ifndef __DISTANCETASK__
#define __DISTANCETASK__

#include "Task.h"
#include "Sonar.h"
#include "Environment.h"

class DistanceTask: public Task {
	int echoPin, triggerPin;

	Sonar* proximitySensor;
	Environment* env;
public:
	DistanceTask(int echoPin, int triggerPin, Environment* env);  
	void init(int period);  
	void tick();
};

#endif
