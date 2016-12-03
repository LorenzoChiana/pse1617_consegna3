#include "config.h"

#include "Task.h"
#include "DistanceTask.h"
#include "LedTask.h"
#include "Scheduler.h"


Scheduler sched;
Environment *env;

void setup() {
  Serial.begin(9600);

  Environment* env = new Environment();
  env->init(PIN_TX, PIN_TX);

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
