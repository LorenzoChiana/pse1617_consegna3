#ifndef __DETECTPRESENCETASK__
#define __DETECTPRESENCETASK__

#include "Task.h"
#include "Sonar.h"
#include "GlobalState.h"

class DetectPresenceTask: public Task {
	int echoPin, triggerPin;
	bool isNear;

	Sonar* proximitySensor;
	GlobalState* Global;
public:
	DetectPresenceTask(int echoPin, int triggerPin, GlobalState* Global);  
	void init(int period);  
	void tick();
};

#endif
