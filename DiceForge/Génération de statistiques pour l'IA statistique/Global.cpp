#include "Global.h"


std::mutex mutexThread;
//const std::string filesPath = "../files/";
std::ofstream outfile = std::ofstream("stats.txt");
const std::string filesPath = "../statistics/";
bool isProcessingFileFinished = false;
int processedFiles = 0;

std::map<int, std::string> typesSimple = std::map<int, std::string>();
std::map<int, std::string> typesDouble = std::map<int, std::string>();
std::map<int, std::string> typeNoChoice = std::map<int, std::string>();

std::vector<Player> winners = std::vector<Player>();
std::vector<Player> loosers = std::vector<Player>();

std::vector<std::string> files = std::vector<std::string>();
std::vector<std::string> exploitChoices = std::vector<std::string>();
std::vector<std::string> exploitChoicesLoosers = std::vector<std::string>();
std::vector<std::string> addGoldToHammerValues = std::vector<std::string>();
std::vector<std::string> addGoldToHammerValuesLoosers = std::vector<std::string>();
std::vector<std::string> powerChoiceFaces = std::vector<std::string>();
std::vector<std::string> powerChoiceFacesLoosers = std::vector<std::string>();
std::vector<std::string> tempList = std::vector<std::string>();

std::vector<ForgeChoice> forgeChoices = std::vector<ForgeChoice>();
std::vector<ForgeChoice> forgeChoicesLoosers = std::vector<ForgeChoice>();

std::vector<HybridChoice> hybridChoices = std::vector<HybridChoice>();
std::vector<HybridChoice> hybridChoicesLoosers = std::vector<HybridChoice>();

std::vector<ChoiceBetweenRessources> choiceBetweenRessources = std::vector<ChoiceBetweenRessources>();
std::vector<ChoiceBetweenRessources> choiceBetweenRessourcesLoosers = std::vector<ChoiceBetweenRessources>();
std::vector<SatyreChoice> satyreChoices = std::vector<SatyreChoice>();
std::vector<SatyreChoice> satyreChoicesLoosers = std::vector<SatyreChoice>();

std::vector<ForgeChoice> tempForge = std::vector<ForgeChoice>();
std::vector<HybridChoice> tempHybrid = std::vector<HybridChoice>();
std::vector<ChoiceBetweenRessources> tempChoiceBetween = std::vector<ChoiceBetweenRessources>();
std::vector<SatyreChoice> tempSatyre = std::vector<SatyreChoice>();
