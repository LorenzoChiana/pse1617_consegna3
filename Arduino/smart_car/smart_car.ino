#include "config.h"

#include "Task.h"
#include "DistanceTask.h"
#include "LedTask.h"
#include "PressionTask.h"
#include "ControllerTask.h"
#include "Scheduler.h"

Scheduler sched;
Environment *env;

void setup() {
  Serial.begin(9600);

  Environment* env = new Environment();
  env->init(PIN_RX, PIN_TX);
  env->setState(PARK);
  sched.init(CLOCK);

  Task* controllerTask = new ControllerTask(env);
  controllerTask->init(3*CLOCK);
  sched.addTask(controllerTask);
/*
  Task* distanceTask = new DistanceTask(ECHO_PIN, TRIG_PIN, env);
  distanceTask->init(3*CLOCK);
  sched.addTask(distanceTask);
  */
  Task* ledTask = new LedTask(L1_PIN, L2_PIN, env);
  ledTask->init(3*CLOCK);
  sched.addTask(ledTask);
  
  Task* pressionTask = new PressionTask(T1_PIN, SERVO_PIN, env);
  pressionTask->init(3*CLOCK);
  sched.addTask(pressionTask);

}

void loop() {
  sched.schedule();
}
