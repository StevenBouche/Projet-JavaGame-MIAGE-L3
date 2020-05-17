#include <iostream>
#include <vector>
#include <algorithm>
#include <filesystem>
#include <string>
#include <Windows.h>
#include <stdexcept>
#include <chrono>
#include <ctime>  
#include <thread>
#include <functional>
#include <iomanip>
#include "GameProcesses.h"

void read_directory(const std::string& name, std::vector<std::string>& v)
{    
    HANDLE dir;
    WIN32_FIND_DATA file_data;
    std::wstring stemp = std::wstring(name.begin(), name.end());

    if ((dir = FindFirstFile(stemp.c_str(), &file_data)) == INVALID_HANDLE_VALUE)
        return;

    do {
        char ch[260];
        char DefChar = ' ';
        WideCharToMultiByte(CP_ACP, 0, file_data.cFileName, -1, ch, 260, &DefChar, NULL);
        std::string file_name(ch);
        const bool is_directory = (file_data.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY) != 0;

        if (file_name[0] == '.')
            continue;

        if (is_directory)
            continue;

        v.push_back(file_name);
    } while (FindNextFile(dir, &file_data));

    FindClose(dir);
}

std::string findWinnerInFile(std::ifstream& myfile) {
    std::string winner;
    std::string line;

    if (myfile.is_open())
    {
        while (getline(myfile, line))
        {
            std::string playerId = line.substr(0, line.find("\t"));

            std::string eventType = line.substr(line.find("\t") + 1, line.find("\n"));
            eventType = eventType.substr(0, eventType.find("\t"));

            if (eventType == typeNoChoice[PLACE]) {
                std::string values = line.substr(line.find(eventType) + eventType.length() + 1, line.length());
                if (values == "1") {
                    winner = playerId;
                    break;
                }
            }

        }
    }
    myfile.clear();
    myfile.seekg(0);
    return winner;
}

void printStatsInt(std::ofstream& outfile, std::string name, int win, int loose) {
    std::cout << name << ": " << ((float)win / (float)loose) * 100 << "% (winner)" << std::endl;
    outfile << name << ": " << ((float)win / (float)loose) * 100 << std::endl;
}

void processFile(std::string fileName) {  
    
    std::ifstream myfile(filesPath + fileName);
    std::string winnerId = findWinnerInFile(myfile);
    
    mutexThread.lock();        
    winners.push_back(Player(winnerId));
    mutexThread.unlock();

    std::string line;
    if (myfile.is_open())
    {
        while (getline(myfile, line))
        {
            std::string playerId = line.substr(0, line.find("\t"));

            std::string eventType = line.substr(line.find("\t") + 1, line.find("\n"));
            eventType = eventType.substr(0, eventType.find("\t"));

            // -------- NO CHOICE --------- //
            if (eventType == typeNoChoice[ONE_MORE_TURN]) {
                if (playerId == winnerId) {
                    mutexThread.lock();
                    winners.back().noValueChoices.push_back(typeNoChoice[ONE_MORE_TURN]);
                    mutexThread.unlock();                    
                }
                else {
                    Player p(playerId);
                    p.noValueChoices.push_back(typeNoChoice[ONE_MORE_TURN]);
                    mutexThread.lock();
                    loosers.push_back(p);
                    mutexThread.unlock();
                }
                continue;
            }
            else if (eventType == typeNoChoice[ADD_HAMMER]) {
                if (playerId == winnerId) {
                    mutexThread.lock();
                    winners.back().noValueChoices.push_back(typeNoChoice[ADD_HAMMER]);
                    mutexThread.unlock();
                }
                else {
                    Player p(playerId);
                    p.noValueChoices.push_back(typeNoChoice[ADD_HAMMER]);
                    mutexThread.lock();
                    loosers.push_back(p);
                    mutexThread.unlock();
                }
                continue;
            }

            size_t pos = line.find_last_of("\t");
            std::string value = line.substr(pos + 1, line.length());

            // ------ SIMPLE ---------- //
            for (auto it = typesSimple.begin(); it != typesSimple.end(); it++) {
                if (it->second == eventType) {
                    if (playerId == winnerId) {
                        mutexThread.lock();
                        winners.back().simpleChoices.push_back(SimpleChoice(it->second, value));
                        mutexThread.unlock();
                    }
                    else {
                        Player p(playerId);
                        p.simpleChoices.push_back(SimpleChoice(it->second, value));
                        mutexThread.lock();
                        loosers.push_back(p);
                        mutexThread.unlock();
                    }
                    break;
                }
            }

            std::string values = (line.substr(line.find("\t") + 1, line.find("\n")));
            values = values.substr(values.find("\t") + 1, values.length());

            std::string value1 = values.substr(0, values.find("\t"));
            try {
                std::string value2 = values.substr(values.find_last_of("\t") + 1, values.length());


                // ------ DOUBLE ------ //
                for (auto it = typesDouble.begin(); it != typesDouble.end(); it++) {
                    if (it->second == eventType) {
                        if (playerId == winnerId) {
                            mutexThread.lock();
                            winners.back().doubleChoices.push_back(DoubleChoice(it->second, value1, value2));
                            mutexThread.unlock();
                        }
                        else {
                            Player p(playerId);
                            p.doubleChoices.push_back(DoubleChoice(it->second, value1, value2));
                            mutexThread.lock();
                            loosers.push_back(p);
                            mutexThread.unlock();
                        }
                        break;
                    }
                }

            }
            catch (std::out_of_range o) {
                //o.what();
            }

        }
        myfile.close();
    }
    
}

void threadFunction(int start, int end) { 
    
    for (int i = start; i < end; i++) {        
        processFile(files.at(i));
        mutexThread.lock();
        processedFiles++;
        mutexThread.unlock();        
    }
    return;
}

void printElapsedTime(std::chrono::system_clock::time_point start) {
    auto end = std::chrono::system_clock::now();
    std::chrono::duration<double> elapsed_seconds = end - start;
    std::cout << "Done ! elapsed time: " << elapsed_seconds.count() << std::endl << std::endl;
}

template <typename T, typename U>
void runInThread(std::vector<U> tab, T func, const int& MAX_THREADS) {    
    std::thread* poolThread = new std::thread[MAX_THREADS];
    int index = 0;
    for (int i = 0; i < tab.size(); i += static_cast<int>((std::floor(tab.size() / MAX_THREADS) + 1))) {
        int start = i - 1 >= 0 ? i - 1 : 0;
        int end = i + static_cast<int>(std::floor(tab.size() / MAX_THREADS));
        if (end >= tab.size()) {
            end = static_cast<int>(tab.size());
        }
        poolThread[index] = std::thread(func, start, end);  
        //func(start, end);
        index++;
    }

    if (!isProcessingFileFinished) {
        std::thread filesProcessingThread([&]() {
            int loading = 0;
            int filesNumber = static_cast<int>(files.size());
            while (processedFiles != filesNumber) {
                std::cout << "Processing files ";
                switch (loading++) {
                case 0:
                    std::cout << "\\" << std::endl;
                    break;
                case 1:
                    std::cout << "|" << std::endl;
                    break;

                case 2:
                    std::cout << "-" << std::endl;
                    break;

                case 3:
                    std::cout << "/" << std::endl;
                    loading = 0;
                    break;
                }
                std::cout << std::setprecision(4) << (static_cast<float>(processedFiles) / static_cast<float>(filesNumber))* 100.0f << "%\t" << processedFiles << "/" << filesNumber << std::endl;
                Sleep(1);
                system("cls");
            }
            isProcessingFileFinished = true;
            });
        filesProcessingThread.detach();
    }

    for (int i = 0; i < index; i++) {
        poolThread[i].join();
    } 
}

int main() {                     	  
    read_directory(filesPath + "/*", files);
     
    typeNoChoice[PLACE] = "PLACE";
    typeNoChoice[ONE_MORE_TURN] = "ONE_MORE_TURN";
    typeNoChoice[ADD_HAMMER] = "ADD_HAMMER";

    typesSimple[EXPLOIT] = "EXPLOIT";
    typesSimple[CHOICE_3GOLD_FOR_4GLORY] = "CHOICE_3GOLD_FOR_4GLORY";
    typesSimple[ADD_GOLD_TO_HAMMER] = "ADD_GOLD_TO_HAMMER";
    typesSimple[POWER_CHOICE] = "POWER_CHOICE";

    typesDouble[FORGE] = "FORGE";
    typesDouble[HYBRID] = "HYBRID";    
    typesDouble[CHOICE_BETWEEN_RESSOURCES] = "CHOICE_BETWEEN_RESSOURCES";
    typesDouble[CHOICE_FORGE_FACE_SPECIAL] = "CHOICE_FORGE_FACE_SPECIAL";
    typesDouble[SATYRE_CHOICE] = "SATYRE_CHOICE";        

    const unsigned int MAX_THREADS = 3;
    
    auto start = std::chrono::system_clock::now();
    std::function<void(int, int)> f = threadFunction;

    runInThread(files, f, MAX_THREADS);
    Sleep(1000);
    files.clear();

    int oneMoreTurn = 0, oneMoreTurnLoosers = 0;
    int addHammer = 0, addHammerLoosers = 0;
    int choice3GoldFor4Glory = 0, choice3GoldFor4GloryLoosers = 0;    

    for (Player p : winners) {

        for (std::string s : p.noValueChoices) {
            if (s == typeNoChoice[ONE_MORE_TURN]) {
                oneMoreTurn++;
            }
            else if (s == typeNoChoice[ADD_HAMMER]) {
                addHammer++;
            }
        }

        for (SimpleChoice s : p.simpleChoices) {
            if (s.eventType == typesSimple[EXPLOIT]) {
                exploitChoices.push_back(s.choice);
            }
            else if (s.eventType == typesSimple[CHOICE_3GOLD_FOR_4GLORY]) {
                if (s.choice == "true") {
                    choice3GoldFor4Glory++;
                }
            }
            else if (s.eventType == typesSimple[ADD_GOLD_TO_HAMMER]) {
                addGoldToHammerValues.push_back(s.choice);
            }
            else if (s.eventType == typesSimple[POWER_CHOICE]) {
                powerChoiceFaces.push_back(s.choice);
            }            
        }
    
        for (DoubleChoice s : p.doubleChoices) {
            if (s.eventType == typesDouble[FORGE]) {
                forgeChoices.push_back(ForgeChoice(s.value1, s.value2));
            }
            else if (s.eventType == typesDouble[HYBRID]) {
                hybridChoices.push_back(HybridChoice(s.value1, std::stoi(s.value2)));
            }
            else if (s.eventType == typesDouble[CHOICE_BETWEEN_RESSOURCES]) {
                choiceBetweenRessources.push_back(ChoiceBetweenRessources(s.value1, std::stoi(s.value2)));
            }
            else if (s.eventType == typesDouble[CHOICE_FORGE_FACE_SPECIAL]) {
                forgeChoices.push_back(ForgeChoice(s.value1, s.value2));
            }
            else if (s.eventType == typesDouble[SATYRE_CHOICE]) {
                satyreChoices.push_back(SatyreChoice(s.value1, s.value2));
            }
        }

    }
    winners.clear();

    for (Player p : loosers) {

        for (std::string s : p.noValueChoices) {
            if (s == typeNoChoice[ONE_MORE_TURN]) {
                oneMoreTurnLoosers++;
            }
            else if (s == typeNoChoice[ADD_HAMMER]) {
                addHammerLoosers++;
            }
        }

        for (SimpleChoice s : p.simpleChoices) {
            if (s.eventType == typesSimple[EXPLOIT]) {
                exploitChoicesLoosers.push_back(s.choice);
            }
            else if (s.eventType == typesSimple[CHOICE_3GOLD_FOR_4GLORY]) {
                if (s.choice == "true") {
                    choice3GoldFor4GloryLoosers++;
                }
            }
            else if (s.eventType == typesSimple[ADD_GOLD_TO_HAMMER]) {
                addGoldToHammerValuesLoosers.push_back(s.choice);
            }
            else if (s.eventType == typesSimple[POWER_CHOICE]) {
                powerChoiceFacesLoosers.push_back(s.choice);
            }
        }

        for (DoubleChoice s : p.doubleChoices) {
            if (s.eventType == typesDouble[FORGE]) {
                forgeChoicesLoosers.push_back(ForgeChoice(s.value1, s.value2));
            }
            else if (s.eventType == typesDouble[HYBRID]) {
                hybridChoicesLoosers.push_back(HybridChoice(s.value1, std::stoi(s.value2)));
            }
            else if (s.eventType == typesDouble[CHOICE_BETWEEN_RESSOURCES]) {
                choiceBetweenRessourcesLoosers.push_back(ChoiceBetweenRessources(s.value1, std::stoi(s.value2)));
            }
            else if (s.eventType == typesDouble[CHOICE_FORGE_FACE_SPECIAL]) {
                forgeChoicesLoosers.push_back(ForgeChoice(s.value1, s.value2));
            }
            else if (s.eventType == typesDouble[SATYRE_CHOICE]) {
                satyreChoicesLoosers.push_back(SatyreChoice(s.value1, s.value2));
            }
        }

    }    
    loosers.clear();
    

    printStatsInt(outfile, "oneMoreTurn", oneMoreTurn, oneMoreTurnLoosers);
    printStatsInt(outfile, "addHammer", addHammer, addHammerLoosers);
    printStatsInt(outfile, "choice3GoldFor4Glory", choice3GoldFor4Glory, choice3GoldFor4GloryLoosers);
   
    auto startProcess = std::chrono::system_clock::now();
    std::function<void(int, int)> foo = processExploit;
    runInThread(exploitChoicesLoosers, foo, MAX_THREADS);       
    exploitChoicesLoosers.clear();  
    tempList.clear();

    foo = processExploitWinnersOnly;
    runInThread(exploitChoices, foo, MAX_THREADS);
    printElapsedTime(startProcess);     
    exploitChoices.clear();    
    tempList.clear();

    startProcess = std::chrono::system_clock::now();
    foo = processAddGold;
    runInThread(addGoldToHammerValuesLoosers, foo, MAX_THREADS);
    addGoldToHammerValuesLoosers.clear();
    tempList.clear();

    foo = processAddGoldWinnerOnly;
    runInThread(addGoldToHammerValues, foo, MAX_THREADS);    
    printElapsedTime(startProcess);
    addGoldToHammerValues.clear();
    tempList.clear();


    startProcess = std::chrono::system_clock::now();
    foo = processPowerChoice;
    runInThread(powerChoiceFacesLoosers, foo, MAX_THREADS);
    powerChoiceFacesLoosers.clear();
    tempList.clear();

    foo = processPowerChoiceWinnersOnly;
    runInThread(powerChoiceFaces, foo, MAX_THREADS);    
    printElapsedTime(startProcess);
    powerChoiceFaces.clear();
    tempList.clear();

    startProcess = std::chrono::system_clock::now();    
    foo = processForge;
    runInThread(forgeChoicesLoosers, foo, MAX_THREADS);
    forgeChoicesLoosers.clear();
    tempForge.clear();

    foo = processForgeWinnersOnly;
    runInThread(forgeChoices, foo, MAX_THREADS);    
    printElapsedTime(startProcess);
    forgeChoices.clear();
    tempForge.clear();

    startProcess = std::chrono::system_clock::now();    
    foo = processHybrid;
    runInThread(hybridChoicesLoosers, foo, MAX_THREADS);
    hybridChoicesLoosers.clear();
    tempHybrid.clear();

    foo = processHybridWinnersOnly;
    runInThread(hybridChoices, foo, MAX_THREADS);        
    printElapsedTime(startProcess);
    hybridChoices.clear();
    hybridChoices.clear();

    startProcess = std::chrono::system_clock::now();
    foo = processChoiceBtRessources;
    runInThread(choiceBetweenRessourcesLoosers, foo, MAX_THREADS);
    choiceBetweenRessourcesLoosers.clear();
    tempChoiceBetween.clear();

    foo = processChoiceBtRessourcesWinnersOnly;
    runInThread(choiceBetweenRessources, foo, MAX_THREADS);    
    printElapsedTime(startProcess);
    choiceBetweenRessources.clear();
    tempChoiceBetween.clear();


    startProcess = std::chrono::system_clock::now();    
    foo = processSatyre;
    runInThread(satyreChoicesLoosers, foo, MAX_THREADS);
    satyreChoicesLoosers.clear();
    tempSatyre.clear();

    foo = processSatyreWinnersOnly;
    runInThread(satyreChoices, foo, MAX_THREADS);    
    printElapsedTime(startProcess);
    satyreChoices.clear();    
    tempSatyre.clear();
    
    auto end = std::chrono::system_clock::now();

    std::chrono::duration<double> elapsed_seconds = end - start;
    float elapsed_minutes = static_cast<float>(elapsed_seconds.count()) / 60.0f;
    float elapsed_hours = elapsed_minutes / 60.0f;

    std::time_t end_time = std::chrono::system_clock::to_time_t(end);

    std::cout << "Done ! elapsed time: " << elapsed_seconds.count() << "s" << std::endl;
    outfile << "\n\n-------------------------------------" << std::endl;
    outfile << "Done ! elapsed time: " << elapsed_seconds.count() << "s"  << std::endl;

    std::cout << "Elapsed time: " << elapsed_minutes << "mn" << std::endl;
    outfile << "Elapsed time: " << elapsed_minutes << "mn" << std::endl;

    std::cout << "Elapsed time: " << elapsed_hours << "h" << std::endl;
    outfile << "Elapsed time: " << elapsed_hours << "h" << std::endl;
    outfile << "-------------------------------------" << std::endl;
    
    outfile.close();    
	return 0;
}
