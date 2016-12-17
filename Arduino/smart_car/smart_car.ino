#include <Arduino.h>
#include "config.h"

#include "Task.h"
#include "DistanceTask.h"
#include "LedTask.h"
#include "PressionTask.h"
#include "ControllerTask.h"
#include "Scheduler.h"

/*
 * Matteo Minardi
 * Lorenzo Chiana
 * Consegna tre
 */

#define debug

Scheduler sched(5);
Environment* env;

void setup() {
  Serial.begin(9600);
  //Oggetto della classe che contiente il canale di comunicazioni e le variabili globali
  Environment* env = new Environment();
  env->init(PIN_RX, PIN_TX);
  sched.init(CLOCK);
  //Task che processa gli input dell'utente per il controllo degli stati 
  Task* controllerTask = new ControllerTask(env);
  controllerTask->init(3*CLOCK);
  sched.addTask(controllerTask);
  //Task che monitora la distanza, la tiene aggiornata e manda messaggi se necessario
  Task* distanceTask = new DistanceTask(ECHO_PIN, TRIG_PIN, env);
  distanceTask->init(CLOCK);
  sched.addTask(distanceTask);
  //Task che controlla la pressione del punsante, ne invia i messaggi di contatto e
  //Permette di pilotare il servo dinamicamente con un protocollo di comunicazione con l'utente
  Task* pressionTask = new PressionTask(T1_PIN, SERVO_PIN, env);
  pressionTask->init(CLOCK);
  sched.addTask(pressionTask);
  //Task modulare che contiene la gestione della parte output led. Ne gestisce le operazioni in base alla situazione del sistema
  Task* ledTask = new LedTask(L1_PIN, L2_PIN, env);
  ledTask->init(2*CLOCK);
  sched.addTask(ledTask); 
}

void loop() {
  sched.schedule();
}
