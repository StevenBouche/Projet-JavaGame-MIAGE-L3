#pragma once
#include <mutex>
#include <map>
#include <fstream>
#include "DataStructures.h"

extern std::mutex mutexThread;
//const std::string filesPath ;// "../files/";
extern std::ofstream outfile;
extern const std::string filesPath;
extern bool isProcessingFileFinished;
extern int processedFiles;

extern std::map<int, std::string> typesSimple;
extern std::map<int, std::string> typesDouble;
extern std::map<int, std::string> typeNoChoice;

extern std::vector<Player> winners;
extern std::vector<Player> loosers;

extern std::vector<std::string> files;
extern std::vector<std::string> exploitChoices;
extern std::vector<std::string> exploitChoicesLoosers;
extern std::vector<std::string> addGoldToHammerValues;
extern std::vector<std::string> addGoldToHammerValuesLoosers;
extern std::vector<std::string> powerChoiceFaces;
extern std::vector<std::string> powerChoiceFacesLoosers;
extern std::vector<std::string> tempList;

extern std::vector<ForgeChoice> forgeChoices;
extern std::vector<ForgeChoice> forgeChoicesLoosers;

extern std::vector<HybridChoice> hybridChoices;
extern std::vector<HybridChoice> hybridChoicesLoosers;

extern std::vector<ChoiceBetweenRessources> choiceBetweenRessources;
extern std::vector<ChoiceBetweenRessources> choiceBetweenRessourcesLoosers;
extern std::vector<SatyreChoice> satyreChoices;
extern std::vector<SatyreChoice> satyreChoicesLoosers;

extern std::vector<ForgeChoice> tempForge;
extern std::vector<HybridChoice> tempHybrid;
extern std::vector<ChoiceBetweenRessources> tempChoiceBetween;
extern std::vector<SatyreChoice> tempSatyre;