#ifndef __PIRSENSOR__
#define __PIRSENSOR__

#include "MotionDetector.h"

class PIRSensor: public MotionDetector {
 
public: 
  PIRSensor(int pin);
  bool detected();

private:
  int pin;

};

#endif
