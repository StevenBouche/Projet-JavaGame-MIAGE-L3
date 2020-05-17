#include "GameProcesses.h"
#include <iostream>
#include <Windows.h>

//create tempArray for winner

void processExploit(int start, int end) {
    Sleep(100);
    for (int i = start; i < end; i++) {
        int temp = 0;
        int tempw = 0;
        std::string s = exploitChoicesLoosers.at(i);
        

        mutexThread.lock();
        if (std::find(tempList.begin(), tempList.end(), s) != tempList.end()) {
            mutexThread.unlock();
            continue;
        }
        else {
            tempList.push_back(s);
            mutexThread.unlock();
        }

        for (int j = i; j < exploitChoicesLoosers.size(); j++) {
            if (exploitChoicesLoosers.at(j) == s) {
                temp++;
            }
        }
        for (std::string sw : exploitChoices) {
            if (sw == s) {
                tempw++;
            }
        }
        mutexThread.lock();
        std::cout << typesSimple[EXPLOIT] << " " << s << " " << ((float)tempw / (float)temp) * 100 << "% G:" << tempw << ", P:" << temp << std::endl;
        outfile << typesSimple[EXPLOIT] << " " << s << " " << ((float)tempw / (float)temp) * 100 << "%" << std::endl;        
        mutexThread.unlock();
    }
    return;
}

void processExploitWinnersOnly(int start, int end) {
    Sleep(100);    
    for (int i = start; i < end; i++) {
        int temp = 0;
        std::string s = exploitChoices.at(i);

        mutexThread.lock();
        if (std::find(tempList.begin(), tempList.end(), s) != tempList.end()) {
            mutexThread.unlock();
            continue;
        }
        else {
            tempList.push_back(s);
            mutexThread.unlock();
        }        

        for (int j = i + 1; j < exploitChoices.size(); j++) {
            if (s == exploitChoices.at(j)) {
                temp++;
            }
        }
        mutexThread.lock();                              
        std::cout << typesSimple[EXPLOIT] << " " << s << " " << ((float)temp / (float)exploitChoices.size()) * 100 << "% des gagnant ont fait ce choix" << std::endl;
        outfile << typesSimple[EXPLOIT] << " " << s << " " << ((float)temp / (float)exploitChoices.size()) * 100 << "% des gagnant ont fait ce choix" << std::endl;                    
        mutexThread.unlock();
    }    
    return;
}

void processAddGold(int start, int end) {
    Sleep(100);
    for (int i = start; i < end; i++) {
        int temp = 0;
        int tempw = 0;
        std::string s = addGoldToHammerValuesLoosers.at(i);

        mutexThread.lock();
        if (std::find(tempList.begin(), tempList.end(), s) != tempList.end()) {
            mutexThread.unlock();
            continue;
        }
        else {
            tempList.push_back(s);
            mutexThread.unlock();
        }

        for (int j = i; j < addGoldToHammerValuesLoosers.size(); j++) {
            if (addGoldToHammerValuesLoosers.at(j) == s) {
                temp++;
            }
        }
        for (std::string sw : addGoldToHammerValues) {
            if (sw == s) {
                tempw++;
            }
        }
        mutexThread.lock();
        std::cout << typesSimple[ADD_GOLD_TO_HAMMER] << " " << s << " " << ((float)tempw / (float)temp) * 100 << "%" << std::endl;
        outfile << typesSimple[ADD_GOLD_TO_HAMMER] << " " << s << " " << ((float)tempw / (float)temp) * 100 << "%" << std::endl;        
        mutexThread.unlock();
    }
    return;
}

void processAddGoldWinnerOnly(int start, int end) {
    Sleep(100);    
    for (int i = start; i < end; i++) {
        int temp = 0;
        std::string s = addGoldToHammerValues.at(i);
        mutexThread.lock();
        if (std::find(tempList.begin(), tempList.end(), s) != tempList.end()) {
            mutexThread.unlock();
            continue;
        }
        else {
            tempList.push_back(s);
            mutexThread.unlock();
        }

        for (int j = i + 1; j < addGoldToHammerValues.size(); j++) {
            if (s == addGoldToHammerValues.at(j)) {
                temp++;
            }
        }
        mutexThread.lock();                   
        std::cout << typesSimple[ADD_GOLD_TO_HAMMER] << " " << s << " " << ((float)temp / (float)addGoldToHammerValues.size()) * 100 << "% des gagnant ont fait ce choix" << std::endl;
        outfile << typesSimple[ADD_GOLD_TO_HAMMER] << " " << s << " " << ((float)temp / (float)addGoldToHammerValues.size()) * 100 << "% des gagnant ont fait ce choix" << std::endl;        
        mutexThread.unlock();
    }    
    return;
}

void processPowerChoice(int start, int end) {
    Sleep(100);
    for (int i = start; i < end; i++) {
        int temp = 0;
        int tempw = 0;
        std::string s = powerChoiceFacesLoosers.at(i);
        mutexThread.lock();
        if (std::find(tempList.begin(), tempList.end(), s) != tempList.end()) {
            mutexThread.unlock();
            continue;
        }
        else {
            tempList.push_back(s);
            mutexThread.unlock();
        }
        for (int j = i; j < powerChoiceFacesLoosers.size(); j++) {
            if (powerChoiceFacesLoosers.at(j) == s) {
                temp++;
            }
        }
        for (std::string sw : powerChoiceFaces) {
            if (sw == s) {
                tempw++;
            }
        }
        mutexThread.lock();        
        std::cout << typesSimple[POWER_CHOICE] << " " << s << " " << ((float)tempw / (float)temp) * 100 << "%" << std::endl;
        outfile << typesSimple[POWER_CHOICE] << " " << s << " " << ((float)tempw / (float)temp) * 100 << "%" << std::endl;        
        mutexThread.unlock();
    }
    return;
}

void processPowerChoiceWinnersOnly(int start, int end) {
    Sleep(100);    
    for (int i = start; i < end; i++) {
        int temp = 0;
        std::string s = powerChoiceFaces.at(i);
        mutexThread.lock();
        if (std::find(tempList.begin(), tempList.end(), s) != tempList.end()) {
            mutexThread.unlock();
            continue;
        }
        else {
            tempList.emplace_back(s);
            mutexThread.unlock();
        }
        for (int j = i + 1; j < powerChoiceFaces.size(); j++) {
            if (s == powerChoiceFaces.at(j)) {
                temp++;
            }
        }
        mutexThread.lock();               
        std::cout << typesSimple[POWER_CHOICE] << " " << s << " " << ((float)temp / (float)powerChoiceFaces.size()) * 100 << "% des gagnant ont fait ce choix" << std::endl;
        outfile << typesSimple[POWER_CHOICE] << " " << s << " " << ((float)temp / (float)powerChoiceFaces.size()) * 100 << "% des gagnant ont fait ce choix" << std::endl;        
        mutexThread.unlock();
    }   
    return;
}

void processForge(int start, int end) {
    Sleep(100);
    for (int i = start; i < end; i++) {
        int temp = 0;
        int tempw = 0;
        ForgeChoice s = forgeChoicesLoosers.at(i);

        mutexThread.lock();
        bool add = true;
        for (ForgeChoice c : tempForge) {
            if (c.newFace == s.newFace && c.oldFace == s.oldFace) {
                add = false;
                break;
            }
        }        
        mutexThread.unlock();

        if (!add) continue;
        
        mutexThread.lock();
        tempForge.push_back(s);
        mutexThread.unlock();

        for (int j = i; j < forgeChoicesLoosers.size(); j++) {
            if (forgeChoicesLoosers.at(j).newFace == s.newFace && forgeChoicesLoosers.at(j).oldFace == s.oldFace) {
                temp++;
            }
        }
        for (ForgeChoice sw : forgeChoices) {
            if (sw.newFace == s.newFace && sw.oldFace == s.oldFace) {
                tempw++;
            }
        }

        mutexThread.lock();                
        std::cout << typesDouble[FORGE] << " " << s.newFace << " " << s.oldFace << " " << ((float)tempw / (float)temp) * 100 << "%" << std::endl;
        outfile << typesDouble[FORGE] << " " << s.newFace << " " << s.oldFace << " " << ((float)tempw / (float)temp) * 100 << "%" << std::endl;                    
        mutexThread.unlock();
    }
    return;
}

void processForgeWinnersOnly(int start, int end) {
    Sleep(100);    
    for (int i = start; i < end; i++) {
        int temp = 0;
        ForgeChoice s = forgeChoices.at(i);
        mutexThread.lock();
        bool add = true;
        for (ForgeChoice c : tempForge) {
            if (c.newFace == s.newFace && c.oldFace == s.oldFace) {
                add = false;
                break;
            }
        }        
        mutexThread.unlock();

        if (!add) continue;

        mutexThread.lock();
        tempForge.emplace_back(s);
        mutexThread.unlock();

        for (int j = i; j < forgeChoices.size(); j++) {
            if (forgeChoices.at(j).newFace == s.newFace && forgeChoices.at(j).oldFace == s.oldFace) {
                temp++;
            }
        }       

        mutexThread.lock();                       
        std::cout << typesDouble[FORGE] << " " << s.newFace << " " << s.oldFace << " " << ((float)temp / (float)forgeChoices.size()) * 100 << "% des gagnant ont fait ce choix" << std::endl;
        outfile << typesDouble[FORGE] << " " << s.newFace << " " << s.oldFace << " " << ((float)temp / (float)forgeChoices.size()) * 100 << "% des gagnant ont fait ce choix" << std::endl;        
        mutexThread.unlock();        
    }    
    return;
}

void processHybrid(int start, int end) {
    Sleep(100);
    for (int i = start; i < end; i++) {
        int temp = 0;
        int tempw = 0;
        HybridChoice s = hybridChoicesLoosers.at(i);
        bool add = true;
        mutexThread.lock();
        for (HybridChoice c : tempHybrid) {
            if (c.value == s.value && c.type == s.type) {
                add = false;
                break;
            }
        }
        mutexThread.unlock();

        if (!add) continue;

        mutexThread.lock();
        tempHybrid.push_back(s);
        mutexThread.unlock();

        for (int j = i; j < hybridChoicesLoosers.size(); j++) {
            if (hybridChoicesLoosers.at(j).value == s.value && hybridChoicesLoosers.at(j).type == s.type) {
                temp++;
            }
        }
        for (HybridChoice sw : hybridChoices) {
            if (sw.value == s.value && sw.type == s.type) {
                tempw++;
            }
        }
           
        mutexThread.lock();        
        std::cout << typesDouble[HYBRID] << " " << s.type << " " << s.value << " " << ((float)tempw / (float)temp) * 100 << "%" << std::endl;
        outfile << typesDouble[HYBRID] << " " << s.type << " " << s.value << " " << ((float)tempw / (float)temp) * 100 << "%" << std::endl;            
        mutexThread.unlock();
    }
    return;
}

void processHybridWinnersOnly(int start, int end) {
    Sleep(100);    
    for (int i = start; i < end; i++) {
        int temp = 0;
        HybridChoice s = hybridChoices.at(i);

        bool add = true;
        mutexThread.lock();
        for (HybridChoice c : tempHybrid) {
            if (c.value == s.value && c.type == s.type) {
                add = false;
                break;
            }
        }
        mutexThread.unlock();

        if (!add) continue;

        mutexThread.lock();
        tempHybrid.emplace_back(s);
        mutexThread.unlock();

        for (HybridChoice c : hybridChoices) {
            if (c.value == s.value && c.type == s.type) {
                temp++;
            }
        }

        mutexThread.lock();                       
        std::cout << typesDouble[HYBRID] << " " << s.type << " " << s.value << " " << ((float)temp / (float)hybridChoices.size()) * 100 << "% des gagnant ont fait ce choix" << std::endl;
        outfile << typesDouble[HYBRID] << " " << s.type << " " << s.value << " " << ((float)temp / (float)hybridChoices.size()) * 100 << "% des gagnant ont fait ce choix" << std::endl;        
        mutexThread.unlock();        
    }    
    return;
}

void processChoiceBtRessources(int start, int end) {
    Sleep(100);
    for (int i = start; i < end; i++) {
        int temp = 0;
        int tempw = 0;
        ChoiceBetweenRessources s = choiceBetweenRessourcesLoosers.at(i);
        bool add = true;

        mutexThread.lock();
        for (ChoiceBetweenRessources c : tempChoiceBetween) {
            if (c.ammount == s.ammount && c.type == s.type) {
                add = false;
                break;
            }
        }
        mutexThread.unlock();

        if (!add) continue;

        mutexThread.lock();
        tempChoiceBetween.push_back(s);
        mutexThread.unlock();

        for (int j = i; j < choiceBetweenRessourcesLoosers.size(); j++) {
            if (choiceBetweenRessourcesLoosers.at(j).ammount == s.ammount
                && choiceBetweenRessourcesLoosers.at(j).type == s.type) {
                temp++;
            }
        }
        for (ChoiceBetweenRessources sw : choiceBetweenRessources) {
            if (sw.ammount == s.ammount && sw.type == s.type) {
                tempw++;
            }
        }
        mutexThread.lock();                                 
        std::cout << typesDouble[CHOICE_BETWEEN_RESSOURCES] << " " << s.type << " " << s.ammount << " " << ((float)tempw / (float)temp) * 100 << "%" << std::endl;
        outfile << typesDouble[CHOICE_BETWEEN_RESSOURCES] << " " << s.type << " " << s.ammount << " " << ((float)tempw / (float)temp) * 100 << "%" << std::endl;            
        mutexThread.unlock();
    }
    return;
}

void processChoiceBtRessourcesWinnersOnly(int start, int end) {
    Sleep(100);    
    for (int i = start; i < end; i++) {
        int temp = 0;
        ChoiceBetweenRessources s = choiceBetweenRessources.at(i);
        bool add = true;

        mutexThread.lock();
        for (ChoiceBetweenRessources c : tempChoiceBetween) {
            if (c.ammount == s.ammount && c.type == s.type) {
                add = false;
                break;
            }
        }
        mutexThread.unlock();

        if (!add) continue;

        mutexThread.lock();
        tempChoiceBetween.emplace_back(s);
        mutexThread.unlock();

        for (int j = i; j < choiceBetweenRessources.size(); j++) {
            if (choiceBetweenRessources.at(j).ammount == s.ammount && choiceBetweenRessources.at(j).type == s.type) {
                temp++;
            }
        }
        

        mutexThread.lock();        
        std::cout << typesDouble[CHOICE_BETWEEN_RESSOURCES] << " " << s.type << " " << s.ammount << " " << ((float)temp / (float)choiceBetweenRessources.size()) * 100 << "% des gagnant ont fait ce choix" << std::endl;
        outfile << typesDouble[CHOICE_BETWEEN_RESSOURCES] << " " << s.type << " " << s.ammount << " " << ((float)temp / (float)choiceBetweenRessources.size()) * 100 << "% des gagnant ont fait ce choix" << std::endl;        
        mutexThread.unlock();        
    }    
    return;
}

void processSatyre(int start, int end) {
    Sleep(100);
    for (int i = start; i < end; i++) {
        int temp = 0;
        int tempw = 0;
        SatyreChoice s = satyreChoicesLoosers.at(i);
        bool add = true;

        mutexThread.lock();
        for (SatyreChoice c : tempSatyre) {
            if (c.face1 == s.face1 && c.face2 == s.face2) {
                add = false;
                break;
            }
        }
        mutexThread.unlock();

        if (!add) continue;

        mutexThread.lock();
        tempSatyre.push_back(s);
        mutexThread.unlock();

        for (int j = i; j < satyreChoicesLoosers.size(); j++) {
            if (satyreChoicesLoosers.at(j).face1 == s.face1 && satyreChoicesLoosers.at(j).face2 == s.face2) {
                temp++;
            }
        }
        for (SatyreChoice sw : satyreChoices) {
            if (sw.face1 == s.face1 && sw.face2 == s.face2) {
                tempw++;
            }
        }
        
        mutexThread.lock();        
        std::cout << typesDouble[SATYRE_CHOICE] << " " << s.face1 << " " << s.face2 << " " << ((float)tempw / (float)temp) * 100 << "%" << std::endl;
        outfile << typesDouble[SATYRE_CHOICE] << " " << s.face1 << " " << s.face2 << " " << ((float)tempw / (float)temp) * 100 << "%" << std::endl;            
        mutexThread.unlock();
    }
    return;
}

void processSatyreWinnersOnly(int start, int end) {
    Sleep(100);    
    for (int i = start; i < end; i++) {
        int temp = 0;
        SatyreChoice s = satyreChoices.at(i);
        bool add = true;
        mutexThread.lock();        
        for (SatyreChoice c : tempSatyre) {
            if (c.face1 == s.face1 && c.face2 == s.face2) {
                add = false;
                break;
            }
        }
        mutexThread.unlock();

        if (!add) continue;

        mutexThread.lock();
        tempSatyre.emplace_back(s);
        mutexThread.unlock();

        for (SatyreChoice c : satyreChoices) {
            if (c.face1 == s.face1 && c.face2 == s.face2) {
                temp++;
            }
        }
        

        mutexThread.lock();        
        std::cout << typesDouble[SATYRE_CHOICE] << " " << s.face1 << " " << s.face2 << " " << ((float)temp / (float)satyreChoices.size()) * 100 << "% des gagnant ont fait ce choix." << std::endl;
        outfile << typesDouble[SATYRE_CHOICE] << " " << s.face1 << " " << s.face2 << " " << ((float)temp / (float)satyreChoices.size()) * 100 << "% des gagnant ont fait ce choix." << std::endl;        
        mutexThread.unlock();       
    }    
    return;
}