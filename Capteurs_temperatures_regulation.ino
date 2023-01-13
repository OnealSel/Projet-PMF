
// Basé sur le code de LadyAda traduit et modifie par moi, domaine public
#include "DHT.h"
#define DHT1PIN 5
#define DHT2pin 2
int peltier = 4;
float t_consigne=18;
 // broche ou l'on a branche le capteur
 
// de-commenter le capteur utilise
//#define DHTTYPE DHT11 // DHT 11
#define DHT1TYPE DHT22 
#define DHT2TYPE DHT22
// DHT 22 (AM2302)
//#define DHTTYPE DHT21 // DHT 21 (AM2301)
DHT DHT1(5, DHT22);//déclaration du capteur 1
DHT DHT2(2, DHT22);//déclaration du capteur 2
 
void setup()
{
 Serial.begin(9600);
pinMode(peltier, OUTPUT);
 Serial.println("*******CAPTEUR DES TEMPERATURES ET D'HUMIDITE******");
 DHT1.begin();
 DHT2.begin();
}
void loop()
{
 delay(2000);
 
 // La lecture du capteur prend 250ms
 // Les valeurs lues peuvet etre vieilles de jusqu'a 2 secondes (le capteur est lent)
 float h = DHT1.readHumidity();//on lit l'hygrometrie  du capteur 1
 float h2 = DHT2.readHumidity();//on lit l'hygrometrie du capteur 2
 float t = DHT1.readTemperature();//on lit la temperature en celsius 
float t2 = DHT2.readTemperature();//on lit la temperature en celsius  
 // pour lire en farenheit, il faut le paramère (isFahrenheit = true) :
 float f = DHT1.readTemperature(true);
 float f2 = DHT2.readTemperature(true);
 float alpha;
 float rosee;
 
 //On verifie si la lecture a echoue, si oui on quitte la boucle pour recommencer.
 if (isnan(h) || isnan(t) || isnan(f))
 {
   Serial.println("Echec de lecture du DHT22!");
   return;
 }
 
 // Calcul de l'indice de temperature en Farenheit
 //float hif = dht.computeHeatIndex(f, h);
 // Calcul de l'indice de temperature en Celsius
 //float hic = dht.computeHeatIndex(t, h, false);
  alpha = log(h / 100) + (17.27 * t) / (237.3 + t);
  rosee = (237.3 * alpha) / (17.27 - alpha);
 //Affichages :
 Serial.print("Humidite: ");
 Serial.print(h);
 Serial.print(" %\t");
 Serial.print("Temperature interne: ");
 Serial.print(t);
 Serial.print(" *C ");
 Serial.print(f);
 Serial.print(" *F\t");

 Serial.print("Point de rosee: ");
 Serial.print(rosee);  
 Serial.println( "*C");
 Serial.print("Temperature de l'exterieur: ");
 Serial.print(t2);
 Serial.print(" *C ");
 Serial.print(f2);
 Serial.print(" *F\n");
  Serial.println( "-------------------------------------------------------------------------------------------------------");
  
// Regulation automatique de la température du mini frigo USB
if (t<=t_consigne){
  Serial.println("Alerte Condensation");
  digitalWrite(peltier,LOW); 
}

else if(t>t_consigne){
  
  digitalWrite(peltier,HIGH); 
}
  
  delay(2000);
}