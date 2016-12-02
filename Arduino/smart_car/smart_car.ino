#include "config.h"

Scheduler sched;
Envirnoment *env;

void setup() {
  Serial.begin(9600);

  Envirnoment = new Envirnoment();
  Envirnoment->init();

  sched.init(CLOCK);

  Task* distanceTask = new DistanceTask(ECHO_PIN, TRIG_PIN, env);
  distanceTask->init(3*CLOCK);
  sched.addTask(distanceTask);

  Task* ledTask = new LedTask(L1_PIN, L2_PIN, env);
  distanceTask->init(3*CLOCK);
  sched.addTask(distanceTask);

}

void loop() {
  sched.schedule();
}
